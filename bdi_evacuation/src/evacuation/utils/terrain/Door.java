package evacuation.utils.terrain;

import evacuation.utils.Position;

public class Door
{
	private Square mSquare;
	private Room mRoom1;
	private Room mRoom2;
	private boolean mExit;

	public Door(Square square, Room room1, Room room2, boolean exit)
	{
		mSquare = square;
		mRoom1 = room1;
		mRoom2 = room2;
		mExit = exit;
	}

	public Position getPosition()
	{
		return mSquare.getPosition();
	}

	public boolean isExit()
	{
		return mExit;
	}
	public boolean isPartOf(Room room)
	{
		return room == mRoom1 || room == mRoom2;
	}

	public Room getRoom1()
	{
		return mRoom1;
	}
	public Room getRoom2()
	{
		return mRoom2;
	}
}
