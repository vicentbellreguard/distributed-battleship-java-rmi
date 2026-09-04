
package es.uned.common;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

public interface ServicioDatosInterface extends Remote {
	
	/** PARTIDA */
	public void registrarPartida(Partida nuevaPartida) throws RemoteException;
	public void registrarPartida(int idPart) throws RemoteException;
	// Map que contiene los jugadores que forman cada partida
	public void partida(String jugador, String contrincante, int numPartida) throws RemoteException;
	// Devuelve el map con las partidas iniciadas
	public Map<Integer, Partida> listarPartidas() throws RemoteException;
	
	/** JUGADORES */
	public boolean registraJugador(Usuario nuevoUser) throws RemoteException;
	public boolean loginJugador (String nombreUsario, String pas ) throws RemoteException;
	// Devuelve Map con los jugadores y su puntuación histórica
	public Map<String,Integer> listarJugadores() throws RemoteException;
	// Para comprobar si un jugador puede empezar o unirse a una partida
	public boolean checkJugador(String nombre) throws RemoteException;
	public void registrarCallback(CallbackJugadorInterface cbclienteobject, String user) throws RemoteException;
	public void salirJugador(String nombreUsario) throws RemoteException;

	/** GETTERS */
	public List<String> getJugadoresRegistrados() throws RemoteException;
	public List<Integer> getPartidasJugando() throws RemoteException;	
	public String dameJugador1(String jug2) throws RemoteException;
	public String dameJugador2(String jug1) throws RemoteException;
	public CallbackJugadorInterface dameCallback(String jugador) throws RemoteException;
	// Devuelve el jugador al que corresponde el id de la partida pasado como parametro
	public String dameContrincante(int numPartida) throws RemoteException;
	public String consultarPuntuacion(String jugador) throws RemoteException;
	
	/** BARCOS */
	public void colocarBarcos(List<String> posFilaJugador2, String jugador) throws RemoteException;
	// Devuelve la cantidad de barcos introducidos por el jugador
	public int dameBarcos(String jugador) throws RemoteException;
	// Al finalizar la partida, se eliminarán los barcos de cada jugador
	public void vaciarTablero(String jugador) throws RemoteException;
	
	/** DISPARO */
	public String disparoJugador(String casilla, String nombre, int idPartida) throws RemoteException;

}
