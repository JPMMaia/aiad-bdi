package evacuation.processes;

import evacuation.utils.Move;
import evacuation.utils.Position;
import evacuation.utils.TypesObjects;
import evacuation.utils.WorldMethods;
import jadex.bridge.service.types.clock.IClockService;
import jadex.commons.SimplePropertyObject;
import jadex.extension.envsupport.environment.IEnvironmentSpace;
import jadex.extension.envsupport.environment.ISpaceObject;
import jadex.extension.envsupport.environment.ISpaceProcess;
import jadex.extension.envsupport.environment.space2d.Grid2D;
import jadex.extension.envsupport.environment.space2d.Space2D;
import jadex.extension.envsupport.math.IVector1;
import jadex.extension.envsupport.math.Vector1Double;
import jadex.extension.envsupport.math.Vector2Double;
import jadex.extension.envsupport.math.Vector2Int;

import java.util.*;

public class StatisticsProcess extends SimplePropertyObject implements ISpaceProcess {

    //TIMERS FOR THE INCIDENT
    private static final long initialWaitingTime = 4000;
    private static long startTime = 0; //get from the jadex clock
    private SimulationInitialState simulationInitialState;
    private SimulationFinalState simulationFinalState;
    //SPACE VARIABLES FOR CALCULATIONS
    private Space2D space;
    private WorldMethods worldMethods;

    @Override
    public void start(IClockService arg0, IEnvironmentSpace arg1) {

        //variables initialization
        space = (Space2D)arg1;
        startTime = arg0.getTime();
        worldMethods = new WorldMethods((Grid2D)space);

        if(simulationInitialState == null){
            simulationInitialState = new SimulationInitialState();
            simulationInitialState.numActive = space.getSpaceObjectsByType(TypesObjects.WANDERER).length;
            simulationInitialState.numConservative = space.getSpaceObjectsByType(TypesObjects.CONSERVATIVE).length;
            simulationInitialState.numHerding = space.getSpaceObjectsByType(TypesObjects.HERDING).length;
            simulationInitialState.numDoors = space.getSpaceObjectsByType(TypesObjects.DOOR).length;
            //TODO - special door -> manual
        }
    }

    @Override
    public void shutdown(IEnvironmentSpace iEnvironmentSpace) {

    }

    @Override
    public void execute(IClockService iClockService, IEnvironmentSpace iEnvironmentSpace) {

        if(worldMethods.isIncident() && !worldMethods.agentsAlive()){
            simulationFinalState = new SimulationFinalState();
            simulationFinalState.incidentType = worldMethods.getIncidentType();
            simulationFinalState.numDead = space.getSpaceObjectsByType(TypesObjects.DEAD_AGENT).length;
            simulationFinalState.numEscaped = space.getSpaceObjectsByType(TypesObjects.ESCAPED_AGENT).length;
            simulationFinalState.numHurt = worldMethods.countHurt();
            simulationFinalState.evacuationTimeSeconds = iClockService.getTime() - startTime - initialWaitingTime;

            if(verify()){
                printStatisticsToCSV();
                System.exit(0);
            }

            System.exit(-1);
        }
    }

    private class SimulationInitialState {
        public int numActive;
        public int numHerding;
        public int numConservative;
        public int numDoors;
    }

    private class SimulationFinalState {
        public int numHurt;
        public int numEscaped;
        public int numDead;
        public String incidentType;
        public long evacuationTimeSeconds;
    }

    boolean verify(){
        int finalAgents = simulationFinalState.numDead + simulationFinalState.numEscaped;
        int initialAgents = simulationInitialState.numActive + simulationInitialState.numConservative + simulationInitialState.numHerding;
        if(initialAgents == finalAgents)
            return true;
        return false;
    }

    private void printStatisticsToCSV() {
        //simulationInitialState
        //simulationFinalState
    }
}
