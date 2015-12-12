package evacuation.utils.terrain;

import evacuation.utils.Position;
import evacuation.utils.pathFinder.GraphPathFinder;

import java.util.ArrayList;
import java.util.List;

public class ExploredTerrain implements ITerrain
{
	private Terrain mTerrain;
	private List<Door> mExploredDoors;
	private List<Door> mUnexploredDoors;
	private List<Room> mExploredRooms;
	private List<Door> mExitDoors;
	private GraphPathFinder mUnexploredDoorFinder;

	public ExploredTerrain(Terrain terrain)
	{
		mTerrain = terrain;
		mExploredDoors = new ArrayList<>();
		mUnexploredDoors = new ArrayList<>();
		mExploredRooms = new ArrayList<>();
		mExitDoors = new ArrayList<>();
		mUnexploredDoorFinder = new GraphPathFinder(this);
	}

	public void exploreSquare(int x, int y)
	{
		Square square = mTerrain.getSquare(x, y);

		if(square.isPartOfRoom())
			exploreRoom(square.getRoom());

		else if(square.isDoor())
			exploreDoor(square.getDoor());
	}
	private void exploreRoom(Room room)
	{
		// Add room to the explored rooms:
		mExploredRooms.add(room);

		// Add doors to unexplored doors:
		List<Door> doors = room.getDoors();
		for(int i = 0; i < doors.size(); i++)
		{
			Door door = doors.get(i);
			if(!mExploredDoors.contains(door))
			{
				if(mUnexploredDoors.contains(door) && mExploredRooms.contains(door.getRoom1()) && mExploredRooms.contains(door.getRoom2()))
				{
					mUnexploredDoors.remove(door);
					mExploredDoors.add(door);
				}
				else
				{
					mUnexploredDoors.add(door);
					if(door.isExit())
						mExitDoors.add(door);
				}
			}
		}
	}
	private void exploreDoor(Door door)
	{
		if(mUnexploredDoors.contains(door))
		{
			mUnexploredDoors.remove(door);
			mExploredDoors.add(door);

			Room room1 = door.getRoom1();
			if(!mExploredRooms.contains(room1))
				exploreRoom(room1);

			Room room2 = door.getRoom2();
			if(!mExploredRooms.contains(room2))
				exploreRoom(room2);
		}
	}

	public Door findNearestDoor(int x, int y)
	{
		Square square = mTerrain.getSquare(x, y);
		if(square.isDoor())
			return square.getDoor();

		if(square.isPartOfRoom())
			return findNearestDoor(x, y, square.getRoom().getDoors());

		if(square.isWall())
			return findNearestDoor(x, y, mExploredDoors, mUnexploredDoors);

		return null;
	}
	public Door findNearestUnexploredDoor(int x, int y)
	{
		Square square = mTerrain.getSquare(x, y);

		if(square.isDoor() && !isExplored(square))
			return square.getDoor();

		return mUnexploredDoorFinder.run(new Position(x, y));
	}
	public Door findNearestExitDoor(int x, int y)
	{
		Square currentSquare = getSquare(x, y);

		List<Room> rooms = new ArrayList<>();
		if(currentSquare.isDoor())
		{
			Door door = currentSquare.getDoor();
			rooms.add(door.getRoom1());
			rooms.add(door.getRoom2());
		}
		else if(currentSquare.isPartOfRoom())
		{
			rooms.add(currentSquare.getRoom());
		}
		else
			return NullDoor.getInstance();

		Door nearestDoor = NullDoor.getInstance();
		for (Room currentRoom : rooms)
		{
			List<Door> availableDoors = currentRoom.getDoors();
			int nearestDoorDistance = Integer.MAX_VALUE;
			for (Door availableDoor : availableDoors)
			{
				if(availableDoor.isExit())
				{
					int distance = availableDoor.getPosition().distance(x, y);
					if(distance < nearestDoorDistance)
					{
						nearestDoorDistance = distance;
						nearestDoor = availableDoor;
					}
				}
			}
		}

		return nearestDoor;
	}
	private Door findNearestDoor(int x, int y, List<Door> doors)
	{
		if(doors.isEmpty())
			return null;

		Door nearestDoor = doors.get(0);
		float lowestDistance = nearestDoor.getPosition().distance(x, y);

		for (int i = 1; i < doors.size(); i++)
		{
			Door currentDoor = doors.get(i);
			int currentDistance = currentDoor.getPosition().distance(x, y);
			if(currentDistance < lowestDistance)
			{
				nearestDoor = currentDoor;
				lowestDistance = currentDistance;
			}
		}

		return nearestDoor;
	}
	private Door findNearestDoor(int x, int y, List<Door> doors1, List<Door> doors2)
	{
		Door door1 = findNearestDoor(x, y, doors1);
		Door door2 = findNearestDoor(x, y, doors2);

		if(door1 == null)
			return door2;
		else if(door2 == null)
			return door1;

		if(door1.getPosition().distance(x, y) < door2.getPosition().distance(x, y))
			return door1;

		return door2;
	}

	public boolean isExplored(int x, int y)
	{
		return isExplored(mTerrain.getSquare(x, y));
	}

	@Override
	public boolean isObstacle(int x, int y)
	{
		Square square = mTerrain.getSquare(x, y);

		if(!isExplored(square))
		{
			if(square.isDoor() && mUnexploredDoors.contains(square.getDoor()))
				return false;

			return true;
		}

		return square.isObstacle();
	}

	public boolean isExplored(Square square)
	{
		if(mExploredRooms.isEmpty())
			return false;

		if(square.isWall())
			return false;

		if(square.isPartOfRoom())
			return mExploredRooms.contains(square.getRoom());

		if(square.isDoor())
			return mExploredDoors.contains(square.getDoor());

		return true;
	}

	@Override
	public Square getSquare(int x, int y)
	{
		return mTerrain.getSquare(x, y);
	}
	public List<Door> getExploredDoors()
	{
		return mExploredDoors;
	}
	public List<Door> getUnexploredDoors()
	{
		return mUnexploredDoors;
	}
	public Terrain getTerrain()
	{
		return mTerrain;
	}

	public static ExploredTerrain createFromFile(String filename, int width, int height)
	{
		Terrain terrain = Terrain.createFromFile(filename, width, height);
		return new ExploredTerrain(terrain);
	}

	public Position getRandomExploredPosition() {
		//TODO
		return new Position(37,10);
	}
}