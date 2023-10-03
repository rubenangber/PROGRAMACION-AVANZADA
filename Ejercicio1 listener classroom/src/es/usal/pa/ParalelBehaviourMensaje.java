package es.usal.pa;

import jade.core.behaviours.ParallelBehaviour;
import jade.core.behaviours.ThreadedBehaviourFactory;

public class ParalelBehaviourMensaje extends ParallelBehaviour {
	
	protected CyclicbehaviourImprimir cyclicbehaviourImprimir;
	protected OneShotBehaviourEnviar oneShotBehaviourEnviar;
	
	public ParalelBehaviourMensaje(CyclicBehaviourimprimirListener cyclicBehaviourimprimirListener) {
		ThreadedBehaviourFactory tbf = new ThreadedBehaviourFactory();
		cyclicbehaviourImprimir = new CyclicbehaviourImprimir();
		oneShotBehaviourEnviar = new OneShotBehaviourEnviar();
		
		addSubBehaviour(tbf.wrap(cyclicbehaviourImprimir));
		addSubBehaviour(tbf.wrap(oneShotBehaviourEnviar));
		
	}
}
