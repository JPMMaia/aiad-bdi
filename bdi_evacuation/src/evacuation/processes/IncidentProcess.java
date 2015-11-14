package evacuation.processes;

import evacuation.utils.Move;
import evacuation.utils.Position;
import evacuation.utils.TypesObjects;
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
	Position lastPosition;

	//SPACE VARIABLES FOR CALCULATIONS
	private Space2D space;

	int incidentType;

	Move move;

    @Override
    public void start(IClockService arg0, IEnvironmentSpace arg1) {

		//variables initialization
        space = (Space2D)arg1;
        //spaceHeight = space.getAreaSize().getXAsInteger();
        //spaceWidth = space.getAreaSize().getYAsInteger();

		move = new Move(space.getAreaSize().getXAsInteger(),space.getAreaSize().getYAsInteger());

		startTime = arg0.getTime();
		lastPosition = move.getRandomPosition();
		desiredNumIncidentPositions = 1;
		incidentPositions = new HashSet<>();

		incidentType = 0; //r.nextInt(2); //0 - fire ; 1 - water;
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
		Position newPosition = move.getNewPosition(lastPosition);
		if(savedIncidentPosition(newPosition)){
			Map<String, Object> properties = new HashMap<>();
			properties.put("position", new Vector2Int(newPosition.x, newPosition.y));
			properties.put("type", incidentType); //fire type
			space.createSpaceObject(TypesObjects.INCIDENT, properties, null);
			lastPosition = newPosition;
		}
	}

	private boolean savedIncidentPosition(Position position) {
		return incidentPositions.add(position.x + "." + position.y); // returns false if the object already exists
	}

}
