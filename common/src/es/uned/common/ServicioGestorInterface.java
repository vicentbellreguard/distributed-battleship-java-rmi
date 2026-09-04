
package es.uned.common;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

public interface ServicioGestorInterface extends Remote {
	
	/** PARTIDA */
	public int iniciarPartida(String jugador1, CallbackJugadorInterface cbclienteobject) throws RemoteException, MalformedURLException, NotBoundException; 
	public void registrarPartida(int numPartida) throws RemoteException, MalformedURLException, NotBoundException;
	// Devuelve el map con las partidas iniciadas
	public Map<Integer, Partida> listarPartidas() throws RemoteException, MalformedURLException, NotBoundException;	
	// Cuando el jugador 2 se une a la partida
	public void empezarPartida(int numPartida, CallbackJugadorInterface cbclienteobject, String nombre) throws RemoteException, MalformedURLException, NotBoundException;

	/** BARCOS */
	public void colocacionBarcos(CallbackJugadorInterface cbclienteobject, CallbackJugadorInterface jugador) throws RemoteException, MalformedURLException, NotBoundException;
	public String colocarCasillas(String casilla, String orientacion, String jugador) throws RemoteException, MalformedURLException, NotBoundException;
	// El jugador 2 introduce las coordenadas del primer barco
	public void introducirCoordenadasBarco1J2(CallbackJugadorInterface jugador) throws RemoteException, MalformedURLException, NotBoundException;
	// El jugador 1 introduce las coordenadas del primer barco
	public void introducirCoordenadasBarco1J1(CallbackJugadorInterface jugador) throws RemoteException, MalformedURLException, NotBoundException;
	// El jugador 1 y 2 introducen las coordenadas del segundo barco
	public void introducirCoordenadasBarco2(CallbackJugadorInterface jugador) throws RemoteException, MalformedURLException, NotBoundException;
	// Si el jugador 1 o 2 introduce mal las coordenadas se volverá a pedir una casilla
	public void repetirCasilla(CallbackJugadorInterface jugador) throws RemoteException, MalformedURLException, NotBoundException;
	// Cuando se termina la partida se vacian las coordenadas de los barcos del jugador
	public void vaciarTablero(String jugador) throws RemoteException, MalformedURLException, NotBoundException;
	
	/** GETTERS */
	// Devuelve el ID de las partidas que estan en juego actualmente
	public List<Integer> getPartidasJugando() throws RemoteException, MalformedURLException, NotBoundException;
	public String consultarPuntuacion(String jugador) throws RemoteException;
	public int dameBarcos(String jugador) throws RemoteException, MalformedURLException, NotBoundException;
	public String dameJugador2(String jug1) throws RemoteException, MalformedURLException, NotBoundException;
	// Devuelve el jugador al que corresponde el id de la partida pasado como parametro
	public String dameContrincante(int numPartida) throws RemoteException, MalformedURLException, NotBoundException;
	public CallbackJugadorInterface dameCallback(String jug) throws RemoteException, MalformedURLException, NotBoundException;
	
	/** DISPARO */
	// Siempre empieza disparando el jugador 1
	public void realizarDisparo(CallbackJugadorInterface jugador1)throws RemoteException, MalformedURLException, NotBoundException;
	// Realiza los disparos del jugador 1 y comprueba sus efectos
	public void realizarDisparoJugador1(String fila, String nombre, CallbackJugadorInterface jugador1, int idPartida)throws RemoteException, MalformedURLException, NotBoundException;
	// Realiza los disparos del jugador 2 y comprueba sus efectos
	public void realizarDisparoJugador2(String fila, String nombre, CallbackJugadorInterface jugador2, int idPartida)throws RemoteException, MalformedURLException, NotBoundException;
	
}
