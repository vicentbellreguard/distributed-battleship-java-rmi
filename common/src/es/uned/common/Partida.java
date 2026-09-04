
package es.uned.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Partida implements Serializable{

	private static final long serialVersionUID = 1L;

	public Partida(int id, String nombreJug1) {
		this.id = id;
		this.nombreJug1 = nombreJug1;
	}
	
	public Partida(String nombreJug2) {
		this.nombreJug2 = nombreJug2;
	}
	
	private int id;
	
	private String nombreJug1;
	private String nombreJug2;
	
	private int puntosJug1;
	private int puntosJug2;
	
	private List<String> casillasDisparadasJ1 = new ArrayList<String>();
	private List<String> casillasDisparadasJ2 = new ArrayList<String>();
	
	
	public int getId() {
		return id;
	}	
	public void setId(int id) {
		this.id = id;
	}
	
	
	
	public String getNombreJug1() {
		return nombreJug1;
	}	
	public void setNombreJug1(String nombreJug1) {
		this.nombreJug1 = nombreJug1;
	}
	
		
	public String getNombreJug2() {
		return nombreJug2;
	}	
	public void setNombreJug2(String nombreJug2) {
		this.nombreJug2 = nombreJug2;
	}
	
	
	
		
	public int getPuntosJug1() {
		return puntosJug1;
	}	
	public void setPuntosJug1(int puntosJug1) {
		this.puntosJug1 = this.puntosJug1 + puntosJug1;
	}
		
	public int getPuntosJug2() {
		return puntosJug2;
	}	
	public void setPuntosJug2(int puntosJug2) {
		this.puntosJug2 = this.puntosJug2 + puntosJug2;
	}
	

	
	
	public List<String> getCasillasDisparadasJ1() {
		return casillasDisparadasJ1;
	}	
	public void setCasillasDisparadasJ1(List<String> casillasDisparadasJ1) {
		this.casillasDisparadasJ1 = casillasDisparadasJ1;
	}
	
	public List<String> getCasillasDisparadasJ2() {
		return casillasDisparadasJ2;
	}
	public void setCasillasDisparadasJ2(List<String> casillasDisparadasJ2) {
		this.casillasDisparadasJ2 = casillasDisparadasJ2;
	}
	
}
