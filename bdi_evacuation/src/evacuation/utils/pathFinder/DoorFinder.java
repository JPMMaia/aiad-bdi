package evacuation.utils.pathFinder;

import evacuation.utils.Position;
import evacuation.utils.terrain.Door;
import evacuation.utils.terrain.ExploredTerrain;
import evacuation.utils.terrain.Square;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class DoorFinder
{
	private Comparator<GraphNode> mNodeComparator;
	private ExploredTerrain mTerrain;
	private Graph mGraph;
	private List<GraphNode> mOpenList;
	private List<GraphNode> mClosedList;

	public DoorFinder(ExploredTerrain terrain)
	{
		mTerrain = terrain;
		mGraph = new Graph();
		mNodeComparator = new GraphNode.NodeComparator();
		mOpenList = new ArrayList<>();
		mClosedList = new ArrayList<>();

		// Add doors to the graph:
		List<Door> doors = new ArrayList<>();
		doors.addAll(terrain.getUnexploredDoors());
		doors.addAll(terrain.getExploredDoors());
		DoorsGraphBuilder.build(mGraph, terrain.getTerrain().getRooms(), terrain.getTerrain().getDoors());
	}

	public Door run(Position startPosition)
	{
		GraphNode currentSolution = null;

		mOpenList.clear();
		mClosedList.clear();

		// Add first node:
		Square startSquare = mTerrain.getSquare(startPosition.x, startPosition.y);
		if(!startSquare.isDoor())
			DoorsGraphBuilder.addNode(mGraph, mTerrain.getSquare(startPosition.x, startPosition.y));

		mOpenList.add(mGraph.getNode(new Position(startPosition.x, startPosition.y)));
		while(!mOpenList.isEmpty())
		{
			// Get square with lowest score:
			Collections.sort(mOpenList, mNodeComparator);
			GraphNode currentNode = mOpenList.get(0);

			// Remove node from open list and add it to the closed list:
			mOpenList.remove(currentNode);
			mClosedList.add(currentNode);

			List<GraphEdge> edges = currentNode.getEdges();
			for (GraphEdge edge : edges)
			{
				GraphNode destination = edge.getDestination();

				// Ignore node if it is in the closed list:
				if(mClosedList.contains(destination))
					continue;

				// Add node to the open list, if it is not already there:
				if(!mOpenList.contains(destination))
				{
					// Calculate scores for the walkable node and add it to the open list:
					destination.setParent(currentNode);
					destination.setCost(calculateCost(currentNode, destination));
					mOpenList.add(destination);

					// If this node corresponds to the destination, we reached the destination:
					Position position = destination.getPosition();
					Square square = mTerrain.getSquare(position.x, position.y);
					if(square.isDoor())
					{
						// If we found an unexplored door, then if have reached our solution:
						if(!mTerrain.isExplored(square))
						{
							if(currentSolution == null || destination.getCost() < currentSolution.getCost())
								currentSolution = destination;
						}

					}

					continue;
				}

				// If the node is already in the open list, try to improve its score:
				updateScore(destination, currentNode);
			}

			if(currentSolution != null)
				break;
		}

		// Remove start node:
		if(!startSquare.isDoor())
			mGraph.removeNode(startPosition);

		if(currentSolution != null)
		{
			Position position = currentSolution.getPosition();
			return mTerrain.getSquare(position.x, position.y).getDoor();
		}

		return null;
	}

	private static int calculateCost(GraphNode previous, GraphNode next)
	{
		return previous.getCost() + previous.getPosition().distance(next.getPosition());
	}
	private static void updateScore(GraphNode node, GraphNode newParent)
	{
		int currentCost = node.getCost();
		int newCost = calculateCost(newParent, node);

		if(newCost < currentCost)
		{
			node.setParent(newParent);
			node.setCost(newCost);
		}
	}
}