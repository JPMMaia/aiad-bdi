package evacuation.utils.terrain;

import java.util.ArrayList;
import java.util.List;

public class Room
{
	private List<Door> mDoors;
	private List<Square> mSquares;

	public Room()
	{
		mDoors = new ArrayList<>();
		mSquares = new ArrayList<>();
	}

	public void add(Square square)
	{
		mSquares.add(square);
	}
	public void add(Door door)
	{
		mDoors.add(door);
	}

	public boolean contains(Square square)
	{
		return mSquares.contains(square);
	}
	public boolean contains(Door door)
	{
		return mDoors.contains(door);
	}

	public List<Door> getDoors()
	{
		return mDoors;
	}
}
