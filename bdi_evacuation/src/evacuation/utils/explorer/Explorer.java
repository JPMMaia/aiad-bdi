package evacuation.utils.explorer;

import evacuation.utils.Position;
import evacuation.utils.terrain.ExploredTerrain;
import evacuation.utils.terrain.Terrain;

public class Explorer
{
	private ExploredTerrain mExploredTerrain;
	private Position mPosition;
	private ExplorerGoal mGoal;

	public Explorer(Terrain terrain, Position position)
	{
		mExploredTerrain = new ExploredTerrain(terrain);
		mPosition = position;
		mGoal = ExplorerGoal.FindExit;
	}

	public boolean move(int x, int y)
	{
		if(mExploredTerrain.isObstacle(x, y))
			return false;

		mExploredTerrain.exploreSquare(x, y);
		mPosition = new Position(x, y);

		return true;
	}

	public Position getNextPosition()
	{
		if(mGoal == ExplorerGoal.FindExit)
		{

		}

		return new Position(0, 0);
	}
	public Position getPosition()
	{
		return mPosition;
	}

	public void setGoal(ExplorerGoal goal)
	{
		mGoal = goal;
	}
	public void setGoal(Position goalPosition)
	{
		mPosition = goalPosition;
		mGoal = ExplorerGoal.FindPosition;
	}
}
