package es.usal.pa;

import java.util.Scanner;

import jade.core.behaviours.OneShotBehaviour;

public class OneShotBehaviourEnviar extends OneShotBehaviour {

	@Override
	public void action() {
		// TODO Auto-generated method stub
		Scanner sc = new Scanner(System.in);
		String mensaje;
		while(true) {
			mensaje = sc.nextLine();
			Utils.enviarMensaje(myAgent, "mensajeria", mensaje);
		}
	}

}
