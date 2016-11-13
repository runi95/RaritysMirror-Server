package javafx.view;

import java.io.File;

import javafx.beans.value.ChangeListener;
import javafx.event.EventHandler;
import javafx.model.CanvasObject;
import javafx.model.CanvasObjectList;
import javafx.model.ImageObject;
import javafx.model.TextObject;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.text.Font;

public class CustomCanvas extends Canvas {
	
	CanvasObjectList currentSlide;
	String font;
	int fontSize;
	
	public CustomCanvas() {
		setOnKeyPressed(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent event) {
				if(currentSlide.getList().isEmpty())
					return;
				
				CanvasObject selectedObject = currentSlide.getSelected();
				
				if(selectedObject == null)
					return;
				
				if(event.getCode() == KeyCode.DELETE)
					currentSlide.getList().remove(selectedObject);
				else
					selectedObject.keyPressed(event);
				
				event.consume();
				
				draw();
			}
		});

		setOnMousePressed(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				if(currentSlide.getList().isEmpty())
					return;
				
				double x = event.getX(), y = event.getY();
				CanvasObject selected = currentSlide.selectFirstInHitbox(x, y);
				
				if(selected != null) {
					selected.mousePressed(x, y);
					requestFocus();
				}
				
				event.consume();
				draw();
			}
		});

		setOnMouseDragged(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				CanvasObject selected = currentSlide.getSelected();
				if(selected != null) {
					double x = event.getX(), y = event.getY();
					selected.mouseDragged(x, y);
					event.consume();
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
		
		font = Font.getDefault().getName();
		fontSize = 20;
		getGraphicsContext2D().setFont(new Font(font, fontSize));
	}

	public void addListener(ChangeListener<CanvasObject> listener) {
		currentSlide.addListener(listener);
	}
	
	public Font getFont() {
		return getGraphicsContext2D().getFont();
	}
	
	public void setCurrentSlide(CanvasObjectList currentSlide) {
		this.currentSlide = currentSlide;
		draw();
	}
	
	public CanvasObjectList getCurrentSlide() {
		return currentSlide;
	}
	
	public void addText(String text, String font, int fontSize) {
		CanvasObject c = new TextObject(getGraphicsContext2D(), 0, 0, text, font, fontSize);
		
		currentSlide.getList().add(c);
		
		currentSlide.deselectAllBut(c);
		draw();
		
		requestFocus();
	}
	
	public void addImage(Image img) {
		CanvasObject c = new ImageObject(getGraphicsContext2D(), 0, 0, img);
		
		currentSlide.getList().add(c);
		
		currentSlide.deselectAllBut(c);
		draw();
	}
	
	public void setFont(String font, int fontSize) {
		if(currentSlide.getSelected() != null && currentSlide.getSelected() instanceof TextObject) {
			((TextObject)currentSlide.getSelected()).setFont(new Font(font, fontSize));
			draw();
		}
	}
	
	private void draw() {
		getGraphicsContext2D().clearRect(0, 0, getWidth(), getHeight());
		
		if (currentSlide.getList().isEmpty())
			return;

		for(CanvasObject c : currentSlide.getList())
			c.draw();
	}
}
