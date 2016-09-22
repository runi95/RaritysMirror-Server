package javafx.view;

import javafx.Resources;
import javafx.scene.control.TitledPane;

public class Startup extends TitledPane {

	public Startup() {
		Resources.loadFXML(this);
	}
}
