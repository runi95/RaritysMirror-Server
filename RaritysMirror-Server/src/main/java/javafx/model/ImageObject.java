package javafx.model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;

public class ImageObject extends CanvasObject {

	private Image img;
	private GraphicsContext gc;
	
	public ImageObject(GraphicsContext gc, double x, double y, Image img) { this.gc = gc; setX(x); setY(y); this.img = img; setWidth(img.getWidth()); setHeight(img.getHeight()); }
	
	public Image getImage() { return img; }
	public GraphicsContext getGraphics() { return gc; }
	
	@Override
	public void draw() {
		getGraphics().drawImage(img, getX(), getY());
		
		if(isSelected())
			drawSelected(getX(), getY(), img.getWidth(), img.getHeight());
	}
	
	private void drawSelected(double posX, double posY, double width, double height) {
		getGraphics().setStroke(Color.color(0.1, 0.40, 0.8));
		getGraphics().strokeLine(posX, posY, posX + width, posY);
		getGraphics().strokeLine(posX, posY, posX, posY + height);
		getGraphics().strokeLine(posX + width, posY + height, posX, posY + height);
		getGraphics().strokeLine(posX + width, posY + height, posX + width, posY);
		getGraphics().setFill(Color.color(0.2, 0.6, 0.9));
		getGraphics().fillArc(posX - 3, posY - 3, 6, 6, 0, 360, ArcType.ROUND);
		getGraphics().fillArc(posX - 3, posY + height - 3, 6, 6, 0, 360, ArcType.ROUND);
		getGraphics().fillArc(posX + width - 3, posY - 3, 6, 6, 0, 360, ArcType.ROUND);
		getGraphics().fillArc(posX + width - 3, posY + height - 3, 6, 6, 0, 360, ArcType.ROUND);
	}
}
