package evacuation.factories;

import jadex.extension.envsupport.environment.ISpaceObject;
import jadex.extension.envsupport.environment.space2d.Space2D;
import jadex.extension.envsupport.math.Vector2Int;

import java.util.HashMap;
import java.util.Map;

public class WandererAgentFactory extends AbstractAgentFactory
{
	private static WandererAgentFactory mInstance;

	public static WandererAgentFactory getInstance()
	{
		if(mInstance == null)
			mInstance = new WandererAgentFactory();

		return mInstance;
	}

	private WandererAgentFactory()
	{
	}

	public ISpaceObject createAgent(Space2D space, int positionX, int positionY)
	{
		Map<String, Object> agentArguments = new HashMap<>(1);
		agentArguments.put("position", new Vector2Int(positionX, positionY));

		return super.createAgent(space, "wanderer", agentArguments);
	}
}