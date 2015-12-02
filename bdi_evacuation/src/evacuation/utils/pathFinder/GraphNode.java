package evacuation.utils.pathFinder;

import evacuation.utils.Position;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class GraphNode
{
	public static class NodeComparator implements Comparator<GraphNode>
	{
		@Override
		public int compare(GraphNode o1, GraphNode o2)
		{
			if(o1.mPosition.equals(o2.mPosition))
				return 0;

			if(o1.mCost < o2.mCost)
				return Integer.MIN_VALUE;

			if(o1.mCost == o2.mCost)
				return 0;

			return Integer.MAX_VALUE;
		}
	}

	private Position mPosition;
	private List<GraphEdge> mEdges;
	private int mCost;
	private GraphNode mParent;

	public GraphNode(Position position)
	{
		mPosition = position;
		mEdges = new ArrayList<>();
		mCost = 0;
		mParent = null;
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

	public int getCost()
	{
		return mCost;
	}
	public List<GraphEdge> getEdges()
	{
		return mEdges;
	}
	public GraphNode getParent()
	{
		return mParent;
	}
	public Position getPosition()
	{
		return mPosition;
	}

	public void setCost(int cost)
	{
		mCost = cost;
	}
	public void setParent(GraphNode parent)
	{
		mParent = parent;
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