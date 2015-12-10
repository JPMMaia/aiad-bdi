package evacuation.agents;

import evacuation.utils.PersonState;
import evacuation.utils.Position;
import evacuation.utils.TypesObjects;
import evacuation.utils.TypesProperties;
import jadex.bdiv3.annotation.*;
import jadex.extension.envsupport.environment.ISpaceObject;
import jadex.extension.envsupport.environment.SpaceObject;
import jadex.extension.envsupport.math.IVector1;
import jadex.micro.annotation.Agent;
import jadex.micro.annotation.AgentBody;

import java.util.HashSet;

@Agent
public class SocialAgentBDI extends WalkerBDI{

    //CONSTANTS***************************
    protected static final int DISTANCE_TO_HELP = 3;

    // CURE ATTRIBUTES
    protected HashSet<SpaceObject> curesSet;
    SpaceObject agentNeedsHelp;

    //PUSH ATTRIBUTES
    protected HashSet<SpaceObject> pushSet;

    boolean iAmHurt = false;

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

            if(agentIsHurt()) {
                //System.out.println("cant cure is hurt");
                return;
            }

            if(agentNeedsHelp != null) {

                //go to an adjacent cell
                //-calculate distance from hurtAgent
                Position targetPosition = Position.convertToPosition(agentNeedsHelp.getProperty(TypesProperties.POSITION));
                IVector1 distanceIV = worldMethods.getDistanceBetweenTwoPositions(currentPosition, targetPosition);
                double distance = distanceIV.getAsDouble();

                if (distance <= 1) {//if it is near -> cure
                    //System.out.println("cure"); //create cure object
                    if(!worldMethods.isCureObjectInPosition(targetPosition)) {
                        SpaceObject cure = worldMethods.makeObjectInCell(targetPosition, TypesObjects.CURE_AGENT);
                        curesSet.add(cure);
                    }
                }
                else { //go to the hurt
                    mExplorer.setGoal(targetPosition, true);
                    mExplorer.move();
                    nextPosition = mExplorer.getPosition();
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
            SpaceObject pushObj = worldMethods.makeObjectInCell(currentPosition,TypesObjects.PUSH_AGENT);
            pushSet.add(pushObj);
        }
    }

    @AgentBody
    public void body(){
        super.body();
        curesSet = new HashSet<>();
        pushSet = new HashSet<>();
    }

    //OTHER METHODS
    boolean agentIsHurt(){
        return iAmHurt;
    }

    void deleteCures(){
        for(SpaceObject cure : curesSet){
            space.destroyAndVerifySpaceObject(cure.getId());
        }
    }

    void deletePush(){
        for(SpaceObject p : pushSet){
            space.destroyAndVerifySpaceObject(p.getId());
        }
    }
}
