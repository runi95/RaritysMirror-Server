package javafx.model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class ImageObject extends CanvasObject {

	private Image img;
	
	public ImageObject(GraphicsContext gc, double x, double y, Image img) { super(gc); setX(x); setY(y); this.img = img; setWidth(img.getWidth()); setHeight(img.getHeight()); }
	
	public Image getImage() { return img; }
	
	@Override
	public void draw() {
		getGraphics().drawImage(img, getX(), getY());
		
		if(isSelected())
			drawSelected(getX(), getY(), img.getWidth(), img.getHeight());
	}
}
