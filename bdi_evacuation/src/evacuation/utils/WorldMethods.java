package evacuation.utils;

import jadex.extension.envsupport.environment.space2d.Grid2D;
import jadex.extension.envsupport.math.IVector1;
import jadex.extension.envsupport.math.Vector1Double;
import jadex.extension.envsupport.math.Vector2Double;

import java.util.Set;

public class WorldMethods {

    protected Grid2D space;

    public WorldMethods(Grid2D space){
        this.space = space;
    }

    public boolean noCollisions(Position p) {
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
