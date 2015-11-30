package evacuation.utils.terrain;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class TerrainReader
{
	public static boolean readMap(String filename, char[][] map, int width, int height)
	{
		File file = new File(filename);
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
}
