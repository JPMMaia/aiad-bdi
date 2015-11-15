package evacuation.agents;

import evacuation.utils.Move;
import evacuation.utils.Position;
import evacuation.utils.TypesObjects;
import evacuation.utils.TypesProperties;
import jadex.bdiv3.BDIAgent;
import jadex.bdiv3.annotation.*;
import jadex.extension.envsupport.environment.ISpaceObject;
import jadex.extension.envsupport.environment.space2d.Grid2D;
import jadex.extension.envsupport.math.*;
import jadex.micro.annotation.Agent;
import jadex.micro.annotation.AgentBody;

import java.util.HashSet;
import java.util.Set;

@Agent
public class EvacuationAgentBDI {

    @Agent
    protected BDIAgent agent;

    // ATTRIBUTES ****************************

    Move move;
    //int agentType; // 0 active, 1- herding, 2 - conservative
    protected HashSet<Object> targets;

    //BELIEFS ********************************

    @Belief
    protected Grid2D space = (Grid2D)agent.getParentAccess().getExtension("2dspace").get();

    @Belief
    protected ISpaceObject myself = space.getAvatar(agent.getComponentDescription(), agent.getModel().getFullName());

    @Belief
    protected int velocity = 50; //range [0-100]

    @Belief
    protected boolean indoor = true;

    @Belief
    protected int condition = 100; //range [0-100]


    //Beliefs that trigger goals********

    @Belief(updaterate=100)
    protected boolean isIncident = (space.getSpaceObjectsByType(TypesObjects.INCIDENT).length != 0);

    @Belief(dynamic=true)
    Position nextPosition;

    @Belief
    protected int riskPerception = 0;

    @Belief
    protected boolean samePosition = false;

    @Belief(dynamic=true)
    protected boolean inPanic = (riskPerception > 90);

    @Belief
    protected boolean isTrapped = false;

    @Belief
    protected boolean isBeingPushed = false;

    @Belief
    protected boolean isDown = false;

    //GOALS************************************

    @Goal
    public class WanderGoal {

        @GoalParameter
        protected String goal = "WanderGoal";
    }

    @Goal(excludemode= Goal.ExcludeMode.Never)
    public class MaintainSafetyGoal {

        @GoalMaintainCondition(beliefs="riskPerception")
        protected boolean maintain() {
            //System.out.println("mantain riskPerception");
            return riskPerception <= 10;
        }

        @GoalTargetCondition(beliefs="riskPerception")
        protected boolean target() {
            //System.out.println("target riskPerception");
            return riskPerception <= 10;
        }
    }

    @Goal
    public class IncreaseDistanceFromDangerGoal {

        @GoalParameter
        protected String goal = "IncreaseDistanceFromDangerGoal";
    }

    @Goal
    public class FindExitGoal {

        @GoalParameter
        protected String goal = "FindExitGoal";
    }

    @Goal
    public class FollowOthersGoal {

        @GoalParameter
        protected String goal = "FollowOthersGoal";
    }

    /*

    @Goal
    public class HelpOthersGoal { //altruisticamente ajudando os outros. //triggered by others call warning (env)

        @GoalParameter
        protected String goal = "HelpOthersGoal";
    } //periodicamente getNum certo raio alguem com condição fisica inferior a x

    @Goal
    public class PushOthersGoal {

        @GoalCreationCondition(beliefs="inPanic") //empurrar os outros triggered by riskPerception > 90 && isTrapped == true
        public PushOthersGoal() {
        }

    }

    @Goal
    public class ABeliefBasedGoal {

        @GoalContextCondition(beliefs="s")
        public boolean checkCondition() {
            return false;
        }

        @GoalCreationCondition(beliefs="s")
        public ABeliefBasedGoal() {
        }

    }*/

     // PLANS**********************************

    @Plan(trigger=@Trigger(goals=WanderGoal.class))
    public class WanderPlan {
        @PlanBody
        protected void WanderPlanBody() {

            //System.out.println("WanderPlanBody");

            Position oldPosition = move.getPosition(myself);
            Position wantedPosition = move.getNewPosition(oldPosition);
            if(noCollisions(wantedPosition))
                nextPosition = wantedPosition;

            if(!isIncident)
                agent.dispatchTopLevelGoal(new WanderGoal());
        }
    }

