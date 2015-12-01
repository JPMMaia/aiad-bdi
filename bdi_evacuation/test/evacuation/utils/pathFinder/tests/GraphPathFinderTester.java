package evacuation.utils.pathFinder.tests;

import evacuation.utils.Position;
import evacuation.utils.pathFinder.GraphPathFinder;
import evacuation.utils.terrain.Door;
import evacuation.utils.terrain.ExploredTerrain;
import org.junit.Assert;
import org.junit.Test;

public class GraphPathFinderTester
{
	@Test
	public void findShortestPathTest()
	{
		ExploredTerrain terrain = ExploredTerrain.createFromFile("test_resources/TerrainMap3.txt", 20, 20);

		GraphPathFinder pathFinder = new GraphPathFinder(terrain);

		Door solution1 = pathFinder.run(new Position(1, 1));
		Assert.assertNotNull(solution1);
		Assert.assertEquals(new Position(8, 3), solution1.getPosition());

		terrain.exploreSquare(8, 3);
		Door solution2 = pathFinder.run(new Position(1, 1));
		Assert.assertNotNull(solution2);
		Assert.assertEquals(new Position(13, 2), solution2.getPosition());

		terrain.exploreSquare(13, 2);
		Door solution3 = pathFinder.run(new Position(1, 4));
		Assert.assertNotNull(solution3);
		Assert.assertEquals(new Position(13, 5), solution3.getPosition());
	}
}