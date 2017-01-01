package backend;

import java.io.IOException;
import java.net.ServerSocket;

public class Server {
	public static void main(String[] args) {
		// TODO: Remove this test function once GUI is set up properly!
		Server server = new Server();
		server.start();
	}
	
	// TODO: Simplify everything down to the simplest way possible.
	// TODO: Add possibility for more advanced options afterwards.
	// TODO: Server should be able to change the presentation live.
	
	public static final int portNumber = 1995;
	private static int id = 1;
	private boolean running = true;
	
	public void start() {
		try (ServerSocket serverSocket = new ServerSocket(portNumber); ){
			while(running) {
				new ServerThread(serverSocket.accept(), getId()).start();
				setId(getId() + 1);
			}
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	synchronized static int getId() {
		return id;
	}
	
	synchronized static void setId(int id) {
		Server.id = id;
	}
	
}
