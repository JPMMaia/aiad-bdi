package evacuation.agents;

import evacuation.utils.Move;
import jadex.bdiv3.annotation.*;
import jadex.extension.envsupport.environment.ISpaceObject;
import jadex.micro.annotation.Agent;
import jadex.micro.annotation.AgentBody;

@Agent
public class MaintainSafetyBDI extends MaintainHealthBDI{

    boolean speed_mode = true;
    boolean patience_mode = true;

    @Belief(updaterate=500)
    protected int riskPerception = evaluateAgentSituation();

    @Belief(dynamic=true)
    protected boolean inPanic = (riskPerception > 90);

    // GOALS**********************************

    @Goal
    public class MaintainSafetyGoal {

        @GoalParameter
        protected String goal = "MaintainSafetyGoal";
    }

    @Goal
    public class IncreaseDistanceFromDangerGoal {

        @GoalParameter
        protected String goal = "IncreaseDistanceFromDangerGoal";
    }

    @Goal
    public class FindExitGoal {

        @GoalParameter
        protected String goal = "FindExitGoal";
    }

    // PLANS**********************************

    @Plan(trigger=@Trigger(goals=MaintainSafetyGoal.class))
    public class MaintainSafetyPlan {

        @PlanBody
        protected void MaintainSafetyPlanBody() {
            //System.out.println("MaintainSafetyPlanBody");
            agent.dispatchTopLevelGoal(new IncreaseDistanceFromDangerGoal());
        }
    }

    @Plan(trigger=@Trigger(goals=IncreaseDistanceFromDangerGoal.class))
    public class IncreaseDistanceFromDangerPlan {
        @PlanBody
        protected void IncreaseDistanceFromDangerPlanBody() {
            //System.out.println("IncreaseDistanceFromDangerPlanBody");
            if(indoor){
                agent.dispatchTopLevelGoal(new FindExitGoal());
            }
            else{
                //agent.dispatchTopLevelGoal(goGoal()); check danger position and run to the opposite way
            }
        }
    }

    //FUNCTIONS FOR THE RISK CALCULATIONS************************

    private int evaluateAgentSituation() {

        int res = 0;
        boolean helping = false;
        boolean pushing = false;

        res = evaluateRiskConditionVelocity();

        if(agent.getGoals().isEmpty()) {

            agent.dispatchTopLevelGoal(new ReceivePushOthersGoal());

            if (!worldMethods.isIncident()) {
                agent.dispatchTopLevelGoal(new WanderGoal());
                res = riskPerception;
            }
            else{
                if(condition > 50 && inPanic && patience_mode && !patient) {
                    if (worldMethods.getNumAgentInCellMap(currentPosition) == 2) {
                        System.out.println("someone in my cell");
                        agent.dispatchTopLevelGoal(new PushOthersGoal());
                        pushing = true;
                    }
                    else
                        agent.dispatchTopLevelGoal(new MaintainSafetyGoal());

                }
                else if(condition > 50 && worldMethods.someoneNeedsHelp(currentPosition, DISTANCE_TO_HELP)){
                    agent.dispatchTopLevelGoal(new HelpOthersGoal());
                    helping = true;
                }
                else if(condition > 50)
                    agent.dispatchTopLevelGoal(new MaintainSafetyGoal());
                else{ //condition <= 50
                    agent.dispatchTopLevelGoal(new MaintainHealthGoal());
                }

                if(!helping){
                    deleteCures();
                }

                if(!pushing){
                    deletePush();
                }

            }
        }

        //System.out.println("condition - " + condition);
        //System.out.println("riskPerception - " + riskPerception);

        return res;
    }

    protected int evaluateRiskConditionVelocity() {
        evaluateCondition();
        return evaluateRiskAndVelocity();
    }

    protected int evaluateRiskAndVelocity() {

        int valueForRiskPerception;

        //incident distance
        ISpaceObject[] incidentsArray = worldMethods.getIncidentObjects();

        //calculate condition from each instance
        double minDistance = Move.maximumDistance;

        for (ISpaceObject incident : incidentsArray){

            double distance = Move.distanceBetween(currentPosition, incident.getProperty("position"));
            minDistance = Math.min(minDistance,distance);
        }

        if(minDistance == 0) {
            valueForRiskPerception = 100;
        }
        else if(minDistance <= Math.sqrt(2))
            valueForRiskPerception = 90;
        else if(incidentsArray.length > 0)
            valueForRiskPerception = 50;
        else
            valueForRiskPerception = 0;

        //escaping possibility
        if(!emptyPathToTheExit){
            if(valueForRiskPerception < 90)
                valueForRiskPerception = 90;
        }

        valueForRiskPerception = personState.getRiskPerception(valueForRiskPerception);

        if(speed_mode == true)
            evaluateVelocity(valueForRiskPerception);

        return valueForRiskPerception;
    }

    private void evaluateVelocity(int valueForRiskPerception) {
        //[0.25-2]
        speed = condition/100.0 + valueForRiskPerception*2/100.0;
    }

    @AgentBody
    public void body(){
        super.body();
        riskPerception = 0;
    }
}
