

package es.uned.common;

import java.rmi.*;

/** La interfaz remota de jugador debe contener al menos un método que será invocado por el servidor en el callback. 
    El servidor debe invocar el método notifícame cuando realiza el callback, pasando como argumento una cadena de caracteres (String). 
    Una vez recibido el callback, el jugador utiliza esta cadena para componer otra cadena que devuelve al servidor. */
public interface CallbackJugadorInterface extends Remote {
	
	public void notificame(String mensaje) throws RemoteException;
}
