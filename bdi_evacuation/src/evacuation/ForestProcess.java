package evacuation;

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

public class ForestProcess extends SimplePropertyObject implements ISpaceProcess
{
    @Override
    public void start(IClockService arg0, IEnvironmentSpace arg1)
	{
        Space2D space = (Space2D)arg1;

		IVector2 areaSize = space.getAreaSize();
		int spaceHeight = areaSize.getYAsInteger();
        int spaceWidth = areaSize.getXAsInteger();

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
				char terrainTypeChar = map[i][j];
				int terrainTypeInt;
				switch (terrainTypeChar)
				{
					case ' ':
						continue;

					case 'X':
						terrainTypeInt = 1;
						break;

					default:
						continue;
				}

				drawTerrainSquare(space, j, i, terrainTypeInt);
			}
		}
	}

	private void drawTerrainSquare(Space2D space, int x, int y, int terrainType)
	{
		Map properties = new HashMap();
		properties.put("position", new Vector2Int(x, y));
		properties.put("type", terrainType);

		space.createSpaceObject("terrain", properties, null);
	}
}