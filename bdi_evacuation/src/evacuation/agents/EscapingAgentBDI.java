package evacuation.agents;

import evacuation.utils.Position;
import evacuation.utils.TypesObjects;
import evacuation.utils.TypesProperties;
import jadex.bdiv3.annotation.*;
import jadex.extension.envsupport.environment.ISpaceObject;
import jadex.extension.envsupport.math.*;
import jadex.micro.annotation.Agent;
import jadex.micro.annotation.AgentBody;

import java.util.HashSet;

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
        ISpaceObject door = worldMethods.pickClosestObject(doors, null, currentPosition);

        //get path for the door -> improve the search - TODO Maiah
        return worldMethods.findPathToObject(door, currentPosition);
    }

    //BODY***********************************************

    @AgentBody
    public void body(){
        super.body();
    }
}