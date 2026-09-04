
package es.uned.common;

import java.io.Serializable;

public class Usuario implements Serializable {

	private static final long serialVersionUID = 1L;
	private String nombre;
	private String pass;
	
	
	public Usuario(String nombre, String pass) {
		super();
		this.nombre = nombre;
		this.pass = pass;
	}


	public String getNombre() {
		return nombre;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public String getPass() {
		return pass;
	}
	
	public void setPass(String pass) {
		this.pass = pass;
	}
	
	public boolean comprobarPass(String pass)
	{
		if (this.pass.equals(pass))
		{
			return true;
		} else {
			return false;
		}
		
	}
	@Override
	public String toString() {
		return "Nombre=:" + nombre;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
