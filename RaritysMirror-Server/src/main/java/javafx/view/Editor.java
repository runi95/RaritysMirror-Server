package javafx.view;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.model.CanvasObject;
import javafx.model.EditorModel;
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

	EditorModel model;

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

	public void initialize(URL location, ResourceBundle resources) {
		// Initialize Table:
		initModel(new EditorModel());
		
		TableColumn<Slide, Label> nameColumn = new TableColumn<Slide, Label>("Name");
		nameColumn.setCellValueFactory(new PropertyValueFactory<Slide, Label>("name"));
		nameColumn.setEditable(false);

		sliderTable.getColumns().add(nameColumn);

		Slide defaultSlide = new Slide("default");
		model.getSlideList().add(defaultSlide);
		
		sliderTable.setItems(model.getSlideList());
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
		
		model.getFontList().addAll(Font.getFamilies());
		model.getFontSizeList().addAll("10", "12", "15", "20", "30");
		fontComboBox.setItems(model.getFontList());
		fontComboBox.setValue(Font.getDefault().getName());
		fontSizeComboBox.setItems(model.getFontSizeList());
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
		model.getSlideList().add(new Slide("Slide #" + (model.getSlideList().size() + 1)));
		sliderTable.getSelectionModel().select(model.getSlideList().get(model.getSlideList().size()-1));
	}
	
	private void removeSlide() {
		if(model.getSlideList().size() > 1)
			model.getSlideList().remove(sliderTable.getSelectionModel().getSelectedItem());
	}
	
	public void addTextButtonClicked() {
		canvas.addText("", fontComboBox.getValue(), getFontSizeInteger());
	}
	
	public void addTextButtonEntered() {
		addTextImageView.setImage(EditorModel.ADD_TEXT_SELECTED);
	}
	
	public void addTextButtonExited() {
		addTextImageView.setImage(EditorModel.ADD_TEXT_UNSELECTED);
	}
	
	public void addImageButtonClicked() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Add Image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Images", "*.bmp", "*.jpeg", "*.jpe", "*.jfif", "*.jpg", "*.png" ),
                new FileChooser.ExtensionFilter("JPG", "*.jpg", "*.jpeg", "*.jpe", "*.jfif"),
                new FileChooser.ExtensionFilter("PNG", "*.png")
            );
		File file =	fileChooser.showOpenDialog(canvas.getScene().getWindow());
		if(file != null) {
			Image image = new Image(file.toURI().toString());
			canvas.addImage(image);
		}
			
	}
	
	public void fontComboBox() {
		canvas.setFont(fontComboBox.getValue(), getFontSizeInteger());
	}
	
	public void addImageButtonEntered() {
		addImageImageView.setImage(EditorModel.ADD_IMAGE_SELECTED);
	}
	
	public void addImageButtonExited() {
		addImageImageView.setImage(EditorModel.ADD_IMAGE_UNSELECTED);
	}
	
	public void addSlideButtonClicked() {
		addSlide();
	}
	
	public void addSlideButtonEntered() {
		addSlideImageView.setImage(EditorModel.ADD_SLIDE_SELECTED);
	}
	
	public void addSlideButtonExited() {
		addSlideImageView.setImage(EditorModel.ADD_SLIDE_UNSELECTED);
	}
	
	public void removeSlideButtonClicked() {
		removeSlide();
	}
	
	public void removeSlideButtonEntered() {
		removeSlideImageView.setImage(EditorModel.REMOVE_SLIDE_SELECTED);
	}
	
	public void removeSlideButtonExited() {
		removeSlideImageView.setImage(EditorModel.REMOVE_SLIDE_UNSELECTED);
	}
	
	private int getFontSizeInteger() {
		try {
			return Integer.parseInt(fontSizeComboBox.getValue());
		}catch (NumberFormatException e) {
			fontSizeComboBox.setValue("20");
			return 20;
		}
	}
	
	public void initModel(EditorModel model) {
		if (this.model != null) {
            throw new IllegalStateException("Model can only be initialized once");
        }
		
		this.model = model;
		
		/*
		model.getServerRunning().addListener((obs, oldSetting, newSetting) -> {
			if(newSetting) {
				int port = Integer.parseInt(portTextField.getText());
				if(checkPort(port))
					return;
				
				model.getServer().start(port);
				startStopServerButton.setText("Stop Server");
			}else{
				model.getServer().stop();
				startStopServerButton.setText("Start Server");
			}
				
		});
		*/
	}
}
