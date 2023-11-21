package es.usal.pa.agent;

import es.usal.pa.modelo.User;

import java.util.Arrays;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import es.usal.pa.modelo.LlegadaLinea;


import jade.content.lang.sl.SLCodec;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.core.behaviours.ThreadedBehaviourFactory;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

public class AgentRuta extends Agent
{
    protected ParallelBehaviour parallelBehaviour;


	@Override
	protected void setup()
	{
		//Crear servicios proporcionados por el agentes y registrarlos en la plataforma
	    DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setName("Ruta");
        //establezco el tipo del servicio para poder localizarlo cuando haga una busqueda
        sd.setType("ruta"); 
        sd.addOntologies("ontologia");
        sd.addLanguages(new SLCodec().getName());
        dfd.addServices(sd);
	    
        try {
            DFService.register(this, dfd);
        } catch (FIPAException e) {
            System.err.println("Agente " + getLocalName() + ": " + e.getMessage());
        }
        // Tratar hasta 5 peticiones a la vez
        int maxPeticionesConcurrentes = 5;
        ParallelBehaviour parallelBehaviour = new ParallelBehaviour();

        ThreadedBehaviourFactory tbf;
        for (int i = 0; i < maxPeticionesConcurrentes; i++) {
            tbf = new ThreadedBehaviourFactory();
            parallelBehaviour.addSubBehaviour(tbf.wrap(new CyclicBehaviourRuta(this)));
        }

        addBehaviour(parallelBehaviour);

	   
    }
	
	
}
