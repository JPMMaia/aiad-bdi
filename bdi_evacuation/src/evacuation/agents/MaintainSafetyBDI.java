package evacuation.agents;

import jadex.bdiv3.annotation.*;
import jadex.micro.annotation.Agent;

/**
 * Created by Paula on 15/11/2015.
 */
@Agent
public class MaintainSafetyBDI extends SocialAgentBDI{

    @Belief
    protected int riskPerception = 0;

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
                evaluateRisk();
            }
        }
    }

    @Plan(trigger=@Trigger(goals=MaintainSafetyGoal.class))
    public class MaintainSafetyPlan {

        @PlanBody
        protected void MaintainSafetyPlanBody() {
            //System.out.println("MaintainSafetyPlanBody");
            agent.dispatchTopLevelGoal(new IncreaseDistanceFromDangerGoal());
            evaluateRisk();
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

    protected void evaluateRisk() {
        //TODO
        riskPerception =  90;
    }
}
