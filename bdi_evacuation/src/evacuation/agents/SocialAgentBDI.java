package evacuation.agents;

import evacuation.utils.Position;
import evacuation.utils.TypesObjects;
import evacuation.utils.TypesProperties;
import jadex.bdiv3.annotation.*;
import jadex.extension.envsupport.environment.ISpaceObject;
import jadex.extension.envsupport.math.IVector1;
import jadex.extension.envsupport.math.IVector2;
import jadex.extension.envsupport.math.Vector2Int;
import jadex.micro.annotation.Agent;
import jadex.micro.annotation.AgentBody;

import java.util.HashMap;
import java.util.Map;

@Agent
public class SocialAgentBDI extends WalkerBDI{

    //CONSTANTS***************************

    protected static final int DISTANCE_TO_HELP = 10;

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

                if (distance <= 1) {//if it is near you cure
                    System.out.println("cure"); //create cure object
                    Map<String, Object> properties = new HashMap<>();
                    properties.put("position", new Vector2Int(targetPosition.x, targetPosition.y));
                    space.createSpaceObject(TypesObjects.CURE_AGENT, properties, null);
                }
                else //go to the hurt
                    nextPosition = worldMethods.findPathToObject(hurtAgent, currentPosition);
            }
        }
    }

    @Plan(trigger=@Trigger(goals=PushOthersGoal.class))
    public class PushOthersPlan {
        //pushOthersPlan - other condition gets worse and if = 0 they die
        @PlanBody
        protected void PushOthersPlanBody() {
            //if there is someone in the same cell as me
            //create a push object

            //-> find a place where the being pushed is used
        }
    }

    @AgentBody
    public void body(){
        super.body();
    }
}
