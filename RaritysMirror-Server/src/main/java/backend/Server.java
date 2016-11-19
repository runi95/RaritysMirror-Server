package backend;

import java.io.IOException;
import java.net.ServerSocket;

public class Server {
	
	// TODO: Simplify everything down to the simplest way possible.
	// TODO: Add possitibility for more advanced options afterwards.
	// TODO: Server should be able to change the presentation live.
	
	public static final int portNumber = 1995;
	private boolean running = true;
	
	public void start() {
		try (ServerSocket serverSocket = new ServerSocket(portNumber); ){
			while(running)
				new ServerThread(serverSocket.accept()).start(); 
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
}
