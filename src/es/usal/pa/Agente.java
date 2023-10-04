package es.usal.pa;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;

public class Agente extends Agent {
	
	protected CyclicBehaviour cyclicbehaviour; 
	
	@Override
	public void setup() {
		//definir servicios
		
		//definir comportamientos
		
		cyclicbehaviour = new CyclicBehaviour() {
			public void action() {
				block();
			}
		};
	}

}
