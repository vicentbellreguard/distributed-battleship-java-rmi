
package es.uned.cliente;

import java.util.Scanner;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Map;

import es.uned.common.CallbackJugadorInterface;
import es.uned.common.Eventos;
import es.uned.common.Gui;
import es.uned.common.Partida;
import es.uned.common.ServicioAutenticacionInterface;
import es.uned.common.ServicioGestorInterface;
import es.uned.common.Usuario;
import es.uned.common.Utils;

public class Jugador {

	private static ServicioAutenticacionInterface serAutenticacion;
	private static ServicioGestorInterface serGestor;
	
	private static Registry registry  = null;
	private static String nombre = "";
	//private static String URL_CALL = "";

	private static Scanner reader = new Scanner(System.in);
	private static int idPartida;
	// Para el callback
	public static Eventos ListaEventos;
	
	private static void startRegistro() throws RemoteException
	{
		try {
			registry = LocateRegistry.getRegistry(Utils.RegistryPort);
			registry.list();
		}catch(RemoteException ex) {
			registry = LocateRegistry.createRegistry(Utils.RegistryPort);
		}
	}

	
	public static void main(String[] args) throws Exception 
	{
		startRegistro();
		// Nos conectamos al servicio autenticacion
		String URL_AUTENTICACION = "rmi://"+Utils.ServerIP+":"+Utils.RegistryPort+"/ServicioAutenticacion";
		serAutenticacion = (ServicioAutenticacionInterface) Naming.lookup(URL_AUTENTICACION);	
		
		// Nos conectamos al servicio de datos
		String URL_GESTOR = "rmi://"+Utils.ServerIP+":"+Utils.RegistryPort+"/ServicioGestor";
		serGestor = (ServicioGestorInterface) Naming.lookup(URL_GESTOR);
		
		gui();	
	}
	
	
	private static void gui() throws RemoteException, InterruptedException, MalformedURLException, NotBoundException 
	{
		int opt = 0;
		boolean logeado = false;
		
		 do {
			opt = Gui.menu("Menu Principal", 
				  new String[]{ "Registrar un nuevo jugador", 
								 "Hacer login", 
								  "Salir" });
			switch (opt)
			{
		     	case 0: 
		     		// Pedimos nombre de usuario y contraseña
		     		nombre = Gui.input("Nombre usuario: ");
		     		String pass  = Gui.input("Contraseña: ");
		     		Usuario nuevoUser = new Usuario (nombre, pass);
		     		boolean resul = serAutenticacion.registrarJugador(nuevoUser); 
		     		
		     		if (resul)
		     		{
		     			System.out.println("\n" + "Se ha registrado correctamente: " + "\n");
		     		} else {
		     			System.out.println("\n" + "NO Se ha registrado correctamente: " + "\n");
		     		}		     				     	
		     		break;
		     
			 case 1: 
					nombre = Gui.input("Nombre usuario: ");
					String password  = Gui.input("Contraseña: ");
					logeado = serAutenticacion.loginJugador(nombre, password);
					if (logeado)
		     		{
		     			System.out.println("\n" + "Se ha identificado correctamente" + "\n");
		     		} else {
		     			System.out.println("\n" + "NO se ha identificado correctamente " + "\n");
		     		}
			    	break;
			    	
			 case 2:
				 	System.out.println("Salimos del sistema");
				 	break;
			}
			
		 } while(opt!=2 && logeado==false);
		 
		 int opt1 = 0;
		 
		 if(logeado)
		 {
			// Levantamos servicio de Callback
			//URL_CALL = "rmi://"+Utils.ServerIP+":"+Utils.RegistryPort+"/SerCall/"+nombre;
			// Para cambiar el repositorio de clases si activas la carga dinámica de clases con el gestor de seguridad
			//Utils.setCodeBase(CallbackJugadorInterface.class);
			//creamos lista para los eventos
		    ListaEventos = new Eventos();  
		    CallbackJugadorInterface callbackObj = new CallbackJugadorImpl(ListaEventos);
		    //Naming.rebind(URL_CALL, callbackObj);
		    
			do {
				 // Menú del usuario logeado
				 opt1 = Gui.menu("Menu Jugador", 
						   new String[]{ "Información del jugador(consultar puntuación histórica)", 
						        		 "Iniciar una partida", 
						        		 "Listar partidas iniciadas a la espera de contrincante", 
						        		 "Unirse a una partida ya creada",
						        		 "Salir Logout"});
				 switch (opt1) {
				 	 case 0:
				 		System.out.println(serGestor.consultarPuntuacion(nombre));
						 break;
						 
					 case 1:	
						 // En el momento en que el jugador crea una partida se bloquea y se queda esperando a que alguien se una
						 idPartida = serGestor.iniciarPartida(nombre, callbackObj);
						 System.out.println("id partida: " + idPartida);
						 						 
						     // Casilla y posicion donde colocar los barcos
						     String casilla = "";
							 String posicion = "";	
							 // Casilla para disparar
							 String casillaDisparo = "";
							 // Indica si el jugador aún está jugando la partida
							 boolean jugando = true;

							  // Mientras esté jugando la partida
							  while(jugando) {								  
								String texto = ListaEventos.getEvento();   
							    
								switch(texto) {
												
								    // Según el texto que reciba, hace una cosa u otra 
									case "Partida iniciada, permanezca a la espera...":
										System.out.printf(texto + "\n");
										break;																																		
									case "Otro jugador se ha unido a la partida":
										System.out.printf("\n" + texto + "\n\n");
										break;
									case "COMIENZA EL JUEGO":
										System.out.printf(texto + "\n\n");
										break;
									case "INTRODUZCA COORDENABAS BARCO 1":
										System.out.printf(texto + "\n\n");
										break;
									case "INTRODUZCA COORDENABAS BARCO 2":
										System.out.printf("\n\n" + texto + "\n\n");
										break;			
									case "Modifique la posición del barco incorrecto":
										System.out.printf("\n\n\n" + texto + "\n\n");
										break;	
									case "Introduzca casilla donde colocar la parte delantera del barco (FILA: A-J , COLUMNA: 1-10):":
										System.out.printf(texto + "\n");
										casilla = reader.next();
										break;
									case "Introduzca orientación del barco (V o H):":
										System.out.printf(texto + "\n");
									    posicion = reader.next();
									    
									    switch(serGestor.colocarCasillas(casilla,posicion,nombre)) {
									    	// Si la casilla introducida es incorrecta se tendrá que introducir una casilla nueva
								    		case "CASILLA INCORRECTA! Introduce de nuevo las coordenadas:":
								    			 System.out.println("casilla " + casilla + " incorrecta! El barco se sale del tablero");
								    			 serGestor.repetirCasilla(callbackObj);
								    			 break;
									    }
									    
										// Si los dos barcos introducidos son correctos, el J1 empezará a disparar
										if (serGestor.dameBarcos(nombre) == 6 && serGestor.dameBarcos(serGestor.dameJugador2(nombre)) == 6) {//
										    serGestor.realizarDisparo(callbackObj);	
										}								    
										break;
									case "Casilla repetida! Vuelve a introducir casilla":
										System.out.printf(texto + "\n");
										casillaDisparo = reader.next();
										serGestor.realizarDisparoJugador1(casillaDisparo,nombre,callbackObj,idPartida);
										break;														
									case "Introduzca coordenadas de tiro [YX]":
										System.out.printf("\n" + texto + "\n");
										casillaDisparo = reader.next();
										serGestor.realizarDisparoJugador1(casillaDisparo,nombre,callbackObj,idPartida);
										break;
									case "Disparo realizado por ti (TOCADO)":
										System.out.printf("\n" + texto + "\n");
										break;
									case "Disparo realizado por ti (AGUA)":
										System.out.printf("\n" + texto + "\n");
										break;
									case "Disparo realizado por el jugador contrario (TOCADO)":
										System.out.printf("\n" + texto + "\n");
										break;
									case "Disparo realizado por el jugador contrario (AGUA)":
										System.out.printf("\n" + texto + "\n");
										break;
									case "Has hundido la flota de tu contrincante (has ganado)":
										System.out.printf("\n" + texto + "\n\n");
										break;
									case "Te han hundido tu flota (has perdido)":
										System.out.printf("\n" + texto + "\n\n");
										break;
									case "SALIR":	
										serGestor.vaciarTablero(nombre);
										jugando = false;
										break;
								}
							 }							 
						     break; 
						 
					 case 2:						 						 
						 Map<Integer, Partida> partidas = serGestor.listarPartidas();
						 
						for(Partida p: partidas.values())
						{
							// Si la partida ya tiene contrincante no se muestra por pantalla
							if(!(serGestor.getPartidasJugando().contains(p.getId()))) {
								System.out.println("ID: "+p.getId()+" - Oponente: "+p.getNombreJug1() + "\n");
							}
						}
						break;
						 
					 case 3:						 
						 System.out.println("Introducir id partida: ");
						 int partida = reader.nextInt();	
						 System.out.println("\n");
						 serGestor.registrarPartida(partida);
						 // Le pasamos el callback del jugador que se ha unido a la partida
						 serGestor.empezarPartida(partida, callbackObj, nombre);
						 
						// Casilla y posicion donde colocar los barcos
						 String pos = "";
						 String cas = "";
						 // Casilla para disparar
						 String casillaDisparoJugador2 = "";						 
						 boolean jugadando = true;
						 						
						 // Mientras esté jugando la partida
						 while (jugadando) {	
						    String texto1=ListaEventos.getEvento();	
						 
						        // Según el texto que reciba, hace una cosa u otra 						    
								if (texto1.equals("Introduzca casilla donde colocar la parte delantera del barco (FILA: A-J , COLUMNA: 1-10):")) {
									System.out.printf(texto1 + "\n");
								    cas = reader.next();	
								}
								if (texto1.equals("Modifique la posición del barco incorrecto")) 
									System.out.printf("\n" + texto1 + "\n\n");
								
								if (texto1.equals("INTRODUZCA COORDENABAS BARCO 1")) 
									System.out.printf(texto1 + "\n\n");
								
								if (texto1.equals("INTRODUZCA COORDENABAS BARCO 2")) 
									System.out.printf("\n\n" + texto1 + "\n\n");
								
								if (texto1.equals("Introduzca orientación del barco (V o H):")) {
									System.out.printf(texto1 + "\n");
								    pos = reader.next();
								    
								    switch(serGestor.colocarCasillas(cas,pos,nombre)) {
								    	case "CASILLA INCORRECTA! Introduce de nuevo las coordenadas:":
								    		System.out.println("casilla " + cas + " incorrecta! El barco se sale del tablero");
								    		serGestor.repetirCasilla(callbackObj);
								    		break;
								    }	
								    
									// Si el J1 termina de colocar los barcos antes que el J2, se esperará a disparar cuando haya terminado de colocar los barcos el J2
									if((serGestor.dameBarcos(nombre) == 6) && (serGestor.dameBarcos(serGestor.dameContrincante(partida)) == 6)) {
									   CallbackJugadorInterface jugador2 = serGestor.dameCallback(serGestor.dameContrincante(partida));
									   serGestor.realizarDisparo(jugador2);	
									}
								}
								
								if (texto1.equals("COMIENZA EL JUEGO")) 
									System.out.printf(texto1 + "\n\n");
								
								if (texto1.equals("Introduzca coordenadas de tiro [YX]")) {
									System.out.printf("\n" + texto1 + "\n");
									casillaDisparoJugador2 = reader.next();
									serGestor.realizarDisparoJugador2(casillaDisparoJugador2,nombre,callbackObj,partida);
								}
								if(texto1.equals("Casilla repetida! Vuelve a introducir casilla")) {
									System.out.printf(texto1 + "\n");
									casillaDisparoJugador2 = reader.next();
									serGestor.realizarDisparoJugador2(casillaDisparoJugador2,nombre,callbackObj,partida);
								}
								if (texto1.equals("Disparo realizado por ti (TOCADO)")) 
									System.out.printf("\n" + texto1 + "\n");
								
								if (texto1.equals("Disparo realizado por ti (AGUA)")) 
									System.out.printf("\n" + texto1 + "\n");
								
								if (texto1.equals("Disparo realizado por el jugador contrario (TOCADO)")) 
									System.out.printf("\n" + texto1 + "\n");
								
								if (texto1.equals("Disparo realizado por el jugador contrario (AGUA)")) 
									System.out.printf("\n" + texto1 + "\n");
								
								if (texto1.equals("Has hundido la flota de tu contrincante (has ganado)")) 
									System.out.printf("\n" + texto1 + "\n");
								
								if (texto1.equals("Te han hundido tu flota (has perdido)")) 
									System.out.printf("\n" + texto1 + "\n");
								
								if (texto1.equals("SALIR")) {
									serGestor.vaciarTablero(nombre);
									jugando = false;
									break;	
								}
							}	
						    System.out.println("\n");
						    break; 
						 
					 case 4:						  
						 serAutenticacion.salirJugador(nombre);
						 break; 
				 }
			}    while(opt1 != 4);
		 }
   }
}