package evacuation.agents;

import evacuation.utils.Position;
import jadex.micro.annotation.Agent;

/**
 * Created by Paula on 15/11/2015.
 */
@Agent
public class ConservativeBDI extends EscapingAgentBDI {

    @Override
    protected Position findExit() {

        //2 - conservative - look for a known empty path - greedy
        return nextPosition;
    }
}
