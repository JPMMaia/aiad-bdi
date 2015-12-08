package evacuation.agents;

import evacuation.utils.Position;
import evacuation.utils.TypesObjects;
import evacuation.utils.explorer.ExplorerGoal;
import jadex.bdiv3.annotation.*;
import jadex.micro.annotation.Agent;
import jadex.micro.annotation.AgentBody;

@Agent
public class EscapingAgentBDI extends MaintainSafetyBDI{

    //PLANS******************************

    @Plan(trigger=@Trigger(goals=FindExitGoal.class))
    public class FindExitPlan {

        @PlanBody
        protected void FindExitPlanBody() {
            //System.out.println("Find exit");

            Position wantedPosition = findExit();
            nextPosition = wantedPosition;
        }
    }

    private void successfullyEscaped() {
        worldMethods.makeObjectInCell(currentPosition, TypesObjects.ESCAPED_AGENT);
        if(hurtObject != null)
            space.destroySpaceObject(hurtObject.getId());

        deleteCures();
        deletePush();
        worldMethods.resolveTwoAgentsInSameCell(currentPosition, null);
        space.destroyAndVerifySpaceObject(myself.getId());
        agent.killAgent();
    }

    protected Position findExit() {
        return null;
    }

    // FUNCTIONS *************************************

    protected Position findNewPositionWhenIncident() {
        /*
        //find one door in same division
        ISpaceObject[] doors = space.getSpaceObjectsByType(TypesObjects.DOOR);
        ISpaceObject door = worldMethods.pickClosestObject(doors, currentPosition);

        Position wantedPosition = worldMethods.getDoorPosition((SpaceObject) door);

        if(wantedPosition.equals(nextPosition)){
            successfullyEscaped();
        }

        //get path for the door -> improve the search - TODO Maiah

        return worldMethods.findPathToObject(door, currentPosition);*/

        mExplorer.setGoal(ExplorerGoal.FindExit);
        mExplorer.move();

        if(mExplorer.reachedExit())
            successfullyEscaped();

        return mExplorer.getPosition();
    }

    //BODY***********************************************

    @AgentBody
    public void body(){
        super.body();
    }
}