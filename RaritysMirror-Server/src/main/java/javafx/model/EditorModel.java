package javafx.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;

public class EditorModel {
	public final static Image ADD_SLIDE_SELECTED = new Image("javafx/view/images/AddSlide-Selected.png"), ADD_SLIDE_UNSELECTED = new Image("javafx/view/images/AddSlide-Unselected.png"), REMOVE_SLIDE_SELECTED = new Image("javafx/view/images/RemoveSlide-Selected.png"), REMOVE_SLIDE_UNSELECTED = new Image("javafx/view/images/RemoveSlide-Unselected.png"), ADD_IMAGE_SELECTED = new Image("javafx/view/images/AddImage-Selected.png"), ADD_IMAGE_UNSELECTED = new Image("javafx/view/images/AddImage-Unselected.png"), ADD_TEXT_SELECTED = new Image("javafx/view/images/AddText-Selected.png"), ADD_TEXT_UNSELECTED = new Image("javafx/view/images/AddText-Unselected.png");
	
	private ObservableList<Slide> slideList = FXCollections.observableArrayList();
	private ObservableList<String> fontList = FXCollections.observableArrayList();
	private ObservableList<String> fontSizeList = FXCollections.observableArrayList();
	
	public ObservableList<Slide> getSlideList() {
		return slideList;
	}
	
	public ObservableList<String> getFontList() {
		return fontList;
	}
	
	public ObservableList<String> getFontSizeList() {
		return fontSizeList;
	}
}
