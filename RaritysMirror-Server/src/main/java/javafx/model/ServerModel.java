package javafx.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class ServerModel {
	BooleanProperty serverRunning = new SimpleBooleanProperty(false);
	
	public BooleanProperty getServerRunning() {
		return serverRunning;
	}
}
