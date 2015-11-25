package evacuation.utils.terrain;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Terrain
{
	private char[][] mTerrain;
	private int mWidth;
	private int mHeight;

	public Terrain(char[][] terrain, int width, int height)
	{
		mTerrain = terrain;
		mWidth = width;
		mHeight = height;
	}

	public boolean isObstacle(int x, int y)
	{
		if (x < 0 || x >= mWidth || y < 0 || y >= mHeight)
			return true;

		if(getAvatar(x, y) == ' ' || getAvatar(x, y) == 0)
			return false;

		return true;
	}

	public void setAvatar(int x, int y, char avatar)
	{
		mTerrain[y][x] = avatar;
	}
	public char getAvatar(int x, int y)
	{
		return mTerrain[y][x];
	}

	public static Terrain createFromFile(String filename, int width, int height)
	{
		char[][] terrainMap = new char[height][width];

		File file = new File(filename);
		try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file)))
		{
			for(int i = 0; i < height; i++)
			{
				bufferedReader.read(terrainMap[i], 0, width);
				bufferedReader.skip(2);
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return null;
		}

		return new Terrain(terrainMap, width, height);
	}
}
