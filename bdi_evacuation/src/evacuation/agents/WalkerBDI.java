package evacuation.agents;

import evacuation.processes.WorldGenerator;
import evacuation.utils.Move;
import evacuation.utils.Position;
import evacuation.utils.TypesProperties;
import evacuation.utils.WorldMethods;
import evacuation.utils.explorer.Explorer;
import jadex.bdiv3.BDIAgent;
import jadex.bdiv3.annotation.*;
import jadex.extension.envsupport.environment.ISpaceObject;
import jadex.extension.envsupport.environment.space2d.Grid2D;
import jadex.extension.envsupport.math.Vector2Int;
import jadex.micro.annotation.Agent;
import jadex.micro.annotation.AgentBody;

import java.util.Collections;

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

    @Belief(dynamic=true)
    protected Double speed = 1.0;

    @Belief
    protected boolean indoor = true;

    //ATTRIBUTES********************************************

    WorldMethods worldMethods = new WorldMethods(space);
    Move move = new Move( space.getAreaSize().getXAsInteger(), space.getAreaSize().getYAsInteger());
    protected Explorer mExplorer = new Explorer(WorldGenerator.getTerrain(),  Position.convertToPosition(myself.getProperty(TypesProperties.POSITION)));


    public int getWaitTime() {
        int waitTime = (int)(1000.0/speed);
        return waitTime;
    }

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
            if(currentPosition != null) {
                Position oldPosition = move.getPosition(myself);
                mExplorer.setGoal(move.getNewPosition(oldPosition), false);
                mExplorer.move();
                nextPosition = mExplorer.getPosition();
            }
            else{
                move.getPosition(myself);
            }
        }
    }

    @Plan(trigger=@Trigger(factchangeds="nextPosition"))
    public class GoPlan {
        @PlanBody
        protected void GoPlanBody() {

            moveAndUpdate();
            try {
                Thread.sleep(getWaitTime());
            } catch (InterruptedException e) {
                System.out.println("unable to sleep");
            }
        }
    }

    @AgentBody
    public void body(){

        currentPosition = move.getPosition(myself);
        speed = 1.0; //cells by second 1 -> 1000 millis ; 2 -> 500 ; 4 -> 250 millis
        while(true){
            if(configuredInitialPosition(currentPosition)){
               break;
            }

            try {
                Thread.sleep(getWaitTime());
            } catch (InterruptedException e) {
                System.out.println("unable to sleep");
            }
        }
    }

    private synchronized boolean configuredInitialPosition(Position currentPosition) {
        if(worldMethods.getNumAgentInCellMap(currentPosition) < 2) {
            worldMethods.putAgentInNewCellMap(currentPosition);
            return true;
        }
        return false;
    }

    private synchronized void moveAndUpdate(){
        if(worldMethods.noCollisionsInPosition(nextPosition)){
            if(!nextPosition.equals(currentPosition)) {
                worldMethods.resolveTwoAgentsInSameCell(currentPosition, nextPosition);
                myself.setProperty("position", new Vector2Int(nextPosition.x, nextPosition.y));
                currentPosition = nextPosition;
            }
        }
    }
}
