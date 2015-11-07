import jadex.bridge.service.types.clock.IClockService;
import jadex.commons.SimplePropertyObject;
import jadex.extension.envsupport.environment.IEnvironmentSpace;
import jadex.extension.envsupport.environment.ISpaceProcess;
import jadex.extension.envsupport.environment.space2d.Space2D;
import jadex.extension.envsupport.math.Vector2Int;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by Leonel Araújo on 22/11/2014.
 */
public class DrawScenaryProcess extends SimplePropertyObject implements ISpaceProcess {

	Space2D space;
	Random r;
	int spaceHeight;
	int spaceWidth;
	
    @Override
    public void start(IClockService arg0, IEnvironmentSpace arg1) {

        space = (Space2D)arg1;

        spaceHeight = space.getAreaSize().getXAsInteger();
        spaceWidth = space.getAreaSize().getYAsInteger();

        r = new Random();
        
        // Add 15 items to grid
        for(int i = 0; i < 15; i++) {
            Map properties = new HashMap();
            properties.put("position", new Vector2Int(r.nextInt(spaceWidth), r.nextInt(spaceHeight)));
            properties.put("type", r.nextInt(3));

            space.createSpaceObject("terrain", properties, null);
        }

    }

    @Override
    public void shutdown(IEnvironmentSpace iEnvironmentSpace) {

    }

    @Override
    public void execute(IClockService iClockService, IEnvironmentSpace iEnvironmentSpace) {
    	//Scenary animations
        Map properties = new HashMap();
        properties.put("position", new Vector2Int(r.nextInt(spaceWidth), r.nextInt(spaceHeight)));
        properties.put("type", 1);
        space.createSpaceObject("terrain", properties, null);
    }


}
