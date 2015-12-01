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
	private GraphEdge findEdge(List<GraphEdge> edges, Position position)
	{
		for (GraphEdge edge : edges)
		{
			if(edge.getDestination().getPosition().equals(position))
				return edge;
		}

		return null;
	}

	@Test
	public void buildTest()
	{
		Terrain terrain = Terrain.createFromFile("test_resources/TerrainMap3.txt", 20, 20);

		Graph graph = new Graph();
		DoorsGraphBuilder.build(graph, terrain.getRooms(), terrain.getDoors());
		DoorsGraphBuilder.addNode(graph, terrain.getSquare(1, 1));

		List<GraphNode> nodes = graph.getNodes();
		GraphNode startNode = nodes.get(nodes.size() - 1);
		List<GraphEdge> edges0 = startNode.getEdges();
		Assert.assertEquals(startNode.getPosition(), new Position(1, 1));
		Assert.assertEquals(1, edges0.size());
		Assert.assertEquals(startNode, edges0.get(0).getSource());

		GraphNode node1 = edges0.get(0).getDestination();
		Assert.assertEquals(node1.getPosition(), new Position(8, 3));

		// Test edges:
		List<GraphEdge> edges1 = node1.getEdges();
		Assert.assertEquals(edges1.size(), 4);
		GraphEdge edge1 = findEdge(edges1, new Position(1, 1));
		Assert.assertNotNull(edge1);
		GraphEdge edge2 = findEdge(edges1, new Position(13, 2));
		Assert.assertNotNull(edge2);
		GraphEdge edge3 = findEdge(edges1, new Position(13, 5));
		Assert.assertNotNull(edge3);
		GraphEdge edge4 = findEdge(edges1, new Position(6, 8));
		Assert.assertNotNull(edge4);

		for (Door door : terrain.getDoors())
			graph.containsNode(door.getPosition());

		// Test weights:
		Assert.assertEquals(new Position(1, 1).distance(8, 3), edge1.getWeight(), 0.01f);
		Assert.assertEquals(new Position(8, 3).distance(13, 2), edge2.getWeight(), 0.01f);
		Assert.assertEquals(new Position(8, 3).distance(13, 5), edge3.getWeight(), 0.01f);
		Assert.assertEquals(new Position(8, 3).distance(6, 8), edge4.getWeight(), 0.01f);

		// Test remove:
		Assert.assertTrue(graph.removeNode(terrain.getSquare(1, 1).getPosition()));
		Assert.assertNull(graph.getNode(new Position(1, 1)));
		List<GraphEdge> edges2 = graph.getNode(new Position(8, 3)).getEdges();
		Assert.assertNull(findEdge(edges2, new Position(1, 1)));
		Assert.assertFalse(graph.removeNode(terrain.getSquare(1, 1).getPosition()));
	}
}