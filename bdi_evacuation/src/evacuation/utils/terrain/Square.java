package evacuation.utils.terrain;

import evacuation.utils.Position;

public class Square
{
	private Position mPosition;
	private Door mDoor;
	private Room mRoom;

	public Square(Position position)
	{
		mPosition = position;
		mDoor = null;
		mRoom = null;
	}

	public boolean isObstacle()
	{
		return isWall();
	}

	public boolean isDoor()
	{
		return mDoor != null;
	}

	public boolean isPartOfRoom()
	{
		return mRoom != null;
	}

	public boolean isWall()
	{
		return !isDoor() && !isPartOfRoom();
	}

	public Position getPosition()
	{
		return mPosition;
	}
	public Door getDoor()
	{
		return mDoor;
	}
	public Room getRoom()
	{
		return mRoom;
	}

	public void setDoor(Door door)
	{
		mDoor = door;
	}
	public void setRoom(Room room)
	{
		mRoom = room;
	}
}