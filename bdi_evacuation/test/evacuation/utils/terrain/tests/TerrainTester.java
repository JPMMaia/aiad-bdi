package evacuation.utils.terrain.tests;

import evacuation.utils.terrain.Door;
import evacuation.utils.terrain.Room;
import evacuation.utils.terrain.Square;
import evacuation.utils.terrain.Terrain;
import org.junit.Assert;
import org.junit.Test;

public class TerrainTester
{
	@Test
	public void readFromFilenameTest()
	{
		Terrain terrain = Terrain.createFromFile("test_resources/TerrainMap3.txt", 20, 20);

		Assert.assertNotNull(terrain);

		// Check part of rooms:
		Assert.assertTrue(terrain.getSquare(1, 1).isPartOfRoom());
		Assert.assertTrue(!terrain.getSquare(13, 2).isPartOfRoom());
		Assert.assertTrue(terrain.getSquare(8, 13).isPartOfRoom());
		Assert.assertTrue(!terrain.getSquare(11, 14).isPartOfRoom());

		// Check doors:
		Assert.assertTrue(terrain.getSquare(13, 2).isDoor());
		Assert.assertTrue(terrain.getSquare(8, 3).isDoor());
		Assert.assertTrue(terrain.getSquare(13, 5).isDoor());
		Assert.assertTrue(terrain.getSquare(6, 8).isDoor());
		Assert.assertTrue(terrain.getSquare(16, 10).isDoor());
		Assert.assertTrue(terrain.getSquare(15, 14).isDoor());
		Assert.assertTrue(terrain.getSquare(6, 17).isDoor());

		// Check one room:
		Room room = terrain.getSquare(1, 1).getRoom();
		Assert.assertTrue(room.contains(terrain.getSquare(8, 3).getDoor()));
		Assert.assertTrue(!room.contains(terrain.getSquare(13, 2).getDoor()));
		for(int y = 1; y <= 4; y++)
			for(int x = 1; x <= 7; x++)
				Assert.assertTrue(room.contains(terrain.getSquare(x, y)));

		// Check one door:
		Door door = terrain.getSquare(8, 3).getDoor();
		Assert.assertTrue(door.isPartOf(terrain.getSquare(7, 3).getRoom()));
		Assert.assertTrue(door.isPartOf(terrain.getSquare(9, 3).getRoom()));
		Assert.assertTrue(!door.isPartOf(terrain.getSquare(17, 1).getRoom()));
		Assert.assertTrue(door.isPartOf(terrain.getSquare(1, 7).getRoom()));

		// Check exit door:
		Square exitDoorSquare = terrain.getSquare(0, 17);
		Assert.assertTrue(exitDoorSquare.isDoor());
		Assert.assertTrue(exitDoorSquare.getDoor().isExit());

		// Check if an agent square is considered to be part of a room:
		Square agentSquare = terrain.getSquare(7, 11);
		Assert.assertTrue(agentSquare.isPartOfRoom());
	}

	@Test
	public void setObstacleTest()
	{
		Terrain terrain = Terrain.createFromFile("test_resources/TerrainMap3.txt", 20, 20);
		Assert.assertNotNull(terrain);

		Square square = terrain.getSquare(11, 11);
		Assert.assertFalse(square.isObstacle());

		terrain.setObstacle(11, 11, true);
		Assert.assertTrue(square.isObstacle());

		terrain.setObstacle(11, 11, false);
		Assert.assertFalse(square.isObstacle());
	}
}
