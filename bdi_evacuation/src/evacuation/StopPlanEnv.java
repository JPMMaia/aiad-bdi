package evacuation;


import jadex.bdi.runtime.IGoal;
import jadex.bdi.runtime.Plan;
import jadex.extension.envsupport.environment.IEnvironmentSpace;
import jadex.extension.envsupport.environment.ISpaceAction;

import java.util.HashMap;
import java.util.Map;

/**
 *  Burn a piece of garbage.
 */
public class StopPlanEnv extends Plan
{
	/**
	 *  The plan body.
	 */
	public void body()
	{
		System.out.println("StopPlan");
		IEnvironmentSpace env = (IEnvironmentSpace)getBeliefbase().getBelief("env").getFact();
	}
}
