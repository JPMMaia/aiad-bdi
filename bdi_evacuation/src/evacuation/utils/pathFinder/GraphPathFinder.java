package evacuation.utils.pathFinder;

import evacuation.utils.Position;
import evacuation.utils.terrain.Door;
import evacuation.utils.terrain.ExploredTerrain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GraphPathFinder
{
	private ExploredTerrain mTerrain;
	private Graph mGraph;
	private List<GraphNode> mOpenList;
	private List<GraphNode> mClosedList;

	public GraphPathFinder(ExploredTerrain terrain)
	{
		mTerrain = terrain;
		mGraph = new Graph();

		// Add doors to the graph:
		List<Door> doors = new ArrayList<>();
		doors.addAll(terrain.getUnexploredDoors());
		doors.addAll(terrain.getExploredDoors());
		DoorsGraphBuilder.build(mGraph, terrain.getExploredRooms(), doors);
	}

	public Door run(Position startPosition)
	{
		// Add start node:
		mGraph.addNode(startPosition);

		while(!mOpenList.isEmpty())
		{

		}

		// Remove start node:
		mGraph.removeNode(startPosition);

		return null;
	}
}
