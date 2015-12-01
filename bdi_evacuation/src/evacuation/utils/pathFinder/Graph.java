package evacuation.utils.pathFinder;

import evacuation.utils.Position;

import java.util.ArrayList;
import java.util.List;

public class Graph
{
	private List<GraphNode> mNodes;
	private List<GraphEdge> mEdges;
	private Position mGoal;

	public Graph(Position goal)
	{
		mGoal = goal;
		mNodes = new ArrayList<>();
		mEdges = new ArrayList<>();
	}

	public void addNode(GraphNode node)
	{
		mNodes.add(node);
	}
	public boolean addNode(Position nodePosition)
	{
		if(containsNode(nodePosition))
			return false;

		mNodes.add(new GraphNode(nodePosition, mGoal));

		return true;
	}

	public boolean addEdge(Position sourcePosition, Position destinationPosition)
	{
		if(containsEdge(sourcePosition, destinationPosition))
			return false;

		GraphNode sourceNode = getNode(sourcePosition);
		if(sourceNode == null)
			return false;

		GraphNode destinationNode = getNode(destinationPosition);
		if(destinationNode == null)
			return false;

		GraphEdge graphEdge = new GraphEdge(sourceNode, destinationNode);
		sourceNode.addEdge(graphEdge);
		mEdges.add(graphEdge);

		return true;
	}

	public GraphNode getNode(Position position)
	{
		for (GraphNode node : mNodes)
		{
			if(position.equals(node.getPosition()))
				return node;
		}

		return null;
	}

	public boolean containsNode(Position position)
	{
		for (GraphNode node : mNodes)
		{
			if(position.equals(node.getPosition()))
				return true;
		}

		return false;
	}
	public boolean containsEdge(Position sourcePosition, Position destinationPosition)
	{
		for (GraphEdge edge : mEdges)
		{
			if(sourcePosition.equals(edge.getSource().getPosition()) && destinationPosition.equals(edge.getDestination().getPosition()))
				return true;
		}

		return false;
	}

	public List<GraphNode> getNodes()
	{
		return mNodes;
	}
}
