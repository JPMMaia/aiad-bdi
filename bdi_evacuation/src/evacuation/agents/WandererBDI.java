package evacuation.agents;

import evacuation.utils.Position;
import jadex.extension.envsupport.math.IVector1;
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
        Position exit =  mExplorer.findExit();
        if(worldMethods.getNumAgentInCellMap(exit) >= 2) {
            System.out.println("find a random position");
            mExplorer.setGoal(mExplorer.getRandomPosition(), false);
            mExplorer.move();
            return mExplorer.getPosition();
        }
        return findNewPositionWhenIncident();
    }

    @AgentBody
    public void body(){
        super.body();
    }
}

