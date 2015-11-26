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

        Position position = followOthersToExit();
        if (samePosition)
            position = findNewPositionWhenIncident();
        samePosition = false;
        return position;
    }


    //FUNCTIONS FOR THE PATH CALCULATIONS

    protected Position followOthersToExit() {

        int distance = DISTANCE_TO_HERDING;
        String[] types = {TypesObjects.WANDERER};
        Vector2Double wantedPosition = new Vector2Double(currentPosition.x, currentPosition.y);

        //choose a target
        Set agentsSet = space.getNearGridObjects(wantedPosition, distance, types);

        Position position;

        if(!agentsSet.isEmpty()){ //the self and others
            ISpaceObject[] agentsArray = convertSetToArray(agentsSet);

            //remove self - not necessary because we are only looking for active agents and not other herdings
            //agentsArray = removeSelf(agentsArray);

            ISpaceObject agent = pickClosestObject(agentsArray, targets);

            //follow the target
            position = findPathToObject(agent);
            if (samePosition)
                targets.add(agent.getId());
        }
        else {
            position = currentPosition;
            samePosition = true;
        }

        return position;
    }

    private ISpaceObject[] removeSelf(ISpaceObject[] agentsArray) {

        ISpaceObject[] res = new ISpaceObject[agentsArray.length-1];

        for(int i = 0; i < agentsArray.length && i < res.length; i++){
            Object targetId = agentsArray[i].getId();
            Object ownId = myself.getId();
            if(!targetId.equals(ownId))
                res[i] = agentsArray[i];
        }
        return res;
    }

    private ISpaceObject[] convertSetToArray(Set agentsSet) {
        return (ISpaceObject[]) agentsSet.toArray(new ISpaceObject[agentsSet.size()]);
    }

    @AgentBody
    public void body(){
        targets = new HashSet<>();
        super.body();
        agent.dispatchTopLevelGoal(new MaintainSafetyGoal());
        agent.dispatchTopLevelGoal(new WanderGoal());
    }
}