package goals;
import jadex.bdiv3.annotation.Goal;
import jadex.bdiv3.annotation.GoalParameter;
import jadex.bdiv3.annotation.GoalResult;
 
@Goal
public class HumanProximityGoal {
 
	@GoalParameter
	protected String p;
 
	@GoalResult
	protected int r;
 
	public HumanProximityGoal(String p) {
		this.p = "Find another person to follow";
	}
 
}