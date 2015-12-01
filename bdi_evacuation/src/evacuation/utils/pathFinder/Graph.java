package evacuation.utils.pathFinder;

import evacuation.utils.Position;

import java.util.ArrayList;
import java.util.List;

public class Graph
{
	private List<GraphNode> mNodes;

	public Graph()
	{
		mNodes = new ArrayList<>();
	}

	public boolean addNode(Position nodePosition)
	{
		if(containsNode(nodePosition))
			return false;

		mNodes.add(new GraphNode(nodePosition));

		return true;
	}
	public boolean removeNode(Position nodePosition)
	{
		for (GraphNode node : mNodes)
		{
			if(nodePosition.equals(node.getPosition()))
			{
				// Remove node:
				mNodes.remove(node);

				// Remove edges:
				for (GraphEdge edge : node.getEdges())
					removeEdge(edge);

				return true;
			}
		}

		return false;
	}

	public boolean addEdge(Position sourcePosition, Position destinationPosition)
	{
		GraphNode sourceNode = getNode(sourcePosition);
		if(sourceNode == null)
			return false;

		GraphNode destinationNode = getNode(destinationPosition);
		if(destinationNode == null)
			return false;

		GraphEdge graphEdge = new GraphEdge(sourceNode, destinationNode);
		sourceNode.addEdge(graphEdge);

		return true;
	}
	public void removeEdge(GraphEdge edge)
	{
		edge.getDestination().removeEdge(edge.getSource().getPosition());
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

	public List<GraphNode> getNodes()
	{
		return mNodes;
	}
}
