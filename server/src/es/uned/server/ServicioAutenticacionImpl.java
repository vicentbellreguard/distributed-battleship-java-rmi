
package es.uned.servidor;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
//import java.util.ArrayList;

import es.uned.common.ServicioAutenticacionInterface;
import es.uned.common.ServicioDatosInterface;
import es.uned.common.Usuario;
import es.uned.common.Utils;

public class ServicioAutenticacionImpl extends UnicastRemoteObject implements ServicioAutenticacionInterface{
	
	private static final long serialVersionUID = 1L;
	
	ServicioDatosInterface servicioDatos = null;
	
	protected ServicioAutenticacionImpl() throws RemoteException {
		super();
	}
			
	public boolean registrarJugador(Usuario nuevoUser) throws RemoteException, MalformedURLException, NotBoundException {		
		try {
			servicioDatos = getRemoteReferenceDatos();
		} catch (Exception e) { 
			return false; 
		}
		return servicioDatos.registraJugador(nuevoUser);	
	}
	
	private ServicioDatosInterface getRemoteReferenceDatos() throws RemoteException, MalformedURLException, NotBoundException {
		ServicioDatosInterface servicioDatos;
		String URL_BBDD = "rmi://"+Utils.ServerIP+":"+Utils.RegistryPort+"/ServicioDatos";
		servicioDatos = (ServicioDatosInterface) Naming.lookup(URL_BBDD);
		return servicioDatos;
	}

	@Override
	public boolean loginJugador(String nombreUsuario, String pass) throws RemoteException, MalformedURLException, NotBoundException {
		servicioDatos = getRemoteReferenceDatos();
		return servicioDatos.loginJugador(nombreUsuario, pass);
	}

	@Override
	public void salirJugador(String nombreUsario) throws RemoteException, MalformedURLException, NotBoundException {
		servicioDatos = getRemoteReferenceDatos();
		servicioDatos.salirJugador(nombreUsario);		
	}

}
