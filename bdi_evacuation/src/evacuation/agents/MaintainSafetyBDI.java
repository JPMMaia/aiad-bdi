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
public class MaintainSafetyBDI extends SocialAgentBDI{

    @Belief
    protected int riskPerception = 0;

    @Belief
    protected int condition = 100; //range [0-100]

    @Belief(dynamic=true)
    protected boolean isDead = (condition == 0);

    @Belief(dynamic=true)
    protected boolean isHurted = (condition < 100);


    // GOALS**********************************

    @Goal(excludemode= Goal.ExcludeMode.Never)
    public class MaintainSafetyGoal {

        @GoalMaintainCondition(beliefs="riskPerception")
        protected boolean maintain() {
            //System.out.println("mantain riskPerception");
            return riskPerception <= 10;
        }

        @GoalTargetCondition(beliefs="riskPerception")
        protected boolean target() {
            //System.out.println("target riskPerception");
            return riskPerception <= 10;
        }
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

    @Plan(trigger=@Trigger(factchangeds="isIncident"))
    public class EvaluateIncidentRiskPlan {

        @PlanBody
        protected void EvaluateIncidentRiskPlanBody() {
            if(isIncident) {
                System.out.println("DANGER!");
                //System.out.println("isIncident - " + isIncident);
                evaluateRiskAndCondition();
            }
        }
    }

    @Plan(trigger=@Trigger(goals=MaintainSafetyGoal.class))
    public class MaintainSafetyPlan {

        @PlanBody
        protected void MaintainSafetyPlanBody() {
            //tem.out.println("MaintainSafetyPlanBody");
            agent.dispatchTopLevelGoal(new IncreaseDistanceFromDangerGoal());
            evaluateRiskAndCondition();
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

    @Plan(trigger=@Trigger(factchangeds="isDead"))
    public class DeadPlan {
        @PlanBody
        protected void DeadPlanBody() {
            if(isDead){
                Map<String, Object> properties = new HashMap<>();
                properties.put("position", new Vector2Int(nextPosition.x, nextPosition.y));
                space.createSpaceObject(TypesObjects.DEAD_AGENT, properties, null);
                agent.killAgent();
            }
        }
    }

    @Plan(trigger=@Trigger(factchangeds="isHurted"))
    public class HurtedPlan {
        @PlanBody
        protected void HurtedPlanBody() {
            if(isHurted){
                Map<String, Object> properties = new HashMap<>();
                properties.put("position", new Vector2Int(nextPosition.x, nextPosition.y));
                space.createSpaceObject(TypesObjects.HURT_AGENT, properties, null);
            }
        }
    }

    //FUNCTIONS FOR THE RISK CALCULATIONS************************

    protected void evaluateRiskAndCondition() {

        //System.out.println("avaliação do risco");
        //incident distance
        ISpaceObject[] incidentsArray = incidentObjects();

        int valueForRiskPerception = updateRiskPerceptionAndCondition(incidentsArray);

        //escaping possibility
        if(!emptyPathToTheExit){
            if(valueForRiskPerception < 90)
                valueForRiskPerception = 90;
        }

        //System.out.println("riskPerception = " + valueForRiskPerception);
        System.out.println("condition = " + condition);
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        riskPerception = valueForRiskPerception;
    }

    private int updateRiskPerceptionAndCondition(ISpaceObject[] incidentsArray) {

        int valueForCondition = 0;
        int valueForRiskPerception;

        //calculate condition from each instance
        double minDistance = Move.maximumDistance;

        for (ISpaceObject incident : incidentsArray){

            double distance = Move.distanceBetween(nextPosition, incident.getProperty("position"));

            if(distance == 0)
                valueForCondition += 5;
            else if(distance <= Math.sqrt(2))
                valueForCondition += 2;
            else if(distance <= Math.sqrt(8))
                valueForCondition += 1;

            minDistance = Math.min(minDistance,distance);
        }

        if(minDistance == 0) {
            valueForRiskPerception = 100;
        }
        else if(minDistance <= Math.sqrt(2))
            valueForRiskPerception = 90;
        else
            valueForRiskPerception = 50;

        condition = Math.max(condition - valueForCondition, 0);
        return valueForRiskPerception;
    }

    private ISpaceObject[] incidentObjects() {

        return space.getSpaceObjectsByType(TypesObjects.INCIDENT);
    }

    @AgentBody
    public void body(){
        super.body();
    }
}
