package es.usal.pa.agent;

import jade.content.lang.sl.SLCodec;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

public class AgentLinea extends Agent
{
	@Override
    protected void setup() {
        // Crear servicios proporcionados por el agente y registrarlos en la plataforma
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setName("Linea");
        sd.setType("linea");
        sd.addOntologies("ontologia");
        sd.addLanguages(new SLCodec().getName());
        dfd.addServices(sd);

        try {
            DFService.register(this, dfd);
        } catch (FIPAException e) {
            System.err.println("Agente " + getLocalName() + ": " + e.getMessage());
        }

        // Esperar y procesar solicitudes de información de la línea
        CyclicBehaviourLinea cyclicBehaviourLinea = new CyclicBehaviourLinea(this);
        addBehaviour(cyclicBehaviourLinea);

    }
	
}
