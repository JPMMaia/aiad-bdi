package evacuation.agents;

import evacuation.utils.Move;
import evacuation.utils.TypesObjects;
import jadex.bdiv3.annotation.*;
import jadex.extension.envsupport.environment.ISpaceObject;
import jadex.extension.envsupport.math.Vector2Int;
import jadex.micro.annotation.Agent;
import jadex.micro.annotation.AgentBody;

import java.util.HashMap;
import java.util.Map;

@Agent
public class MaintainHealthBDI extends SocialAgentBDI{

    @Belief
    public int condition = 100; //range [0-100]

    @Belief(dynamic=true)
    public boolean isDead = (condition == 0);

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
    public class MaintainSafetyPlan {

        @PlanBody
        protected void MaintainSafetyPlanBody() {

            //if there is a cure object in the world
            ISpaceObject cureObject = worldMethods.getCureObject(currentPosition);

            //grab the object
            if(cureObject != null){
                //increase helth

                condition += 20;

                //delete object
                if(!space.destroyAndVerifySpaceObject(cureObject.getId()))
                    System.out.println("The cure object do not exist anymore.");
            }
        }
    }

    @Plan(trigger=@Trigger(factchangeds="isDead"))
    public class DeadPlan {
        @PlanBody
        protected void DeadPlanBody() {
            if(isDead){
                Map<String, Object> properties = new HashMap<>();
                properties.put("position", new Vector2Int(currentPosition.x, currentPosition.y));
                space.createSpaceObject(TypesObjects.DEAD_AGENT, properties, null);
                if(hurtObject != null)
                    space.destroySpaceObject(hurtObject.getId());

                agent.killAgent();
            }
        }
    }

    @Plan(trigger=@Trigger(factchangeds="isHurt"))
    public class HurtedPlan {
        @PlanBody
        protected void HurtedPlanBody() {
            if(isHurt){
                if(hurtObject == null) {
                    Map<String, Object> properties = new HashMap<>();
                    properties.put("position", new Vector2Int(currentPosition.x, currentPosition.y));
                    hurtObject = space.createSpaceObject(TypesObjects.HURT_AGENT, properties, null);
                }
            }
        }
    }

    @Plan(trigger=@Trigger(factchangeds="isHurt"))
    public class RecoveredPlan {
        @PlanBody
        protected void RecoveredPlanBody() {
            if(condition > 50 && hurtObject != null){
                space.destroySpaceObject(hurtObject.getId());
                hurtObject = null;
            }
        }
    }

    //FUNCTIONS FOR THE CONDITION CALCULATIONS************************


    public void evaluateCondition() {

        ISpaceObject[] incidentsArray = worldMethods.getIncidentObjects();

        int valueForCondition = 0;

        //calculate condition from each instance

        for (ISpaceObject incident : incidentsArray){

            double distance = Move.distanceBetween(currentPosition, incident.getProperty("position"));

            if(distance == 0)
                valueForCondition += 10;
            else if(distance <= Math.sqrt(2))
                valueForCondition += 5;
            else if(distance <= Math.sqrt(8))
                valueForCondition += 2;
        }

        condition = Math.max(condition - valueForCondition, 0);
    }

    @AgentBody
    public void body(){
        super.body();
    }
}
