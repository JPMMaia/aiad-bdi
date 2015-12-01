package evacuation.utils.pathFinder;

import evacuation.utils.Position;
import evacuation.utils.terrain.Door;
import evacuation.utils.terrain.ITerrain;
import evacuation.utils.terrain.Room;
import evacuation.utils.terrain.Square;

import java.util.List;

public class DoorsGraphBuilder
{
	private Graph mGraph;

	public Graph getResult()
	{
		return mGraph;
	}

	public boolean build(ITerrain terrain, List<Room> rooms, List<Door> doors, Position startPosition, Position goal)
	{
		mGraph = new Graph(goal);

		addDoors(doors);
		addEdges(rooms);
		addStartNode(startPosition, terrain);

		return true;
	}

	private void addDoors(List<Door> doors)
	{
		for (Door door : doors)
			mGraph.addNode(door.getPosition());
	}
	private void addEdges(List<Room> rooms)
	{
		for (Room room : rooms)
		{
			List<Door> roomDoors = room.getDoors();
			for (int i = 0; i < roomDoors.size(); i++)
			{
				for (int j = 0; j < roomDoors.size(); j++)
				{
					if(i == j)
						continue;

					// Add edge, only if the nodes exist:
					mGraph.addEdge(roomDoors.get(i).getPosition(), roomDoors.get(j).getPosition());
				}
			}
		}
	}
	private void addStartNode(Position startPosition, ITerrain terrain)
	{
		Square square = terrain.getSquare(startPosition.x, startPosition.y);

		// If square has door, then it was already added:
		if(square.isDoor())
			return;

		if(!square.isPartOfRoom())
			return;

		mGraph.addNode(startPosition);
		for (Door door : square.getRoom().getDoors())
		{
			Position doorPosition = door.getPosition();
			mGraph.addEdge(startPosition, doorPosition);
			mGraph.addEdge(doorPosition, startPosition);
		}
	}
}
