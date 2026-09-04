
package es.uned.common;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServicioAutenticacionInterface extends Remote  {

	// Registrar nuevo jugador
	public boolean registrarJugador(Usuario nuevoUser) throws RemoteException, MalformedURLException, NotBoundException;
	// Login Jugador
	public boolean loginJugador(String nombreUsuario, String pass) throws RemoteException, MalformedURLException, NotBoundException;
	public void salirJugador(String nombreUsario) throws RemoteException, MalformedURLException, NotBoundException;
}
