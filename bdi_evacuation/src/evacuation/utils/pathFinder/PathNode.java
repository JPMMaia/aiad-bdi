package evacuation.utils.pathFinder;

import evacuation.utils.Position;

import java.util.Comparator;

public class PathNode
{
	public static class NodeComparator implements Comparator<PathNode>
	{
		@Override
		public int compare(PathNode o1, PathNode o2)
		{
			if(o1.mPosition.equals(o2.mPosition))
				return 0;

			if(o1.mFScore < o2.mFScore)
				return Integer.MIN_VALUE;

			if(o1.mFScore == o2.mFScore)
				return o1.mHScore - o2.mHScore;

			return Integer.MAX_VALUE;
		}
	}

	protected PathNode mParent;
	protected Position mPosition;
	protected Position mDestination;
	protected int mGScore;
	protected int mHScore;
	protected int mFScore;

	public PathNode(PathNode parent, Position position, Position destination)
	{
		mParent = parent;
		mPosition = position;
		mDestination = destination;
	}

	public void initialize()
	{
		mHScore = calculateHScore(mPosition, mDestination);
		mGScore = calculateGScore(mParent, mPosition);
		mFScore = calculateFScore(mGScore, mHScore);
	}

	@Override
	public boolean equals(Object obj)
	{
		if (!obj.getClass().equals(PathNode.class))
			return false;

		PathNode node = (PathNode) obj;

		return mPosition.equals(node.mPosition);
	}

	@Override
	public String toString()
	{
		return "Node -> FScore: " + mFScore + " | HScore: " + mHScore + " at " + mPosition.toString();
	}

	public void updateScore(PathNode newParent)
	{
		//  Check if the F score is lower when we use the current generated path:
		int newGScore = calculateGScore(newParent, mPosition);
		if (newGScore < mGScore)
		{
			// If it is, update the score and the parent as well:
			mGScore = newGScore;
			mFScore = calculateFScore(mGScore, mHScore);
			mParent = newParent;
		}
	}

	public PathNode getParent()
	{
		return mParent;
	}
	public Position getPosition()
	{
		return mPosition;
	}

	private static int calculateCost(Position previous, Position next)
	{
		int dx = Math.abs(previous.x - next.x);
		int dy = Math.abs(previous.y - next.y);

		// If moving on the diagonal:
		if(dx == 1 && dy == 1)
			return 14;

		// If moving on the horizontal or vertical:
		if(dx == 1 || dy == 1)
			return 10;

		// If not moving:
		return 0;
	}
	private static int calculateGScore(PathNode parent, Position position)
	{
		if (parent == null)
			return 0;

		return parent.mGScore + calculateCost(parent.mPosition, position);
	}
	private static int calculateHScore(Position position, Position destination)
	{
		return Math.abs(position.x - destination.x) + Math.abs(position.y - destination.y);
	}
	private static int calculateFScore(int gScore, int hScore)
	{
		return gScore + hScore;
	}
}