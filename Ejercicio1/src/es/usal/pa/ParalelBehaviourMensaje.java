package es.usal.pa;

import jade.core.behaviours.ParallelBehaviour;
import jade.core.behaviours.ThreadedBehaviourFactory;

public class ParalelBehaviourMensaje extends ParallelBehaviour {
	
	protected CyclicbehaviourImprimir cyclicbehaviourImprimir;
	protected OneShotBehaviourEnviar oneShotBehaviourEnviar;
	
	public ParalelBehaviourMensaje() {
		ThreadedBehaviourFactory tbf = new ThreadedBehaviourFactory();
		cyclicbehaviourImprimir = new CyclicbehaviourImprimir();
		addSubBehaviour(tbf.wrap(cyclicbehaviourImprimir));
		
		oneShotBehaviourEnviar = new OneShotBehaviourEnviar();
		addSubBehaviour(tbf.wrap(oneShotBehaviourEnviar));
		
	}
}
