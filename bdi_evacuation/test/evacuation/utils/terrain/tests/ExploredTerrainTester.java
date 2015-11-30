package evacuation.utils.terrain.tests;

import evacuation.utils.terrain.ExploredTerrain;
import evacuation.utils.terrain.Terrain;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class ExploredTerrainTester
{
	private static ExploredTerrain sExploredTerrain;

	@BeforeClass
	public static void initialize()
	{
		Terrain terrain = Terrain.createFromFile("test_resources/TerrainMap3.txt", 20, 20);
		sExploredTerrain = new ExploredTerrain(terrain);
	}

	@Test
	public void testExploration()
	{
		// Room not yet explored:
		Assert.assertTrue(sExploredTerrain.isObstacle(1, 1));

		// Explore square:
		sExploredTerrain.exploreSquare(1, 1);

		// Check if the whole room is marked as explored:
		for(int y = 1; y <= 4; y++)
		{
			for(int x = 1; x <= 7; x++)
			{
				Assert.assertTrue(sExploredTerrain.isExplored(x, y));
				Assert.assertTrue(!sExploredTerrain.isObstacle(x, y));
			}
		}

		// Check if door is not marked as explored, but neither is marked as obstacle:
		Assert.assertTrue(!sExploredTerrain.isExplored(8, 3));
		Assert.assertTrue(!sExploredTerrain.isObstacle(8, 3));

		// Explore door:
		sExploredTerrain.exploreSquare(8, 3);

		// Check if door is explored, and if the room that it leads to is explored too:
		Assert.assertTrue(sExploredTerrain.isExplored(8, 3));
		Assert.assertTrue(!sExploredTerrain.isObstacle(8, 3));
		Assert.assertTrue(sExploredTerrain.isExplored(9, 3));
		Assert.assertTrue(!sExploredTerrain.isObstacle(9, 3));

		// Check if a door with both rooms marked as explored, is marked as explored too:
		sExploredTerrain.exploreSquare(13, 5);
		sExploredTerrain.exploreSquare(16, 10);
		Assert.assertTrue(sExploredTerrain.isExplored(6, 8));
		Assert.assertTrue(!sExploredTerrain.isObstacle(6, 8));
	}

	@Test
	public void findNearestDoorTest()
	{
		sExploredTerrain.exploreSquare(8, 3);
		Assert.assertSame(sExploredTerrain.getSquare(8, 3).getDoor(), sExploredTerrain.findNearestDoor(8, 3));

		sExploredTerrain.exploreSquare(1, 1);
		Assert.assertSame(sExploredTerrain.getSquare(8, 3).getDoor(), sExploredTerrain.findNearestDoor(1, 1));

		sExploredTerrain.exploreSquare(6, 13);
		Assert.assertSame(sExploredTerrain.getSquare(6, 8).getDoor(), sExploredTerrain.findNearestDoor(6, 13));

		sExploredTerrain.exploreSquare(6, 17);
		Assert.assertSame(sExploredTerrain.getSquare(6, 8).getDoor(), sExploredTerrain.findNearestDoor(6, 13));

		Assert.assertSame(sExploredTerrain.getSquare(8, 3).getDoor(), sExploredTerrain.findNearestDoor(8, 4));
	}

	@Test
	public void findNearestUnexploredDoorTest()
	{
		sExploredTerrain.exploreSquare(1, 1);
		Assert.assertSame(sExploredTerrain.getSquare(8, 3).getDoor(), sExploredTerrain.findNearestUnexploredDoor(1, 1));

		sExploredTerrain.exploreSquare(8, 3);
		Assert.assertSame(sExploredTerrain.getSquare(13, 2).getDoor(), sExploredTerrain.findNearestUnexploredDoor(8, 3));
		Assert.assertSame(sExploredTerrain.getSquare(13, 2).getDoor(), sExploredTerrain.findNearestUnexploredDoor(1, 1));

		sExploredTerrain.exploreSquare(13, 2);
		Assert.assertSame(sExploredTerrain.getSquare(13, 5).getDoor(), sExploredTerrain.findNearestUnexploredDoor(13, 2));

		sExploredTerrain.exploreSquare(13, 5);
		Assert.assertSame(sExploredTerrain.getSquare(16, 10).getDoor(), sExploredTerrain.findNearestUnexploredDoor(13, 5));

		sExploredTerrain.exploreSquare(16, 10);
		Assert.assertSame(sExploredTerrain.getSquare(15, 14).getDoor(), sExploredTerrain.findNearestUnexploredDoor(16, 10));

		sExploredTerrain.exploreSquare(6, 17);
		Assert.assertSame(null, sExploredTerrain.findNearestUnexploredDoor(7, 17));
	}
}