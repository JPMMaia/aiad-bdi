package evacuation.utils.explorer;

import evacuation.utils.NullPosition;
import evacuation.utils.Position;
import evacuation.utils.pathFinder.PathFinder;
import evacuation.utils.terrain.*;

import java.util.List;

public class Explorer
{
	private ExploredTerrain mExploredTerrain;
	private Position mPosition;
	private ExplorerGoal mGoal;
	private Position mGoalPosition;
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
		calculateNextPosition();
		if(mCurrentPath.isEmpty())
			return false;

		// Try to move to the next position:
		Position position = mCurrentPath.get(0);
		move(position.x, position.y);

		// Remove position as we have already moved:
		mCurrentPath.remove(0);

		return true;
	}
	private void move(int x, int y)
	{
		mExploredTerrain.exploreSquare(x, y);
		mPosition = new Position(x, y);
	}

	private Position calculateNextPosition()
	{
		if(mGoal == ExplorerGoal.FindExit)
			mGoalPosition = findExit();

		Position destination;
		if(mExploredTerrain.isObstacle(mGoalPosition.x, mGoalPosition.y) && !mExploredTerrain.getSquare(mGoalPosition.x, mGoalPosition.y).isWall())
			destination = mExploredTerrain.findNearestUnexploredDoor(mGoalPosition.x, mGoalPosition.y).getPosition();
		else
			destination = mGoalPosition;

		mCurrentPath = PathFinder.run(mExploredTerrain, mPosition, destination);
		if(mCurrentPath.size() > 1)
		{
			mCurrentPath.remove(0);
			return mCurrentPath.get(0);
		}

		return NullPosition.getInstance();
	}
	private Position findExit()
	{
		Door nearestExitDoor = mExploredTerrain.findNearestExitDoor(mPosition.x, mPosition.y);
		if(nearestExitDoor == NullDoor.getInstance())
			return mExploredTerrain.findNearestUnexploredDoor(mPosition.x, mPosition.y).getPosition();

		return nearestExitDoor.getPosition();
	}

	public boolean reachedExit()
	{
		Square square = mExploredTerrain.getSquare(mPosition.x, mPosition.y);
		if(!square.isDoor())
			return false;

		return square.getDoor().isExit();
	}

	public Position getPosition()
	{
		return mPosition;
	}

	public void setGoal(ExplorerGoal goal)
	{
		mGoal = goal;
	}

	public void setGoal(Position goalPosition, boolean movable)
	{
		mGoal = ExplorerGoal.FindPosition;
		mGoalPosition = goalPosition;
	}
}
