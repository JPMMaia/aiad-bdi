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

		moveExplorer(explorer, 6);
		Assert.assertEquals(new Position(13, 2), explorer.getPosition());

		moveExplorer(explorer, 5);
		Assert.assertEquals(new Position(13, 5), explorer.getPosition());

		moveExplorer(explorer, 8);
		Assert.assertEquals(new Position(16, 10), explorer.getPosition());

		moveExplorer(explorer, 17);
		Assert.assertEquals(new Position(0, 11), explorer.getPosition());
	}
}
