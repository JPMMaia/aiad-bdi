package evacuation.agents;

import evacuation.utils.Position;
import jadex.micro.annotation.Agent;

@Agent
public class WandererBDI extends EvacuationAgentBDI {

    /****************************
     PLANS
     ***************************/

    @Override
    protected Position findExit() {
        return findNewPositionWhenIncident();
    }

    /****************************
     BODY
     ***************************/




    /****************************
     FUNCTIONS FOR THE PATH CALCULATION WHEN INCIDENT
     ***************************/

}

//deals with being hurt by the environment - condition decreases if distance from danger < value
// condition decreases if is being pushed
// condition = 0 -> is dead -> the others can pass by


//TODO
//ask the world if there is any door available
//if no empty door available riskPerception += 5