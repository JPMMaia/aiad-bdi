package evacuation.utils.terrain;

import evacuation.utils.Position;

public class NullSquare extends Square
{
	private static NullSquare sInstance = new NullSquare();

	public NullSquare()
	{
		super(new Position(0, 0));
	}

	public static NullSquare getInstance()
	{
		return sInstance;
	}

	@Override
	public boolean isObstacle()
	{
		return true;
	}

	@Override
	public boolean isDoor()
	{
		return false;
	}

	@Override
	public boolean isPartOfRoom()
	{
		return false;
	}

	@Override
	public boolean isWall()
	{
		return true;
	}

	@Override
	public Door getDoor()
	{
		return null;
	}

	@Override
	public Room getRoom()
	{
		return null;
	}

	@Override
	public void setDoor(Door door)
	{
	}

	@Override
	public void setRoom(Room room)
	{
	}
}