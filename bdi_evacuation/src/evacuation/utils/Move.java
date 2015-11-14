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

    private ArrayList<Position> directions;
    private Random r;

    public Move(){
        r = new Random();

        directions = new ArrayList<>();
        directions.add(new Position(-1,0));
        directions.add(new Position(1,0));
        directions.add(new Position(0,-1));
        directions.add(new Position(0,1));
    }

    public Position getNewPosition(Position lastPosition, int spaceWidth, int spaceHeight) {
        Position direction = directions.get(r.nextInt(4));
        Position newPosition = new Position(lastPosition.x + direction.x, lastPosition.y + direction.y);
        if(isBetweenLimits(newPosition, spaceWidth, spaceHeight)){
            return newPosition;
        }
        return lastPosition;
    }

    public Position getRandomPosition(int spaceWidth, int spaceHeight) {
        return new Position(r.nextInt(spaceWidth), r.nextInt(spaceHeight));
    }

    public boolean isBetweenLimits(Position newPosition, int spaceWidth, int spaceHeight) {
        return (newPosition.x < spaceHeight && newPosition.x >= 0 && newPosition.y < spaceWidth && newPosition.y >= 0);
    }

    public Position getPosition(ISpaceObject myself) {
        Object lastPosition = myself.getProperty("position");
        return convertToPosition(lastPosition);
    }

    private Position convertToPosition(Object lastPosition) {
        try {
            Vector2Int lastPosInt = (Vector2Int) lastPosition;
            return new Position(lastPosInt.getXAsInteger(), lastPosInt.getYAsInteger());
        } catch (Exception e){
            System.out.print("Unable to cast to int");
        }
        try {
            Vector2Double lastPosDouble = (Vector2Double) lastPosition;
            return new Position(lastPosDouble.getXAsInteger(), lastPosDouble.getYAsInteger());
        } catch (Exception e){
            System.out.print("Unable to cast to double");
        }

        return new Position();
    }
}
