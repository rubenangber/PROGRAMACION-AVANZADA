package es.usal.pa.agent;

import java.io.IOException;
import java.io.Serializable;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Vector;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.Comparator;


import es.usal.pa.modelo.Linea;
import es.usal.pa.modelo.Llegada;
import es.usal.pa.modelo.LlegadaLinea;
import es.usal.pa.modelo.Parada;


import es.usal.pa.modelo.User;
import jade.content.lang.sl.SLCodec;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.Envelope;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;


public class CyclicBehaviourRuta extends CyclicBehaviour
{
	private AgentRuta agent;
	
	 public CyclicBehaviourRuta(AgentRuta agent) {
	        super(agent);
	        this.agent = agent;
	 }
	
	public void action() {
		// 1. Recibir mensaje de usuario
        ACLMessage msgUsuario=this.myAgent.blockingReceive(MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.REQUEST), MessageTemplate.MatchOntology("ontologia")));
        
        if (msgUsuario != null) {
            // Procesar solicitud de recorrido
        	 
            if (msgUsuario != null && msgUsuario.getProtocol() != null && msgUsuario.getProtocol().equals("fipa-request")) {
            	 
            	User u;
				try {
					u = (User) msgUsuario.getContentObject();
					int origen = u.getOrigen();
		            int destino = u.getDestino();
		            float hora = u.getHora();
		            // 2. Enviar mensaje a Linea
		            DFAgentDescription [] dfd = Utils.buscarAgentes(myAgent, "linea");
		            if (dfd != null) {
		            	ACLMessage msgLineaE = new ACLMessage(ACLMessage.REQUEST);
		            	for(int i=0;i<dfd.length;i++)
		            		msgLineaE.addReceiver(dfd[i].getName());
		            	 msgLineaE.setProtocol("fipa-request"); 
		                 msgLineaE.setOntology("ontologia");
		                 msgLineaE.setContent("SolicitarHorarios");
		                 myAgent.send(msgLineaE);
		                 
		            }else {block();}
                   
		            Float[] inicioA = null;
                    Integer[] paradaA = null;
                    Float[] tiempoA = null;

                    Float[] inicioB = null;
                    Integer[] paradaB = null;
                    Float[] tiempoB = null;
                    
		            // Recibir mensaje de Agente LineaA
		            ACLMessage msgLineaAR = this.myAgent.blockingReceive(MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.INFORM), MessageTemplate.MatchOntology("ontologia")));
		            
                    // Recibir mensaje de Agente LineaB
                    ACLMessage msgLineaBR = this.myAgent.blockingReceive(MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.INFORM), MessageTemplate.MatchOntology("ontologia")));
		            
                    if (msgLineaAR != null ||msgLineaAR != null) {
                    	Linea LA=(Linea)msgLineaAR.getContentObject();
                    	int linea_recibida_LA=LA.getLinea();
                    	Linea LB=(Linea)msgLineaBR.getContentObject();
                    	int linea_recibida_LB=LB.getLinea();
                    
                    	try {
                    		if(linea_recibida_LA==1 ) {
                    			Serializable contentA = msgLineaAR.getContentObject();
                    			inicioA = ((Linea) contentA).getInicio();
                    			paradaA = ((Linea) contentA).getParada();
                    			tiempoA = ((Linea) contentA).getTiempo();
                    			//System.out.print("\n\t\tlinea A:  " + Arrays.toString(inicioA) + Arrays.toString(paradaA) + Arrays.toString(tiempoA));
                            
                    		}else if(linea_recibida_LB==1) {
                    			Serializable contentA = msgLineaBR.getContentObject();
                    			inicioA = ((Linea) contentA).getInicio();
                            	paradaA = ((Linea) contentA).getParada();
                            	tiempoA = ((Linea) contentA).getTiempo();
                            	//System.out.print("\n\t\tlinea A:  " + Arrays.toString(inicioA) + Arrays.toString(paradaA) + Arrays.toString(tiempoA));
                    		}
                    		if(linea_recibida_LB==2) {
                    			Serializable contentB = msgLineaBR.getContentObject();
                    			inicioB = ((Linea) contentB).getInicio();
                    			paradaB = ((Linea) contentB).getParada();
                    			tiempoB = ((Linea) contentB).getTiempo();
                    			//System.out.print("\n\t\tlinea B:  " + Arrays.toString(inicioB) + Arrays.toString(paradaB) + Arrays.toString(tiempoB));

                    		}else if(linea_recibida_LA==2) {
                    			Serializable contentB = msgLineaAR.getContentObject();
                    			inicioB = ((Linea) contentB).getInicio();
                    			paradaB = ((Linea) contentB).getParada();
                    			tiempoB = ((Linea) contentB).getTiempo();
                    			//System.out.print("\n\t\tlinea B:  " + Arrays.toString(inicioB) + Arrays.toString(paradaB) + Arrays.toString(tiempoB));
                    		}

                    		List<LlegadaLinea> horarios = calcularHorariosParada(inicioA, paradaA, tiempoA, inicioB, paradaB, tiempoB);
                        
                    		// Calcular el recorrido mínimo
                    		List<Parada> resultado = calcularRecorridoMinimo(horarios, origen, destino, hora, paradaA,tiempoA, paradaB,tiempoB);
                    		
                    		// Verificamos si se encontró un recorrido
                    		if (resultado == null) {
                    			System.out.println("No se encontró un recorrido desde el origen hasta el destino.");
                    			block();
                    		} else {
                    			
                    		
                    		
                    		// 4. Enviar mensaje a AgenteUsuario
	                    	
	                       	ACLMessage msgUsuarioE = new ACLMessage(ACLMessage.INFORM);
	                       	msgUsuarioE.addReceiver(msgUsuario.getSender());
	                       	msgUsuarioE.setOntology("ontologia");
	                        
	                       	msgUsuarioE.setLanguage(new SLCodec().getName());
	                        
	                       	msgUsuarioE.setEnvelope(new Envelope());
	                       	msgUsuarioE.getEnvelope().setPayloadEncoding("ISO8859_1");
	                       	try {
	                        	msgUsuarioE.setContentObject((Serializable) resultado);
	                        } catch (IOException e) {
	                            e.printStackTrace();
	                        }
	                       	this.myAgent.send(msgUsuarioE);
                    		}
                        } catch (UnreadableException e) {
                    		e.printStackTrace();
                    	}
                    } else {
                    	System.out.println("No se encontraron las lineas del Bus nocturno");
                    	block();
                    }
				} catch (UnreadableException e) {
            		e.printStackTrace();
            	}
            } else {
            	System.out.println("El mensaje recibido no es el que se esperaba");
            	block();
            }
        } else {
        	System.out.println("No se recibio correctamente el mensaje del usuario");
        	block();
        }
        
    }
	
	//----------------FUNCIONES PARA CALCULAR EL RECORRIDO MINIMO--------------------
	
	static Map<Integer, Map<Integer, List<Llegada>>> grafo = new HashMap<>();
	static Map<Integer, Map<Integer, Integer>> grafo2 = new HashMap<>();
    public static List<Parada> calcularRecorridoMinimo(List<LlegadaLinea> horarios, int origen, int destino, float horaSalida, Integer[] paradaA, Float[] tiemposA, Integer[] paradaB, Float[] tiemposB) {
        grafo.clear();
        grafo2.clear();
        for (Integer parada : paradaA) {
            grafo.put(parada, new HashMap<>());
            grafo2.put(parada, new HashMap<>());
        }

        for (Integer parada : paradaB) {
            grafo.put(parada, new HashMap<>());
            grafo2.put(parada, new HashMap<>());
        }

        // Construir conexiones para la línea A
        for (int i = 0; i < paradaA.length - 1; i++) {
            int paradaActualA = paradaA[i];
            int paradaSiguienteA = paradaA[i + 1];
            float tiempoA = tiemposA[i];
            agregarConexion(paradaActualA, paradaSiguienteA,tiempoA, horarios,1);
            agregarConexion2(paradaActualA, paradaSiguienteA,tiempoA);
        }

        // Construir conexiones para la línea B
        for (int i = 0; i < paradaB.length - 1; i++) {
            int paradaActualB = paradaB[i];
            int paradaSiguienteB = paradaB[i + 1];
            float tiempoB = tiemposB[i];
            agregarConexion(paradaActualB, paradaSiguienteB, tiempoB, horarios,2);
            agregarConexion2(paradaActualB, paradaSiguienteB,tiempoB);
        }

        //mostrarRelacionParadas(grafo);
        //mostrarRelacionParadasTiempo(grafo2);
        
        boolean hayNuevasLlegadas = true;

        while (hayNuevasLlegadas) {
            hayNuevasLlegadas = false;

            for (LlegadaLinea llegada : horarios) {
                Integer paradaAnterior = null;
                float tiempoAcumulado = horaSalida;
                List<Llegada> llegadas = llegada.getLlegadas();

                if (llegadas != null) {
                    for (Llegada llegadaParada : llegadas) {
                        Integer parada = llegadaParada.getParada();
                        float tiempo = llegadaParada.getTiempo();

                        if (!grafo.containsKey(parada)) {
                            grafo.put(parada, new HashMap<>());
                        }

                        if (paradaAnterior != null && !grafo.get(paradaAnterior).containsKey(parada)) {
                            grafo.get(paradaAnterior).put(parada, new ArrayList<>());
                            hayNuevasLlegadas = true;
                        }

                        paradaAnterior = parada;
                        tiempoAcumulado += tiempo;
                    }

                    if (paradaAnterior != null) {
                        Llegada ultimaLlegada = llegadas.get(llegadas.size() - 1);
                        float tiempoTotal = ultimaLlegada.getTiempo() + tiempoAcumulado; // Cambio aquí
                        grafo.get(paradaAnterior).get(ultimaLlegada.getParada()).add(new Llegada(destino, tiempoTotal));
                        hayNuevasLlegadas = true;
                    }
                }
            }
        }
        
        // Utilizar el algoritmo de Dijkstra para encontrar el recorrido mínimo
        Map<Integer, Integer> distancias = new HashMap<>();
        Map<Integer, Integer> padres = new HashMap<>();
        PriorityQueue<Integer> colaPrioridad = new PriorityQueue<>(Comparator.comparingInt(distancias::get));

        for (Integer parada : grafo.keySet()) {
            if (parada.equals(origen)) {
                distancias.put(parada, 0);
                padres.put(parada, null);
                colaPrioridad.offer(parada);
            } else {
                distancias.put(parada, Integer.MAX_VALUE);
                padres.put(parada, null);
            }
            
        }

        List<Parada> resultado = calcularRecorridoMinimoRecursivo(colaPrioridad, distancias, padres, destino, origen, horaSalida, horarios);
        if (resultado != null) {
            return resultado;
        } else {
            System.out.println("No hay rutas disponibles desde el origen hasta el destino.");
            return null;
        }    }

    private static List<Parada> calcularRecorridoMinimoRecursivo(
    	    PriorityQueue<Integer> colaPrioridad,
    	    Map<Integer, Integer> distancias,
    	    Map<Integer, Integer> padres,
    	    Integer destino,
    	    Integer origen,
    	    float salida,
    	    List<LlegadaLinea> horarios
    	) {
    	    if (colaPrioridad.isEmpty()) {
    	        System.out.println("No hay rutas disponibles desde el origen hasta el destino.");
    	        return null;
    	    }
    	    int flag = 0;
    	    

    	    Integer actual = colaPrioridad.poll();

    	   

    	    for (Map.Entry<Integer, List<Llegada>> vecinoEntry : grafo.get(actual).entrySet()) {
    	        int nuevaDistancia = (int) (distancias.get(actual) + obtenerHoraLlegada(actual, horarios, salida));

    	        Integer distanciaVecino = distancias.get(vecinoEntry.getKey());

    	        if (distanciaVecino == null || nuevaDistancia < distanciaVecino) {
    	            distancias.put(vecinoEntry.getKey(), nuevaDistancia);

    	            // Solo actualiza el padre si la nueva distancia es menor
    	            if (distanciaVecino == null || nuevaDistancia < distanciaVecino) {
    	                padres.put(vecinoEntry.getKey(), actual);
    	            }
    	            for (int i = 0; i < horarios.size(); i++) {
    	                LlegadaLinea llegada = horarios.get(i);

    	                if (llegada.getParada() == origen) {
    	                    if (salida <= llegada.getHora() && flag == 0) {
    	                        salida = llegada.getHora();
    	                        flag = 1;
    	                    }
    	                }

    	            }
    	            colaPrioridad.offer(vecinoEntry.getKey());
    	        }
    	    }

    	    if (actual.equals(destino)) {
    	        List<Integer> caminoForzado1 = new ArrayList<>();
    	        Integer tempforzado = destino;
    	        while (tempforzado != null) {
    	        	caminoForzado1.add(tempforzado);
    	            tempforzado = padres.get(tempforzado);
    	        }

    	        Collections.reverse(caminoForzado1);
    	        
    	        if (caminoForzado1.contains(4) && caminoForzado1.contains(9)) {
    	            
    	            int indiceParada4 = caminoForzado1.indexOf(4);
    	            // Construir el camino forzado: desde el origen hasta la parada 4
    	            List<Integer> caminoForzado = new ArrayList<>(caminoForzado1.subList(0, indiceParada4 + 1));

    	            // Agregar el camino forzado: forzar el camino de 4 a 5 y continuar hasta el destino
    	            caminoForzado.add(5);
    	            caminoForzado.add(6);
    	            caminoForzado.add(7);
    	            caminoForzado.add(8);
    	            caminoForzado.addAll(caminoForzado1.subList(indiceParada4 +2, caminoForzado1.size()));

    	                
    	            // Imprimir camino forzado
    	            caminoForzado1 = caminoForzado;
    	                
    	        }

    	            float nuevasalidaFor = salida;
    	            List<Parada> recorridoFor = new ArrayList<>();
    	            Iterator<Integer> iteratorFor = caminoForzado1.iterator();
    	            Integer paradaActualFor = iteratorFor.next(); // Obtener la primera parada

    	            while (iteratorFor.hasNext()) {
    	                Integer paradaSiguiente = iteratorFor.next();

    	                float tiempoLlegada = obtenerTiempoLlegada(paradaActualFor, paradaSiguiente, horarios, nuevasalidaFor, grafo2);
    	                
    	                recorridoFor.add(new Parada(paradaActualFor, nuevasalidaFor, obtenerLinea(paradaActualFor, nuevasalidaFor, horarios,paradaSiguiente)));

    	                nuevasalidaFor += tiempoLlegada;
    	                paradaActualFor = paradaSiguiente; // Mover a la siguiente parada
    	            }
    	            //System.out.println("Parada " + paradaActualFor + ": Hora de LLEGADA = " + nuevasalidaFor);
    	            recorridoFor.add(new Parada(paradaActualFor, nuevasalidaFor,obtenerNumeroLinea(paradaActualFor, horarios) ));
    	            
    	        List<Integer> caminoMinimo = new ArrayList<>();
    	        Integer temp = destino;
    	        while (temp != null) {
    	            caminoMinimo.add(temp);
    	            temp = padres.get(temp);
    	        }

    	        Collections.reverse(caminoMinimo);
    	        float nuevasalida=salida;
    	        List<Parada> recorrido = new ArrayList<>();
    	        Iterator<Integer> iterator = caminoMinimo.iterator();
    	        Integer paradaActual = iterator.next(); // Obtener la primera parada
    	        
    	        
    	        while (iterator.hasNext()) {
    	            Integer paradaSiguiente = iterator.next();

    	            float tiempoLlegada = obtenerTiempoLlegada(paradaActual, paradaSiguiente, horarios, nuevasalida, grafo2);
	                recorrido.add(new Parada(paradaActual, nuevasalida, obtenerLinea(paradaActual, nuevasalida, horarios, paradaSiguiente)));

    	            nuevasalida += tiempoLlegada;
    	            paradaActual = paradaSiguiente; // Mover a la siguiente parada
    	        }
    	        //System.out.println("Parada " + paradaActual + ": Hora de LLEGADA = " + nuevasalida);
    	        recorrido.add(new Parada(paradaActual, nuevasalida, obtenerNumeroLinea(paradaActual, horarios)));

    	        // Calcular el tiempos finales
    	        float tiempoFinalCaminoMinimo = calcularTiempoFinal(caminoMinimo, horarios, salida, grafo2);
    	        float tiempoFinalCaminoForzado = calcularTiempoFinal(caminoForzado1, horarios, salida, grafo2);

    	        
    	        if (tiempoFinalCaminoMinimo < tiempoFinalCaminoForzado) {        	        
    	            return recorrido;
    	        } else {
    	            return recorridoFor;
    	        }
    	    } else {
    	        if (actual.equals(origen) && !actual.equals(origen)) {
    	            return null;
    	        }

    	        return calcularRecorridoMinimoRecursivo(colaPrioridad, distancias, padres, destino, origen, salida, horarios);
    	    }
    	}
    
    
    private static float calcularTiempoFinal(List<Integer> recorrido, List<LlegadaLinea> horarios, float salida, Map<Integer, Map<Integer, Integer>> grafo2) {
        float tiempoAcumulado = salida;
        Iterator<Integer> iterator = recorrido.iterator();
        Integer paradaActual = iterator.next();

        while (iterator.hasNext()) {
            Integer paradaSiguiente = iterator.next();
            float tiempoLlegada = obtenerTiempoLlegada(paradaActual, paradaSiguiente, horarios, tiempoAcumulado, grafo2);
            tiempoAcumulado += tiempoLlegada;
            paradaActual = paradaSiguiente;
        }

        return tiempoAcumulado;
    }
    private static float obtenerTiempoLlegada(
    	    int parada,
    	    int paradaSiguiente,
    	    List<LlegadaLinea> horarios,
    	    float salida,
    	    Map<Integer, Map<Integer, Integer>> grafo2
    	) {
    	    for (Map.Entry<Integer, Map<Integer, Integer>> entry2 : grafo2.entrySet()) {
    	        Integer paradaOrigen2 = entry2.getKey();
    	        Map<Integer, Integer> destinos2 = entry2.getValue();

    	        for (Map.Entry<Integer, Integer> destinoEntry2 : destinos2.entrySet()) {
    	            Integer paradaDestino2 = destinoEntry2.getKey();
    	            float tiempoDirecto = destinoEntry2.getValue();
    	            if (paradaOrigen2 == parada && paradaDestino2 == paradaSiguiente && tiempoDirecto > 0) {
    	              for (Map.Entry<Integer, Map<Integer, List<Llegada>>> entry : grafo.entrySet()) {
    	                Integer paradaOrigen = entry.getKey();
    	                Map<Integer, List<Llegada>> destinos = entry.getValue();
    	                
    	                  for (Map.Entry<Integer, List<Llegada>> destinoEntry : destinos.entrySet()) {
    	                    Integer paradaDestino = destinoEntry.getKey();
    	                    List<Llegada> llegadas = destinoEntry.getValue();
    	                    if (paradaOrigen == parada && paradaDestino == paradaSiguiente) {
    	                    	if (llegadas.isEmpty()) {
    	                    		System.out.println("No hay horarios disponibles.");
    	                    	} else {
    	                        
    	                    		for (Llegada llegada : llegadas) {
    	                            	if(llegada.getTiempo() >= salida && paradaDestino == paradaSiguiente ) {
    	    	                    		if(parada==4||parada==9) tiempoDirecto+=llegada.getTiempo()-salida;
    	        	                        return tiempoDirecto;
    	                            	}
    	                    		}
    	                    	}
    	                    }
    	                  }
    	              }
    	            }
    	        }
    	    }
    	    // Manejar el caso donde no hay horarios disponibles después de la salida o no hay conexión directa en grafo
    	    return Float.MAX_VALUE;    	    
    }

    
    private static float obtenerHoraLlegada(int numeroParada,List<LlegadaLinea> horarios, float salida) {
	    for (LlegadaLinea llegadaLinea : horarios) {
	        if (llegadaLinea.getParada() == numeroParada) {
	            return llegadaLinea.getHora()+salida;
	        }
	    }
	    return 0.0f; 
	}
    private static int obtenerLinea(int parada, float nuevasalida, List<LlegadaLinea> horarios, Integer paradaSiguiente) {
        for (Map.Entry<Integer, Map<Integer, List<Llegada>>> entry : grafo.entrySet()) {
            Integer paradaOrigen = entry.getKey();
            Map<Integer, List<Llegada>> destinos = entry.getValue();

            for (Map.Entry<Integer, List<Llegada>> destinoEntry : destinos.entrySet()) {
                Integer paradaDestino = destinoEntry.getKey();
                List<Llegada> llegadas = destinoEntry.getValue();

                if (paradaOrigen == parada && (paradaSiguiente == null || paradaDestino == paradaSiguiente) && llegadas != null && !llegadas.isEmpty()) {
                    // Buscar la línea basándose en el tiempo de llegada
                    for (Llegada llegada : llegadas) {
                        if (llegada.getTiempo() >= nuevasalida) {
                            // Se ha encontrado el horario después o en el momento de salida
                            
                                // Si no es la última parada, buscar la línea basándose en la parada siguiente
                                int linea = obtenerLineaPorTiempo(parada, paradaSiguiente, llegada.getTiempo(), horarios);
                                if (linea != -1) {
                                    return linea;
                                }
                            
                        }
                    }
                }
            }
        }
        return -1;  // Valor predeterminado si no se encuentra la línea
    }



    private static int obtenerLineaPorTiempo(int paradaOrigen, Integer paradaDestino, float tiempo, List<LlegadaLinea> horarios) {
        for (LlegadaLinea llegadaLinea : horarios) {
            if (llegadaLinea.getParada() == paradaOrigen && llegadaLinea.getHora() == tiempo) {
                return llegadaLinea.getLinea();
            }
        }
        return -1;  // Valor predeterminado si no se encuentra la línea
    }
    private static int obtenerNumeroLinea(int parada, List<LlegadaLinea> horarios) {
        for (LlegadaLinea llegadaLinea : horarios) {
            if (llegadaLinea.getParada() == parada) {
                return llegadaLinea.getLinea();
            }
        }
        // Devuelve 0 si no se encuentra la información de la línea
        return 0;
    }





