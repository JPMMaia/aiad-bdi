package evacuation.utils.terrain.tests;

import evacuation.utils.terrain.Door;
import evacuation.utils.terrain.ExploredTerrain;
import evacuation.utils.terrain.Terrain;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ExploredTerrainTester
{
	private ExploredTerrain mExploredTerrain;

	@Before
	public void initialize()
	{
		Terrain terrain = Terrain.createFromFile("test_resources/TerrainMap3.txt", 20, 20);
		mExploredTerrain = new ExploredTerrain(terrain);
	}

	@Test
	public void testExploration()
	{
		// Room not yet explored:
		Assert.assertTrue(mExploredTerrain.isObstacle(1, 1));

		// Explore square:
		mExploredTerrain.exploreSquare(1, 1);

		// Check if the whole room is marked as explored:
		for(int y = 1; y <= 4; y++)
		{
			for(int x = 1; x <= 7; x++)
			{
				Assert.assertTrue(mExploredTerrain.isExplored(x, y));
				Assert.assertTrue(!mExploredTerrain.isObstacle(x, y));
			}
		}

		// Check if door is not marked as explored, but neither is marked as obstacle:
		Assert.assertTrue(!mExploredTerrain.isExplored(8, 3));
		Assert.assertTrue(!mExploredTerrain.isObstacle(8, 3));

		// Explore door:
		mExploredTerrain.exploreSquare(8, 3);

		// Check if door is explored, and if the room that it leads to is explored too:
		Assert.assertTrue(mExploredTerrain.isExplored(8, 3));
		Assert.assertTrue(!mExploredTerrain.isObstacle(8, 3));
		Assert.assertTrue(mExploredTerrain.isExplored(9, 3));
		Assert.assertTrue(!mExploredTerrain.isObstacle(9, 3));

		// Check if a door with both rooms marked as explored, is marked as explored too:
		mExploredTerrain.exploreSquare(13, 5);
		mExploredTerrain.exploreSquare(16, 10);
		Assert.assertTrue(mExploredTerrain.isExplored(6, 8));
		Assert.assertTrue(!mExploredTerrain.isObstacle(6, 8));
	}

	@Test
	public void findNearestDoorTest()
	{
		mExploredTerrain.exploreSquare(8, 3);
		Assert.assertSame(mExploredTerrain.getSquare(8, 3).getDoor(), mExploredTerrain.findNearestDoor(8, 3));

		mExploredTerrain.exploreSquare(1, 1);
		Assert.assertSame(mExploredTerrain.getSquare(8, 3).getDoor(), mExploredTerrain.findNearestDoor(1, 1));

		mExploredTerrain.exploreSquare(6, 13);
		Assert.assertSame(mExploredTerrain.getSquare(6, 8).getDoor(), mExploredTerrain.findNearestDoor(6, 13));

		mExploredTerrain.exploreSquare(6, 17);
		Assert.assertSame(mExploredTerrain.getSquare(6, 8).getDoor(), mExploredTerrain.findNearestDoor(6, 13));

		Assert.assertSame(mExploredTerrain.getSquare(8, 3).getDoor(), mExploredTerrain.findNearestDoor(8, 4));
	}

	@Test
	public void findNearestUnexploredDoorTest()
	{
		mExploredTerrain.exploreSquare(1, 1);
		Assert.assertSame(mExploredTerrain.getSquare(8, 3).getDoor(), mExploredTerrain.findNearestUnexploredDoor(1, 1));

		mExploredTerrain.exploreSquare(8, 3);
		Assert.assertSame(mExploredTerrain.getSquare(13, 2).getDoor(), mExploredTerrain.findNearestUnexploredDoor(8, 3));
		Assert.assertSame(mExploredTerrain.getSquare(13, 2).getDoor(), mExploredTerrain.findNearestUnexploredDoor(1, 1));

		mExploredTerrain.exploreSquare(13, 2);
		Assert.assertSame(mExploredTerrain.getSquare(13, 5).getDoor(), mExploredTerrain.findNearestUnexploredDoor(13, 2));

		mExploredTerrain.exploreSquare(13, 5);
		Assert.assertSame(mExploredTerrain.getSquare(16, 10).getDoor(), mExploredTerrain.findNearestUnexploredDoor(13, 5));

		mExploredTerrain.exploreSquare(16, 10);
		Assert.assertSame(mExploredTerrain.getSquare(15, 14).getDoor(), mExploredTerrain.findNearestUnexploredDoor(16, 10));

		mExploredTerrain.exploreSquare(15, 14);
		Assert.assertSame(mExploredTerrain.getSquare(6, 17).getDoor(), mExploredTerrain.findNearestUnexploredDoor(15, 14));

		mExploredTerrain.exploreSquare(6, 17);
		Assert.assertSame(mExploredTerrain.getSquare(0, 17).getDoor(), mExploredTerrain.findNearestUnexploredDoor(7, 17));
	}

	@Test
	public void findNearestExitDoorTest()
	{
		Assert.assertTrue(mExploredTerrain.getSquare(0, 11).isDoor());
		Assert.assertTrue(mExploredTerrain.getSquare(0, 11).getDoor().isExit());

		mExploredTerrain.exploreSquare(16, 10);
		Assert.assertSame(mExploredTerrain.getSquare(0, 11).getDoor(), mExploredTerrain.findNearestExitDoor(16, 10));
	}
}