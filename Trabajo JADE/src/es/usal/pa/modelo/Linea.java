package es.usal.pa.modelo;

import java.io.Serializable;
import java.util.Hashtable;
import java.util.Vector;

public class Linea implements Serializable
{	
	
	private int linea;
    private Float[] inicio;
    private Integer[] parada;
    private Float[] tiempo;

    public Linea(int linea, Float[] inicio, Integer[] parada, Float[] tiempo) {
        this.linea = linea;
        this.inicio = inicio;
        this.parada = parada;
        this.tiempo = tiempo;
    }

    public int getLinea() {
        return linea;
    }

    public Float[] getInicio() {
        return inicio;
    }

    public Integer[] getParada() {
        return parada;
    }

    public Float[] getTiempo() {
        return tiempo;
    }
	
}
