package javafx.model;

import backend.Server;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class ServerModel {
	private final Server server = new Server();
	BooleanProperty serverRunning = new SimpleBooleanProperty(false);
	
	public BooleanProperty getServerRunning() {
		return serverRunning;
	}
	
	public Server getServer() {
		return server;
	}
}
