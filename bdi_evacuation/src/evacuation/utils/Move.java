package evacuation.utils;

import jadex.extension.envsupport.environment.ISpaceObject;
import jadex.extension.envsupport.math.Vector2Double;
import jadex.extension.envsupport.math.Vector2Int;

import java.util.ArrayList;
import java.util.Random;

public class Move {

    //ver o que a space. fornece para ajudar nos algoritmos. Pelo menos o caminho mais curto ele tem

    static public ArrayList<Position> directions;
    public static Random r;
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
        if(lastPosition != null) {
            Position direction = directions.get(r.nextInt(4));
            Position newPosition = new Position(lastPosition.x + direction.x, lastPosition.y + direction.y);
            if (isBetweenLimits(newPosition)) {
                return newPosition;
            }
        }
        return lastPosition;
    }

    public Position getRandomPosition() {
        return new Position(r.nextInt(spaceWidth), r.nextInt(spaceHeight));
    }

    public boolean isBetweenLimits(Position newPosition) {
        return (newPosition.x < spaceWidth && newPosition.x >= 0 && newPosition.y < spaceHeight && newPosition.y >= 0);
    }

    public Position getPosition(ISpaceObject myself) {
        Object lastPosition = myself.getProperty("position");
        return Position.convertToPosition(lastPosition);
    }

    public static double distanceBetween(Position nextPosition, Object position) {
        Position incident = Position.convertToPosition(position);
        double difX = (nextPosition.x - incident.x);
        double difY = (nextPosition.y - incident.y);
        return Math.sqrt(difX*difX + difY*difY);
    }
}
