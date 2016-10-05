package javafx.view;

import java.io.File;

import javafx.event.EventHandler;
import javafx.model.CanvasObject;
import javafx.model.CanvasObjectList;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.scene.text.Font;

public class CustomCanvas extends Canvas {
	
	CanvasObjectList currentSlide;
	
	public CustomCanvas() {
		setOnKeyPressed(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent event) {
				CanvasObject selectedObject = currentSlide.getSelected();
				
				if(selectedObject == null)
					return;
				
				if(event.getCode() == KeyCode.DELETE) {
					currentSlide.getList().remove(selectedObject);
					draw();
				}else if(selectedObject.getText() != null) {
					if(event.getCode() == KeyCode.RIGHT|| event.getCode() == KeyCode.KP_RIGHT)
						selectedObject.movePointer(true);
					else if(event.getCode() == KeyCode.LEFT || event.getCode() == KeyCode.KP_LEFT)
						selectedObject.movePointer(false);
					else if(event.getCode() == KeyCode.BACK_SPACE)
						selectedObject.removeChar(selectedObject.getText().length() - 1);
					else if(event.getCode().isLetterKey() || event.getCode().isDigitKey())
						selectedObject.appendText(event.getText());
					
					draw();
				}
				
				event.consume();
			}
		});

		setOnMousePressed(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				if(currentSlide.getList().isEmpty())
					return;
				
				double x = event.getX(), y = event.getY();
				CanvasObject selected = currentSlide.selectFirstInHitbox(x, y);
				
				if(selected != null) {
					selected.setSelectedX(x - selected.getX());
					selected.setSelectedY(y - selected.getY());
				}
				draw();
			}
		});

		setOnMouseDragged(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				CanvasObject selected = currentSlide.getSelected();
				if(selected != null) {
					double x = event.getX(), y = event.getY();
					selected.setX(x - selected.getSelectedX());
					selected.setY(y - selected.getSelectedY());
					draw();
				}
			}
		});

		setOnDragOver(new EventHandler<DragEvent>() {
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

		setOnDragDropped(new EventHandler<DragEvent>() {
			public void handle(DragEvent event) {
				Dragboard db = event.getDragboard();

				if (db.hasHtml()) {
					String imageUrl = db.getHtml();
					
					if (imageUrl.startsWith("<img")) {
						imageUrl = imageUrl.substring(imageUrl.indexOf("src=\"") + 5);
						imageUrl = imageUrl.substring(0, imageUrl.indexOf('"'));
						
					}else
						imageUrl = db.getUrl();
					
					Image dbimage = new Image(imageUrl);

					addImage(dbimage);
					
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

	public void setCurrentSlide(CanvasObjectList currentSlide) {
		this.currentSlide = currentSlide;
		draw();
	}
	
	public CanvasObjectList getCurrentSlide() {
		return currentSlide;
	}
	
	public void addText(String text) {
		CanvasObject c = new CanvasObject(0, 0, text, 20);
		
		currentSlide.getList().add(c);
		
		currentSlide.deselectAllBut(c);
		draw();
		
		requestFocus();
	}
	
	public void addImage(Image img) {
		CanvasObject c = new CanvasObject(0, 0, img);
		
		currentSlide.getList().add(c);
		
		currentSlide.deselectAllBut(c);
		draw();
	}
	
	private void draw() {
		getGraphicsContext2D().clearRect(0, 0, getWidth(), getHeight());
		
		if (currentSlide.getList().isEmpty())
			return;

		for (CanvasObject c : currentSlide.getList()) {
			if (c.getImage() != null)
				drawImage((Image) c.getImage(), c.getX(), c.getY(), c.isSelected());
			
			if (c.getText() != null) {
				drawString((String) c.getText(), c.getX(), c.getY(), c.getSize(), c.getPointerPosition(), c.isSelected());
			}
		}
	}
	
	private void drawString(String s, double x, double y, double size, int pointer, boolean select) {
		getGraphicsContext2D().setFont(new Font(size));
		getGraphicsContext2D().setStroke(Color.color(0.0, 0.0, 0.0));
		getGraphicsContext2D().strokeText(s, x, y + getGraphicsContext2D().getFont().getSize());
		
		getGraphicsContext2D().strokeLine(pointer*(size/2), y, pointer*(size/2), y + size);
		
		if(select)
			drawSelected(x, y, s.length()*(size/2), size);
	}

	private void drawImage(Image dbimage, double x, double y, boolean select) {
		getGraphicsContext2D().drawImage(dbimage, x, y);
		
		if(select)
			drawSelected(x, y, dbimage.getWidth(), dbimage.getHeight());
			
	}
	
	private void drawSelected(double posX, double posY, double width, double height) {
		getGraphicsContext2D().setStroke(Color.color(0.1, 0.40, 0.8));
		getGraphicsContext2D().strokeLine(posX, posY, posX + width, posY);
		getGraphicsContext2D().strokeLine(posX, posY, posX, posY + height);
		getGraphicsContext2D().strokeLine(posX + width, posY + height, posX, posY + height);
		getGraphicsContext2D().strokeLine(posX + width, posY + height, posX + width, posY);
		getGraphicsContext2D().setFill(Color.color(0.2, 0.6, 0.9));
		getGraphicsContext2D().fillArc(posX - 3, posY - 3, 6, 6, 0, 360, ArcType.ROUND);
		getGraphicsContext2D().fillArc(posX - 3, posY + height - 3, 6, 6, 0, 360, ArcType.ROUND);
		getGraphicsContext2D().fillArc(posX + width - 3, posY - 3, 6, 6, 0, 360, ArcType.ROUND);
		getGraphicsContext2D().fillArc(posX + width - 3, posY + height - 3, 6, 6, 0, 360, ArcType.ROUND);
	}
}
