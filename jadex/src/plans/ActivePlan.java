package plans;

import jadex.bdiv3.annotation.Plan;
import jadex.bdiv3.annotation.PlanBody;
 
@Plan
public class ActivePlan {
 
	private int secs;
 
	public ActivePlan(int seconds) {
		secs = seconds;
	}
 
	@PlanBody
	public void activePlanBody() {
		System.out.println("I am looking for the fastest route!");
 
		// Choose the heuristics to find the shortest plan.
		//ex. go to the nearest door
		
	}
 
}