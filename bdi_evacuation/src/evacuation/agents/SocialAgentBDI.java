package evacuation.agents;

import jadex.bdiv3.annotation.*;
import jadex.micro.annotation.Agent;
import jadex.micro.annotation.AgentBody;

/**
 * Created by Paula on 15/11/2015.
 */
@Agent
public class SocialAgentBDI extends WalkerBDI{



    @Goal
    public class HelpOthersGoal { //altruisticamente ajudando os outros. //triggered by others call warning (env)

        @GoalParameter
        protected String goal = "HelpOthersGoal";
    } //periodicamente getNum certo raio alguem com condição fisica inferior a x

    @Goal
    public class PushOthersGoal {

        @GoalParameter
        protected String goal = "PushOthersGoal";

    }

    @Plan(trigger=@Trigger(goals=HelpOthersGoal.class))
    public class HelpOthersPlan {
        //helpOthersPlan - loose time and improve others condition
        @PlanBody
        protected void HelpOthersPlanBody() {
        }
    }

    @Plan(trigger=@Trigger(goals=PushOthersGoal.class))
    public class PushOthersPlan {
        //pushOthersPlan - other condition gets worse and if = 0 they die
        @PlanBody
        protected void PushOthersPlanBody() {
        }
    }

    @AgentBody
    public void body(){
        super.body();
    }
}

//deals with being hurt by the environment - condition decreases if distance from danger < value
// condition decreases if is being pushed
// condition = 0 -> is dead -> the others can pass by


//TODO
//ask the world if there is any door available
//if no empty door available riskPerception += 5

