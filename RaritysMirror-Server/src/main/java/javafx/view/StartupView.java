package javafx.view;

import javafx.Resources;
import javafx.scene.control.TabPane;

public class StartupView extends TabPane {
	public StartupView() {
		Resources.loadFXML(this);
	}
}
