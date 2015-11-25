package evacuation.utils.pathFinder;

import evacuation.utils.Position;
import evacuation.utils.terrain.Terrain;

import java.util.*;

public class PathFinder
{
	private Comparator<Node> mNodeComparator;
	private List<Node> mOpenList;
	private List<Node> mClosedList;
	private Position mDestination;
	private Terrain mTerrain;

	public PathFinder()
	{
		mOpenList = new ArrayList<>();
		mClosedList = new ArrayList<>();
	}

	public void initialize(Terrain terrain, Position start, Position destination)
	{
		mNodeComparator = new Node.NodeComparator();
		Node startNode = new Node(null, start, destination);
		startNode.initialize();
		mOpenList.add(startNode);
		mDestination = destination;
		mTerrain = terrain;
	}

	public List<Position> run()
	{
		while(!mOpenList.isEmpty())
		{
			// Get square with lowest score:
			Collections.sort(mOpenList, mNodeComparator);
			Node currentNode = mOpenList.get(0);

			// Remove node from open list and add it to the closed list:
			mOpenList.remove(currentNode);
			mClosedList.add(currentNode);

			List<Node> walkableNodes = getWalkableNodes(currentNode);
			for (Node walkableNode : walkableNodes)
			{
				// Ignore node if it is in the closed list:
				if(mClosedList.contains(walkableNode))
					continue;

				// Add node to the open list, if it is not already there:
				if(!mOpenList.contains(walkableNode))
				{
					// Calculate scores for the walkable node and add it to the open list:
					walkableNode.initialize();
					mOpenList.add(walkableNode);

					// If this node corresponds to the destination, we reached the destination:
					if(walkableNode.getPosition().equals(mDestination))
						return findSolution(walkableNode);

					continue;
				}

				// If the node is already in the open list, try to improve its score:
				walkableNode.updateScore(currentNode);
			}
		}

		// Solution not found, return empty list:
		return new ArrayList<>();
	}

	private List<Node> getWalkableNodes(Node parent)
	{
		Position position = parent.getPosition();
		int xWest = position.x - 1;
		int xEast = position.x + 1;
		int yNorth = position.y - 1;
		int ySouth = position.y + 1;

		List<Node> walkableNodes = new ArrayList<>();

		if(!mTerrain.isObstacle(xWest, position.y))
			walkableNodes.add(new Node(parent, new Position(xWest, position.y), mDestination));

		if(!mTerrain.isObstacle(xEast, position.y))
			walkableNodes.add(new Node(parent, new Position(xEast, position.y), mDestination));

		if(!mTerrain.isObstacle(position.x, yNorth))
			walkableNodes.add(new Node(parent, new Position(position.x, yNorth), mDestination));

		if(!mTerrain.isObstacle(position.x, ySouth))
			walkableNodes.add(new Node(parent, new Position(position.x, ySouth), mDestination));

		return walkableNodes;
	}

	private List<Position> findSolution(Node finalNode)
	{
		List<Position> solution = new ArrayList<>();
		solution.add(finalNode.getPosition());

		while(finalNode.getParent() != null)
		{
			finalNode = finalNode.getParent();
			solution.add(finalNode.getPosition());
		}

		return solution;
	}

	public static List<Position> run(Terrain terrain, Position start, Position destination)
	{
		PathFinder finder = new PathFinder();
		finder.initialize(terrain, start, destination);

		return finder.run();
	}
}