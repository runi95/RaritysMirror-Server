package backend;

import java.net.Socket;

public class ServerThread extends Thread {

	private Socket socket = null;
	
	public ServerThread(Socket socket) {
		super("ServerThread");
		this.socket = socket;
	}
	
	public void run() {
		System.out.println(socket.getInetAddress() + " has connected!");
	}

}
