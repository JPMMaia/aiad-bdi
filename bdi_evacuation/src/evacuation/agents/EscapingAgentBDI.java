package evacuation.agents;

import evacuation.utils.Position;
import evacuation.utils.TypesObjects;
import jadex.bdiv3.annotation.*;
import jadex.extension.envsupport.environment.ISpaceObject;
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

    protected Position findExit() {
        return null;
    }

    // FUNCTIONS *************************************

    protected Position findNewPositionWhenIncident() {

        //find one door in same division
        ISpaceObject[] doors = space.getSpaceObjectsByType(TypesObjects.DOOR);
        ISpaceObject door = worldMethods.pickClosestObject(doors, currentPosition);

        //get path for the door -> improve the search - TODO Maiah
        return worldMethods.findPathToObject(door, currentPosition);
    }

    //BODY***********************************************

    @AgentBody
    public void body(){
        super.body();
    }
}