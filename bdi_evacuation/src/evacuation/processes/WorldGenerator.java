package evacuation.processes;

import evacuation.factories.AbstractAgentFactory;
import evacuation.factories.AgentType;
import evacuation.utils.terrain.Terrain;
import evacuation.utils.terrain.TerrainReader;
import evacuation.utils.TypesObjects;
import jadex.bridge.service.types.clock.IClockService;
import jadex.commons.SimplePropertyObject;
import jadex.extension.envsupport.environment.IEnvironmentSpace;
import jadex.extension.envsupport.environment.ISpaceProcess;
import jadex.extension.envsupport.environment.space2d.Space2D;
import jadex.extension.envsupport.math.IVector2;
import jadex.extension.envsupport.math.Vector2Int;

import java.util.HashMap;
import java.util.Map;

public class WorldGenerator extends SimplePropertyObject implements ISpaceProcess
{
	private static final String TERRAIN_FILENAME = "resources/Map2.txt";
	private static final int TERRAIN_WIDTH = 40;
	private static final int TERRAIN_HEIGHT = 20;

	private static Terrain sTerrain;
	public static Terrain getTerrain()
	{
		if(sTerrain == null)
			sTerrain = Terrain.createFromFile(TERRAIN_FILENAME, TERRAIN_WIDTH, TERRAIN_HEIGHT);

		return sTerrain;
	}

    @Override
    public void start(IClockService arg0, IEnvironmentSpace arg1)
	{
        Space2D space = (Space2D) arg1;

		// Get width and height of the space:
		IVector2 areaSize = space.getAreaSize();
		int spaceWidth = areaSize.getXAsInteger();
		int spaceHeight = areaSize.getYAsInteger();
		if(spaceWidth != TERRAIN_WIDTH && spaceHeight != TERRAIN_HEIGHT)
			throw new RuntimeException("Dimensions of space aren't equal to the dimensions of the terrain map read from file!");

		// Create a map from a file:
		char[][] map = new char[spaceHeight][spaceWidth];
		readMap(map, spaceWidth, spaceHeight);
		drawMap(space, map);
    }

    @Override
    public void shutdown(IEnvironmentSpace iEnvironmentSpace)
	{
    }

    @Override
    public void execute(IClockService iClockService, IEnvironmentSpace iEnvironmentSpace)
	{
    }

	private boolean readMap(char[][] map, int width, int height)
	{
		return TerrainReader.readMap(TERRAIN_FILENAME, map, width, height);
	}

	private void drawMap(Space2D space, char[][] map)
	{
		for(int i = 0; i < map.length; i++)
		{
			for(int j = 0; j < map[i].length; j++)
			{
				switch (map[i][j])
				{
					case ' ':
						continue;

					case 'X':
						drawTerrainSquare(space, j, i, 1);
						break;

					case 'D':
					case 'E':
						drawExitDoor(space, j, i);
						break;

					case 'A':
						AbstractAgentFactory.createAgent(AgentType.Wanderer, space, j, i);
						break;

					case 'H':
						AbstractAgentFactory.createAgent(AgentType.Herding, space, j, i);
						break;

					case 'C':
						AbstractAgentFactory.createAgent(AgentType.Conservative, space, j, i);
						break;
				}
			}
		}
	}

	private void drawExitDoor(Space2D space, int x, int y) {
		Map<String, Object> properties = new HashMap<>();
		properties.put("position", new Vector2Int(x, y));
		space.createSpaceObject(TypesObjects.DOOR, properties, null);
	}

	private void drawTerrainSquare(Space2D space, int x, int y, int terrainType)
	{
		Map<String, Object> properties = new HashMap<>();
		properties.put("position", new Vector2Int(x, y));
		properties.put("type", terrainType);

		space.createSpaceObject(TypesObjects.TERRAIN, properties, null);
	}
}