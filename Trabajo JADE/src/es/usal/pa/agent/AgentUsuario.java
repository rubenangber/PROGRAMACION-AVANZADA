package es.usal.pa.agent;

import java.util.List;
import java.util.Scanner;

import es.usal.pa.modelo.User;
import es.usal.pa.modelo.Parada;


import es.usal.pa.agent.Utils;
import jade.content.lang.sl.SLCodec;
import jade.core.Agent;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.Envelope;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

import java.io.Serializable;
import java.io.IOException;
import java.util.Scanner;

public class AgentUsuario extends Agent
{
	protected void setup()
	{
		
		User u=new User();
		Scanner scanner=new Scanner(System.in);
		System.out.print("\nBUS NOCTURNO\n ");
		System.out.print("\nOrigen: ");
		u.setOrigen(new Integer(scanner.nextLine()));
		System.out.print("Destino: ");
		u.setDestino(new Integer(scanner.nextLine()));
		System.out.print("Hora: ");
		u.setHora(new Float(scanner.nextLine()));
		
			
		try {
			enviarSolicitudRuta(u);
		} catch (UnreadableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		scanner.close();

	}
	
	 private void enviarSolicitudRuta(User u) throws UnreadableException {
	        // Buscar el agente que proporciona el servicio de rutas
	        DFAgentDescription [] dfd = Utils.buscarAgentes(this, "ruta");

	        if (dfd != null) {
	            // Crear mensaje de solicitud
	        	ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
	        	for(int i=0;i<dfd.length;i++)
	        		msg.addReceiver(dfd[i].getName());
	        	msg.setProtocol("fipa-request");
	        	msg.setOntology("ontologia");
	        	msg.setLanguage(new SLCodec().getName());
	        	msg.setEnvelope(new Envelope());
	        	msg.getEnvelope().setPayloadEncoding("ISO8859_1");

	        	// Agrega un protocolo al mensaje
	        	

	            // Configurar el contenido del mensaje (Usuario con origen, destino y hora de salida)
	            User usuario = new User(u.getOrigen(), u.getDestino(), u.getHora());
	            
	            try {
	                msg.setContentObject((Serializable) usuario);
	            } catch (IOException e) {
	                e.printStackTrace();
	            }

	            // Enviar el mensaje
	            send(msg);
	            ACLMessage msgUsuarioE = blockingReceive(MessageTemplate.MatchPerformative(ACLMessage.INFORM));
	            List<Parada> recorrido=null;
	            try
	    		{	
	            	recorrido = (List<Parada>)msgUsuarioE.getContentObject();
	            	System.out.println("\nRecorrido Minimo:");
	    			System.out.print("\n "+msgUsuarioE.getContentObject()+ "\n");
	    			recorrerRecorrido(recorrido);
	    		}
	    		catch (UnreadableException e)
	    		{
	    			e.printStackTrace();
	    		}
	           
	            //addBehaviour(new BehaviourRecibirRecorrido(this));
	            
	        }
	 }

	private void recorrerRecorrido(List<Parada> resultado) {
		// Imprimir el recorrido por pantalla
        
        if (resultado != null) {
        	for (int i = 0; i < resultado.size(); i++) {
        	    Parada parada = resultado.get(i);
        	    System.out.print("PARADA: " + parada.getNumero());

        	    // Imprime la línea solo para las paradas que no son la última
        	    if (i < resultado.size() - 1) {
        	    	
        	        System.out.println("\tTIEMPO de llegada a la parada: " + parada.getHora()+"\tAhora subes en la LINEA: " + parada.getLinea());
        	    } else {
        	        System.out.println( "\tTIEMPO TOTAL: " + parada.getHora()+ "\t\t\tAcaba en la LINEA:"+ parada.getLinea());
        	    }
        	}

		} else {
			System.out.println("No se encontró un recorrido desde el origen hasta el destino.");
		
		}
	}

}
