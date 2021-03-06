package evacuation.utils;

import jadex.extension.envsupport.environment.SpaceObject;
import jadex.extension.envsupport.math.Vector2Double;
import jadex.extension.envsupport.math.Vector2Int;public class Position {
	public int x;
	public int y;
	
	public Position(){}
	public Position(int x, int y){
		this.x = x;
		this.y = y;
	}
	@Override
	public boolean equals(Object other)
	{
		if(!(other instanceof Position))
			return false;

		Position position = (Position)other;
		return (this.x == position.x) && (this.y == position.y);
	}
	public int distance(Position position)
	{
		int dx = position.x - x;
		int dy = position.y - y;

		return Math.round((float) Math.sqrt(dx * dx + dy * dy));
	}
	public int distance(int x, int y)
	{
		int dx = x - this.x;
		int dy = y - this.y;

		return Math.round((float) Math.sqrt(dx * dx + dy * dy));
	}

	@Override
	public String toString()
	{
		return "Position [" + this.x + ", " + this.y + "]";
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

	public static Position convertSpaceObjectToPosition(Object objPosition) {

		SpaceObject spaceObj = (SpaceObject) objPosition;
		Object pos = spaceObj.getProperty("position");

		try {
			Vector2Double lastPosDouble = (Vector2Double) pos;
			return new Position(lastPosDouble.getXAsInteger(), lastPosDouble.getYAsInteger());
		} catch (Exception e){
			//System.out.println("Unable to cast to double");
		}
		try {
			Vector2Int lastPosInt = (Vector2Int) pos;
			return new Position(lastPosInt.getXAsInteger(), lastPosInt.getYAsInteger());
		} catch (Exception e){
			//System.out.println("Unable to cast to int");
		}

		return new Position();
	}
}