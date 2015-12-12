package evacuation.agents;

import evacuation.utils.Position;
import evacuation.utils.TypesObjects;
import evacuation.utils.TypesProperties;
import jadex.extension.envsupport.environment.ISpaceObject;
import jadex.extension.envsupport.environment.SpaceObject;
import jadex.extension.envsupport.math.Vector2Double;
import jadex.micro.annotation.Agent;
import jadex.micro.annotation.AgentBody;

import java.util.HashSet;
import java.util.Set;

@Agent
public class HerdingBDI extends EscapingAgentBDI {

    //CONSTANTS***************************

    protected static final int DISTANCE_TO_HERDING = 20;

    //PLANS*******************************

    @Override
    protected Position findExit() {

        //3 - herding - if there are others and if they can conduct to exit - follow goal
        //            - else find exit

        //find agents in a certain perimeter

        Position newPosition = followOthersToExit();
        return newPosition;
    }

    //FUNCTIONS FOR THE PATH CALCULATIONS

    protected Position followOthersToExit() {

        int distance = DISTANCE_TO_HERDING;
        String[] types = {TypesObjects.WANDERER, TypesObjects.CONSERVATIVE};
        Vector2Double wantedPosition = new Vector2Double(currentPosition.x, currentPosition.y);

        //choose a target

        Set agentsSet = space.getNearGridObjects(wantedPosition, distance, types);
        //System.out.println("agents to follow - " +  agentsSet.size());

        Position position;

        if(!agentsSet.isEmpty()){
            ISpaceObject[] agentsArray = convertSetToArray(agentsSet);

            ISpaceObject agent = worldMethods.pickClosestObject(agentsArray, currentPosition);

            try {
                ISpaceObject checkStillAlive = space.getSpaceObject(agent.getId());
            }catch(Exception e){
                //System.out.println("Jadex: desculpem mas afinal ja tinha sido eliminado");
                position = findNewPositionWhenIncident();
                return position;
            }

            if(!worldMethods.isWallBetween(currentPosition,Position.convertSpaceObjectToPosition(agent))) {
                //System.out.println("Sem parede no meio");
                //follow the target
                mExplorer.setGoal(Position.convertSpaceObjectToPosition(agent), true);
                mExplorer.move();
                position = mExplorer.getPosition();
                return position;
            }

            //System.out.println("Com parede no meio");
        }
        else
            position = findNewPositionWhenIncident();

        position = findNewPositionWhenIncident();
        return position;
    }

    private ISpaceObject[] convertSetToArray(Set agentsSet) {
        return (ISpaceObject[]) agentsSet.toArray(new ISpaceObject[agentsSet.size()]);
    }

    @AgentBody
    public void body(){
        super.body();
    }
}