package evacuation.agents;

import evacuation.utils.Position;
import jadex.micro.annotation.Agent;
import jadex.micro.annotation.AgentBody;

@Agent
public class ConservativeBDI extends EscapingAgentBDI {

    @Override
    protected Position findExit() {

        //2 - conservative - look for a known empty path - greedy
        return nextPosition;
    }

    @AgentBody
    public void body(){
        super.body();
    }
}
