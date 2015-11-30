package evacuation.processes;

import evacuation.utils.Move;
import evacuation.utils.Position;
import evacuation.utils.TypesObjects;
import jadex.bridge.service.types.clock.IClockService;
import jadex.commons.SimplePropertyObject;
import jadex.extension.envsupport.environment.IEnvironmentSpace;
import jadex.extension.envsupport.environment.ISpaceObject;
import jadex.extension.envsupport.environment.ISpaceProcess;
import jadex.extension.envsupport.environment.space2d.Space2D;
import jadex.extension.envsupport.math.IVector1;
import jadex.extension.envsupport.math.Vector1Double;
import jadex.extension.envsupport.math.Vector2Double;
import jadex.extension.envsupport.math.Vector2Int;

import java.util.*;

public class IncidentProcess extends SimplePropertyObject implements ISpaceProcess {

	//TIMERS FOR THE INCIDENT
	private static final long initialWaitingTime = 2000;
	private static long startTime = 0; //get from the jadex clock
	private static long incidentProgressTimer = 3000; //interval between the generation of new incident objects

	//OTHER VARIABLES FOR CALCULATIONS OF NEW INCIDENTS
	private int desiredNumIncidentPositions; //for the calculations
	HashSet<String> incidentPositions; //set with incident positions (for efficiency)
	Position lastPosition;

	//SPACE VARIABLES FOR CALCULATIONS
	private Space2D space;

	int incidentType;

	Move move;
	Random r;

    @Override
    public void start(IClockService arg0, IEnvironmentSpace arg1) {

		//variables initialization
        space = (Space2D)arg1;

		move = new Move(space.getAreaSize().getXAsInteger(),space.getAreaSize().getYAsInteger());
		r = move.r;
		startTime = arg0.getTime();
		lastPosition = move.getRandomPosition();
		desiredNumIncidentPositions = 1;
		incidentPositions = new HashSet<>();

		incidentType = 2;//r.nextInt(3); //0 - fire ; 1 - water; 2 - terrorist
	}

    @Override
    public void shutdown(IEnvironmentSpace iEnvironmentSpace) {

    }

    @Override
    public void execute(IClockService iClockService, IEnvironmentSpace iEnvironmentSpace) {

		long currentTime = iClockService.getTime() - startTime;

    	if(currentTime > initialWaitingTime){
			if(currentTime > (incidentProgressTimer * desiredNumIncidentPositions)) {
				createIncident();
				desiredNumIncidentPositions++;
			}
    	}
    }

	private void createIncident() {

		if(incidentType == 0) {
			createFire();
		}
		else if(incidentType == 1){
			createWater();
		}
		else if (incidentType == 2){
			createTerrorist();
		}
	}

	private boolean createIncidentObject(Position newPosition){
		if (savedIncidentPosition(newPosition)) {
			Map<String, Object> properties = new HashMap<>();
			properties.put("position", new Vector2Int(newPosition.x, newPosition.y));
			properties.put("type", incidentType); //fire type
			space.createSpaceObject(TypesObjects.INCIDENT, properties, null);
			lastPosition = newPosition;
			return true;
		}
		return false;
	}

	private void createFire() {
		Position newPosition = move.getNewPosition(lastPosition);
		createIncidentObject(newPosition);
	}

	ArrayList<Position> openObjects = new ArrayList<>();

	private void createWater() {

		Position newPosition;

		if(openObjects.isEmpty()){
			newPosition = move.getRandomPosition();
			while(!noWallsInPosition(newPosition)){
				newPosition = move.getRandomPosition();
			}
			createIncidentObject(newPosition);
			openObjects.add(newPosition);
		}
		else{
			ArrayList<Position> newOpenObjects = new ArrayList<>();

			for (Position element : openObjects) {
				for(Position direction : move.directions){
					Position wantedPosition = new Position(element.x + direction.x, element.y + direction.y);
					if(move.isBetweenLimits(wantedPosition) && noWallsInPosition(wantedPosition) && !openObjects.contains(wantedPosition)) {
						if(createIncidentObject(wantedPosition)) {
							newOpenObjects.add(wantedPosition);
						}
					}
				}
			}

			openObjects = newOpenObjects;
		}
	}

	private void createTerrorist() {
		Position newPosition = move.getRandomPosition();
		while(!noWallsInPosition(newPosition)){
			newPosition = move.getRandomPosition();
		}
		createIncidentObject(newPosition);
	}

	private boolean savedIncidentPosition(Position position) {
		return incidentPositions.add(position.x + "." + position.y); // returns false if the object already exists
	}

	public boolean noWallsInPosition(Position p) {
		Vector2Double wantedPosition = new Vector2Double(p.x,p.y);
		IVector1 distance = new Vector1Double(0);
		Set terrainSet = space.getNearObjects(wantedPosition,distance,TypesObjects.TERRAIN);

		if(!terrainSet.isEmpty()) //there is a wall or an obstacle
			return false;

		return true;
	}
}
