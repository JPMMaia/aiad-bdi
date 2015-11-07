package goals;
import jadex.bdiv3.annotation.Goal;
import jadex.bdiv3.annotation.GoalParameter;
import jadex.bdiv3.annotation.GoalResult;
 
@Goal
public class ActiveGoal {
 
	@GoalParameter
	protected String p;
 
	@GoalResult
	protected int r;
 
	public ActiveGoal(String p) {
		this.p = "Find the shortest path to the exit";
	}
 
}