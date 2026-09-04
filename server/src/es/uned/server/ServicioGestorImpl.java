
package es.uned.servidor;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import es.uned.common.CallbackJugadorInterface;
import es.uned.common.Partida;
import es.uned.common.ServicioDatosInterface;
import es.uned.common.ServicioGestorInterface;
import es.uned.common.Utils;

public class ServicioGestorImpl extends UnicastRemoteObject implements ServicioGestorInterface {
	private ServicioDatosInterface servicioDatos = null;	
	private int idPartida = 0;
	
	protected ServicioGestorImpl() throws RemoteException {
		super();
	}

	private static final long serialVersionUID = 1L;

	private ServicioDatosInterface getRemoteReferenceDatos() throws RemoteException, MalformedURLException, NotBoundException 
	{
		ServicioDatosInterface servicioDatos;
		String URL_BBDD = "rmi://"+Utils.ServerIP+":"+Utils.RegistryPort+"/ServicioDatos";
		servicioDatos = (ServicioDatosInterface) Naming.lookup(URL_BBDD);
		return servicioDatos;
	}

	
	
	/***********
	 * PARTIDA
	 ***********/
	
	public int getIdPartida()
	{
		idPartida++;
		return  idPartida;
	}
	
	
	public int iniciarPartida(String jugador1, CallbackJugadorInterface callbackJug1) throws RemoteException, MalformedURLException, NotBoundException 
	{
		servicioDatos = getRemoteReferenceDatos();
		Partida nuevaPartida = new Partida(getIdPartida(), jugador1);
		servicioDatos.registrarPartida(nuevaPartida);
		servicioDatos.registrarCallback(callbackJug1, jugador1);
		callbackJug1.notificame("Partida iniciada, permanezca a la espera...");
		return nuevaPartida.getId();
	}	

	
	public void registrarPartida(int numPartida) throws RemoteException, MalformedURLException, NotBoundException 
	{
		servicioDatos.registrarPartida(numPartida);
	}
	
	
	// Devuelve el map con las partidas iniciadas
	public Map<Integer, Partida> listarPartidas() throws RemoteException, MalformedURLException, NotBoundException 
	{
		servicioDatos = getRemoteReferenceDatos();			
		return servicioDatos.listarPartidas();
	}	
	
	
	// Cuando el jugador 2 se une a la partida
	public void empezarPartida(int numPartida, CallbackJugadorInterface callbackJug2, String jugador2) throws RemoteException, MalformedURLException, NotBoundException 
	{
		servicioDatos.registrarCallback(callbackJug2, jugador2);
		// Crea un Map con los jugadores de cada partida
		servicioDatos.partida(servicioDatos.dameContrincante(numPartida), jugador2, numPartida);		
		// Jugador 1
		String jug1 = servicioDatos.dameContrincante(numPartida);
		// Colocacion de los barcos de los dos jugadores
		colocacionBarcos(servicioDatos.dameCallback(jugador2), servicioDatos.dameCallback(jug1));	
	}
	
	
	
	/**********
	 * BARCOS
	 **********/
	
