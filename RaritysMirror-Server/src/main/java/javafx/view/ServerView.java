package javafx.view;

import backend.Server;
import javafx.fxml.FXML;
import javafx.model.ServerModel;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

public class ServerView extends BorderPane {
	
	private ServerModel model;
	boolean serverRunning = false;
	
	@FXML
	Button startStopServerButton;
	
	@FXML
	TextField portTextField;
	
	//On actions:
	public void startServerClicked() {
		model.getServerRunning().setValue(!model.getServerRunning().getValue());
	}
	
	public void initModel(ServerModel model) {
		if (this.model != null) {
            throw new IllegalStateException("Model can only be initialized once");
        }
		
		this.model = model;
		
		model.getServerRunning().addListener((obs, oldSetting, newSetting) -> {
			if(newSetting) {
				int port = Integer.parseInt(portTextField.getText());
				if(checkPort(port))
					return;
				
				Server.start(port);
				startStopServerButton.setText("Stop Server");
			}else{
				Server.stop();
				startStopServerButton.setText("Start Server");
			}
				
		});
	}
	
	private boolean checkPort(int port) {
		if(port < 0)
			return true;
		
		if(port > 65535)
			return true;
		
		return false;
	}
}
