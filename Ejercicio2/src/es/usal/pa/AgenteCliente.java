package es.usal.pa;

import java.util.List;
import java.util.Scanner;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

public class AgenteCliente extends Agent {

	@Override
	protected void setup() {
		//registrar comportamiento
		addBehaviour(new CyclicBehaviour() {
			
			@Override
			public void action() {
				// TODO Auto-generated method stub
				Scanner sc = new Scanner(System.in);
				String mensaje = sc.nextLine();
				
				Utils.enviarMensaje(this.myAgent, "buscar", mensaje);
				
				//Esperar respuesta a imprimir mediante un bucle todas las noticias
				ACLMessage msg = this.myAgent.blockingReceive(MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.INFORM), MessageTemplate.MatchOntology("ontologia")));

				try {
					List<String> listado = (List<String>)msg.getContentObject();
					for(int i = 0; i < listado.size(); i++) {
						System.out.println(listado.get(i));
					}
				} catch (UnreadableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}
}