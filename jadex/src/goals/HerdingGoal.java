package goals;
import jadex.bdiv3.annotation.Goal;
import jadex.bdiv3.annotation.GoalParameter;
import jadex.bdiv3.annotation.GoalResult;
 
@Goal
public class HerdingGoal {
 
	@GoalParameter
	protected String p;
 
	@GoalResult
	protected int r;
 
	public HerdingGoal(String p) {
		this.p = "Follow the nearest neighbor";
	}
 
}