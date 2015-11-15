package evacuation.agents;

import evacuation.utils.Move;
import evacuation.utils.Position;
import evacuation.utils.TypesObjects;
import jadex.bdiv3.BDIAgent;
import jadex.bdiv3.annotation.*;
import jadex.extension.envsupport.environment.ISpaceObject;
import jadex.extension.envsupport.environment.space2d.Grid2D;
import jadex.extension.envsupport.math.IVector1;
import jadex.extension.envsupport.math.Vector1Double;
import jadex.extension.envsupport.math.Vector2Double;
import jadex.extension.envsupport.math.Vector2Int;
import jadex.micro.annotation.Agent;

import java.util.Set;

@Agent
public class WalkerBDI {

    @Agent
    protected BDIAgent agent;

    // ATTRIBUTES ****************************

    Move move;

    //BELIEFS ********************************

    @Belief
    protected Grid2D space = (Grid2D)agent.getParentAccess().getExtension("2dspace").get();

    @Belief
    protected ISpaceObject myself = space.getAvatar(agent.getComponentDescription(), agent.getModel().getFullName());

    @Belief
    protected int velocity = 50; //range [0-100]

    //Beliefs that trigger goals********

    @Belief(updaterate=100)
    protected boolean isIncident = (space.getSpaceObjectsByType(TypesObjects.INCIDENT).length != 0);

    @Belief(dynamic=true)
    Position nextPosition;

    //GOALS************************************

    @Goal
    public class WanderGoal {

        @GoalParameter
        protected String goal = "WanderGoal";
    }

     // PLANS**********************************

    @Plan(trigger=@Trigger(goals=WanderGoal.class))
    public class WanderPlan {
        @PlanBody
        protected void WanderPlanBody() {

            //System.out.println("WanderPlanBody");

            Position oldPosition = move.getPosition(myself);
            Position wantedPosition = move.getNewPosition(oldPosition);
            if(noCollisions(wantedPosition))
                nextPosition = wantedPosition;

            if(!isIncident)
                agent.dispatchTopLevelGoal(new WanderGoal());
        }
    }

    @Plan(trigger=@Trigger(factchangeds="nextPosition"))
    public class GoPlan {
        @PlanBody
        protected void GoPlanBody() {
            //System.out.println("Position - (" + nextPosition.x + ", " + nextPosition.y + ")");
            myself.setProperty("position", new Vector2Int(nextPosition.x, nextPosition.y));

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                System.out.println("unable to sleep");
            }
        }
    }

    // FUNCTIONS *************************************

    protected boolean noCollisions(Position p) {
        Vector2Double wantedPosition = new Vector2Double(p.x,p.y);
        IVector1 distance = new Vector1Double(0);

        Set terrainSet = space.getNearObjects(wantedPosition,distance,TypesObjects.TERRAIN);
        Set incidentSet = space.getNearObjects(wantedPosition,distance,TypesObjects.INCIDENT);
        Set agentSet = space.getNearObjects(wantedPosition,distance,TypesObjects.WANDERER);

        if(!terrainSet.isEmpty()) //there is a wall or an obstacle
            return false;
        else if(!incidentSet.isEmpty()) //there are incidents in the way
            return false;
        else if(agentSet.size() > 1) //there are two agents in the position
            return false;

        return true;
    }
}