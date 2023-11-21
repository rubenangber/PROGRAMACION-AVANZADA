package es.usal.pa.modelo;

import java.io.Serializable;

public class Parada implements Serializable {
    private int numero;
    private float hora;
    private int numeroLinea;

    public Parada(int numero) {
        this.numero = numero;
    }

    public Parada(Integer parada, float tiempoTotal) {
		this.numero=parada;
		this.hora=tiempoTotal;
	}

	public Parada(Integer paradaActual, float nuevasalida, int linea) {
		this.numero=paradaActual;
		
		this.hora=nuevasalida;
		this.numeroLinea=linea;
		
	}

	public int getNumero() {
        return numero;
    }
	public float getHora() {
        return hora;
    }
	public int getLinea() {
        return numeroLinea;
    }

    // Puedes añadir más métodos según tus necesidades

    @Override
    public String toString() {
        return "Parada " + numero;
    }

	public void setHora(float hora) {
		this.hora=hora;
		
	}

	public void setHoraSalida(float f) {
		this.hora=f;
		
	}

	public void setHoraLlegada(float horaLlegada) {
		this.hora=horaLlegada;
		
	}
}
