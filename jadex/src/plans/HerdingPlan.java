package plans;

import jadex.bdiv3.annotation.Plan;
import jadex.bdiv3.annotation.PlanBody;
 
@Plan
public class HerdingPlan {
 
	private int secs;
 
	public HerdingPlan(int seconds) {
		secs = seconds;
	}
 
	@PlanBody
	public void herdingPlanBody() {
		System.out.println("I am following you!");
 
		//if i have a neighboor 
			//follow
		//else search in memory a known path
		
		//try to find neighbor		
	}
 
}