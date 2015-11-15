package evacuation.factories;

import evacuation.utils.TypesObjects;
import jadex.extension.envsupport.environment.ISpaceObject;
import jadex.extension.envsupport.environment.space2d.Space2D;
import jadex.extension.envsupport.math.Vector2Int;

import java.util.HashMap;
import java.util.Map;

public class HerdingAgentFactory extends AbstractAgentFactory
{
	private static HerdingAgentFactory mInstance;

	public static HerdingAgentFactory getInstance()
	{
		if(mInstance == null)
			mInstance = new HerdingAgentFactory();

		return mInstance;
	}

	private HerdingAgentFactory()
	{
	}

	public ISpaceObject createAgent(Space2D space, int positionX, int positionY)
	{
		Map<String, Object> agentArguments = new HashMap<>(1);
		agentArguments.put("position", new Vector2Int(positionX, positionY));

		return super.createAgent(space, TypesObjects.HERDING, agentArguments);
	}
}