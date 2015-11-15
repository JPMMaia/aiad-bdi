package evacuation.factories;

import evacuation.utils.TypesObjects;
import jadex.extension.envsupport.environment.ISpaceObject;
import jadex.extension.envsupport.environment.space2d.Space2D;
import jadex.extension.envsupport.math.Vector2Int;

import java.util.HashMap;
import java.util.Map;

public class ConservativeAgentFactory extends AbstractAgentFactory
{
	private static ConservativeAgentFactory mInstance;

	public static ConservativeAgentFactory getInstance()
	{
		if(mInstance == null)
			mInstance = new ConservativeAgentFactory();

		return mInstance;
	}

	private ConservativeAgentFactory()
	{
	}

	public ISpaceObject createAgent(Space2D space, int positionX, int positionY)
	{
		Map<String, Object> agentArguments = new HashMap<>(1);
		agentArguments.put("position", new Vector2Int(positionX, positionY));

		return super.createAgent(space, TypesObjects.CONSERVATIVE, agentArguments);
	}
}