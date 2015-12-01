package evacuation.utils.pathFinder;

public class GraphEdge
{
	private GraphNode mSource;
	private GraphNode mDestination;
	private float mWeight;

	public GraphEdge(GraphNode source, GraphNode destination)
	{
		mSource = source;
		mDestination = destination;
		mWeight = mSource.getPosition().distance(mDestination.getPosition());
	}

	public GraphNode getSource()
	{
		return mSource;
	}
	public GraphNode getDestination()
	{
		return mDestination;
	}
	public float getWeight()
	{
		return mWeight;
	}
}