	public void colocacionBarcos(CallbackJugadorInterface jugador2, CallbackJugadorInterface jugador1) throws RemoteException, MalformedURLException, NotBoundException 
	{				
		// JUGADOR QUE SE HA HUNIDO A LA PARTIDA
		introducirCoordenadasBarco1J2(jugador2);
		introducirCoordenadasBarco2(jugador2);
		
		// JUGADOR QUE HA INICIADO LA PARTIDA
		introducirCoordenadasBarco1J1(jugador1);
		introducirCoordenadasBarco2(jugador1);
	}

	
	// El jugador 2 introduce las coordenadas del primer barco
	public void introducirCoordenadasBarco1J2(CallbackJugadorInterface jugador) throws RemoteException, MalformedURLException, NotBoundException 
	{
		jugador.notificame("COMIENZA EL JUEGO");
		jugador.notificame("INTRODUZCA COORDENABAS BARCO 1");	
		jugador.notificame("Introduzca casilla donde colocar la parte delantera del barco (FILA: A-J , COLUMNA: 1-10):");
		jugador.notificame("Introduzca orientación del barco (V o H):");
	}
	
	
	// El jugador 1 introduce las coordenadas del primer barco
	public void introducirCoordenadasBarco1J1(CallbackJugadorInterface jugador) throws RemoteException, MalformedURLException, NotBoundException 
	{
		jugador.notificame("Otro jugador se ha unido a la partida");
		jugador.notificame("COMIENZA EL JUEGO");
		jugador.notificame("INTRODUZCA COORDENABAS BARCO 1");	
		jugador.notificame("Introduzca casilla donde colocar la parte delantera del barco (FILA: A-J , COLUMNA: 1-10):");
		jugador.notificame("Introduzca orientación del barco (V o H):");
	}
	
	
	// El jugador 1 y 2 introducen las coordenadas del segundo barco
	public void introducirCoordenadasBarco2(CallbackJugadorInterface jugador) throws RemoteException, MalformedURLException, NotBoundException 
	{
		jugador.notificame("INTRODUZCA COORDENABAS BARCO 2");
		jugador.notificame("Introduzca casilla donde colocar la parte delantera del barco (FILA: A-J , COLUMNA: 1-10):");
		jugador.notificame("Introduzca orientación del barco (V o H):");
	}
	
	
	// Si el jugador 1 o 2 introduce mal las coordenadas se volverá a pedir una casilla
	public void repetirCasilla(CallbackJugadorInterface jugador) throws RemoteException, MalformedURLException, NotBoundException 
	{
		jugador.notificame("Modifique la posición del barco incorrecto");
		jugador.notificame("Introduzca casilla donde colocar la parte delantera del barco (FILA: A-J , COLUMNA: 1-10):");
		jugador.notificame("Introduzca orientación del barco (V o H):");
	}
	
	
	public String colocarCasillas(String cas, String orientacion, String jugador) throws RemoteException, MalformedURLException, NotBoundException 
	{
		String efecto = "CASILLA VALIDA";
		// Filas validas horizontal
		List<String> filasValidasH = new ArrayList<>();
		// MINÚSCULAS
		filasValidasH.add("a");
		filasValidasH.add("b");
		filasValidasH.add("c");
		filasValidasH.add("d");
		filasValidasH.add("e");
		filasValidasH.add("f");
		filasValidasH.add("g");
		filasValidasH.add("h");
		filasValidasH.add("i");
		filasValidasH.add("j");
		
		// MAYÚSCULAS
		filasValidasH.add("A");
		filasValidasH.add("B");
		filasValidasH.add("C");
		filasValidasH.add("D");
		filasValidasH.add("E");
		filasValidasH.add("F");
		filasValidasH.add("G");
		filasValidasH.add("H");
		filasValidasH.add("I");
		filasValidasH.add("J");
		
		// Filas validas vertical
		List<String> filasValidasV = new ArrayList<>();
		// MINÚSCULAS
		filasValidasV.add("a");
		filasValidasV.add("b");
		filasValidasV.add("c");
		filasValidasV.add("d");
		filasValidasV.add("e");
		filasValidasV.add("f");
		filasValidasV.add("g");
		filasValidasV.add("h");
		
		// MAYÚSCULAS
		filasValidasV.add("A");
		filasValidasV.add("B");
		filasValidasV.add("C");
		filasValidasV.add("D");
		filasValidasV.add("E");
		filasValidasV.add("F");
		filasValidasV.add("G");
		filasValidasV.add("H");
		
		// Lista donde se guardaran las casillas de los barcos
		List<String> posBarco = new ArrayList<>();

		// Si las casillas pedidas por el jugador no se salen del tablero se colocará el barco
		boolean colocar = false;
		
		// Cogemos la FILA de la casilla pasada por parametro
		char f = cas.charAt(0);
		String fila = Character.toString(f);
		 
		// Cogemos la COLUMNA de la casilla pasada por parametro
		//Borramos el primer caracter de la casilla, que es la FILA, para quedarnos solo con la columna
        String columna = cas.substring(1);
		int numColumna = Integer.parseInt(columna);
		
		/*char casilla = cas.charAt(1);
		String columna = Character.toString(casilla);  
		int numColumna = Integer.parseInt(columna); */
		
		
		// ERROR CASILLA
		 if(orientacion.equalsIgnoreCase("v")) 
		 {		 			 
			 if(numColumna <= 10 && filasValidasV.contains(fila)) 
			 {
				 colocar = true; 
			 }
		 } else if(orientacion.equalsIgnoreCase("h")) 
		   {
			 if(numColumna <= 8 && filasValidasH.contains(fila)) 
			 {
				 colocar = true;
			 }	
		 } 
		 
		 
		// Si la casilla introduzida es correcta se procede a la colocación
		if(colocar) {
		
		// Si el barco se coloca en vertical se suma una fila a cada posición del barco
		if(orientacion.equalsIgnoreCase("v")) {

          switch(fila) {			
			case "a":
				String b1columna = "b"+columna;
				String c1columna = "c"+columna;
				
				posBarco.add(cas);
				posBarco.add(b1columna);
				posBarco.add(c1columna);
				break;
			case "A":
				String b1col = "B"+columna;
				String c1col = "C"+columna;
				
				posBarco.add(cas);
				posBarco.add(b1col);
				posBarco.add(c1col);
				break;
				
			case "b":
				String c2columna = "c"+columna;
				String d1columna = "d"+columna;
				
				posBarco.add(cas);
				posBarco.add(c2columna);
				posBarco.add(d1columna);
				break;
			case "B":
				String c2col = "C"+columna;
				String d1col = "D"+columna;
				
				posBarco.add(cas);
				posBarco.add(c2col);
				posBarco.add(d1col);
				break;
				
			case "c":
				String d2columna = "d"+columna;
				String ecolumna = "e"+columna;
				
				posBarco.add(cas);
				posBarco.add(d2columna);
				posBarco.add(ecolumna);
				break;
			case "C":
				String d2col = "D"+columna;
				String ecol = "E"+columna;
				
				posBarco.add(cas);
				posBarco.add(d2col);
				posBarco.add(ecol);
				break;
				
			case "d":
				String e1columna = "e"+columna;
				String fcolumna = "f"+columna;
				
				posBarco.add(cas);
				posBarco.add(e1columna);
				posBarco.add(fcolumna);
				break;
			case "D":
				String e1col = "E"+columna;
				String fcol = "F"+columna;
				
				posBarco.add(cas);
				posBarco.add(e1col);
				posBarco.add(fcol);
				break;
				
			case "e":
				String f1columna = "f"+columna;
				String gcolumna = "g"+columna;
				
				posBarco.add(cas);
				posBarco.add(f1columna);
				posBarco.add(gcolumna);
				break;
			case "E":
				String f1col = "F"+columna;
				String gcol = "G"+columna;
				
				posBarco.add(cas);
				posBarco.add(f1col);
				posBarco.add(gcol);
				break;
				
			case "f":
				String g1columna = "g"+columna;
				String hcolumna = "h"+columna;
				
				posBarco.add(cas);
				posBarco.add(g1columna);
				posBarco.add(hcolumna);
				break;
			case "F":
				String g1col = "G"+columna;
				String hcol = "H"+columna;
				
				posBarco.add(cas);
				posBarco.add(g1col);
				posBarco.add(hcol);
				break;
				
			case "g":
				String h1columna = "h"+columna;
				String icolumna = "i"+columna;
				
				posBarco.add(cas);
				posBarco.add(h1columna);
				posBarco.add(icolumna);
				break;
			case "G":
				String h1col = "H"+columna;
				String icol = "I"+columna;
				
				posBarco.add(cas);
				posBarco.add(h1col);
				posBarco.add(icol);
				break;
				
			case "h":
				String i1columna = "i"+columna;
				String jcolumna = "j"+columna;
				
				posBarco.add(cas);
				posBarco.add(i1columna);
				posBarco.add(jcolumna);
				break;
			case "H":
				String i1col = "I"+columna;
				String jcol = "J"+columna;
				
				posBarco.add(cas);
				posBarco.add(i1col);
				posBarco.add(jcol);
				break;
		  }         		 

        // Si el barco se coloca en horizontal se suma una columna a cada posición del barco
		} else if(orientacion.equalsIgnoreCase("h")) {
			   
			// Pasamos las columnas(int) a String
			int numColumna1 = numColumna+1;
		    String cas2= fila+numColumna1+"";
		    int numColumna2 = numColumna1+1;
		    String cas3= fila+numColumna2+"";
			
		    posBarco.add(cas);
		    posBarco.add(cas2);
		    posBarco.add(cas3);
		}
		
		 } 
		
		// Si la casilla es correcta se colocará el barco 
		if(colocar) {
			servicioDatos.colocarBarcos(posBarco, jugador);
		// Si no es correcta se introducirán de nuevo las coordenadas
		} else {
			efecto = "CASILLA INCORRECTA! Introduce de nuevo las coordenadas:";
		  }
		return efecto;
	}
	
	
	// Cuando se termina la partida se vacian las coordenadas de los barcos del jugador
	public void vaciarTablero(String jugador) throws RemoteException, MalformedURLException, NotBoundException 
	{
		servicioDatos.vaciarTablero(jugador);
	}
	
	
	
