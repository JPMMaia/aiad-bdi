package evacuation.utils.pathFinder;

import evacuation.utils.Position;

import java.util.ArrayList;
import java.util.List;

public class GraphNode
{
	private Position mPosition;
	private Position mGoal;
	private List<GraphEdge> mEdges;

	public GraphNode(Position position, Position goal)
	{
		mPosition = position;
		mGoal = goal;
		mEdges = new ArrayList<>();
	}

	public void addEdge(GraphEdge edge)
	{
		mEdges.add(edge);
	}

	public List<GraphEdge> getEdges()
	{
		return mEdges;
	}
	public Position getPosition()
	{
		return mPosition;
	}
}