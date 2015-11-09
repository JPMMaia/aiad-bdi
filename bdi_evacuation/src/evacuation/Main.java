package evacuation;

import jadex.base.Starter;
import jadex.bridge.IComponentIdentifier;
import jadex.bridge.IExternalAccess;
import jadex.bridge.service.RequiredServiceInfo;
import jadex.bridge.service.search.SServiceProvider;
import jadex.bridge.service.types.cms.CreationInfo;
import jadex.bridge.service.types.cms.IComponentManagementService;
import jadex.commons.future.ThreadSuspendable;

import java.util.HashMap;
import java.util.Map;

public class Main
{
	public static void main(String[] args)
	{
		ThreadSuspendable sus = new ThreadSuspendable();

		/**
		 * The interface for accessing components from the outside.
		 */
		IExternalAccess pl = Starter.createPlatform(new String[0]).get(sus);

		/**
		 * General interface for components that the container can execute.
		 */
		IComponentManagementService cms = SServiceProvider.getService(pl.getServiceProvider(), IComponentManagementService.class, RequiredServiceInfo.SCOPE_PLATFORM).get(sus);


		Map<String, Object> dic = new HashMap<>();
		dic.put("positionX", 1);
		dic.put("positionY", 1);

		for (int i = 0; i < 1; i++) {
			/**
			 * Interface for component identifiers.
			 */
			String classPath = "evacuation/agents/WandererBDI.class";
			IComponentIdentifier hw = cms.createComponent(classPath, new CreationInfo(dic)).getFirstResult(sus);
			System.out.println("started: "+hw);
		}
	}
}
