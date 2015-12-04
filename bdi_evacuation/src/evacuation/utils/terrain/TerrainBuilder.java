package evacuation.utils.terrain;

import evacuation.utils.Position;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TerrainBuilder
{
	private Square[][] mSquares;
	private int mWidth;
	private int mHeight;
	private List<Room> mRooms;
	private List<Door> mDoors;

	public Terrain getResult()
	{
		return new Terrain(mSquares, mWidth, mHeight, mRooms, mDoors);
	}

	public boolean build(String filename, int width, int height)
	{
		mRooms = new ArrayList<>();
		mDoors = new ArrayList<>();
		mWidth = width;
		mHeight = height;

		// Read char map from file:
		char[][] map = new char[height][width];
		if(!readFile(filename, map, width, height))
			return false;

		// Initialize matrix of Squares:
		initializeSquaresMatrix(width, height);

		// Create rooms and doors:
		createRoomsAndDoors(map, width, height);

		return true;
	}

	private void initializeSquaresMatrix(int width, int height)
	{
		// Create matrix of squares:
		mSquares = new Square[height][width];

		for(int y = 0; y < height; y++)
			for(int x = 0; x < width; x++)
				setSquare(x, y, new Square(new Position(x, y)));
	}
	private void createRoomsAndDoors(char[][] map, int width, int height)
	{
		List<Position> doorsLocation = new ArrayList<>();

		for(int y = 0; y < height; y++)
		{
			for(int x = 0; x < width; x++)
			{
				char avatar = map[y][x];

				if(avatar == ' ')
					createRoom(map, x, y);

				else if(avatar == 'D' || avatar == 'E')
					doorsLocation.add(new Position(x, y));
			}
		}

		// Create doors:
		for (Position position : doorsLocation)
			createDoor(position.x, position.y, map[position.y][position.x] == 'E');
	}

	private void createRoom(char[][] map, int x, int y)
	{
		Room room = new Room();

		floodFill(map, x, y, room);

		mRooms.add(room);
	}
	private void floodFill(char[][] map, int x, int y, Room room)
	{
		if(x < 0 || x >= mWidth || y < 0 || y >= mHeight)
			return;

		if(map[y][x] != ' ')
			return;

		map[y][x] = 'P';
		Square square = mSquares[y][x];
		square.setRoom(room);
		room.add(square);

		floodFill(map, x, y - 1, room);
		floodFill(map, x + 1, y, room);
		floodFill(map, x - 1, y, room);
		floodFill(map, x, y + 1, room);
	}

	private boolean createDoor(int x, int y, boolean isExit)
	{
		Square square = getSquare(x, y);

		// Get adjacent squares:
		Square north = getSquare(x, y - 1);
		Square east = getSquare(x + 1, y);
		Square west = getSquare(x - 1, y);
		Square south = getSquare(x, y + 1);

		Square adjacent1;
		Square adjacent2;

		// If horizontal door:
		if(north.isWall() && south.isWall() && !east.isWall() && !west.isWall())
		{
			adjacent1 = east;
			adjacent2 = west;
		}

		// If vertical door:
		else if(!north.isWall() && !south.isWall() && east.isWall() && west.isWall())
		{
			adjacent1 = north;
			adjacent2 = south;
		}

		// If exit door:
		else if(isExit && (!north.isWall() || !south.isWall() || !east.isWall() || !west.isWall()))
		{
			if(!north.isWall())
				adjacent1 = north;
			else if(!south.isWall())
				adjacent1 = south;
			else if(!east.isWall())
				adjacent1 = east;
			else
				adjacent1 = west;

			adjacent2 = NullSquare.getInstance();
		}

		// If invalid door:
		else
			return false;

		Door door = new Door(square, adjacent1.getRoom(), adjacent2.getRoom(), isExit);
		adjacent1.getRoom().add(door);
		adjacent2.getRoom().add(door);
		square.setDoor(door);
		mDoors.add(door);

		return true;
	}

	private Square getSquare(int x, int y)
	{
		if(x < 0 || x >= mWidth || y < 0 || y >= mHeight)
			return NullSquare.getInstance();

		return mSquares[y][x];
	}
	private void setSquare(int x, int y, Square square)
	{
		mSquares[y][x] = square;
	}

	private boolean readFile(String filename, char[][] map, int width, int height)
	{
		File file = new File(filename);
		try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file)))
		{
			for(int i = 0; i < height; i++)
			{
				bufferedReader.read(map[i], 0, width);
				bufferedReader.skip(2);
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return false;
		}

		return true;
	}
}