	/***********
	 * GETTERS
	 ***********/
	
	// Devuelve el ID de las partidas que estan en juego actualmente
	public List<Integer> getPartidasJugando() throws RemoteException, MalformedURLException, NotBoundException {
		return servicioDatos.getPartidasJugando();
	}
		
		
	public String consultarPuntuacion(String jugador) throws RemoteException 
	{
		return servicioDatos.consultarPuntuacion(jugador);
	}
	
	
	public int dameBarcos(String jugador) throws RemoteException, MalformedURLException, NotBoundException 
	{
		return servicioDatos.dameBarcos(jugador);
	}
	
	
	public String dameJugador2(String jug1) throws RemoteException, MalformedURLException, NotBoundException 
	{
		return servicioDatos.dameJugador2(jug1);
	}
	
	
	// Devuelve el jugador al que corresponde el id de la partida pasado como parametro
	public String dameContrincante(int numPartida) throws RemoteException, MalformedURLException, NotBoundException
	{
		return servicioDatos.dameContrincante(numPartida);
	}
	
	
	public CallbackJugadorInterface dameCallback(String jug) throws RemoteException, MalformedURLException, NotBoundException  
	{
		return servicioDatos.dameCallback(jug);
	}
	
		
	
	/***********
	 * DISPAROS
	 ***********/
	
