
package es.uned.basededatos;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import es.uned.common.CallbackJugadorInterface;
import es.uned.common.Partida;
import es.uned.common.ServicioDatosInterface;
import es.uned.common.Usuario;

public class ServicioDatosImpl extends UnicastRemoteObject implements ServicioDatosInterface {
	
	public ServicioDatosImpl() throws RemoteException {
		super();
	}

	private static final long serialVersionUID = 1L;
	
	/*
	 * JUGADORES
	 */
	
	// Map con el nombre de usuario registrado y su contraseña
	private static Map<String,Usuario> jugadoresRegistrados = new HashMap<String,Usuario>();
	// ArrayList con el nickname de los usuarios conectados
	private List<String> jugadoresConectados = new ArrayList<String>();
	// Maps con el jugador y su contrincante
	private Map<String,String> contrincante = new HashMap<String,String>();
	private Map<String,String> contrincante1 = new HashMap<String,String>();
	// Map con los id de las partidas y su correspondiente callback
	private Map<String, CallbackJugadorInterface> CallbackJugador = new HashMap<>();
		
	/*
	 * PARTIDA
	 */
	
	// Map con los id y la información de cada partida
	private Map<Integer, Partida> partida = new HashMap<>();	
	// ArrayList con los id de las partidas iniciadas
	private List<Integer> partidasJugando = new ArrayList<Integer>();	
	// Puntuacion histórica de cada jugador
	private Map<String,Integer> puntuacionJugador = new HashMap<>();
	
	/*
	 * BARCO
	 */
	
    // Map que contiene el jugador y sus barcos
	private Map<String, List<String>> barcoJugador = new HashMap<>();
	
	
	/***********
	 * PARTIDA
	 ***********/
	
	public void registrarPartida(Partida nuevaPartida) throws RemoteException
	{
		partida.put(nuevaPartida.getId(), nuevaPartida);
	}
	
	
	public void registrarPartida(int idPart) throws RemoteException 
	{
		partidasJugando.add(idPart);
	}
	
	
	// Map que contiene los jugadores que forman cada partida
	public void partida(String jug1, String jug2, int numPartida) throws RemoteException 
	{
		contrincante.put(jug1,jug2);
		// Map con clave y valor al revés
		contrincante1.put(jug2,jug1);
		// Partida completa, ya tiene el J1 y J2
		partida.get(numPartida).setNombreJug2(jug2);
	}
	
	
	// Devuelve el map con las partidas iniciadas
	public Map<Integer, Partida> listarPartidas() throws RemoteException 
	{		
		return partida;
	}
				
	
	
	/*************
	 * JUGADORES
	 *************/
	
	// Devuelve Map con los jugadores y su puntuación histórica
	public Map<String,Integer> listarJugadores() throws RemoteException
	{
		return puntuacionJugador;
	}		
		
		
	public boolean registraJugador(Usuario nuevoUser) throws RemoteException 
	{
		if(jugadoresRegistrados.containsKey(nuevoUser.getNombre()))
		{
			// El jugador ya está registrado
			return false;
		} else {
			jugadoresRegistrados.put(nuevoUser.getNombre(), nuevoUser);
			return true;
		}		
	}
	
	
	public boolean loginJugador(String nombreUsario, String pas) throws RemoteException 
	{
		// Comprobamos que no este ya conectado
		if (jugadoresConectados.contains(nombreUsario))
		{
			return false;
		}
		// Comprobamos que esta en la lista de jugadores
		else if (jugadoresRegistrados.containsKey(nombreUsario))
		{
			// Comprobamos que coincide la contraseña
			String passUsuario = jugadoresRegistrados.get(nombreUsario).getPass();
			if(passUsuario.equals(pas))
			{
				// Añadimos el jugador la lista de conectados
				jugadoresConectados.add(nombreUsario);
				return true;
			} else {
				return false;
			}
				
		} else {
			return false;
		}
	}

	
	public void registrarCallback(CallbackJugadorInterface cbclienteobject, String user) throws RemoteException 
	{
		CallbackJugador.put(user, cbclienteobject);
	}
	
	
	// Para comprobar si un jugador puede empezar o unirse a una partida
	public boolean checkJugador(String nombre)
	{
		if (jugadoresRegistrados.containsKey(nombre))
			return true;
		else
			return false;
		
	}
	
	
	public void salirJugador(String nombreUsario) throws RemoteException 
	{
		jugadoresConectados.remove(nombreUsario);	
	}
	
	
	
	/***********
	 * GETTERS
	 ***********/
	
	public List<Integer> getPartidasJugando() throws RemoteException {
		return partidasJugando;
	}
	
	
	public List<String> getJugadoresRegistrados() throws RemoteException 
	{
		return jugadoresConectados;  			
	}
	
	
	public CallbackJugadorInterface dameCallback(String jug) throws RemoteException 
	{
		return CallbackJugador.get(jug);
	}
	
	
	public String dameJugador1(String jug2) throws RemoteException 
	{
		return contrincante1.get(jug2);
	}
	
	
	public String dameJugador2(String jug1) throws RemoteException 
	{
		return contrincante.get(jug1);
	}
	
	
	// Devuelve el jugador al que corresponde el id de la partida pasado como parametro
	public String dameContrincante(int numPartida) throws RemoteException 
	{
		Partida part =  partida.get(numPartida);
		return part.getNombreJug1();		
	}
	
	
	public String consultarPuntuacion(String jugador) throws RemoteException
	{		
		// Si el jugador no ha hecho ningún punto el map estará vacío y devolverá "0"
		if(listarJugadores().get(jugador) == null) {
			return "El jugador " + jugador + " tiene 0 puntos\n"; 			
		} 				
		else {
			return "El jugador " + jugador + " tiene " + listarJugadores().get(jugador) + " puntos\n";
		}
	}
	
	
	
