

import jadex.bdiv3.BDIAgent;
import jadex.bdiv3.annotation.Belief;
import jadex.extension.envsupport.environment.ISpaceObject;
import jadex.extension.envsupport.environment.space2d.Grid2D;
import jadex.extension.envsupport.math.Vector2Int;
import jadex.micro.annotation.Agent;
import jadex.micro.annotation.AgentBody;

import java.util.Random;

/**
 * Created by Leonel AraÃºjo on 22/11/2014.
 */

@Agent
public class HerdingBDI {
	
    @Agent
    protected BDIAgent agent;
    
    /****************************
     			BELIEFS
     ***************************/
    
    @Belief
    protected Grid2D space = (Grid2D)agent.getParentAccess().getExtension("2dspace").get();

    @Belief
    protected ISpaceObject myself = space.getAvatar(agent.getComponentDescription(), agent.getModel().getFullName());

    PersonState p;
	
    @Belief
    protected boolean indoor; //range [0-100]
    
    @Belief
    protected int condition; //range [0-100]
    
    @Belief
    protected int velocity; //range [0-100]
    
    @Belief
    protected int riskPerception; //range [0-100] //TODO metodo que faz update a percepcao de risco consoante o ambiente
    
    @Belief
    protected boolean isTrapped;
    
    @Belief
    protected boolean isBeingPushed;
    
    @Belief
    protected boolean isDown;
    
    /****************************
		      GOALS
     ***************************/
    
    //safety -> TODO trigger riskPerception > 10 -> Estabelecer os níveis de segurança e diminuir a percepção de risco.
    //increaseDistanceFromDanger
    //findExit //Procurar informações
    //followOthers
    //go
    //helpOthers altruisticamente ajudando os outros. //triggered by others call warning (env)
    //pushOthers empurrar os outros triggered by riskPerception > 90 && isTrapped == true

    
    
    /****************************
              PLANS
     ***************************/
    
    //SafetyPlan - Open the goal getDistanceFromDanger
    //getDistanceFromDangerPlan - if i am indoor check the door position and run -> open the goal findExit
    //                            else check danger position and run to the opposite way
    //getExitPlan
    //1 - herding - if there are others and if they can conduct to exit - follow goal
    //            - else find exit
    //2 - conservative - look for a known empty path
    //3 - active - look for the fastest empty path
    //goExitPlan
    //helpOthersPlan - loose time and the other isDown = false
    //pushOthersPlan - other condition gets worse

    
    
    
    /****************************
	           BODY
	***************************/

    //deal with being hurt by the environment - condition decreases if distance from danger < value
    // condition decreases if is being pushed
    // condition = 0 -> is dead -> the others can pass by
    
    @AgentBody
    public void body(){
        ISpaceObject[] arvoresNoEspaco = space.getSpaceObjectsByType("terrain");

        Random r = new Random();

        int spaceHeight = space.getAreaSize().getXAsInteger();
        int spaceWidth = space.getAreaSize().getYAsInteger();

        myself.setProperty("position", new Vector2Int(r.nextInt(spaceWidth), r.nextInt(spaceHeight)));

    }

}
