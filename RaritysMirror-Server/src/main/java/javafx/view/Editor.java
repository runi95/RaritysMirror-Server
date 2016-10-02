package javafx.view;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.Resources;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.model.CanvasObject;
import javafx.model.CanvasObjectList;
import javafx.model.Slide;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.scene.text.Font;

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
	Canvas canvas;
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

		sliderTable.setItems(slideList);

		// Initialize Canvas
		gc = canvas.getGraphicsContext2D();
		
		scroll.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if(newValue)
					canvas.requestFocus();
			}
		});
		
		canvas.setOnKeyPressed(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent event) {
				if(event.getCode() == KeyCode.DELETE) {
					canvasList.getList().remove(selected);
					selected = null;
					draw();
				}
			}
		});

		canvas.setOnMousePressed(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				// TODO: Make sure you can select elements on the canvas!
//				drawString("The Quick Brown Fox Jumped The White Fence", event.getX(), event.getY(), true);
				if(canvasList.getList().isEmpty())
					return;
				
				double x = event.getX(), y = event.getY();
				selected = canvasList.selectFirstInHitbox(x, y);
				
				if(selected != null) {
					selected.setSelectedX(x - selected.getX());
					selected.setSelectedY(y - selected.getY());
				}
				draw();
			}
		});

		canvas.setOnMouseDragged(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				if(selected != null) {
					double x = event.getX(), y = event.getY();
					selected.setX(x - selected.getSelectedX());
					selected.setY(y - selected.getSelectedY());
					draw();
				}
			}
		});

		canvas.setOnDragOver(new EventHandler<DragEvent>() {
			public void handle(DragEvent event) {
				Dragboard db = event.getDragboard();
//				 System.out.println(db.hasFiles() + " || " + db.hasHtml() + " || " + db.hasImage() + " || " + db.hasRtf() + " || " +
//				 db.hasString() + " || " + db.hasUrl());
//				 System.out.println(db.getString());
				if (db.hasFiles() || db.hasUrl()) {
					event.acceptTransferModes(TransferMode.ANY);
				}

				event.consume();
			}
		});

		canvas.setOnDragDropped(new EventHandler<DragEvent>() {
			public void handle(DragEvent event) {
				Dragboard db = event.getDragboard();

				if (db.hasHtml()) {
					String imageUrl = db.getHtml();

					if (imageUrl.startsWith("<img")) {
						imageUrl = imageUrl.substring(imageUrl.indexOf("src=\"") + 5);
						imageUrl = imageUrl.substring(0, imageUrl.indexOf('"'));
						Image dbimage = new Image(imageUrl);

						addImage(dbimage);
					}
				}else if (db.hasFiles()) {

					for (File file : db.getFiles()) {
						String absolutePath = file.toURI().toString();
						Image dbimage = new Image(absolutePath);

						addImage(dbimage);
					}

					event.setDropCompleted(true);
				} else {
					event.setDropCompleted(false);
				}
				event.consume();
			}
		});
	}

	private void addImage(Image img) {
		CanvasObject c = new CanvasObject(0, 0, img);
		
		canvasList.getList().add(c);
		
		selected = c;
		canvasList.deselectAllBut(c);
		draw();
	}
	
	private void draw() {
		gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
		
		if (canvasList.getList().isEmpty())
			return;

		for (CanvasObject c : canvasList.getList()) {
			if (c.getImage() != null)
				drawImage((Image) c.getImage(), c.getX(), c.getY(), c.isSelected());
			
			if (c.getText() != null) {
				drawString((String) c.getText(), c.getX(), c.getY(), c.getSize(), c.isSelected());
			}
		}
	}
	
	private void drawString(String s, double x, double y, double size, boolean select) {
		gc.setFont(new Font(size));
		gc.setStroke(Color.color(0.0, 0.0, 0.0));
		gc.strokeText(s, x, y + gc.getFont().getSize());
		
		if(select)
			drawSelected(x, y, s.length()*(size/2), size);
	}

	private void drawImage(Image dbimage, double x, double y, boolean select) {
		gc.drawImage(dbimage, x, y);
		
		if(select)
			drawSelected(x, y, dbimage.getWidth(), dbimage.getHeight());
			
	}
	
	private void drawSelected(double posX, double posY, double width, double height) {
		gc.setStroke(Color.color(0.1, 0.40, 0.8));
		gc.strokeLine(posX, posY, posX + width, posY);
		gc.strokeLine(posX, posY, posX, posY + height);
		gc.strokeLine(posX + width, posY + height, posX, posY + height);
		gc.strokeLine(posX + width, posY + height, posX + width, posY);
		gc.setFill(Color.color(0.2, 0.6, 0.9));
		gc.fillArc(posX - 3, posY - 3, 6, 6, 0, 360, ArcType.ROUND);
		gc.fillArc(posX - 3, posY + height - 3, 6, 6, 0, 360, ArcType.ROUND);
		gc.fillArc(posX + width - 3, posY - 3, 6, 6, 0, 360, ArcType.ROUND);
		gc.fillArc(posX + width - 3, posY + height - 3, 6, 6, 0, 360, ArcType.ROUND);
	}
	
	private void addSlide() {
		slideList.add(new Slide("Slide #" + (slideList.size() + 1)));
	}
	
	private void removeSlide() {
		slideList.removeAll(sliderTable.getSelectionModel().getSelectedItems());
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