	// Siempre empieza disparando el jugador 1
	public void realizarDisparo(CallbackJugadorInterface jugador1) throws RemoteException, MalformedURLException, NotBoundException 
	{
		jugador1.notificame("Introduzca coordenadas de tiro [YX]");
	} 
	
	
	// Realiza los disparos del jugador 1 y comprueba sus efectos
	public void realizarDisparoJugador1(String casillaDisparo, String nombre, CallbackJugadorInterface jugador1, int idPartida) throws RemoteException, MalformedURLException, NotBoundException 
	{
		CallbackJugadorInterface jugador2 = servicioDatos.dameCallback(servicioDatos.dameJugador2(nombre));
				
		switch(servicioDatos.disparoJugador(casillaDisparo,nombre,idPartida)){
			case "Has hundido la flota de tu contrincante (has ganado)":
				jugador1.notificame("Has hundido la flota de tu contrincante (has ganado)");
				jugador2.notificame("Te han hundido tu flota (has perdido)");
				jugador1.notificame("SALIR");
				jugador2.notificame("SALIR");
				break;
			case "Disparo realizado por ti (TOCADO)":
				jugador1.notificame("Disparo realizado por ti (TOCADO)");
				jugador2.notificame("Disparo realizado por el jugador contrario (TOCADO)");
				jugador2.notificame("Introduzca coordenadas de tiro [YX]");//
				break;
			case "Disparo realizado por ti (AGUA)":
				jugador1.notificame("Disparo realizado por ti (AGUA)");
				jugador2.notificame("Disparo realizado por el jugador contrario (AGUA)");
				jugador2.notificame("Introduzca coordenadas de tiro [YX]");//
				break;
			case "Casilla repetida! Vuelve a introducir casilla":
				jugador1.notificame("Casilla repetida! Vuelve a introducir casilla");
				break;
		}
	}


	// Realiza los disparos del jugador 2 y comprueba sus efectos
	public void realizarDisparoJugador2(String casillaDisparo, String nombre, CallbackJugadorInterface jugador2, int idPartida) throws RemoteException, MalformedURLException, NotBoundException 
	{
		CallbackJugadorInterface jugador1 = servicioDatos.dameCallback(servicioDatos.dameContrincante(idPartida));
				
		switch(servicioDatos.disparoJugador(casillaDisparo,nombre,idPartida)){
			case "Has hundido la flota de tu contrincante (has ganado)":
				jugador2.notificame("Has hundido la flota de tu contrincante (has ganado)");
				jugador1.notificame("Te han hundido tu flota (has perdido)");
				jugador2.notificame("SALIR");
				jugador1.notificame("SALIR");
				break;
			case "Disparo realizado por ti (TOCADO)":
				jugador2.notificame("Disparo realizado por ti (TOCADO)");
				jugador1.notificame("Disparo realizado por el jugador contrario (TOCADO)");
				jugador1.notificame("Introduzca coordenadas de tiro [YX]");//
				break;
			case "Disparo realizado por ti (AGUA)":
				jugador2.notificame("Disparo realizado por ti (AGUA)");
				jugador1.notificame("Disparo realizado por el jugador contrario (AGUA)");
				jugador1.notificame("Introduzca coordenadas de tiro [YX]");//
				break;
			case "Casilla repetida! Vuelve a introducir casilla":
				jugador2.notificame("Casilla repetida! Vuelve a introducir casilla");
				break;
		}
	}
	
	
}
