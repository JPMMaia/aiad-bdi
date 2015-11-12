package evacuation.processes;

import evacuation.utils.Position;
import jadex.bridge.service.types.clock.IClockService;
import jadex.commons.SimplePropertyObject;
import jadex.extension.envsupport.environment.IEnvironmentSpace;
import jadex.extension.envsupport.environment.ISpaceProcess;
import jadex.extension.envsupport.environment.space2d.Space2D;
import jadex.extension.envsupport.math.Vector2Int;

import java.util.*;

public class IncidentProcess extends SimplePropertyObject implements ISpaceProcess {

	//TIMERS FOR THE INCIDENT
	private static final long initialWaitingTime = 5000;
	private static long startTime = 0; //get from the jadex clock
	private static long incidentProgressTimer = 3000; //interval between the generation of new incident objects

	//OTHER VARIABLES FOR CALCULATIONS OF NEW INCIDENTS
	private int desiredNumIncidentPositions; //for the calculations
	HashSet<String> incidentPositions; //set with incident positions (for efficiency)
	private static ArrayList<Position> directions;
	Position lastPosition;

	//SPACE VARIABLES FOR CALCULATIONS
	private Space2D space;
	int spaceHeight;
	int spaceWidth;
	Random r;

	int incidentType;

    @Override
    public void start(IClockService arg0, IEnvironmentSpace arg1) {

		//variables initializations
        space = (Space2D)arg1;
        spaceHeight = space.getAreaSize().getXAsInteger();
        spaceWidth = space.getAreaSize().getYAsInteger();
        r = new Random();

		startTime = arg0.getTime();
		lastPosition = new Position(r.nextInt(spaceWidth), r.nextInt(spaceHeight)); //pick a random valid position
		desiredNumIncidentPositions = 1;
		incidentPositions = new HashSet<>();

        directions = new ArrayList<>();
		directions.add(new Position(-1,0));
		directions.add(new Position(1,0));
		directions.add(new Position(0,-1));
		directions.add(new Position(0,1));

		incidentType = 0; //r.nextInt(2); //0 - fire ; 1 - water;
	}

    @Override
    public void shutdown(IEnvironmentSpace iEnvironmentSpace) {

    }

    @Override
    public void execute(IClockService iClockService, IEnvironmentSpace iEnvironmentSpace) {

		//DEBUG
    	//System.out.println((iClockService.getTime() - startTime));
    	long currentTime = iClockService.getTime() - startTime;

    	if(currentTime > initialWaitingTime){
			if(currentTime > (incidentProgressTimer * desiredNumIncidentPositions)) {
				creteIncident();
				desiredNumIncidentPositions++;
			}
    	}
    }

	private void creteIncident() {
		Position position = getNewIncidentPosition();
		if(savedIncidentPosition(position)){
			Map<String, Object> properties = new HashMap<>();
			properties.put("position", new Vector2Int(position.x, position.y));
			properties.put("type", incidentType); //fire type
			space.createSpaceObject("incident", properties, null);
		}
	}

	private boolean savedIncidentPosition(Position position) {
		return incidentPositions.add(position.x + "." + position.y); // returns false if the object already exists
	}

	private Position getNewIncidentPosition() {
		Position direction = directions.get(r.nextInt(4));
		Position newPosition = new Position(lastPosition.x + direction.x, lastPosition.y + direction.y);
		if(isBetweenLimits(newPosition)){
			lastPosition = newPosition;
			return newPosition;
		}
		return lastPosition;
	}

	private boolean isBetweenLimits(Position newPosition) {
		return (newPosition.x < spaceHeight && newPosition.x >= 0 && newPosition.y < spaceWidth && newPosition.y >= 0);
	}
}
