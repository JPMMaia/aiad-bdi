package evacuation.utils.terrain;

import evacuation.utils.Position;

public class NullDoor extends Door
{
	private static NullDoor sInstance = new NullDoor();
	public static NullDoor getInstance()
	{
		return sInstance;
	}

	private NullDoor()
	{
		super(NullSquare.getInstance(), NullRoom.getInstance(), NullRoom.getInstance(), false);
	}

	@Override
	public Position getPosition()
	{
		return super.getPosition();
	}

	@Override
	public boolean isExit()
	{
		return super.isExit();
	}

	@Override
	public boolean isPartOf(Room room)
	{
		return super.isPartOf(room);
	}

	@Override
	public Room getRoom1()
	{
		return super.getRoom1();
	}

	@Override
	public Room getRoom2()
	{
		return super.getRoom2();
	}
}
