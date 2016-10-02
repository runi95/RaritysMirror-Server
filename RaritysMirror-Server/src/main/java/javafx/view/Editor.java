package javafx.view;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.Resources;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.model.CanvasObject;
import javafx.model.CanvasObjectList;
import javafx.model.Slide;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;

public class Editor extends BorderPane implements Initializable {

	public final static Image ADD_SLIDE_SELECTED = new Image("javafx/view/images/AddSlide-Selected.png"), ADD_SLIDE_UNSELECTED = new Image("javafx/view/images/AddSlide-Unselected.png"), REMOVE_SLIDE_SELECTED = new Image("javafx/view/images/RemoveSlide-Selected.png"), REMOVE_SLIDE_UNSELECTED = new Image("javafx/view/images/RemoveSlide-Unselected.png");
	
	ObservableList<Slide> slideList = FXCollections.observableArrayList();
	GraphicsContext gc;

	CanvasObjectList canvasList = new CanvasObjectList();
	CanvasObject selected = null;

	@FXML
	TableView<Slide> sliderTable;
	@FXML
	ScrollPane scroll;
	@FXML
	CustomCanvas canvas;
	@FXML
	ImageView addSlideImageView, removeSlideImageView;

	public Editor() {
		Resources.loadFXML(this);
	}

	public void initialize(URL location, ResourceBundle resources) {
		// Initialize Table:
		TableColumn<Slide, Label> nameColumn = new TableColumn<Slide, Label>("Name");
		nameColumn.setCellValueFactory(new PropertyValueFactory<Slide, Label>("name"));
		nameColumn.setEditable(false);

		sliderTable.getColumns().add(nameColumn);

		Slide defaultSlide = new Slide("default");
		slideList.add(defaultSlide);
		
		sliderTable.setItems(slideList);
		sliderTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Slide>() {
	        @Override
	        public void changed(ObservableValue<? extends Slide> ov, Slide oldSlide, Slide newSlide) {
	            canvas.setCurrentSlide(newSlide.getCanvasObjectList());
	        }
	    });
		
		canvas.setCurrentSlide(defaultSlide.getCanvasObjectList());
		
		scroll.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if(newValue)
					canvas.requestFocus();
			}
		});
	}

	private void addSlide() {
		slideList.add(new Slide("Slide #" + (slideList.size() + 1)));
		sliderTable.getSelectionModel().select(slideList.get(slideList.size()-1));
	}
	
	private void removeSlide() {
		if(slideList.size() > 1)
			slideList.remove(sliderTable.getSelectionModel().getSelectedItem());
	}
	
	public void addSlideButtonClicked() {
		addSlide();
	}
	
	public void addSlideButtonEntered() {
		addSlideImageView.setImage(ADD_SLIDE_SELECTED);
	}
	
	public void addSlideButtonExited() {
		addSlideImageView.setImage(ADD_SLIDE_UNSELECTED);
	}
	
	public void removeSlideButtonClicked() {
		removeSlide();
	}
	
	public void removeSlideButtonEntered() {
		removeSlideImageView.setImage(REMOVE_SLIDE_SELECTED);
	}
	
	public void removeSlideButtonExited() {
		removeSlideImageView.setImage(REMOVE_SLIDE_UNSELECTED);
	}
}
