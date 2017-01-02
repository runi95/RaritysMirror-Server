package backend;

import java.io.IOException;
import java.net.ServerSocket;

public class Server {
	// TODO: Simplify everything down to the simplest way possible.
	// TODO: Add possibility for more advanced options afterwards.
	// TODO: Server should be able to change the presentation live.
	
	private static int id = 1;
	private static boolean running;
	
	public static void start(int portNumber) {
		running = true;
		
		try (ServerSocket serverSocket = new ServerSocket(portNumber); ){
			while(running) {
				new ServerThread(serverSocket.accept(), getId()).start();
				setId(getId() + 1);
			}
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void stop() {
		running = false;
	}
	
	synchronized static int getId() {
		return id;
	}
	
	synchronized static void setId(int id) {
		Server.id = id;
	}
	
}
