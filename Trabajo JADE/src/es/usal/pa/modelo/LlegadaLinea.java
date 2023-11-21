package es.usal.pa.modelo;

import java.io.Serializable;
import java.util.List;

public class LlegadaLinea implements Serializable {
    public float hora;
    private int linea;
    private int indice;
    private List<Float> horariosParada;
    public int parada;
    private List<Llegada> llegadas;

    public LlegadaLinea(Integer parada, Float hora) {
        this.parada = parada;
        this.hora = hora;
    }

    public float getHora() {
        return hora;
    }

    public void setHora(float hora) {
        this.hora = hora;
    }

    public int getLinea() {
        return linea;
    }

    public int getParada() {
        return parada;
    }

    

    public LlegadaLinea(List<Llegada> llegadas) {
        this.llegadas = llegadas;
    }

    public LlegadaLinea(Integer parada, float hora, int linea) {
    	this.parada = parada;
        this.hora = hora;
        this.linea = linea;
	}
    public int getIndice() {
        return indice;
    }

	public List<Llegada> getLlegadas() {
        return llegadas;
    }

	public List<Float> getHorariosParada() {
		// TODO Auto-generated method stub
		return horariosParada;
	}


}
