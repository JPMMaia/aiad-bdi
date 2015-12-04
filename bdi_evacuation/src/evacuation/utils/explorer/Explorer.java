package evacuation.utils.explorer;

import evacuation.utils.Position;
import evacuation.utils.pathFinder.PathFinder;
import evacuation.utils.terrain.*;

import java.util.List;

public class Explorer
{
	private ExploredTerrain mExploredTerrain;
	private Position mPosition;
	private ExplorerGoal mGoal;
	private List<Position> mCurrentPath;

	public Explorer(Terrain terrain, Position position)
	{
		mExploredTerrain = new ExploredTerrain(terrain);
		mPosition = position;
		mGoal = ExplorerGoal.FindExit;

		mExploredTerrain.exploreSquare(mPosition.x, mPosition.y);
	}

	public boolean move()
	{
		if(mCurrentPath == null || mCurrentPath.isEmpty())
		{
			calculateNextPosition();
			if(mCurrentPath.isEmpty())
				return false;
		}

		// Try to move to the next position:
		Position position = mCurrentPath.get(0);
		if(!move(position.x, position.y))
			return false;

		// Remove position as we have already moved:
		mCurrentPath.remove(0);

		return true;
	}
	private boolean move(int x, int y)
	{
		if(mExploredTerrain.isObstacle(x, y))
			return false;

		mExploredTerrain.exploreSquare(x, y);
		mPosition = new Position(x, y);

		return true;
	}

	private Position calculateNextPosition()
	{
		if(mGoal == ExplorerGoal.FindExit)
		{
			Position exit = findExit();
			mCurrentPath = PathFinder.run(mExploredTerrain, mPosition, exit);
			if(mCurrentPath.size() > 1)
			{
				mCurrentPath.remove(0);
				return mCurrentPath.get(0);
			}
		}

		return new Position(-1, -1);
	}
	private Position findExit()
	{
		Door nearestExitDoor = mExploredTerrain.findNearestExitDoor(mPosition.x, mPosition.y);
		if(nearestExitDoor == NullDoor.getInstance())
			return mExploredTerrain.findNearestUnexploredDoor(mPosition.x, mPosition.y).getPosition();

		return nearestExitDoor.getPosition();
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
