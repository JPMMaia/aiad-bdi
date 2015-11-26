package evacuation.agents;

import evacuation.utils.Move;
import evacuation.utils.Position;
import evacuation.utils.TypesObjects;
import evacuation.utils.WorldMethods;
import jadex.bdiv3.BDIAgent;
import jadex.bdiv3.annotation.*;
import jadex.extension.envsupport.environment.ISpaceObject;
import jadex.extension.envsupport.environment.space2d.Grid2D;
import jadex.extension.envsupport.math.Vector2Int;
import jadex.micro.annotation.Agent;
import jadex.micro.annotation.AgentBody;

import java.util.Set;

@Agent
public class WalkerBDI {

    @Belief
    protected int velocity = 50; //range [0-100]

    @Agent
    protected BDIAgent agent;

    //BELIEFS **********************************************

    @Belief
    protected Grid2D space = (Grid2D)agent.getParentAccess().getExtension("2dspace").get();

    @Belief
    protected ISpaceObject myself = space.getAvatar(agent.getComponentDescription(), agent.getModel().getFullName());

    @Belief(dynamic=true)
    Position nextPosition;

    @Belief
    protected boolean samePosition = false;

    @Belief
    protected boolean indoor = true;

    @Belief
    protected boolean emptyPathToTheExit = true; //A*

    //ATTRIBUTES********************************************

    WorldMethods worldMethods = new WorldMethods(space);
    Move move = new Move( space.getAreaSize().getXAsInteger(), space.getAreaSize().getYAsInteger());

    //GOALS*************************************************

    @Goal
    public class WanderGoal {

        @GoalParameter
        protected String goal = "WanderGoal";
    }

    // PLANS*********************************************

    @Plan(trigger=@Trigger(goals=WanderGoal.class))
    public class WanderPlan {
        @PlanBody
        protected void WanderPlanBody() {
            Position oldPosition = move.getPosition(myself);
            Position wantedPosition = move.getNewPosition(oldPosition);
            if(worldMethods.noCollisions(wantedPosition))
                nextPosition = wantedPosition;
        }
    }

    @Plan(trigger=@Trigger(factchangeds="nextPosition"))
    public class GoPlan {
        @PlanBody
        protected void GoPlanBody() {

            myself.setProperty("position", new Vector2Int(nextPosition.x, nextPosition.y));

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                System.out.println("unable to sleep");
            }
        }
    }

    // FUNCTIONS *************************************

    @AgentBody
    public void body(){
    }
}
