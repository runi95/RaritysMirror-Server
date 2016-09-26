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
import javafx.model.CanvasObject;
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
import javafx.scene.shape.ArcType;
import javafx.scene.text.Font;

public class Editor extends BorderPane implements Initializable {

	ObservableList<Slide> sliderList = FXCollections.observableArrayList();
	GraphicsContext gc;

	CanvasObject co = null;
	CanvasObject selected = null;

	@FXML
	TableView<Slide> sliderTable;
	@FXML
	Canvas canvas;

	public Editor() {
		Resources.loadFXML(this);
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

		canvas.setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				// TODO: Make sure you can select elements on the canvas!
//				drawString("The Quick Brown Fox Jumped The White Fence", event.getX(), event.getY(), true);
				if(co == null)
					return;
				
				double x = event.getX(), y = event.getY();
				selected = co.selectFirstInHitbox(x, y);
				draw();
			}
		});

		canvas.setOnMouseDragged(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				if(selected != null) {
					double x = event.getX(), y = event.getY();
					selected.setX(x);
					selected.setY(y);
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
		CanvasObject c = new CanvasObject(img, 0, 0);
		
		if(co == null)
			co = c;
		else
			co.add(c);
		
		selected = c;
		co.deselectAllBut(c);
		draw();
	}
	
	private void draw() {
		if (co == null)
			return;

		gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
		CanvasObject c = co;
		while (c != null) {
			if (c.getNode() instanceof Image)
				drawImage((Image) c.getNode(), c.getX(), c.getY(), c.isSelected());
			
			if (c.getNode() instanceof String) {
				drawString((String) c.getNode(), c.getX(), c.getY(), c.getSize(), c.isSelected());
			}
			
			c = c.next();
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
}
