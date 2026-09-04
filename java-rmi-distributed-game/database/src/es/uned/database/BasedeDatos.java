
package es.uned.basededatos;

import java.io.IOException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Iterator;

import es.uned.common.Gui;
import es.uned.common.ServicioDatosInterface;
import es.uned.common.Utils;

public class BasedeDatos {
	
	private static Registry registry  = null;
	private static String URL_BBDD = "rmi://"+Utils.ServerIP+":"+Utils.RegistryPort+"/ServicioDatos";
	private static ServicioDatosImpl SerDatos = null;
	
	private static void startRegistro() throws RemoteException
	{
		try {
			Registry registry = LocateRegistry.getRegistry(Utils.RegistryPort);
			registry.list();
		}catch(RemoteException ex) {
			registry = LocateRegistry.createRegistry(Utils.RegistryPort);
		}
	}

	public static void main(String[] args) throws IOException, NotBoundException  
	{
		Utils.setCodeBase(ServicioDatosInterface.class);
		
		SerDatos = new ServicioDatosImpl();
		startRegistro();

		Naming.rebind(URL_BBDD, SerDatos);	
		System.out.println("Servicio de Datos listo");	
		gui();
		System.in.read();
	}
	
	
	private static void gui() throws RemoteException 
	{
		int opt = 0;		
		do {
			opt = Gui.menu("Menu Principal", 
			 new String[]{ "Información de la Base de Datos", 
						   "Listar jugadores registrados (sus puntuaciones)", 
						   "Salir" });
			
			switch (opt) {
				case 0: 
					// URL del servicio
					System.out.println(URL_BBDD + "\n");
					break;
				case 1: 								
					// Declaramos el Iterador e imprimimos los Elementos del ArrayList
					Iterator<String> it = SerDatos.getJugadoresRegistrados().iterator();
					while(it.hasNext()){
						String elemento = it.next();
						// Si el jugador está registrado pero aún no ha conseguido ningún punto
						if(!(SerDatos.listarJugadores().containsKey(elemento))) {
							System.out.print("El jugador " + elemento + " tiene 0 puntos\n\n");
						} 
						// Si el jugador ya ha conseguido algún punto
						else {
						    System.out.print("El jugador " + elemento + " tiene " + SerDatos.listarJugadores().get(elemento) + " puntos\n\n");
					    }
					} 
					break;
				}
		}
		while (opt != 2);
	}
	
}