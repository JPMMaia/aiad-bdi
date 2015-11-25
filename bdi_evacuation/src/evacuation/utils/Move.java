package evacuation.utils;

import jadex.extension.envsupport.environment.ISpaceObject;
import jadex.extension.envsupport.math.Vector2Double;
import jadex.extension.envsupport.math.Vector2Int;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Paula on 12/11/2015.
 */
public class Move {

    //ver o que a space. fornece para ajudar nos algoritmos. Pelo menos o caminho mais curto ele tem

    static private ArrayList<Position> directions;
    static private Random r;
    static int spaceWidth;
    static int spaceHeight;
    public static double maximumDistance;
    public static double maximumIncidents;

    public Move(int spaceWidth, int spaceHeight){
        r = new Random();

        directions = new ArrayList<>();
        directions.add(new Position(-1,0));
        directions.add(new Position(1,0));
        directions.add(new Position(0,-1));
        directions.add(new Position(0,1));

        this.spaceWidth = spaceWidth;
        this.spaceHeight = spaceHeight;
        this.maximumDistance = Math.sqrt(spaceHeight*spaceHeight+spaceWidth*spaceWidth);
        this.maximumIncidents = spaceHeight*spaceWidth;
    }

    public Position getNewPosition(Position lastPosition) {
        Position direction = directions.get(r.nextInt(4));
        Position newPosition = new Position(lastPosition.x + direction.x, lastPosition.y + direction.y);
        if(isBetweenLimits(newPosition)){
            return newPosition;
        }
        return lastPosition;
    }

    public Position getRandomPosition() {
        return new Position(r.nextInt(spaceWidth), r.nextInt(spaceHeight));
    }

    public boolean isBetweenLimits(Position newPosition) {
        return (newPosition.x < spaceHeight && newPosition.x >= 0 && newPosition.y < spaceWidth && newPosition.y >= 0);
    }

    public Position getPosition(ISpaceObject myself) {
        Object lastPosition = myself.getProperty("position");
        return convertToPosition(lastPosition);
    }

    public static Position convertToPosition(Object objPosition) {

        try {
            Vector2Double lastPosDouble = (Vector2Double) objPosition;
            return new Position(lastPosDouble.getXAsInteger(), lastPosDouble.getYAsInteger());
        } catch (Exception e){
            //System.out.println("Unable to cast to double");
        }
        try {
            Vector2Int lastPosInt = (Vector2Int) objPosition;
            return new Position(lastPosInt.getXAsInteger(), lastPosInt.getYAsInteger());
        } catch (Exception e){
            //System.out.println("Unable to cast to int");
        }

        return new Position();
    }

    public static double distanceBetween(Position nextPosition, Object position) {
        Position incident = convertToPosition(position);
        double difX = (nextPosition.x - incident.x);
        double difY = (nextPosition.y - incident.y);
        return Math.sqrt(difX*difX + difY*difY);
    }
}
