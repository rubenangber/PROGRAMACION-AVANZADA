package es.usal.pa.modelo;

import java.io.Serializable;

public class User implements Serializable
{
	protected Integer origen;
	protected Integer destino;
	protected Float hora;
	
	public User()
	{
		
	}
	public User(Integer origen, Integer destino, Float hora)
	{
		this.origen=origen;
		this.destino=destino;
		this.hora=hora;
	}
	
	public Integer getOrigen(){return origen;}
	public void setOrigen(Integer origen){this.origen = origen;}
	public Integer getDestino(){return destino;}
	public void setDestino(Integer destino){this.destino = destino;}
	public Float getHora(){return hora;}
	public void setHora(Float hora){this.hora = hora;}
	
}
