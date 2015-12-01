package evacuation.utils.pathFinder.tests;

import evacuation.utils.Position;
import evacuation.utils.pathFinder.DoorsGraphBuilder;
import evacuation.utils.pathFinder.Graph;
import evacuation.utils.pathFinder.GraphEdge;
import evacuation.utils.pathFinder.GraphNode;
import evacuation.utils.terrain.Door;
import evacuation.utils.terrain.Terrain;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class DoorsGraphBuilderTester
{
	private boolean findEdge(List<GraphEdge> edges, Position position)
	{
		for (GraphEdge edge : edges)
		{
			if(edge.getDestination().getPosition().equals(position))
				return true;
		}

		return false;
	}

	@Test
	public void buildTest()
	{
		Terrain terrain = Terrain.createFromFile("test_resources/TerrainMap3.txt", 20, 20);

		DoorsGraphBuilder builder = new DoorsGraphBuilder();
		Assert.assertTrue(builder.build(terrain, terrain.getRooms(), terrain.getDoors(), new Position(1, 1), new Position(0, 0)));

		Graph graph = builder.getResult();
		List<GraphNode> nodes = graph.getNodes();

		GraphNode startNode = nodes.get(nodes.size() - 1);
		List<GraphEdge> edges0 = startNode.getEdges();
		Assert.assertEquals(startNode.getPosition(), new Position(1, 1));
		Assert.assertEquals(1, edges0.size());
		Assert.assertEquals(startNode, edges0.get(0).getSource());

		GraphNode node1 = edges0.get(0).getDestination();
		List<GraphEdge> edges1 = node1.getEdges();
		Assert.assertEquals(node1.getPosition(), new Position(8, 3));
		Assert.assertEquals(edges1.size(), 4);
		Assert.assertTrue(findEdge(edges1, new Position(1, 1)));
		Assert.assertTrue(findEdge(edges1, new Position(13, 2)));
		Assert.assertTrue(findEdge(edges1, new Position(13, 5)));
		Assert.assertTrue(findEdge(edges1, new Position(6, 8)));

		for (Door door : terrain.getDoors())
			graph.containsNode(door.getPosition());
	}
}