    @Plan(trigger=@Trigger(factchangeds="nextPosition"))
    public class GoPlan {
        @PlanBody
        protected void GoPlanBody() {
            //System.out.println("Position - (" + nextPosition.x + ", " + nextPosition.y + ")");
            myself.setProperty("position", new Vector2Int(nextPosition.x, nextPosition.y));

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                System.out.println("unable to sleep");
            }
        }
    }

    @Plan(trigger=@Trigger(factchangeds="isIncident"))
    public class EvaluateIncidentRiskPlan {

        @PlanBody
        protected void EvaluateIncidentRiskPlanBody() {
            if(isIncident) {
                System.out.println("DANGER!");
                //System.out.println("isIncident - " + isIncident);
                evaluateRisk();
            }
        }
    }

    @Plan(trigger=@Trigger(goals=MaintainSafetyGoal.class))
    public class MaintainSafetyPlan {

        @PlanBody
        protected void MaintainSafetyPlanBody() {
            //System.out.println("MaintainSafetyPlanBody");
            agent.dispatchTopLevelGoal(new IncreaseDistanceFromDangerGoal());
            evaluateRisk();
        }
    }

    @Plan(trigger=@Trigger(goals=IncreaseDistanceFromDangerGoal.class))
    public class IncreaseDistanceFromDangerPlan {

        @PlanBody
        protected void IncreaseDistanceFromDangerPlanBody() {
            //System.out.println("IncreaseDistanceFromDangerPlanBody");
            if(indoor){
                agent.dispatchTopLevelGoal(new FindExitGoal());
            }
            else{
                //agent.dispatchTopLevelGoal(goGoal()); check danger position and run to the opposite way
            }
        }
    }

    @Plan(trigger=@Trigger(goals=FindExitGoal.class))
    public class FindExitPlan {

        @PlanBody
        protected void FindExitPlanBody() {

            nextPosition = findExit();

            /*
            if(agentType == 0) {
                //1 - active - look for the fastest empty path - the search strategy is able to come back
                nextPosition = findNewPositionWhenIncident();
            }
            else if(agentType == 1){
                //2 - conservative - look for a known empty path - greedy
                nextPosition = findNewPositionWhenIncident();
            }
            else if(agentType == 2){

                //3 - herding - if there are others and if they can conduct to exit - follow goal
                //            - else find exit

                //find agents in a certain perimeter


            }
            */
        }
    }

    protected Position findExit() {
        return null;
    }

     /*

    @Plan(trigger=@Trigger(goals=HelpOthersGoal.class))
    public class HelpOthersPlan {
        //helpOthersPlan - loose time and improve others condition
        @PlanBody
        protected void HelpOthersPlanBody() {
        }
    }

    @Plan(trigger=@Trigger(goals=PushOthersGoal.class))
    public class PushOthersPlan {
        //pushOthersPlan - other condition gets worse and if = 0 they die
        @PlanBody
        protected void PushOthersPlanBody() {
        }
    }*/

    /****************************
     BODY
     ***************************/

    @AgentBody
    public void body(){

        move = new Move( space.getAreaSize().getXAsInteger(), space.getAreaSize().getYAsInteger());
        isIncident = false;
        targets = new HashSet<>();
        //agentType = 0;

        agent.dispatchTopLevelGoal(new MaintainSafetyGoal());
        agent.dispatchTopLevelGoal(new WanderGoal());
    }


    // FUNCTIONS *************************************

    protected boolean noCollisions(Position p) {
        Vector2Double wantedPosition = new Vector2Double(p.x,p.y);
        IVector1 distance = new Vector1Double(0);

        Set terrainSet = space.getNearObjects(wantedPosition,distance,TypesObjects.TERRAIN);
        Set incidentSet = space.getNearObjects(wantedPosition,distance,TypesObjects.INCIDENT);
        Set agentSet = space.getNearObjects(wantedPosition,distance,TypesObjects.WANDERER);

        if(!terrainSet.isEmpty()) //there is a wall or an obstacle
            return false;
        else if(!incidentSet.isEmpty()) //there are incidents in the way
            return false;
        else if(agentSet.size() > 1) //there are two agents in the position
            return false;

        return true;
    }

    protected Position findNewPositionWhenIncident() {

        //find one door in same division
        ISpaceObject[] doors = space.getSpaceObjectsByType(TypesObjects.DOOR);
        ISpaceObject door = pickClosestObject(doors);

        //get path for the door -> improve the search - TODO Maiah
        return findPathToObject(door);
    }

    protected Position findPathToObject(ISpaceObject object) {
        if(object != null){
            //space.getShortestDirection()
            Position destinyPosition = move.convertToPosition(object.getProperty(TypesProperties.POSITION));
            Position oldPosition = new Position(nextPosition.x, nextPosition.y);
            Position newPosition = null;

            if(Math.abs(destinyPosition.x - oldPosition.x) > Math.abs(destinyPosition.y - oldPosition.y)) { //move in x
                if(destinyPosition.x < oldPosition.x)
                    newPosition = new Position(nextPosition.x-1, nextPosition.y);
                else if(destinyPosition.x > oldPosition.x)
                    newPosition = new Position(nextPosition.x+1, nextPosition.y);
            }
            else { //move in y
                if(destinyPosition.y < oldPosition.y)
                    newPosition = new Position(nextPosition.x, nextPosition.y-1);
                else if(destinyPosition.y > oldPosition.y)
                    newPosition = new Position(nextPosition.x, nextPosition.y+1);
            }

            if(newPosition != null && noCollisions(newPosition)) {
                return newPosition;
            }
        }

        samePosition = true;
        return nextPosition;
    }

    protected ISpaceObject pickClosestObject(ISpaceObject[] objects) {

        objects = getTheDoorsInSameDivision(objects);

        Vector2Double currentPosition = new Vector2Double(nextPosition.x,nextPosition.y);

        if(objects.length > 0){
            System.out.println("objects lenght - " + objects.length);
            int pos = 0;

            Position wantedPosition = move.convertToPosition(objects[pos].getProperty(TypesProperties.POSITION));

            IVector1 distance = space.getDistance(currentPosition, new Vector2Double(wantedPosition.x,wantedPosition.y));

            for(int i = 1; i < objects.length; i++){
                IVector1 newDistance = space.getDistance(currentPosition, (IVector2) objects[i].getProperty(TypesProperties.POSITION));

                if(targets.contains(objects[i]))
                    continue;

                if(distance.greater(newDistance)){
                    pos = i;
                    distance = newDistance;
                }
            }
            return objects[pos];
        }
        return null;
    }

    protected ISpaceObject[] getTheDoorsInSameDivision(ISpaceObject[] doors) {
        //TODO
        //See if there is at least one path to the door, without collide to a wall

        return doors;
    }


    /****************************
     FUNCTIONS FOR THE RISK CALCULATIONS
     ***************************/

    protected void evaluateRisk() {
        //TODO
        riskPerception =  90;
    }
}