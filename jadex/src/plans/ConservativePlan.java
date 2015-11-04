package plans;

import jadex.bdiv3.annotation.Plan;
import jadex.bdiv3.annotation.PlanBody;
 
@Plan
public class ConservativePlan {
 
	private int secs;
 
	public ConservativePlan(int seconds) {
		secs = seconds;
	}
 
	@PlanBody
	public void conservativePlanBody() {
		System.out.println("I am looking for a known route!");
 
		// Search in the memory for the path.
		
	}
 
}