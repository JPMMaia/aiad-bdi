package evacuation.utils;

import jadex.extension.envsupport.environment.ISpaceObject;
import jadex.extension.envsupport.environment.SpaceObject;
import jadex.extension.envsupport.environment.space2d.Grid2D;
import jadex.extension.envsupport.math.*;

import java.util.*;

public class WorldMethods {

    static Map numAgentsByCell = Collections.synchronizedMap(new HashMap<String,Integer>());


    public synchronized void putAgentInNewCell(Position currentPosition) {
        String key = currentPosition.x + "." + currentPosition.y;
        Integer value = 1;

        if(numAgentsByCell.containsKey(key)) {
            value = (Integer) numAgentsByCell.get(key);
            numAgentsByCell.remove(key);
            value++;
            numAgentsByCell.put(key, value);
        }
        else{
            numAgentsByCell.put(key, value);
        }
    }

    public synchronized Integer getNumAgentInCell(Position currentPosition) {
        String key = currentPosition.x + "." + currentPosition.y;

        if(numAgentsByCell.containsKey(key))
            return (Integer) numAgentsByCell.get(key);

        return 0;
    }

    public synchronized void removeAgentFromOldCell(Position currentPosition) {
        String key = currentPosition.x + "." + currentPosition.y;
        Integer value;

        if(numAgentsByCell.containsKey(key)) {
            value = (Integer) numAgentsByCell.get(key);
            numAgentsByCell.remove(key);
            value--;

            if(value > 0)
                numAgentsByCell.put(key, value);
        }
    }
    ///////////////////////////////////////////////////////////////////////////////////////////

    protected Grid2D space;

    public WorldMethods(Grid2D space){
        this.space = space;
    }

    //COLLISIONS QUERIES

    public synchronized boolean noCollisionsInPosition(Position p) {
        Vector2Double wantedPosition = new Vector2Double(p.x,p.y);
        IVector1 distance = new Vector1Double(0);

        Set terrainSet = space.getNearObjects(wantedPosition,distance,TypesObjects.TERRAIN);
        Set incidentSet = space.getNearObjects(wantedPosition,distance,TypesObjects.INCIDENT);
        Set hurtSet = space.getNearObjects(wantedPosition,distance,TypesObjects.HURT_AGENT);

        if(!terrainSet.isEmpty()) //there is a wall or an obstacle
            return false;
        else if(!incidentSet.isEmpty()) //there are incidents in the way
            return false;
        else if(!hurtSet.isEmpty()) //there are two agents in the position
            return false;
        else if(getNumAgentInCell(p) >= 2)
            return false;

        return true;
    }

    // INCIDENT QUERIES

    public boolean isIncident() {
        return (space.getSpaceObjectsByType(TypesObjects.INCIDENT).length != 0);
    }

    public ISpaceObject[] getIncidentObjects() {
        return space.getSpaceObjectsByType(TypesObjects.INCIDENT);
    }

    // FIND PATH QUERIES

    public Position findPathToObject(ISpaceObject object, Position currentPosition) {
        if(object != null){
            //space.getShortestDirection()
            Position destinyPosition = Position.convertToPosition(object.getProperty(TypesProperties.POSITION));
            Position oldPosition = new Position(currentPosition.x, currentPosition.y);
            Position newPosition = null;

            if(Math.abs(destinyPosition.x - oldPosition.x) > Math.abs(destinyPosition.y - oldPosition.y)) { //move in x
                if(destinyPosition.x < oldPosition.x)
                    newPosition = new Position(currentPosition.x-1, currentPosition.y);
                else if(destinyPosition.x > oldPosition.x)
                    newPosition = new Position(currentPosition.x+1, currentPosition.y);
            }
            else { //move in y
                if(destinyPosition.y < oldPosition.y)
                    newPosition = new Position(currentPosition.x, currentPosition.y-1);
                else if(destinyPosition.y > oldPosition.y)
                    newPosition = new Position(currentPosition.x, currentPosition.y+1);
            }

            if(newPosition != null && noCollisionsInPosition(newPosition)) {
                return newPosition;
            }
        }

        //samePosition = true;
        return currentPosition;
    }

    public ISpaceObject pickClosestObject(ISpaceObject[] objects, HashSet<Object> targets, Position currentPosition) {

        if(objects.length > 0){
            //System.out.println("objects lenght - " + objects.length);
            int pos = 0;

            Position wantedPosition = Position.convertToPosition(objects[pos].getProperty(TypesProperties.POSITION));

            IVector1 distance = getDistanceBetweenTwoPositions(currentPosition, wantedPosition);
            Vector2Double currentPositionV2D = new Vector2Double(currentPosition.x, currentPosition.y);

            for(int i = 1; i < objects.length; i++){
                IVector1 newDistance = space.getDistance(currentPositionV2D, (IVector2) objects[i].getProperty(TypesProperties.POSITION));

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

    public IVector1 getDistanceBetweenTwoPositions(Position p1, Position p2){
        Vector2Double p1v2d = new Vector2Double(p1.x,p1.y);
        Vector2Double p2v2d = new Vector2Double(p2.x,p2.y);

        return space.getDistance(p1v2d, p2v2d);
    }

    public IVector1 getDistanceBetweenTwoIVector2(IVector2 p1, IVector2 p2){
        return space.getDistance(p1, p2);
    }

    //SOCIAL AGENT QUERIES

    public boolean someoneNeedsHelp(Position actualPosition, int distance) {
        String[] types = {TypesObjects.HURT_AGENT};
        Vector2Double wantedPosition = new Vector2Double(actualPosition.x, actualPosition.y);

        Set agentsSet = space.getNearGridObjects(wantedPosition, distance, types);

        return !agentsSet.isEmpty();
    }

    //GET CURE OBJECT IN THE SAME POSITION

    public ISpaceObject getCureObject(Position currentPosition) {

        Vector2Double wantedPosition = new Vector2Double(currentPosition.x,currentPosition.y);
        IVector1 distance = new Vector1Double(0);

        Set cureSet = space.getNearObjects(wantedPosition,distance,TypesObjects.CURE_AGENT);

        if(cureSet == null || cureSet.isEmpty())
            return null;

        for (Iterator<ISpaceObject> it = cureSet.iterator(); it.hasNext(); ) {
            ISpaceObject obj;
            obj = it.next();
            return obj;
        }

        return null;
    }

    // NEW OBJECTS METHODS


    private SpaceObject makeTwoObjectsSameCell(Position targetPosition, String type){
        SpaceObject res;

        Map<String, Object> properties = new HashMap<>();
        properties.put("position", new Vector2Int(targetPosition.x, targetPosition.y));
        res = (SpaceObject) space.createSpaceObject(type, properties, null);

        return res;
    }

    public void makeSomeoneInMyCell(Position newPosition) {
        //if there is another guy in the new cell, create a someone in my cell
        //if(someoneInMyCell(newPosition)){
        String key = newPosition.x + "." + newPosition.y;
        if(numAgentsByCell.containsKey(key) && numAgentsByCell.get(key) == 1){
            makeTwoObjectsSameCell(newPosition, TypesObjects.SAME_CELL);
        }
    }

    public void deleteSomeoneInMyCellObject(Position currentPosition) {

        Vector2Double wantedPosition = new Vector2Double(currentPosition.x,currentPosition.y);
        IVector1 distance = new Vector1Double(0);

        Set objectSet = space.getNearObjects(wantedPosition,distance,TypesObjects.SAME_CELL);
        for(Object it : objectSet){
            space.destroyAndVerifySpaceObject(((SpaceObject) it).getId());
        }
    }
}






 /*
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
    }*/