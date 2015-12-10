package evacuation.utils;

import evacuation.processes.WorldGenerator;
import evacuation.utils.terrain.Square;
import jadex.extension.envsupport.environment.ISpaceObject;
import jadex.extension.envsupport.environment.SpaceObject;
import jadex.extension.envsupport.environment.space2d.Grid2D;
import jadex.extension.envsupport.math.*;

import java.util.*;

public class WorldMethods {

    static Map numAgentsByCell = Collections.synchronizedMap(new HashMap<String,Integer>());

    public synchronized void putAgentInNewCellMap(Position currentPosition) {
        String key = currentPosition.x + "." + currentPosition.y;
        Integer value = 1;

        if(numAgentsByCell.containsKey(key)) {
            value = (Integer) numAgentsByCell.get(key);
            numAgentsByCell.remove(key);
            value++;
            numAgentsByCell.put(key, value);

            // Adding obstacle:
            if(value >= 2)
                WorldGenerator.getTerrain().setObstacle(currentPosition.x, currentPosition.y, true);
        }
        else{
            numAgentsByCell.put(key, value);
        }

        if(value > 2)
            System.out.println("value - " + value);
    }

    public synchronized Integer getNumAgentInCellMap(Position currentPosition) {
        String key = currentPosition.x + "." + currentPosition.y;

        if(numAgentsByCell.containsKey(key))
            return (Integer) numAgentsByCell.get(key);

        return 0;
    }

    public synchronized void removeAgentFromOldCellMap(Position currentPosition) {
        String key = currentPosition.x + "." + currentPosition.y;
        Integer value = 0;

        if(numAgentsByCell.containsKey(key)) {
            value = (Integer) numAgentsByCell.get(key);
            numAgentsByCell.remove(key);
            value--;

            // Remove obstacle:
            if(value <= 2)
                WorldGenerator.getTerrain().setObstacle(currentPosition.x, currentPosition.y, false);

            if(value > 0)
                numAgentsByCell.put(key, value);
        }
    }

    public int countHurt = 0;

    //TWO AGENTS SAME CELL

    public synchronized void resolveTwoAgentsInSameCell(Position currentPosition, Position nextPosition) {
        if (getNumAgentInCellMap(currentPosition) >= 2) {
            deleteSomeoneInMyCellObject(currentPosition);
        }

        removeAgentFromOldCellMap(currentPosition);
        if(nextPosition != null) {
            checkAndMakeSomeoneInMyCell(nextPosition);
            putAgentInNewCellMap(nextPosition);
        }
    }

    public boolean isPositionADoor(Position p){
        Square square = WorldGenerator.getTerrain().getSquare(p.x, p.y);
        if(square.isDoor())
        {
            return square.getDoor().isExit();
        }
        return false;
    }


    public synchronized void deleteSomeoneInMyCellObject(Position currentPosition) {

        Vector2Double wantedPosition = new Vector2Double(currentPosition.x,currentPosition.y);
        IVector1 distance = new Vector1Double(0);

        Set objectSet = space.getNearObjects(wantedPosition,distance,TypesObjects.SAME_CELL);
        for(Object it : objectSet){
            space.destroyAndVerifySpaceObject(((SpaceObject) it).getId());
        }
    }

