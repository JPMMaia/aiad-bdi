package evacuation.utils.pathFinder;

import evacuation.utils.Position;
import evacuation.utils.terrain.Door;
import evacuation.utils.terrain.Room;
import evacuation.utils.terrain.Square;

import java.util.List;

public class DoorsGraphBuilder
{
	public static void build(Graph graph, List<Room> rooms, List<Door> doors)
	{
		addDoors(graph, doors);
		addEdges(graph, rooms);
	}

	public static void addNode(Graph graph, Square square)
	{
		// If square has door, then it was already added:
		if(square.isDoor())
			return;

		if(!square.isPartOfRoom())
			return;

		Position nodePosition = square.getPosition();
		graph.addNode(nodePosition);
		for (Door door : square.getRoom().getDoors())
		{
			Position doorPosition = door.getPosition();
			graph.addEdge(nodePosition, doorPosition);
			graph.addEdge(doorPosition, nodePosition);
		}
	}

	private static void addDoors(Graph graph, List<Door> doors)
	{
		for (Door door : doors)
			graph.addNode(door.getPosition());
	}
	private static void addEdges(Graph graph, List<Room> rooms)
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
					graph.addEdge(roomDoors.get(i).getPosition(), roomDoors.get(j).getPosition());
				}
			}
		}
	}

}
