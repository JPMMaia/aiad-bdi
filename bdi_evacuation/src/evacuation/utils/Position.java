package evacuation.utils;

import jadex.extension.envsupport.math.IVector1;
import jadex.extension.envsupport.math.Vector2Double;
import jadex.extension.envsupport.math.Vector2Int;

public class Position {
	public int x;
	public int y;
	
	public Position(){}
	public Position(int x, int y){
		this.x = x;
		this.y = y;
	}

	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof Position))
			return false;

		Position position = (Position) obj;
		return position.x == this.x && position.y == this.y;
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


}