//----------------------FUNCIONES PARA GRAFOS---------------------------------
  
    private static void mostrarRelacionParadasTiempo(Map<Integer, Map<Integer, Integer>> grafo) {
        for (Map.Entry<Integer, Map<Integer, Integer>> entry : grafo.entrySet()) {
            Integer paradaOrigen = entry.getKey();
            Map<Integer, Integer> destinos = entry.getValue();
            
            for (Map.Entry<Integer, Integer> destinoEntry : destinos.entrySet()) {
                Integer paradaDestino = destinoEntry.getKey();
                Integer tiempo = destinoEntry.getValue();
                
                System.out.println("Parada " + paradaOrigen + " -> Parada " + paradaDestino + ", Tiempo: " + tiempo + " minutos");
            }
        }
    }
    public static void agregarConexion2(int paradaOrigen, int paradaDestino, float tiempo) {
		
		if (!grafo2.containsKey(paradaOrigen)) {
            grafo2.put(paradaOrigen, new HashMap<>());
        }
        grafo2.get(paradaOrigen).put(paradaDestino, (int) tiempo);
    }
    
    private static void mostrarRelacionParadas(Map<Integer, Map<Integer, List<Llegada>>> grafo) {
        for (Map.Entry<Integer, Map<Integer, List<Llegada>>> entry : grafo.entrySet()) {
            Integer paradaOrigen = entry.getKey();
            Map<Integer, List<Llegada>> destinos = entry.getValue();

            for (Map.Entry<Integer, List<Llegada>> destinoEntry : destinos.entrySet()) {
                Integer paradaDestino = destinoEntry.getKey();
                List<Llegada> llegadas = destinoEntry.getValue();

                System.out.println("Parada " + paradaOrigen + " -> Parada " + paradaDestino + ", Horarios: " + obtenerHorarios(llegadas));
            }
        }
    }

    private static String obtenerHorarios(List<Llegada> llegadas) {
        StringBuilder horarios = new StringBuilder("[");
        for (Llegada llegada : llegadas) {
            horarios.append(llegada.getTiempo()).append(", ");
        }
        if (llegadas.size() > 0) {
            horarios.delete(horarios.length() - 2, horarios.length());
        }
        horarios.append("]");
        return horarios.toString();
    }

    private static void agregarConexion(int paradaOrigen, int paradaDestino, float tiempo, List<LlegadaLinea> horarios, int linea) {
        if (!grafo.containsKey(paradaOrigen)) {
            grafo.put(paradaOrigen, new HashMap<>());
        }

        if (!grafo.get(paradaOrigen).containsKey(paradaDestino)) {
            grafo.get(paradaOrigen).put(paradaDestino, new ArrayList<>());
        }

        // Obtener los horarios asociados a la conexión
        List<Llegada> horariosConexion = obtenerHorariosConexion(paradaOrigen, paradaDestino, horarios, linea);

        grafo.get(paradaOrigen).get(paradaDestino).addAll(horariosConexion);
    }

    private static List<Llegada> obtenerHorariosConexion(int paradaOrigen, int paradaDestino, List<LlegadaLinea> horarios, int linea) {
        List<Llegada> horariosConexion = new ArrayList<>();

        for (LlegadaLinea llegadaLinea : horarios) {
            int parada = llegadaLinea.getParada();
            float tiempo = llegadaLinea.getHora();

            if (parada == paradaOrigen && llegadaLinea.getLinea() == linea) {
                // Encuentra la llegada para la parada de origen y línea correspondiente
                Llegada llegada = new Llegada(paradaDestino, tiempo);
                horariosConexion.add(llegada);
            }
        }

        return horariosConexion;
    }
   

    
	


	

	

   
//-----------------------------------------------------------

	public static List<LlegadaLinea> calcularHorariosParada(Float[] inicioLineaA, Integer[] paradasA, Float[] tiemposA, Float[] inicioLineaB, Integer[] paradasB, Float[] tiemposB) {
	    List<LlegadaLinea> horarios = new ArrayList<>();

	    horarios.addAll(calcularHorariosUnaLinea(1,inicioLineaA, paradasA, tiemposA));
	    horarios.addAll(calcularHorariosUnaLinea(2,inicioLineaB, paradasB, tiemposB));

	    return horarios;
	}

	private static List<LlegadaLinea> calcularHorariosUnaLinea(int linea, Float[] inicioLinea, Integer[] paradas, Float[] tiempos) {
	    List<LlegadaLinea> horarios = new ArrayList<>();

	    for (int i = 0; i < inicioLinea.length; i++) {
	        float tiempoTemp = inicioLinea[i];

	        for (int j = 0; j < paradas.length; j++) {
	            if (j > 0) {
	                tiempoTemp += tiempos[j - 1];
	            }

	            Integer parada = paradas[j];

	            horarios.add(new LlegadaLinea(parada, tiempoTemp,linea));
	        }
	    }

	    return horarios;
	}



	}


