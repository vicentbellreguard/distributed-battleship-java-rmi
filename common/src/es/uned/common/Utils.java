
package es.uned.common;

public class Utils {
	
	public static final String CODEBASE = "java.rmi.server.codebase";
	
	public static final String ServerIP = "localhost";
	public static final int RegistryPort = 8888;
	
	public static void setCodeBase(Class<?> c) {
		String ruta = c.getProtectionDomain().getCodeSource()
					   .getLocation().toString();
		
		String path = System.getProperty(CODEBASE);
		
		if (path != null && !path.isEmpty()) {
			ruta = path + " " + ruta;  
		}
		
		System.setProperty(CODEBASE, ruta);
	}
}