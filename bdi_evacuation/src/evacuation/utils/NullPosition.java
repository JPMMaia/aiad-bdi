package evacuation.utils;

public class NullPosition extends Position
{
	private static NullPosition sInstance;
	public static NullPosition getInstance()
	{
		if(sInstance == null)
			sInstance = new NullPosition();

		return sInstance;
	}

	private NullPosition()
	{
		super(0, 0);
	}

	@Override
	public boolean equals(Object other)
	{
		return super.equals(other);
	}

	@Override
	public int distance(Position position)
	{
		return super.distance(position);
	}

	@Override
	public int distance(int x, int y)
	{
		return super.distance(x, y);
	}

	@Override
	public String toString()
	{
		return super.toString();
	}
}
