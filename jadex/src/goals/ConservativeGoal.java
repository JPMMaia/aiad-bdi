package goals;
import jadex.bdiv3.annotation.Goal;
import jadex.bdiv3.annotation.GoalParameter;
import jadex.bdiv3.annotation.GoalResult;
 
@Goal
public class ConservativeGoal {
 
	@GoalParameter
	protected String p;
 
	@GoalResult
	protected int r;
 
	public ConservativeGoal(String p) {
		this.p = "Follow a known path to the exit";
	}
 
}