    public synchronized void checkAndMakeSomeoneInMyCell(Position newPosition) {
        //if there is another guy in the new cell, create a someone in my cell
        if(!isPositionADoor(newPosition)) {
            String key = newPosition.x + "." + newPosition.y;
            int numAgents = getNumAgentInCellMap(newPosition);
            if (numAgents >= 1) {
                makeObjectInCell(newPosition, TypesObjects.SAME_CELL);
            }
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////

    protected Grid2D space;

    public WorldMethods(Grid2D space){
        this.space = space;
    }

    //COLLISIONS QUERIES

    public boolean noCollisionsInPosition(Position p) {
        Vector2Double wantedPosition = new Vector2Double(p.x,p.y);
        IVector1 distance = new Vector1Double(0);

        //Set terrainSet = space.getNearObjects(wantedPosition,distance,TypesObjects.TERRAIN);
        //Set incidentSet = space.getNearObjects(wantedPosition,distance,TypesObjects.INCIDENT);
        Set hurtSet = space.getNearObjects(wantedPosition,distance,TypesObjects.HURT_AGENT);

        //if(!terrainSet.isEmpty()) //there is a wall or an obstacle
        //    return false;
        //else if(!incidentSet.isEmpty()) //there are incidents in the way
        //    return false;
        if(!hurtSet.isEmpty()) //there are two agents in the position
            return false;
        else if(getNumAgentInCellMap(p) > 1)
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

    public ISpaceObject[] getTerrainObjects() {
        return space.getSpaceObjectsByType(TypesObjects.TERRAIN);
    }

    // FIND PATH QUERIES

    /*
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
    }*/

    public ISpaceObject pickClosestObject(ISpaceObject[] objects, Position currentPosition) {

        if(objects.length > 0){
            //System.out.println("objects lenght - " + objects.length);
            int pos = 0;

            Position wantedPosition = Position.convertToPosition(objects[pos].getProperty(TypesProperties.POSITION));

            IVector1 distance = getDistanceBetweenTwoPositions(currentPosition, wantedPosition);
            Vector2Double currentPositionV2D = new Vector2Double(currentPosition.x, currentPosition.y);

            for(int i = 1; i < objects.length; i++){
                IVector1 newDistance = space.getDistance(currentPositionV2D, (IVector2) objects[i].getProperty(TypesProperties.POSITION));

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

    //SOCIAL AGENT QUERIES

    public ISpaceObject someoneNeedsHelp(Position actualPosition, int distance) {
        String[] types = {TypesObjects.HURT_AGENT};
        Vector2Double wantedPosition = new Vector2Double(actualPosition.x, actualPosition.y);

        Set agentsSet = space.getNearGridObjects(wantedPosition, distance, types);

        for (Iterator<ISpaceObject> it = agentsSet.iterator(); it.hasNext(); ) {
            ISpaceObject obj;
            obj = it.next();
            Position objPosition = Position.convertToPosition(obj.getProperty(TypesProperties.POSITION));
            if(!isWallBetween(actualPosition,objPosition))
                return obj;
        }

        return null;
    }

    //GET OBJECT IN THE SAME POSITION

    public ISpaceObject getObject(Position currentPosition, String type) {

        Vector2Double wantedPosition = new Vector2Double(currentPosition.x,currentPosition.y);
        //IVector1 distance = new Vector1Double(0);

        ArrayList<ISpaceObject> set = (ArrayList<ISpaceObject>) space.getSpaceObjectsByGridPosition(wantedPosition,type);

        if(set == null || set.isEmpty())
            return null;

        for (Iterator<ISpaceObject> it = set.iterator(); it.hasNext(); ) {
            ISpaceObject obj;
            obj = it.next();
            return obj;
        }

        return null;
    }

    // NEW OBJECTS METHODS

    public SpaceObject makeObjectInCell(Position targetPosition, String type){
        SpaceObject res;

        Map<String, Object> properties = new HashMap<>();
        properties.put("position", new Vector2Int(targetPosition.x, targetPosition.y));
        res = (SpaceObject) space.createSpaceObject(type, properties, null);

        if(type.equals(TypesObjects.HURT_AGENT))
            countHurt++;

        return res;
    }

    public SpaceObject getPush(Position currentPosition, HashSet<SpaceObject> pushSet) {

        Vector2Double wantedPosition = new Vector2Double(currentPosition.x,currentPosition.y);
        IVector1 distance = new Vector1Double(0);

        Set set = space.getNearObjects(wantedPosition,distance,TypesObjects.PUSH_AGENT);

        if(set == null || set.isEmpty())
            return null;

        for (Iterator<ISpaceObject> it = set.iterator(); it.hasNext(); ) {
            ISpaceObject obj;
            obj = it.next();

            if(!pushSet.contains(obj))
                return (SpaceObject) obj;
        }

        return null;
    }

    //WALL OBJECTS BETWEEN

    public boolean isWallBetween(Position pos1, Position pos2){

        ArrayList<Position> positionsToCheck = BresenhamLineAlgorithm.line(pos1.x,pos1.y,pos2.x,pos2.y);

        for(Position p : positionsToCheck){
            SpaceObject t = (SpaceObject) getObject(p, TypesObjects.TERRAIN);
            if(t != null && getTerrainType(t).equals("wall"))
                return true;
        }
        return false;
    }

    public Position getDoorPosition(SpaceObject door) {
        Position destinyPosition = Position.convertToPosition(door.getProperty(TypesProperties.POSITION));
        return destinyPosition;
    }

    public boolean agentsAlive() {
        ISpaceObject[] herdingSet = space.getSpaceObjectsByType(TypesObjects.HERDING);
        ISpaceObject[] activeSet = space.getSpaceObjectsByType(TypesObjects.WANDERER);
        ISpaceObject[] conservativeSet = space.getSpaceObjectsByType(TypesObjects.CONSERVATIVE);

        int sumAlive = herdingSet.length + activeSet.length + conservativeSet.length;


        if(sumAlive > 0)
            return true;
        return false;
    }

    public int countHurt() {
        return countHurt;
    }

    public String getIncidentType() {
        String[] str = new String[] {"fire", "water", "terrorism"};
        int intType = 0;
        ISpaceObject[] incidents = getIncidentObjects();
        if(incidents.length > 0){
            SpaceObject incident = (SpaceObject) incidents[0];
            Object objType = incident.getProperty("type");
            String stringType  = objType.toString();
            intType = Integer.parseInt(stringType);
        }
        return str[intType];
    }

    public String getTerrainType(SpaceObject terrain) {

        String[] str = new String[] {"grownd", "wall"};

        Object objType = terrain.getProperty("type");
        String stringType  = objType.toString();
        int intType = Integer.parseInt(stringType);

        return str[intType];
    }
}