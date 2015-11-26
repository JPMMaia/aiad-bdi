package evacuation.utils;

import jadex.extension.envsupport.environment.ISpaceObject;
import jadex.extension.envsupport.environment.space2d.Grid2D;
import jadex.extension.envsupport.math.IVector1;
import jadex.extension.envsupport.math.Vector1Double;
import jadex.extension.envsupport.math.Vector2Double;

import java.util.Set;

public class WorldMethods {

    //CONSTANTS***************************

    protected static final int DISTANCE_TO_HELP = 4;

    protected Grid2D space;

    public WorldMethods(Grid2D space){
        this.space = space;
    }

    public boolean noCollisions(Position p) {
        Vector2Double wantedPosition = new Vector2Double(p.x,p.y);
        IVector1 distance = new Vector1Double(0);

        //space.getSpaceObjectsByGridPosition(wantedPosition,type);

        Set terrainSet = space.getNearObjects(wantedPosition,distance,TypesObjects.TERRAIN);
        Set incidentSet = space.getNearObjects(wantedPosition,distance,TypesObjects.INCIDENT);

        Set activeSet = space.getNearObjects(wantedPosition,distance,TypesObjects.WANDERER);
        Set herdingSet = space.getNearObjects(wantedPosition,distance,TypesObjects.HERDING);
        Set conservativeSet = space.getNearObjects(wantedPosition,distance,TypesObjects.CONSERVATIVE);


        if(!terrainSet.isEmpty()) //there is a wall or an obstacle
            return false;
        else if(!incidentSet.isEmpty()) //there are incidents in the way
            return false;
        else if(activeSet.size() > 1) //there are two agents in the position
            return false;
        else if(herdingSet.size() > 1) //there are two agents in the position
            return false;
        else if(conservativeSet.size() > 1) //there are two agents in the position
            return false;

        //System.out.println("agents number - " + agentsSet.size());

        return true;
    }

    public boolean isIncident() {
        return (space.getSpaceObjectsByType(TypesObjects.INCIDENT).length != 0);
    }

    public ISpaceObject[] incidentObjects() {
        return space.getSpaceObjectsByType(TypesObjects.INCIDENT);
    }

    public boolean someoneInMyCell(Position p) {

        Vector2Double wantedPosition = new Vector2Double(p.x,p.y);
        IVector1 distance = new Vector1Double(0);

        Set activeSet = space.getNearObjects(wantedPosition,distance,TypesObjects.WANDERER);
        Set herdingSet = space.getNearObjects(wantedPosition,distance,TypesObjects.HERDING);
        Set conservativeSet = space.getNearObjects(wantedPosition,distance,TypesObjects.CONSERVATIVE);

        if(activeSet.size() > 0)
            return true;
        else if(herdingSet.size() > 0) //there are two agents in the position
            return true;
        else if(conservativeSet.size() > 0) //there are two agents in the position
            return true;

        return false;
    }

    public boolean someoneNeedsHelp(Position actualPosition) {
        int distance = DISTANCE_TO_HELP;
        String[] types = {TypesObjects.HURT_AGENT};
        Vector2Double wantedPosition = new Vector2Double(actualPosition.x, actualPosition.y);

        Set agentsSet = space.getNearGridObjects(wantedPosition, distance, types);

        return !agentsSet.isEmpty();
    }
}
