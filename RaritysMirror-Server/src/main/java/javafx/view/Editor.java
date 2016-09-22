package javafx.view;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.Resources;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.model.Slide;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;

public class Editor extends BorderPane implements Initializable{

	ObservableList<Slide> sliderList = FXCollections.observableArrayList();
	GraphicsContext gc;
	
	@FXML
	TableView<Slide> sliderTable;
	@FXML
	Canvas canvas;
	
	public Editor() {
		Resources.loadFXML(this);
		System.out.println(this.getWidth());
		System.out.println(this.getHeight());
		canvas.setWidth((this.getWidth() - sliderTable.getWidth()));
		canvas.setHeight(this.getHeight());
		System.out.println(canvas.getWidth());
		System.out.println(canvas.getHeight());
	}

	public void initialize(URL location, ResourceBundle resources) {
		// Initialize Table:
		TableColumn<Slide, Label> nameColumn = new TableColumn<Slide, Label>("Name");
		nameColumn.setCellValueFactory(new PropertyValueFactory<Slide, Label>("name"));
		nameColumn.setEditable(false);
		
		sliderTable.getColumns().add(nameColumn);
		
		sliderTable.setItems(sliderList);
		
		// Initialize Canvas
		gc = canvas.getGraphicsContext2D();
		
		this.widthProperty().addListener((ov, oldValue, newValue) -> {
			System.out.println("Setting width: " + newValue.doubleValue());
            canvas.setWidth((newValue.doubleValue() - sliderTable.getWidth()));
        });
		
		this.heightProperty().addListener((ov, oldValue, newValue) -> {
			System.out.println("Setting Height: " + (newValue.doubleValue() - sliderTable.getHeight()));
            canvas.setHeight(newValue.doubleValue());
        });
		
		/*
		this.prefHeightProperty().addListener((ov, oldValue, newValue) -> {
            canvas.setHeight((newValue.doubleValue() - sliderTable.getHeight()));
        });
		*/
		
		canvas.setOnMouseClicked(new EventHandler <MouseEvent>() {
			public void handle(MouseEvent event) {
				// TODO: Make sure you can select elements on the canvas!
			}
		});
		
		canvas.setOnDragOver(new EventHandler <DragEvent>() {
	        public void handle(DragEvent event) {
	            Dragboard db = event.getDragboard();
//	            System.out.println(db.hasFiles() + " || " + db.hasHtml() + " || " + db.hasImage() + " || " + db.hasRtf() + " || " + db.hasString() + " || " + db.hasUrl());
	            if(db.hasFiles() || db.hasImage()){
	                event.acceptTransferModes(TransferMode.ANY);
	            }

	            event.consume();
	        }
	    });
		
		canvas.setOnDragDropped(new EventHandler <DragEvent>() {
	        public void handle(DragEvent event) {
	            Dragboard db = event.getDragboard();

	            if(db.hasFiles()){

	                for(File file:db.getFiles()){
	                	
	                    //String absolutePath = file.getAbsolutePath();
	                    String absolutePath = file.toURI().toString();

	                    Image dbimage = new Image(absolutePath);
	                    ImageView dbImageView = new ImageView();
	                    dbImageView.setImage(dbimage);

	                    gc.drawImage(dbimage, 0, 0);
	                    gc.setStroke(Color.color(0.07, 0.07, 0.56));
	                    gc.strokeLine(0, 0, 0, dbimage.getHeight());
	                    gc.strokeLine(0, 0, dbimage.getWidth(), 0);
	                    gc.strokeLine(dbimage.getWidth(), 0, dbimage.getWidth(), dbimage.getHeight());
	                    gc.strokeLine(0, dbimage.getHeight(), dbimage.getWidth(), dbimage.getHeight());
	                    canvas.autosize();
	                }

	                event.setDropCompleted(true);
	            }else{
	                event.setDropCompleted(false);
	            }
	            event.consume();
	        }
	    });
	}
	
}
