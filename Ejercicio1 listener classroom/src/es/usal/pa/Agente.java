package es.usal.pa;

import java.util.Scanner;

import jade.content.lang.sl.SLCodec;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.core.behaviours.ThreadedBehaviourFactory;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

public class Agente extends Agent implements CyclicBehaviourimprimirListener {
	
	protected ParalelBehaviourMensaje paralelBehaviourMensaje;

	@Override
	protected void setup() {
		//registrar servicios
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		
		ServiceDescription sd = new ServiceDescription();
		sd.setName("Servicio mensajería");
		sd.setType("mensajeria");
		sd.addOntologies("ontología");
		sd.addLanguages(new SLCodec().getName());
		dfd.addServices(sd);
		
		try {
			DFService.register(this, dfd);
		} catch(FIPAException e) {
			System.err.println("Agente " +getLocalName() + ": " + e.getMessage());
		}
		
		//registrar comportamiento
		paralelBehaviourMensaje = new ParalelBehaviourMensaje(this);
		addBehaviour(paralelBehaviourMensaje);
	}

	@Override
	public void mensajeRecibido(String mensaje) {
		// TODO Auto-generated method stub
		System.out.println("Mensaje: " + mensaje);
	}
}