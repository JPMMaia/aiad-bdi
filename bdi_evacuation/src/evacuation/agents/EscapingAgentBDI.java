package evacuation.agents;

import evacuation.processes.WorldGenerator;
import evacuation.utils.Position;
import evacuation.utils.TypesObjects;
import evacuation.utils.TypesProperties;
import evacuation.utils.explorer.Explorer;
import evacuation.utils.explorer.ExplorerGoal;
import jadex.bdiv3.annotation.*;
import jadex.micro.annotation.Agent;
import jadex.micro.annotation.AgentBody;

@Agent
public class EscapingAgentBDI extends MaintainSafetyBDI{


    //PLANS******************************

    @Plan(trigger=@Trigger(goals=FindExitGoal.class))
    public class FindExitPlan {

        @PlanBody
        protected void FindExitPlanBody() {
            //System.out.println("Find exit");

            Position wantedPosition = findExit();
            nextPosition = wantedPosition;
        }
    }

    private void successfullyEscaped(Position exit) {
        worldMethods.makeObjectInCell(currentPosition, TypesObjects.ESCAPED_AGENT);
        System.out.println("position - " + exit.x + " " + exit.y);
        if(hurtObject != null)
            space.destroyAndVerifySpaceObject(hurtObject.getId());

        deleteCures();
        deletePush();

        space.destroyAndVerifySpaceObject(myself.getId());
        //System.out.println("quase matei o agente");
        agent.killAgent();
        //System.out.println("matei o agente");
        worldMethods.removeAgentFromOldCellMap(exit);
        worldMethods.resolveTwoAgentsInSameCell(currentPosition, null);
    }

    protected Position findExit() {
        return null;
    }

    // FUNCTIONS *************************************

    protected Position findNewPositionWhenIncident() {

        mExplorer.setGoal(ExplorerGoal.FindExit);
        mExplorer.move();

        if(mExplorer.reachedExit()){
            successfullyEscaped(mExplorer.findExit());
        }

        return mExplorer.getPosition();
    }

    //BODY***********************************************

    @AgentBody
    public void body(){
        super.body();
    }
}