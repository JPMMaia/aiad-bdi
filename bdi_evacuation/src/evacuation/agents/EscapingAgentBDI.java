package evacuation.agents;

import evacuation.utils.Move;
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

    //BELIEFS ********************************

    @Belief(dynamic=true)
    protected boolean inPanic = (riskPerception > 90);

    @Belief
    protected boolean isTrapped = false;

    @Belief
    protected boolean isBeingPushed = false;

    @Belief
    protected boolean isDown = false;


    //PLANS******************************

    @Plan(trigger=@Trigger(goals=FindExitGoal.class))
    public class FindExitPlan {

        @PlanBody
        protected void FindExitPlanBody() {
            nextPosition = findExit();
        }
    }

    protected Position findExit() {
        return null;
    }

    // FUNCTIONS *************************************

    protected Position findNewPositionWhenIncident() {

        //find one door in same division
        ISpaceObject[] doors = space.getSpaceObjectsByType(TypesObjects.DOOR);
        ISpaceObject door = pickClosestObject(doors, null);

        //get path for the door -> improve the search - TODO Maiah
        return findPathToObject(door);
    }

    protected Position findPathToObject(ISpaceObject object) {
        if(object != null){
            //space.getShortestDirection()
            Position destinyPosition = move.convertToPosition(object.getProperty(TypesProperties.POSITION));
            Position oldPosition = new Position(nextPosition.x, nextPosition.y);
            Position newPosition = null;

            if(Math.abs(destinyPosition.x - oldPosition.x) > Math.abs(destinyPosition.y - oldPosition.y)) { //move in x
                if(destinyPosition.x < oldPosition.x)
                    newPosition = new Position(nextPosition.x-1, nextPosition.y);
                else if(destinyPosition.x > oldPosition.x)
                    newPosition = new Position(nextPosition.x+1, nextPosition.y);
            }
            else { //move in y
                if(destinyPosition.y < oldPosition.y)
                    newPosition = new Position(nextPosition.x, nextPosition.y-1);
                else if(destinyPosition.y > oldPosition.y)
                    newPosition = new Position(nextPosition.x, nextPosition.y+1);
            }

            if(newPosition != null && noCollisions(newPosition)) {
                return newPosition;
            }
        }

        samePosition = true;
        return nextPosition;
    }

    protected ISpaceObject pickClosestObject(ISpaceObject[] objects, HashSet<Object> targets) {

        objects = getTheDoorsInSameDivision(objects);

        Vector2Double currentPosition = new Vector2Double(nextPosition.x,nextPosition.y);

        if(objects.length > 0){
            System.out.println("objects lenght - " + objects.length);
            int pos = 0;

            Position wantedPosition = move.convertToPosition(objects[pos].getProperty(TypesProperties.POSITION));

            IVector1 distance = space.getDistance(currentPosition, new Vector2Double(wantedPosition.x,wantedPosition.y));

            for(int i = 1; i < objects.length; i++){
                IVector1 newDistance = space.getDistance(currentPosition, (IVector2) objects[i].getProperty(TypesProperties.POSITION));

                if(targets != null)
                    if(targets.contains(objects[i]))
                        continue;

                if(distance.greater(newDistance)){
                    pos = i;
                    distance = newDistance;
                }
            }
            return objects[pos];
        }
        return null;
    }

    protected ISpaceObject[] getTheDoorsInSameDivision(ISpaceObject[] doors) {
        //TODO
        //See if there is at least one path to the door, without collide to a wall

        return doors;
    }


    //BODY***********************************************

    @AgentBody
    public void body(){

        move = new Move( space.getAreaSize().getXAsInteger(), space.getAreaSize().getYAsInteger());
        isIncident = false;

        agent.dispatchTopLevelGoal(new MaintainSafetyGoal());
        agent.dispatchTopLevelGoal(new WanderGoal());
        super.body();
    }
}