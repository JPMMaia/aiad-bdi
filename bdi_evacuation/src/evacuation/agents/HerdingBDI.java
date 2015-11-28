package evacuation.agents;

import evacuation.utils.Position;
import evacuation.utils.TypesObjects;
import jadex.extension.envsupport.environment.ISpaceObject;
import jadex.extension.envsupport.math.Vector2Double;
import jadex.micro.annotation.Agent;
import jadex.micro.annotation.AgentBody;

import java.util.HashSet;
import java.util.Set;

@Agent
public class HerdingBDI extends EscapingAgentBDI {

    //CONSTANTS***************************

    protected static final int DISTANCE_TO_HERDING = 4;

    //BELIEFS*****************************

    protected HashSet<Object> targets;

    //PLANS*******************************

    @Override
    protected Position findExit() {

        //3 - herding - if there are others and if they can conduct to exit - follow goal
        //            - else find exit

        //find agents in a certain perimeter

        Position newPosition = followOthersToExit();
        if (samePosition(newPosition))
            newPosition = findNewPositionWhenIncident();
        return newPosition;
    }

    private boolean samePosition(Position position) {
        return (position.x == currentPosition.x && position.y == currentPosition.y);
    }

    //FUNCTIONS FOR THE PATH CALCULATIONS

    protected Position followOthersToExit() {

        int distance = DISTANCE_TO_HERDING;
        String[] types = {TypesObjects.WANDERER, TypesObjects.CONSERVATIVE};
        Vector2Double wantedPosition = new Vector2Double(currentPosition.x, currentPosition.y);

        //choose a target
        Set agentsSet = space.getNearGridObjects(wantedPosition, distance, types);

        Position position;

        if(!agentsSet.isEmpty()){
            ISpaceObject[] agentsArray = convertSetToArray(agentsSet);

            //remove self - not necessary because we are only looking for active or conservative agents and not other herdings
            //agentsArray = removeSelf(agentsArray);

            ISpaceObject agent = worldMethods.pickClosestObject(agentsArray, targets, currentPosition);

            //follow the target
            position = worldMethods.findPathToObject(agent, currentPosition);
            if (samePosition(position))
                targets.add(agent.getId());
        }
        else {
            position = currentPosition;
        }

        return position;
    }

    private ISpaceObject[] convertSetToArray(Set agentsSet) {
        return (ISpaceObject[]) agentsSet.toArray(new ISpaceObject[agentsSet.size()]);
    }

    @AgentBody
    public void body(){
        targets = new HashSet<>();
        super.body();
    }
}