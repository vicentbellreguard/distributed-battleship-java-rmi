
package es.uned.common;

import java.util.ArrayList;
import java.util.List;

public class Eventos {

private List<String> lista = null;
	
	public Eventos() {
		this.lista = new ArrayList<String>();
	}
	
	public synchronized void addEvento(String evento)  {
		this.lista.add(evento);
		notifyAll();
	}
	
	public synchronized String getEvento() throws InterruptedException { 
		if(this.lista.size() == 0) wait();
		String evento = this.lista.get(0);
		this.lista.remove(0);
		return evento;
	}
	
	public synchronized int getTotalEventosPendientes() throws InterruptedException { 
		return this.lista.size();
	}
	
}
