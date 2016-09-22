package javafx.view;

import javafx.Resources;
import javafx.scene.control.TitledPane;

public class Startup extends TitledPane {
	
	public Startup() {
		Resources.loadFXML(this);
	}
	
	//On actions:
	
	public void openEditorClicked() {
		//TODO: Add stuffs
		System.out.println("open editor clicked!");
	}
	
	public void loadClicked() {
		//TODO: Add stuffs
		System.out.println("load button clicked!");
	}
	
	public void localhostChecked() {
		//TODO: Add stuffs
		System.out.println("localhost changed!");
	}
	
	public void startServerClicked() {
		//TODO: Add stuffs
		System.out.println("start server clicked!");
	}
}
