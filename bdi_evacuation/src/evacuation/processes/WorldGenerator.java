package evacuation.processes;

import evacuation.factories.AbstractAgentFactory;
import evacuation.factories.AgentType;
import evacuation.utils.Types;
import jadex.bridge.service.types.clock.IClockService;
import jadex.commons.SimplePropertyObject;
import jadex.extension.envsupport.environment.IEnvironmentSpace;
import jadex.extension.envsupport.environment.ISpaceProcess;
import jadex.extension.envsupport.environment.space2d.Space2D;
import jadex.extension.envsupport.math.IVector2;
import jadex.extension.envsupport.math.Vector2Int;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class WorldGenerator extends SimplePropertyObject implements ISpaceProcess
{
    @Override
    public void start(IClockService arg0, IEnvironmentSpace arg1)
	{
        Space2D space = (Space2D) arg1;

		// Get width and height of the space:
		IVector2 areaSize = space.getAreaSize();
		int spaceWidth = areaSize.getXAsInteger();
		int spaceHeight = areaSize.getYAsInteger();

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
		File file = new File("resources/Map.txt");
		try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file)))
		{
			for(int i = 0; i < height; i++)
			{
				bufferedReader.read(map[i], 0, width);
				bufferedReader.skip(2);
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return false;
		}

		return true;
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

					case 'E':
						drawExitDoor(space, j, i);
						break;

					case 'W':
						AbstractAgentFactory.createAgent(AgentType.Wanderer, space, j, i);
						break;
				}
			}
		}
	}

	private void drawExitDoor(Space2D space, int x, int y) {
		Map<String, Object> properties = new HashMap<>();
		properties.put("position", new Vector2Int(x, y));
		space.createSpaceObject(Types.DOOR, properties, null);
	}

	private void drawTerrainSquare(Space2D space, int x, int y, int terrainType)
	{
		Map<String, Object> properties = new HashMap<>();
		properties.put("position", new Vector2Int(x, y));
		properties.put("type", terrainType);

		space.createSpaceObject(Types.TERRAIN, properties, null);
	}
}