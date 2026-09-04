
package es.uned.cliente;

import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;

import es.uned.common.CallbackJugadorInterface;
import es.uned.common.Eventos;

/** callback de Jugador es una característica que permite a un jugador de objeto registrarse a sí mismo 
    con un servidor de objeto remoto para callbacks, de forma que el servidor pueda llevar a cabo una invocación 
    del método del jugador cuando el evento ocurra.*/

 // Cada jugador tiene su callback, y en el CallbackJugadorImpl está la lista sincronizada perteneciente a ese jugador.
 public class CallbackJugadorImpl extends UnicastRemoteObject implements CallbackJugadorInterface {

	private static final long serialVersionUID = 1L;
	private Eventos eventos;
	
	public CallbackJugadorImpl (Eventos eventos) throws RemoteException {
		this.eventos=eventos;
     }
	
	@Override
	public void notificame(String mensaje) throws RemoteException {
		eventos.addEvento(mensaje);
	}

}