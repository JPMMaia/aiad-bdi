package evacuation.processes;

import evacuation.utils.Position;
import jadex.bridge.service.types.clock.IClockService;
import jadex.commons.SimplePropertyObject;
import jadex.extension.envsupport.environment.IEnvironmentSpace;
import jadex.extension.envsupport.environment.ISpaceProcess;
import jadex.extension.envsupport.environment.space2d.Space2D;
import jadex.extension.envsupport.math.Vector2Int;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class IncidentProcess extends SimplePropertyObject implements ISpaceProcess {

	private static final int waitTime = 10000;
	private static ArrayList<Position> directions;
	private Space2D space;
	int spaceHeight;
	int spaceWidth;
	Random r;
	int incidentType;
	Position lastPosition;

    @Override
    public void start(IClockService arg0, IEnvironmentSpace arg1) {

        space = (Space2D)arg1;

        spaceHeight = space.getAreaSize().getXAsInteger();
        spaceWidth = space.getAreaSize().getYAsInteger();

        r = new Random();
        
        incidentType = 0; //r.nextInt(2); //0 - fire ; 1 - water;  
        
        directions = new ArrayList<>();
		directions.add(new Position(-1,0));
		directions.add(new Position(1,0));
		directions.add(new Position(0,-1));
		directions.add(new Position(0,1));
        
        //wait time TODO
		//this.wait(timeout); does not work :(
		lastPosition = new Position(r.nextInt(spaceWidth), r.nextInt(spaceHeight));
    }

    @Override
    public void shutdown(IEnvironmentSpace iEnvironmentSpace) {

    }

    @Override
    public void execute(IClockService iClockService, IEnvironmentSpace iEnvironmentSpace) {
    	
    	System.out.println((iClockService.getTime() - iClockService.getStarttime()));
    	
    	if((iClockService.getTime() - iClockService.getStarttime()) > waitTime){ 
	    	Map<String, Object> properties = new HashMap<>();
	    	Position position = getNewFirePosition();
	        properties.put("position", new Vector2Int(position.x, position.y));
	        properties.put("type", incidentType); //fire type
	        space.createSpaceObject("incident", properties, null);
    	}
    }

	private Position getNewFirePosition() {
		Position direction = directions.get(r.nextInt(4));
		Position newPosition = new Position(lastPosition.x + direction.x,lastPosition.y + direction.y);
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
