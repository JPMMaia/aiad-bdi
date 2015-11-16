package evacuation.agents;

import evacuation.utils.Position;
import jadex.micro.annotation.Agent;

@Agent
public class WandererBDI extends EscapingAgentBDI {

    /****************************
     PLANS
     ***************************/

    @Override
    protected Position findExit() {

        //1 - active - look for the fastest empty path - the search strategy is able to come back
        return findNewPositionWhenIncident();
    }

}

