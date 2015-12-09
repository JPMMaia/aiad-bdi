package evacuation.utils.terrain;

import java.util.List;

public class Terrain implements ITerrain
{
	private Square[][] mSquares;
	private int mWidth;
	private int mHeight;
	private List<Room> mRooms;
	private List<Door> mDoors;

	public Terrain(Square[][] squares, int width, int height, List<Room> rooms, List<Door> doors)
	{
		mSquares = squares;
		mWidth = width;
		mHeight = height;
		mRooms = rooms;
		mDoors = doors;
	}

	synchronized public void setObstacle(int x, int y, boolean value)
	{
		getSquare(x, y).setObstacle(value);
	}

	synchronized public boolean isObstacle(int x, int y)
	{
		if (x < 0 || x >= mWidth || y < 0 || y >= mHeight)
			return true;

		return getSquare(x, y).isObstacle();
	}

	synchronized public Square getSquare(int x, int y)
	{
		if(x < 0 || y < 0 || x >= mWidth || y >= mHeight)
			return NullSquare.getInstance();

		return mSquares[y][x];
	}

	synchronized public List<Room> getRooms()
	{
		return mRooms;
	}

	synchronized public List<Door> getDoors()
	{
		return mDoors;
	}

	public static Terrain createFromFile(String filename, int width, int height)
	{
		TerrainBuilder builder = new TerrainBuilder();
		builder.build(filename, width, height);

		return builder.getResult();
	}
}
