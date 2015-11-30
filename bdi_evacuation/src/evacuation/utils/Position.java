package evacuation.utils;

public class Position {
	public int x;
	public int y;
	
	public Position(){}
	public Position(int x, int y){
		this.x = x;
		this.y = y;
	}

	public boolean equals(Object other)
	{
		if(!other.getClass().equals(Position.class))
			return false;

		Position position = (Position) other;
		return (this.x == position.x) && (this.y == position.y);
	}

	public int distance(Position position)
	{
		int dx = position.x - x;
		int dy = position.y - y;

		return Math.round((float) Math.sqrt(dx * dx + dy * dy));
	}
	public int distance(int x, int y)
	{
		int dx = x - this.x;
		int dy = y - this.y;

		return Math.round((float) Math.sqrt(dx * dx + dy * dy));
	}

	@Override
	public String toString()
	{
		return "Position [" + this.x + ", " + this.y + "]";
	}
}
