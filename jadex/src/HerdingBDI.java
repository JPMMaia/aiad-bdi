package agents;

import jadex.bdiv3.BDIAgent;
import jadex.bdiv3.annotation.Belief;
import jadex.bdiv3.annotation.Goal;
import jadex.bdiv3.annotation.Goal.ExcludeMode;
import jadex.bdiv3.annotation.GoalMaintainCondition;
import jadex.bdiv3.annotation.GoalParameter;
import jadex.bdiv3.annotation.Plan;
import jadex.bdiv3.annotation.PlanBody;
import jadex.bdiv3.annotation.Trigger;
import jadex.extension.envsupport.environment.ISpaceObject;
import jadex.extension.envsupport.environment.space2d.Grid2D;
import jadex.extension.envsupport.math.Vector2Int;
import jadex.micro.annotation.Agent;
import jadex.micro.annotation.AgentBody;

import java.util.Random;


@Agent
public class HerdingBDI { //herding pode usar o goal retry -> tenta encontrar outros e segui-los, se isso nao funcionar procura por si
	
	
    @Agent
    protected BDIAgent agent;
    
    /****************************
     			BELIEFS
     ***************************/
   
    
    @Belief
    protected Grid2D space = (Grid2D)agent.getParentAccess().getExtension("2dspace").get();

    @Belief
    protected ISpaceObject myself = space.getAvatar(agent.getComponentDescription(), agent.getModel().getFullName());
	
    @Belief
    protected boolean indoor = true;
    
    @Belief
    protected int velocity = 50; //range [0-100]
    
    @Belief
    protected int condition = 100; //range [0-100]
    
    @Belief
	protected int riskPerception = 0; //range [0-100] //TODO metodo que faz update a percepcao de risco consoante o ambiente
    
    /*****************************
      Beliefs that trigger goals
     *****************************/
    
    @Belief(dynamic=true)
	protected boolean notSafe = (riskPerception > 10);
    
    @Belief
    protected boolean isTrapped = false;
    
    @Belief
    protected boolean isBeingPushed = false;
    
    @Belief
    protected boolean isDown = false;
    
    /****************************
		      GOALS
     ***************************/
    
    @Goal(excludemode=ExcludeMode.Never)
	public class MaintainSafetyGoal {
 
		@GoalMaintainCondition(beliefs="riskPerception")
		protected boolean mantain() {
			System.out.println("mantain riskPerception");
			return riskPerception <=10;
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
	public class GoGoal {
    	
    	@GoalParameter
    	protected String goal = "GoGoal";
	}
    
    @Goal
	public class FollowOthersGoal {
    	
    	@GoalParameter
    	protected String goal = "FollowOthersGoal";
	}
    
    //helpOthers altruisticamente ajudando os outros. //triggered by others call warning (env)
    //pushOthers empurrar os outros triggered by riskPerception > 90 && isTrapped == true
    
    /****************************
              PLANS
     ***************************/
    @Plan(trigger=@Trigger(goals=MaintainSafetyGoal.class))
	public class MaintainSafetyPlan {
 
		@PlanBody
		protected void MaintainSafetyPlanBody() {
			System.out.println("MaintainSafetyPlanBody");
			agent.dispatchTopLevelGoal(new IncreaseDistanceFromDangerGoal());
		}
	}
    
    @Plan(trigger=@Trigger(goals=IncreaseDistanceFromDangerGoal.class))
	public class IncreaseDistanceFromDangerPlan {
 
		@PlanBody
		protected void IncreaseDistanceFromDangerPlanBody() {
			
			//while(riskPerception > 10){
				
				System.out.println("IncreaseDistanceFromDangerPlanBody");
				if(indoor){
					agent.dispatchTopLevelGoal(new FindExitGoal());
				}
				else{
					//agent.dispatchTopLevelGoal(goGoal()); check danger position and run to the opposite way
				}
				
				if(riskPerception > 90){
					isTrapped = true;
				}
			//}
		}
	}
    
    @Plan(trigger=@Trigger(goals=FindExitGoal.class))
	public class FindExitPlan {
 
		@PlanBody
		protected void FindExitPlanBody() {
			System.out.println("FindExitPlan");
			
			//getExitPlan
			    //1 - herding - if there are others and if they can conduct to exit - follow goal
			    //            - else find exit
			    //2 - conservative - look for a known empty path
			    //3 - active - look for the fastest empty path
				
			//ask the world if there is any door available
			//if no empty door available riskPerception += 5
			
			agent.dispatchTopLevelGoal(new GoGoal());
			//if I didn't succeed in go -> riskPerception++
		}
	}
   
    //helpOthersPlan - loose time and the other isDown = false
    //pushOthersPlan - other condition gets worse

    
    
    
    /****************************
	           BODY
	***************************/

    //deals with being hurt by the environment - condition decreases if distance from danger < value
    // condition decreases if is being pushed
    // condition = 0 -> is dead -> the others can pass by
    
    @AgentBody
    public void body(){
    	
        ISpaceObject[] arvoresNoEspaco = space.getSpaceObjectsByType("terrain");

        Random r = new Random();

        int spaceHeight = space.getAreaSize().getXAsInteger();
        int spaceWidth = space.getAreaSize().getYAsInteger();

        myself.setProperty("position", new Vector2Int(r.nextInt(spaceWidth), r.nextInt(spaceHeight)));
        
        //declare here the first goal TODO
        agent.dispatchTopLevelGoal(new MaintainSafetyGoal());
        riskPerception = 90;
    }

}
