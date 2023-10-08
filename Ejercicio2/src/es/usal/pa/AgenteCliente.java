package es.usal.pa;

import java.util.List;
import java.util.Scanner;

import jade.content.lang.sl.SLCodec;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

public class AgenteCliente extends Agent 
{
	
	@Override
	protected void setup()
	{
		addBehaviour(new CyclicBehaviour() {
			
			@Override
			public void action() {
				// TODO Auto-generated method stub
				
				//leer cadena y enviar peticion
				Scanner scanner=new Scanner(System.in);
				String mensaje=scanner.nextLine();
				
				Utils.enviarMensaje(this.myAgent, "buscar", mensaje);
				
				//esperar respuesta e imprimir mediante un bucle todas las noticias
				ACLMessage msg=this.myAgent.blockingReceive(MessageTemplate.and(MessageTemplate.MatchOntology("ontologia"), MessageTemplate.MatchPerformative(ACLMessage.INFORM)));
				try {
					List<String> listado=(List<String>)msg.getContentObject();
					
					for(int i=0; i<listado.size(); i++) {
						System.out.println("Noticia: "+listado.get(i));
					}
				} catch (UnreadableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		});
	}

}