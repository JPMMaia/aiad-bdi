package evacuation.agents;

import evacuation.utils.Move;
import evacuation.utils.Position;
import jadex.bdiv3.annotation.*;
import jadex.extension.envsupport.environment.ISpaceObject;
import jadex.extension.envsupport.environment.SpaceObject;
import jadex.micro.annotation.Agent;
import jadex.micro.annotation.AgentBody;

@Agent
public class MaintainSafetyBDI extends MaintainHealthBDI{
    boolean speed_mode = true;
    boolean patience_mode = true;

    @Belief(updaterate=100)
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
            agent.dispatchTopLevelGoal(new IncreaseDistanceFromDangerGoal());
        }
    }

    @Plan(trigger=@Trigger(goals=IncreaseDistanceFromDangerGoal.class))
    public class IncreaseDistanceFromDangerPlan {
        @PlanBody
        protected void IncreaseDistanceFromDangerPlanBody() {
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

            if (!worldMethods.isIncident()) {
                agent.dispatchTopLevelGoal(new WanderGoal());
                res = riskPerception;
            }
            else{
                agentNeedsHelp = (SpaceObject) worldMethods.someoneNeedsHelp(currentPosition, DISTANCE_TO_HELP);
                boolean someoneHasPushed = worldMethods.someoneHasPushed(currentPosition);

                if(someoneHasPushed){
                    agent.dispatchTopLevelGoal(new ReceivePushOthersGoal());
                }
                else if(!isHurt && inPanic && patience_mode && !patient) {
                    if (worldMethods.getNumAgentInCellMap(currentPosition) >= 2) {
                        agent.dispatchTopLevelGoal(new PushOthersGoal());
                        pushing = true;
                    }
                    else
                        agent.dispatchTopLevelGoal(new MaintainSafetyGoal());

                }
                else if(!isHurt && agentNeedsHelp != null){
                    agent.dispatchTopLevelGoal(new HelpOthersGoal());
                    helping = true;
                }
                else if(!isHurt)
                    agent.dispatchTopLevelGoal(new MaintainSafetyGoal());
                else{ //isHurt
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
        Position exit =  mExplorer.findExit();
        if(worldMethods.getNumAgentInCellMap(exit) >= 2){
            if(valueForRiskPerception < 90)
                valueForRiskPerception = 90;
        }

        int init = valueForRiskPerception;
        valueForRiskPerception = personState.getRiskPerception(valueForRiskPerception);

        if(speed_mode == true)
            evaluateVelocity(valueForRiskPerception);

        return valueForRiskPerception;
    }

    private void evaluateVelocity(int valueForRiskPerception) {
        //[0.5-2]
        speed = condition/100.0 + valueForRiskPerception*4/100.0;
    }

    @AgentBody
    public void body(){
        super.body();
        riskPerception = 0;
    }
}
