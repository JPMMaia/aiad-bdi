package evacuation.utils.terrain.tests;

import evacuation.utils.terrain.Terrain;
import evacuation.utils.pathFinder.PathFinder;
import evacuation.utils.Position;
import junit.framework.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class PathFinderTester
{
	private void compareList(List expected, List actual)
	{
		Assert.assertEquals(expected.size(), actual.size());

		for (int i = 0; i < actual.size(); i++)
			Assert.assertEquals(expected.get(i), actual.get(i));
	}

	@Test
	public void pathFinderTest1()
	{
		Terrain terrain = Terrain.createFromFile("test_resources/TerrainMap1.txt", 9, 6);
		List<Position> actualPath = PathFinder.run(terrain, new Position(1, 1), new Position(7, 4));
		Assert.assertNotNull(actualPath);

		List<Position> expectedPath = Arrays.asList(
				new Position(7, 4),
				new Position(6, 4),
				new Position(5, 4),
				new Position(4, 4),
				new Position(3, 4),
				new Position(3, 3),
				new Position(3, 2),
				new Position(3, 1),
				new Position(2, 1),
				new Position(1, 1)
		);
		compareList(expectedPath, actualPath);
	}

	@Test
	public void pathFinderTest2()
	{
		Terrain terrain = Terrain.createFromFile("test_resources/TerrainMap2.txt", 9, 8);
		List<Position> actualPath = PathFinder.run(terrain, new Position(2, 4), new Position(6, 5));
		Assert.assertNotNull(actualPath);

		List<Position> expectedPath = Arrays.asList(
				new Position(6, 5),
				new Position(6, 6),
				new Position(5, 6),
				new Position(4, 6),
				new Position(3, 6),
				new Position(3, 5),
				new Position(3, 4),
				new Position(2, 4)
		);
		compareList(expectedPath, actualPath);
	}
}