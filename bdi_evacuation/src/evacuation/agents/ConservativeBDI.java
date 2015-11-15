package evacuation.agents;

import evacuation.utils.Position;

/**
 * Created by Paula on 15/11/2015.
 */
public class ConservativeBDI extends EvacuationAgentBDI{

    @Override
    protected Position findExit() {

        //2 - conservative - look for a known empty path - greedy

        return nextPosition;
    }
}
