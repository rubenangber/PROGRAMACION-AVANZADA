package es.usal.pa;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

public class CyclicbehaviourImprimir extends CyclicBehaviour {

	@Override
	public void action() {
		// TODO Auto-generated method stub
		ACLMessage msg = this.myAgent.blockingReceive();
		
		try {
			System.out.println(msg.getSender().getName() + ": " + (String)msg.getContentObject());
		} catch (UnreadableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
