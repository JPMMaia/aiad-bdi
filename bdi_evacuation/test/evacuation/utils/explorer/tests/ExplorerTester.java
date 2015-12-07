package evacuation.utils.explorer.tests;

import evacuation.utils.Position;
import evacuation.utils.explorer.Explorer;
import evacuation.utils.explorer.ExplorerGoal;
import evacuation.utils.terrain.Terrain;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class ExplorerTester
{
	private static Terrain mTerrain;

	@BeforeClass
	public static void initialize()
	{
		mTerrain = Terrain.createFromFile("test_resources/TerrainMap3.txt", 20, 20);
	}

	private void moveExplorer(Explorer explorer, int steps)
	{
		for(int i = 0; i < steps; i++)
			explorer.move();
	}

	@Test
	public void testFindExit()
	{
		Explorer explorer = new Explorer(mTerrain, new Position(1, 1));

		// Set goal to find the exit:
		explorer.setGoal(ExplorerGoal.FindExit);

		moveExplorer(explorer, 9);
		Assert.assertEquals(new Position(8, 3), explorer.getPosition());
		Assert.assertFalse(explorer.reachedExit());

		moveExplorer(explorer, 6);
		Assert.assertEquals(new Position(13, 2), explorer.getPosition());
		Assert.assertFalse(explorer.reachedExit());

		moveExplorer(explorer, 5);
		Assert.assertEquals(new Position(13, 5), explorer.getPosition());
		Assert.assertFalse(explorer.reachedExit());

		moveExplorer(explorer, 8);
		Assert.assertEquals(new Position(16, 10), explorer.getPosition());
		Assert.assertFalse(explorer.reachedExit());

		moveExplorer(explorer, 17);
		Assert.assertEquals(new Position(0, 11), explorer.getPosition());
		Assert.assertTrue(explorer.reachedExit());
	}

	@Test
	public void testFindPosition()
	{
		Explorer explorer = new Explorer(mTerrain, new Position(1, 1));

		// Checkpoint 1:
		Position checkpoint1 = new Position(4, 1);
		explorer.setGoal(checkpoint1);
		moveExplorer(explorer, 3);
		Assert.assertEquals(checkpoint1, explorer.getPosition());
		moveExplorer(explorer, 1);
		Assert.assertEquals(checkpoint1, explorer.getPosition());

		// Checkpoint 2:
		Position checkpoint2 = new Position(10, 3);
		explorer.setGoal(checkpoint2);
		moveExplorer(explorer, 8);
		Assert.assertEquals(checkpoint2, explorer.getPosition());

		// Checkpoint 3:
		Position checkpoint3 = new Position(18, 5);
		explorer.setGoal(checkpoint3);
		moveExplorer(explorer, 10);
		Assert.assertEquals(checkpoint3, explorer.getPosition());

		// Obstacle:
		Position obstacle = new Position(16, 3);
		explorer.setGoal(obstacle);
		moveExplorer(explorer, 4);
		Assert.assertEquals(new Position(18, 5), explorer.getPosition());
	}
}
