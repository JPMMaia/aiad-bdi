package evacuation.agents;

import evacuation.utils.Position;
import jadex.micro.annotation.Agent;
import jadex.micro.annotation.AgentBody;

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

    @AgentBody
    public void body(){
        super.body();
    }

}

