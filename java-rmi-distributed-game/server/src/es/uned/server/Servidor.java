
package es.uned.servidor;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Map.Entry;

import es.uned.common.Gui;
import es.uned.common.Partida;
import es.uned.common.ServicioAutenticacionInterface;
import es.uned.common.ServicioDatosInterface;
import es.uned.common.ServicioGestorInterface;
import es.uned.common.Utils;

public class Servidor {
	
	private static Registry registry  = null;
	private static String URL_BBDD = "rmi://"+Utils.ServerIP+":"+Utils.RegistryPort+"/ServicioDatos";
	private static String URL_AUTENTICACION = "rmi://"+Utils.ServerIP+":"+Utils.RegistryPort+"/ServicioAutenticacion";
	private static String URL_GESTOR = "rmi://"+Utils.ServerIP+":"+Utils.RegistryPort+"/ServicioGestor";

	private static ServicioDatosInterface servDatos;	
	private static ServicioAutenticacionImpl servAuten;
	private static ServicioGestorImpl servGestor;
	
	private static void startRegistro(int puertoRegistro) throws RemoteException{
		try {
			registry = LocateRegistry.getRegistry(puertoRegistro);
			registry.list();
		} catch(RemoteException ex) {
			registry = LocateRegistry.createRegistry(puertoRegistro);
		  }
	}
	
	public static void main(String[] args) throws Exception {
		
		startRegistro(Utils.RegistryPort);
		
		// Accedemos al servicio de datos de la BBDD
		servDatos = (ServicioDatosInterface) Naming.lookup(URL_BBDD);
		
		// Levantamos servicios 
		Utils.setCodeBase(ServicioAutenticacionInterface.class);
		servAuten  = new ServicioAutenticacionImpl();
		Naming.rebind(URL_AUTENTICACION, servAuten );
		System.out.println("Servicio de Autenticacion listo");	
		
		Utils.setCodeBase(ServicioGestorInterface.class);
		servGestor = new ServicioGestorImpl();
		Naming.rebind(URL_GESTOR, servGestor);
		System.out.println("Servicio de Gestión listo");		
		gui();		
		System.in.read();
	}
		
	private static void gui() throws RemoteException, MalformedURLException, NotBoundException {
		int opt = 0;
		
		do {
			opt = Gui.menu("Menu Principal", 
			 new String[]{ "Información del servidor", 
						   "Estado de las partidas que se están jugando en este momento", 
							"Salir" });
			
			switch (opt) {
			case 0:
				// URL de los servicios
				System.out.println(URL_AUTENTICACION);
				System.out.println(URL_GESTOR + "\n");
				break;
			case 1:				
				for (Entry<Integer, Partida> partida : servDatos.listarPartidas().entrySet()){
					Integer clave = partida.getKey();
					Partida valor = partida.getValue();
					
					// Se imprimen solo las partidas que estan actualmente en juego
					if(servGestor.getPartidasJugando().contains(clave) && ((valor.getPuntosJug1() < 16) && (valor.getPuntosJug2() < 16)))
					{					
					    // Se le asignan los puntos al jugador 1
						if(servDatos.listarJugadores().get(valor.getNombreJug1()) != null) {
						}

						// Se le asignan los puntos al jugador 2
						if(servDatos.listarJugadores().get(valor.getNombreJug2()) != null) {
						}
						
						// Si los jugadores aún no están registrados en la lista de jugadores se les asignará 0 puntos
						if(servDatos.listarJugadores().get(valor.getNombreJug1()) == null) {
						}

						if(servDatos.listarJugadores().get(valor.getNombreJug2()) == null) {
						}
						
						System.out.println("ID Partida " + clave + ":\n"); 												
						System.out.println("JUGADOR 1: " + valor.getNombreJug1() + " tiene " + valor.getPuntosJug1() + " puntos\n");
						System.out.println("JUGADOR 2: " + valor.getNombreJug2() + " tiene " + valor.getPuntosJug2() + " puntos\n");
				  }
				} 											
				break;			
			}
		}
		while (opt != 2);
	}
}