package evacuation.factories;

import java.util.Map;

import jadex.extension.envsupport.environment.ISpaceObject;
import jadex.extension.envsupport.environment.space2d.Space2D;

public abstract class AbstractAgentFactory
{
	public static ISpaceObject createAgent(AgentType agentType, Space2D space, int positionX, int positionY)
	{
		switch (agentType)
		{
			case Wanderer:
				return WandererAgentFactory.getInstance().createAgent(space, positionX, positionY);

			case Herding:
				return HerdingAgentFactory.getInstance().createAgent(space, positionX, positionY);


			default:
				return null;
		}
	}

	protected ISpaceObject createAgent(Space2D space, String agentName, Map<String, Object> agentArguments)
	{
		return space.createSpaceObject(agentName, agentArguments, null);
	}
}