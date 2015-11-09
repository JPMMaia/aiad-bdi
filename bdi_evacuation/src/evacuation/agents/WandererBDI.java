package evacuation.agents;

import jadex.bdiv3.BDIAgent;
import jadex.bdiv3.annotation.Belief;
import jadex.extension.envsupport.environment.ISpaceObject;
import jadex.extension.envsupport.environment.space2d.Grid2D;
import jadex.micro.annotation.Agent;
import jadex.micro.annotation.AgentBody;

@Agent
public class WandererBDI
{
    @Agent
    protected BDIAgent mAgent;

    @Belief
    protected Grid2D mSpace = (Grid2D)mAgent.getParentAccess().getExtension("2dspace").get();

    @Belief
    protected ISpaceObject mAvatar = mSpace.getAvatar(mAgent.getComponentDescription(), mAgent.getModel().getFullName());

    @AgentBody
    public void body()
    {
    }
}