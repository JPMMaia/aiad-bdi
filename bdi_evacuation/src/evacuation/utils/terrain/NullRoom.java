package evacuation.utils.terrain;

import java.util.List;

public class NullRoom extends Room
{
	private static NullRoom sInstance = new NullRoom();
	public static NullRoom getInstance()
	{
		return sInstance;
	}

	private NullRoom()
	{
		super();
	}

	@Override
	public void add(Square square)
	{
	}

	@Override
	public void add(Door door)
	{
	}

	@Override
	public boolean contains(Square square)
	{
		return false;
	}

	@Override
	public boolean contains(Door door)
	{
		return false;
	}

	@Override
	public List<Door> getDoors()
	{
		return super.getDoors();
	}
}
