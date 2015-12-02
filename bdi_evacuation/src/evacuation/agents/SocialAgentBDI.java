package evacuation.agents;

import evacuation.utils.PersonState;
import evacuation.utils.Position;
import evacuation.utils.TypesObjects;
import evacuation.utils.TypesProperties;
import jadex.bdiv3.annotation.*;
import jadex.extension.envsupport.environment.ISpaceObject;
import jadex.extension.envsupport.environment.SpaceObject;
import jadex.extension.envsupport.math.IVector1;
import jadex.extension.envsupport.math.Vector2Int;
import jadex.micro.annotation.Agent;
import jadex.micro.annotation.AgentBody;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

@Agent
public class SocialAgentBDI extends WalkerBDI{

    //CONSTANTS***************************
    protected static final int DISTANCE_TO_HELP = 10;

    // CURE ATTRIBUTES
    protected HashSet<SpaceObject> cures;

    //PUSH ATTRIBUTES
    protected HashSet<SpaceObject> pushSet;

    //PERSONALITY

    PersonState personState = new PersonState();

    @Belief
    boolean patient = personState.getPatience();

    @Goal
    public class HelpOthersGoal { //altruisticamente ajudando os outros. //triggered by others call warning (env)

        @GoalParameter
        protected String goal = "HelpOthersGoal";
    } //periodicamente getNum certo raio alguem com condição fisica inferior a x

    @Goal
    public class PushOthersGoal {

        @GoalParameter
        protected String goal = "PushOthersGoal";
    }

    @Goal
    public class ReceivePushOthersGoal {

        @GoalParameter
        protected String goal = "ReceivePushOthersGoal";
    }

    @Plan(trigger=@Trigger(goals=HelpOthersGoal.class))
    public class HelpOthersPlan {
        //helpOthersPlan - loose time and improve others condition
        @PlanBody
        protected void HelpOthersPlanBody() {

            //find the nearest other
            ISpaceObject[] hurtAgents = space.getSpaceObjectsByType(TypesObjects.HURT_AGENT);
            ISpaceObject hurtAgent;

            if(hurtAgents != null && hurtAgents.length > 0) {
                hurtAgent = worldMethods.pickClosestObject(hurtAgents, null, currentPosition);

                //go to an adjacent cell
                //-calculate distance from hurtAgent
                Position targetPosition = Position.convertToPosition(hurtAgent.getProperty(TypesProperties.POSITION));
                IVector1 distanceIV = worldMethods.getDistanceBetweenTwoPositions(currentPosition, targetPosition);
                double distance = distanceIV.getAsDouble();

                if (distance <= 1) {//if it is near than cure
                    //System.out.println("cure"); //create cure object
                    Map<String, Object> properties = new HashMap<>();
                    properties.put("position", new Vector2Int(targetPosition.x, targetPosition.y));
                    SpaceObject cure = (SpaceObject) space.createSpaceObject(TypesObjects.CURE_AGENT, properties, null);
                    cures.add(cure);
                }
                else { //go to the hurt
                    Position wantedPosition = worldMethods.findPathToObject(hurtAgent, currentPosition);
                    nextPosition = wantedPosition;
                }
            }
        }
    }

    @Plan(trigger=@Trigger(goals=PushOthersGoal.class))
    public class PushOthersPlan {
        //pushOthersPlan - other condition gets worse and if = 0 they die
        @PlanBody
        protected void PushOthersPlanBody() {
            //if there is someone in the same cell as me
            //create a pushSet object
            worldMethods.makeObjectInCell(currentPosition,TypesObjects.PUSH_AGENT);
        }
    }

    @AgentBody
    public void body(){
        super.body();
        cures = new HashSet<>();
        pushSet = new HashSet<>();
    }

    //cures method

    void deleteCures(){
        for(SpaceObject cure : cures){
            space.destroyAndVerifySpaceObject(cure.getId());
        }
    }

    void deletePush(){
        for(SpaceObject p : pushSet){
            space.destroyAndVerifySpaceObject(p.getId());
        }
    }
}
