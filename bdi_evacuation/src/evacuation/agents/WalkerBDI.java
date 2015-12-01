package evacuation.agents;

import evacuation.utils.Move;
import evacuation.utils.Position;
import evacuation.utils.WorldMethods;
import jadex.bdiv3.BDIAgent;
import jadex.bdiv3.annotation.*;
import jadex.extension.envsupport.environment.ISpaceObject;
import jadex.extension.envsupport.environment.space2d.Grid2D;
import jadex.extension.envsupport.math.Vector2Int;
import jadex.micro.annotation.Agent;
import jadex.micro.annotation.AgentBody;

@Agent
public class WalkerBDI {
    
    @Agent
    protected BDIAgent agent;

    //BELIEFS **********************************************

    //Space

    @Belief
    protected Grid2D space = (Grid2D)agent.getParentAccess().getExtension("2dspace").get();

    @Belief
    protected ISpaceObject myself = space.getAvatar(agent.getComponentDescription(), agent.getModel().getFullName());

    //Position

    @Belief(dynamic=true)
    Position nextPosition;

    @Belief(dynamic=true)
    Position currentPosition;

    //Speed

    @Belief
    Double speed;

    @Belief
    int millis = 500;

    //Other

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
            nextPosition = wantedPosition;
        }
    }

    @Plan(trigger=@Trigger(factchangeds="nextPosition"))
    public class GoPlan {
        @PlanBody
        protected synchronized void GoPlanBody() {

            if(worldMethods.noCollisionsInPosition(nextPosition)){

                System.out.println("Entrei - " + nextPosition.x + " " + nextPosition.y);

                if(!nextPosition.equals(currentPosition)) {

                    if (worldMethods.getNumAgentInCell(currentPosition) == 2) {
                        worldMethods.deleteSomeoneInMyCellObject(currentPosition);
                    }

                    worldMethods.makeSomeoneInMyCell(nextPosition);
                    worldMethods.removeAgentFromOldCell(currentPosition);
                    worldMethods.putAgentInNewCell(nextPosition);

                    myself.setProperty("position", new Vector2Int(nextPosition.x, nextPosition.y));
                    currentPosition = nextPosition;
                }

                try {
                    Thread.sleep(millis);
                } catch (InterruptedException e) {
                    System.out.println("unable to sleep");
                }
            }
            else{
                System.out.println("NAO entrei - " + nextPosition.x + " " + nextPosition.y);
            }
        }
    }

    // FUNCTIONS *************************************

    @AgentBody
    public void body(){
        currentPosition = move.getPosition(myself);
        //worldMethods.putAgentInNewCell(currentPosition);
    }
}
