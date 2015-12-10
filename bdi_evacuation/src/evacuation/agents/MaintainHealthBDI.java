package evacuation.agents;

import evacuation.processes.WorldGenerator;
import evacuation.utils.Move;
import evacuation.utils.Position;
import evacuation.utils.TypesObjects;
import jadex.bdiv3.annotation.*;
import jadex.extension.envsupport.environment.ISpaceObject;
import jadex.extension.envsupport.environment.SpaceObject;
import jadex.micro.annotation.Agent;
import jadex.micro.annotation.AgentBody;

@Agent
public class MaintainHealthBDI extends SocialAgentBDI{

    //CONSTANTS***************************
    protected static final int HEALTH_INCREMENT = 20;
    protected static final int HEALTH_PUSH_DECREMENT = 2;

    @Belief
    public int condition = 100; //range [0-100]

    @Belief(dynamic=true)
    public boolean isDead = (condition <= 0);

    @Belief(dynamic=true)
    public boolean isHurt = (condition < 50);

    ISpaceObject hurtObject;


    // GOALS**********************************

    @Goal
    public class MaintainHealthGoal {

        @GoalParameter
        protected String goal = "MaintainHealthGoal";
    }


    // PLANS**********************************

    @Plan(trigger=@Trigger(goals=MaintainHealthGoal.class))
    public class MaintainHealthPlan {

        @PlanBody
        protected void MaintainHealthPlanBody() {

            //if there is a cure object in the world
            ISpaceObject cureObject = worldMethods.getAnObject(currentPosition, TypesObjects.CURE_AGENT);

            //grab the object
            if(cureObject != null){
                //increase helth

                condition += HEALTH_INCREMENT;

                //delete object
                if(!space.destroyAndVerifySpaceObject(cureObject.getId())) {
                    System.out.println("The cure object do not exist anymore.");
                    condition -= HEALTH_INCREMENT;
                }
            }
        }
    }

    @Plan(trigger=@Trigger(factchangeds="isDead"))
    public class DeadPlan {
        @PlanBody
        protected void DeadPlanBody() {
            if(isDead){
                processAgentDeath();
            }
        }
    }

    private void processAgentDeath() {
        worldMethods.makeObjectInCell(currentPosition, TypesObjects.DEAD_AGENT);

        if(hurtObject != null)
            space.destroyAndVerifySpaceObject(hurtObject.getId());

        WorldGenerator.getTerrain().setObstacle(currentPosition.x, currentPosition.y, false);
        deleteCures();
        deletePush();
        worldMethods.resolveTwoAgentsInSameCell(currentPosition, null);
        space.destroyAndVerifySpaceObject(myself.getId());
        agent.killAgent();
    }

    @Plan(trigger=@Trigger(factchangeds="isHurt"))
    public class HurtedPlan {
        @PlanBody
        protected void HurtedPlanBody() {
            if(isHurt){
                iAmHurt = true;
                if(hurtObject == null) {
                    hurtObject = worldMethods.makeObjectInCell(currentPosition,TypesObjects.HURT_AGENT);
                    WorldGenerator.getTerrain().setObstacle(currentPosition.x, currentPosition.y, true);
                }
            }
        }
    }

    @Plan(trigger=@Trigger(factchangeds="isHurt"))
    public class RecoveredPlan {
        @PlanBody
        protected void RecoveredPlanBody() {
            if(condition > 50 && hurtObject != null){
                space.destroyAndVerifySpaceObject(hurtObject.getId());
                WorldGenerator.getTerrain().setObstacle(currentPosition.x, currentPosition.y, false);
                hurtObject = null;
                iAmHurt = false;
            }
        }
    }

    //FUNCTIONS FOR THE CONDITION CALCULATIONS************************

    public void evaluateCondition() {

        ISpaceObject[] incidentsArray = worldMethods.getIncidentObjects();

        int valueForCondition = 0;

        //calculate condition from each instance

        for (ISpaceObject incident : incidentsArray){

            Object incidentPosition = incident.getProperty("position");
            if(!worldMethods.isWallBetween(currentPosition, Position.convertToPosition(incidentPosition))){
                double distance = Move.distanceBetween(currentPosition, incidentPosition);

                if(distance == 0)
                    valueForCondition += 5;
                else if(distance <= Math.sqrt(2))
                    valueForCondition += 2;
                else if(distance <= Math.sqrt(8))
                    valueForCondition += 1;
            }
        }

        condition = Math.max(condition - valueForCondition, 0);
    }

    @Plan(trigger=@Trigger(goals=ReceivePushOthersGoal.class))
    public class ReceivePushOthersPlan {
        //pushOthersPlan - other condition gets worse and if = 0 they die
        @PlanBody
        protected void ReceivePushOthersPlanBody() {
            //if there is a push in the same position as me
            //the push can not be mine
            SpaceObject pushObj = worldMethods.getAPush(currentPosition, pushSet);
            if (pushObj != null) {
                if (space.destroyAndVerifySpaceObject(pushObj.getId())) {
                    condition -= HEALTH_PUSH_DECREMENT;
                    if (condition < 0) {
                        condition = 0;
                        processAgentDeath();
                    }
                }
            }

        }
    }

    @AgentBody
    public void body(){
        super.body();
    }

    @Override
    boolean agentIsHurt(){
        return isHurt;
    }
}
