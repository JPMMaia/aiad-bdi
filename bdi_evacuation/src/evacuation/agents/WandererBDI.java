package evacuation.agents;

import evacuation.utils.Move;
import evacuation.utils.Position;
import jadex.bdiv3.BDIAgent;
import jadex.bdiv3.annotation.*;
import jadex.bdiv3.runtime.IGoal;
import jadex.bridge.service.types.clock.IClockService;
import jadex.extension.envsupport.environment.ISpaceObject;
import jadex.extension.envsupport.environment.space2d.Grid2D;
import jadex.extension.envsupport.math.Vector2Int;
import jadex.micro.annotation.Agent;
import jadex.micro.annotation.AgentBody;

import java.util.Collection;
import java.util.Random;

@Agent
public class WandererBDI
{
    @Agent
    protected BDIAgent agent;


    //ATTRIBUTES****************//

    Move move;

    //BELIEFS*******************//


    @Belief
    protected Grid2D space = (Grid2D)agent.getParentAccess().getExtension("2dspace").get();

    @Belief
    protected ISpaceObject myself = space.getAvatar(agent.getComponentDescription(), agent.getModel().getFullName());

    @Belief
    protected boolean indoor = true;

    @Belief
    protected int velocity = 50; //range [0-100]

    @Belief
    protected int condition = 100; //range [0-100]


    /*****************************
     Beliefs that trigger goals
     *****************************/

    @Belief(updaterate=500)
    protected boolean incident = (space.getSpaceObjectsByType("incident").length != 0);

    @Belief
    protected int riskPerception = 0; //range [0-100] //TODO metodo que faz update a percepcao de risco consoante o ambiente

    @Belief(dynamic=true)
    Position nextPosition;

    @Belief(dynamic=true)
    protected boolean inPanic = (riskPerception > 90);

    @Belief
    protected boolean isTrapped = false;

    @Belief
    protected boolean isBeingPushed = false;

    @Belief
    protected boolean isDown = false;

    /****************************
     GOALS
     ***************************/

    @Goal
    public class WanderGoal {

        @GoalParameter
        protected String goal = "WanderGoal";
    }

    @Goal(excludemode= Goal.ExcludeMode.Never)
    public class MaintainSafetyGoal {

        @GoalMaintainCondition(beliefs="riskPerception")
        protected boolean maintain() {
            System.out.println("mantain riskPerception");
            return riskPerception <= 10;
        }

        @GoalTargetCondition(beliefs="riskPerception")
        protected boolean target() {
            System.out.println("target riskPerception");
            return riskPerception <= 10;
        }
    }

    /*

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

    @Goal
    public class FollowOthersGoal {

        @GoalParameter
        protected String goal = "FollowOthersGoal";
    }

    @Goal
    public class HelpOthersGoal { //altruisticamente ajudando os outros. //triggered by others call warning (env)

        @GoalParameter
        protected String goal = "HelpOthersGoal";
    }

    @Goal
    public class PushOthersGoal {

        @GoalCreationCondition(beliefs="inPanic") //empurrar os outros triggered by riskPerception > 90 && isTrapped == true
        public PushOthersGoal() {
        }

    }*/

    /****************************
     PLANS
     ***************************/

    @Plan(trigger=@Trigger(goals=WanderGoal.class))
    public class WanderPlan {
        @PlanBody
        protected void WanderPlanBody() {

            Position oldPosition = move.getPosition(myself);
            nextPosition = move.getNewPosition(oldPosition,
                    space.getAreaSize().getXAsInteger(),
                    space.getAreaSize().getXAsInteger()
            );

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                System.out.println("unable to sleep");
            }

            agent.dispatchTopLevelGoal(new WanderGoal());
        }
    }

    @Plan(trigger=@Trigger(factchangeds="nextPosition"))
    public class GoPlan {
        @PlanBody
        protected void GoPlanBody() {
            myself.setProperty("position", new Vector2Int(nextPosition.x, nextPosition.y));

        }
    }

    /*

    @Goal
    public class ABeliefBasedGoal {

        @GoalContextCondition(beliefs="s")
        public boolean checkCondition() {
            return false;
        }

        @GoalCreationCondition(beliefs="s")
        public ABeliefBasedGoal() {
        }

    }

    @Plan(trigger=@Trigger(goals=MaintainSafetyGoal.class))
    public class MaintainSafetyPlan {

        @PlanBody
        protected void MaintainSafetyPlanBody() {
            System.out.println("MaintainSafetyPlanBody");
            agent.dispatchTopLevelGoal(new IncreaseDistanceFromDangerGoal());
        }
    }

    @Plan(trigger=@Trigger(goals=IncreaseDistanceFromDangerGoal.class))
    public class IncreaseDistanceFromDangerPlan {

        @PlanBody
        protected void IncreaseDistanceFromDangerPlanBody() {
            System.out.println("IncreaseDistanceFromDangerPlanBody");
            riskPerception--;
            System.out.println(riskPerception);

            if(indoor){
                agent.dispatchTopLevelGoal(new FindExitGoal());
            }
            else{
                //agent.dispatchTopLevelGoal(goGoal()); check danger position and run to the opposite way
            }

            if(riskPerception > 90){
                isTrapped = true;
            }
        }
    }

    @Plan(trigger=@Trigger(goals=FindExitGoal.class))
    public class FindExitPlan {

        @PlanBody
        protected void FindExitPlanBody() {
            System.out.println("FindExitPlan");

            //getExitPlan
            //1 - herding - if there are others and if they can conduct to exit - follow goal
            //            - else find exit
            //2 - conservative - look for a known empty path
            //3 - active - look for the fastest empty path

            //ask the world if there is any door available
            //if no empty door available riskPerception += 5

            agent.dispatchTopLevelGoal(new GoGoal());
            //if I didn't succeed in go -> riskPerception++
        }
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
    }*/

    /****************************
     BODY
     ***************************/

    //deals with being hurt by the environment - condition decreases if distance from danger < value
    // condition decreases if is being pushed
    // condition = 0 -> is dead -> the others can pass by

    @AgentBody
    public void body(){

        move = new Move();
        //nextPosition = new Position();

        //agent.dispatchTopLevelGoal(new MaintainSafetyGoal());
        agent.dispatchTopLevelGoal(new WanderGoal());

        Collection<IGoal> goals = agent.getGoals();

        /*while(true){

            //System.out.println(space.getSpaceObjectsByType("incident"));
            //System.out.println("antes de incidente");

            ISpaceObject[] o = space.getSpaceObjectsByType("incident");

            if(o.length != 0) {
                break;
            }

            agent.waitForDelay(3000);

            //declare here the first goal TODO
            // System.out.println("sem incidente");
        }

        //System.out.println("depois de incidente");

        riskPerception = 90;*/

    }
}