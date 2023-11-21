package es.usal.pa.agent;

import jade.content.lang.sl.SLCodec;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Vector;

import es.usal.pa.modelo.Linea;
import es.usal.pa.modelo.User;

public class CyclicBehaviourLinea extends CyclicBehaviour {
	protected Float[] inicioLineaA = {1.0f, 2.0f, 150.0f, 200.0f, 300.0f};
    protected Integer[]paradasLineaA = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
    protected Float[] tiemposLineaA = {2.0f, 3.0f, 2.0f, 7.0f, 1.0f, 4.0f, 3.0f, 2.0f, 1.0f};
    protected int lineaA= 1; 

    protected Float[]inicioLineaB ={1.0f, 7.0f, 15.0f, 320.0f, 350.0f};
    protected Integer[]paradasLineaB = {11, 4, 12, 9, 13};
    protected Float[]tiemposLineaB = {3.0f, 4.0f, 3.0f, 2.0f};
    protected int lineaB= 2;

    public CyclicBehaviourLinea(Agent agent) {
        super(agent);
    }

    @Override
    public void action() {
    	ACLMessage msgRuta = this.myAgent.blockingReceive(MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.REQUEST), MessageTemplate.MatchOntology("ontologia")));

        

        if (msgRuta != null) {
            try {

                ACLMessage reply = msgRuta.createReply();
                reply.setPerformative(ACLMessage.INFORM);

                if (myAgent.getLocalName().equals("LineaA")) {
                    Linea linea1 = new Linea(lineaA,inicioLineaA, paradasLineaA, tiemposLineaA);

                    reply.setContentObject(linea1);
                } else if (myAgent.getLocalName().equals("LineaB")) {
                	Linea linea2 = new Linea(lineaB, inicioLineaB, paradasLineaB, tiemposLineaB);
                    reply.setContentObject(linea2);
                }

                myAgent.send(reply);
            	
            	

            } catch (NumberFormatException e) {
                e.printStackTrace();
            } catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        } else {
            block();
        }
    }

}
