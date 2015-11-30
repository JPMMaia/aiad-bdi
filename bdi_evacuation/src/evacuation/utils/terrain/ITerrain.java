package evacuation.utils.terrain;

public interface ITerrain
{
	Square getSquare(int x, int y);
	boolean isObstacle(int x, int y);
}