	/*********
	 * BARCOS
	 *********/
	
	public void colocarBarcos(List<String> casillas, String jugador) throws RemoteException 
	{		
		// Si el jugador ya está añadido al mapa, solo añadiremos sus barcos
		if(barcoJugador.containsKey(jugador)) {
			// Declaramos el Iterador e imprimimos los Elementos del ArrayList
			Iterator<String> nombreIterator = casillas.iterator();
			while(nombreIterator.hasNext()){
				String elemento = nombreIterator.next();
				barcoJugador.get(jugador).add(elemento);
			}
		 // Añadimos al Map el jugador y sus barcos
		} else {
		barcoJugador.put(jugador, casillas);
		}
	}
	
	
	// Devuelve la cantidad de barcos introducidos por el jugador
	public int dameBarcos(String jugador) throws RemoteException 
	{
		int numCasillas;
		if(!(barcoJugador.containsKey(jugador))) {
			numCasillas = 0;
		} else {
			numCasillas = barcoJugador.get(jugador).size();
		  }		
		  return numCasillas;
	}
	
	
	// Al finalizar la partida, se eliminarán los barcos de cada jugador
	public void vaciarTablero(String jugador) throws RemoteException 
	{
		barcoJugador.get(jugador).clear();
	}
			
		
	
	/***********
	 * DISPARO
	 ***********/
	
	public String disparoJugador(String casillaDisparo, String jugador, int idPartida) throws RemoteException 
	{
		String efecto = "";
		// Listas de las casillas que ha disparado cada jugador
		List<String> casillasDisparadasJ1 = new ArrayList<String>();
		List<String> casillasDisparadasJ2 = new ArrayList<String>();
		   
		if (partida.get(idPartida).getNombreJug1().equals(jugador)) {
				
			// Si el jugador1 no ha repetido disparo en la misma casilla
			if(!(partida.get(idPartida).getCasillasDisparadasJ1().contains(casillaDisparo))) {
				casillasDisparadasJ1 = partida.get(idPartida).getCasillasDisparadasJ1();				
				casillasDisparadasJ1.add(casillaDisparo);
				partida.get(idPartida).setCasillasDisparadasJ1(casillasDisparadasJ1);

			// El contrincante del jugador pasado por parametro
			String cont = contrincante.get(jugador);
			//Lista donde se guardaran las casillas de los barcos del jugador contrincante
			List<String> columnasBarcos = barcoJugador.get(cont);
			
			if(columnasBarcos.contains(casillaDisparo)) {
				efecto = "Disparo realizado por ti (TOCADO)";

				if(puntuacionJugador.containsKey(jugador)) {
					partida.get(idPartida).setPuntosJug1(1); //puntos partida
					puntuacionJugador.put(jugador, puntuacionJugador.get(jugador) + 1);	//puntuacion historica
				} else 
				  {
					partida.get(idPartida).setPuntosJug1(1); //puntos de cada partida
					puntuacionJugador.put(jugador, 1); //puntuacion historica
				  }
				
				if(partida.get(idPartida).getPuntosJug1() == 6) {
					partida.get(idPartida).setPuntosJug1(10); //puntos partida
					puntuacionJugador.put(jugador, puntuacionJugador.get(jugador) + 10); //puntuacion historica
					efecto = "Has hundido la flota de tu contrincante (has ganado)";
				}
				
			} else {
				efecto = "Disparo realizado por ti (AGUA)";
			  }
						
						
			// Si el jugador1 ha repetido casilla de disparo
			} else {
				efecto = "Casilla repetida! Vuelve a introducir casilla";
			  }
			  return efecto;
			
		// Si no coincide se buscará el rival en el Map contrincante1
		} else 
		  {				
			if(!(partida.get(idPartida).getCasillasDisparadasJ2().contains(casillaDisparo))) {
				casillasDisparadasJ2 = partida.get(idPartida).getCasillasDisparadasJ2();				
				casillasDisparadasJ2.add(casillaDisparo);
				partida.get(idPartida).setCasillasDisparadasJ2(casillasDisparadasJ2);

			// El contrincante del jugador pasado por parametro
			String cont = contrincante1.get(jugador);
			// Lista donde se guardaran las casillas de los barcos del jugador contrincante
			List<String> columnasBarcos = barcoJugador.get(cont);				
			
			if(columnasBarcos.contains(casillaDisparo)) {
				efecto = "Disparo realizado por ti (TOCADO)";
				
			if(puntuacionJugador.containsKey(jugador)) {
				partida.get(idPartida).setPuntosJug2(1);	//puntos de cada partida
				puntuacionJugador.put(jugador, puntuacionJugador.get(jugador) + 1);	//puntuacion historica	
							 				
			} else 
			  {
				partida.get(idPartida).setPuntosJug2(1);	//puntos de cada partida
				puntuacionJugador.put(jugador, 1); //puntuacion historica	
			  }		
				
			   if(partida.get(idPartida).getPuntosJug2() == 6) {
					partida.get(idPartida).setPuntosJug2(10);  //puntos de cada partida
					puntuacionJugador.put(jugador, puntuacionJugador.get(jugador) + 10); //puntuacion historica
					efecto = "Has hundido la flota de tu contrincante (has ganado)";
				}
				
			} else 
			  {
				efecto = "Disparo realizado por ti (AGUA)";
			  }
			
			// Si el jugador2 ha repetido casilla de disparo
			} else {
				efecto = "Casilla repetida! Vuelve a introducir casilla";
			  }
			  return efecto;			
		   }
	}
	
	
}