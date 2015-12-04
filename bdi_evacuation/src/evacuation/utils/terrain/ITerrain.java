package evacuation.utils.terrain;

import java.util.List;

public interface ITerrain
{
	Square getSquare(int x, int y);
	boolean isObstacle(int x, int y);
}
