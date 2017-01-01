package backend;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ServerThread extends Thread {

	private ObjectOutputStream oos = null;
	private ObjectInputStream ois = null;
	private ClientInformation ci;
	private int id;
	
	public ServerThread(Socket socket, int id) throws IOException {
		super("ServerThread");
		this.id = id;
		oos = new ObjectOutputStream(socket.getOutputStream());
		ois = new ObjectInputStream(socket.getInputStream());
		
		System.out.println(socket.getInetAddress() + " has connected!");
	}
	
	public void run() {
		Message msg = new Message();
		
		if(ci == null)
			msg.setRequest("version");
		else if (ci.getName() == null)
			msg.setRequest("name");
		else{
			
		}
		
		writeObject(msg);
	}
	
	private void writeObject(Message msg) {
		try {
			oos.writeObject(msg);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		readObject();
	}
	
	private void readObject() {
		try {
			Message rMsg = (Message) ois.readObject();
			parseResponse(rMsg.getResponse(), rMsg.getRequest());
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		run();
	}
	
	private void parseResponse(String response, String request) {
		System.out.println("response: " + response);
		System.out.println("request: " + request);
		
		if(response == null)
			return;
		
		if(response.equals("version"))
			ci = new ClientInformation(request);
		
		if(response.equals("name") || response.equals("setName"))
			if(request == null) {
				Message msg = new Message();
				msg.setRequest("setName");
				msg.setResponse("Screen #" + id);
				writeObject(msg);
			}else
				ci.setName(request);
	}

}
