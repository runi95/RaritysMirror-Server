package javafx.view;

import java.io.File;
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
import javafx.model.TextObject;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;

public class Editor extends BorderPane implements Initializable {

	public final static Image ADD_SLIDE_SELECTED = new Image("javafx/view/images/AddSlide-Selected.png"), ADD_SLIDE_UNSELECTED = new Image("javafx/view/images/AddSlide-Unselected.png"), REMOVE_SLIDE_SELECTED = new Image("javafx/view/images/RemoveSlide-Selected.png"), REMOVE_SLIDE_UNSELECTED = new Image("javafx/view/images/RemoveSlide-Unselected.png"), ADD_IMAGE_SELECTED = new Image("javafx/view/images/AddImage-Selected.png"), ADD_IMAGE_UNSELECTED = new Image("javafx/view/images/AddImage-Unselected.png"), ADD_TEXT_SELECTED = new Image("javafx/view/images/AddText-Selected.png"), ADD_TEXT_UNSELECTED = new Image("javafx/view/images/AddText-Unselected.png");
	
	ObservableList<Slide> slideList = FXCollections.observableArrayList();
	ObservableList<String> fontList = FXCollections.observableArrayList();
	ObservableList<String> fontSizeList = FXCollections.observableArrayList();

	CanvasObjectList canvasList = new CanvasObjectList();
//	CanvasObject selected = null;

	@FXML
	TableView<Slide> sliderTable;
	@FXML
	ScrollPane scroll;
	@FXML
	CustomCanvas canvas;
	@FXML
	ImageView addSlideImageView, removeSlideImageView, addImageImageView, addTextImageView;
	@FXML
	ComboBox<String> fontComboBox;
	@FXML
	ComboBox<String> fontSizeComboBox;

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
		
		fontList.addAll(Font.getFamilies());
		fontSizeList.addAll("10", "12", "15", "20", "30");
		fontComboBox.setItems(fontList);
		fontComboBox.setValue(Font.getDefault().getName());
		fontSizeComboBox.setItems(fontSizeList);
		fontSizeComboBox.setValue("20");
		
		canvas.addListener(new ChangeListener<CanvasObject>() {
			@Override
			public void changed(ObservableValue<? extends CanvasObject> observable, CanvasObject oldValue,
					CanvasObject newValue) {
				if(newValue != null && newValue instanceof TextObject) {
					fontSizeComboBox.setVisible(true);
					fontComboBox.setVisible(true);
				}else{
					fontSizeComboBox.setVisible(false);
					fontComboBox.setVisible(false);
				}
					
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
	
	public void addTextButtonClicked() {
		canvas.addText("", fontComboBox.getValue(), getFontSizeInteger());
	}
	
	public void addTextButtonEntered() {
		addTextImageView.setImage(ADD_TEXT_SELECTED);
	}
	
	public void addTextButtonExited() {
		addTextImageView.setImage(ADD_TEXT_UNSELECTED);
	}
	
	public void addImageButtonClicked() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Add Image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Images", "*.bmp", "*.jpeg", "*.jpe", "*.jfif", "*.jpg", "*.png" ),
                new FileChooser.ExtensionFilter("JPG", "*.jpg", "*.jpeg", "*.jpe", "*.jfif"),
                new FileChooser.ExtensionFilter("PNG", "*.png")
            );
		File file =	fileChooser.showOpenDialog(getScene().getWindow());
		if(file != null) {
			Image image = new Image(file.toURI().toString());
			canvas.addImage(image);
		}
			
	}
	
	public void fontComboBox() {
		canvas.setFont(fontComboBox.getValue(), getFontSizeInteger());
	}
	
	public void addImageButtonEntered() {
		addImageImageView.setImage(ADD_IMAGE_SELECTED);
	}
	
	public void addImageButtonExited() {
		addImageImageView.setImage(ADD_IMAGE_UNSELECTED);
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
	
	private int getFontSizeInteger() {
		try {
			return Integer.parseInt(fontSizeComboBox.getValue());
		}catch (NumberFormatException e) {
			fontSizeComboBox.setValue("20");
			return 20;
		}
	}
}
