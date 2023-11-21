package es.usal.pa.modelo;

public class Llegada {
    private int parada;
    private float tiempo;
    private int linea;
    private int indice;

    public Llegada(int parada, float tiempo, int linea, int indice) {
        this.parada = parada;
        this.tiempo = tiempo;
        this.linea = linea;
        this.indice = indice;
    }

    public Llegada(int paradaDestino, float tiempo2) {
    	this.parada = paradaDestino;
        this.tiempo = tiempo2;
	}

	public int getParada() {
        return parada;
    }


    public float getTiempo() {
        return tiempo;
    }

    public int getLinea() {
        return linea;
    }

    public int getIndice() {
        return indice;
    }

	public void setLinea(int linea) {
		this.linea=linea;
	}

}

