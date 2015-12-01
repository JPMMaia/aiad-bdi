package evacuation.utils.pathFinder;

import evacuation.utils.Position;

import java.util.ArrayList;
import java.util.List;

public class GraphNode
{
	private Position mPosition;
	private List<GraphEdge> mEdges;

	public GraphNode(Position position)
	{
		mPosition = position;
		mEdges = new ArrayList<>();
	}

	public void addEdge(GraphEdge edge)
	{
		mEdges.add(edge);
	}
	public void removeEdge(Position destination)
	{
		for (GraphEdge edge : mEdges)
		{
			if(destination.equals(edge.getDestination().getPosition()))
			{
				mEdges.remove(edge);
				break;
			}
		}
	}

	public List<GraphEdge> getEdges()
	{
		return mEdges;
	}
	public Position getPosition()
	{
		return mPosition;
	}

	@Override
	public boolean equals(Object obj)
	{
		if(!(obj instanceof GraphNode))
			return false;

		GraphNode node = (GraphNode)obj;
		return mPosition.equals(node.getPosition());
	}
}