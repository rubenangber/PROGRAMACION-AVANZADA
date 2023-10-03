package es.usal.pa;

import java.util.ArrayList;
import java.util.List;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

public class CyclicbehaviourImprimir extends CyclicBehaviour {
	
	protected List <CyclicBehaviourimprimirListener> listCyclicBehaviourimprimirListener;
	
	public CyclicbehaviourImprimir() {
		super();
		listCyclicBehaviourimprimirListener = new ArrayList<CyclicBehaviourimprimirListener>();
	}
	
	public void addCyclicBehaviourimprimirListener(CyclicBehaviourimprimirListener cyclicBehaviourimprimirListener) {
		listCyclicBehaviourimprimirListener.add(cyclicBehaviourimprimirListener);
	}
	
	public void removeCyclicBehaviourimprimirListener(CyclicBehaviourimprimirListener cyclicBehaviourimprimirListener) {
		listCyclicBehaviourimprimirListener.remove(cyclicBehaviourimprimirListener);
	}

	@Override
	public void action() {
		// TODO Auto-generated method stub
		ACLMessage msg = this.myAgent.blockingReceive();
		
		try {
			System.out.println("Origen: " + msg.getSender().getName() + ": " + (String)msg.getContentObject());
			for(int i = 0; i < listCyclicBehaviourimprimirListener.size(); i++) {
				listCyclicBehaviourimprimirListener.get(i).mensajeRecibido((String)msg.getContentObject());
			}
		} catch (UnreadableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}