package javafx.model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;

public class CanvasObject {
	public final static double ARC_RADIUS = 6;
	private double x, y, width, height, clickX, clickY;
	private boolean selected;
	private GraphicsContext gc;
	
	public CanvasObject(GraphicsContext gc) {
		this.gc = gc;
	}
	
	public void draw() { }
	public boolean hitboxCheck(double x, double y) { return (((x > this.x) && (y > this.y)) && ((x < (this.x + getWidth())) && (y < (this.y + getHeight())))); }
	
	public void moveTo(double x, double y) { this.x = x; this.y = y; }
	public void setX(double x) { this.x = x; }
	public void setY(double y) { this.y = y; }
	public void setWidth(double width) { this.width = width; }
	public void setHeight(double height) { this.height = height; }
	public void select() { selected = true; }
	public void deselect() { selected = false; }
	
	public GraphicsContext getGraphics() { return gc; }
	public double getWidth() { return width; }
	public double getHeight() { return height; }
	public double getX() { return x; }
	public double getY() { return y; }
	public double getClickX() { return clickX; }
	public double getClickY() { return clickY; }
	public boolean isSelected() { return selected; }
	
	public void keyPressed(KeyEvent keyevent) { }
	public void mousePressed(double x, double y) { clickX = x - getX(); clickY = y - getY(); }
	public void mouseDragged(double x, double y) { setX(x - getClickX()); setY(y - getClickY()); }
	
	public void drawSelected(double posX, double posY, double width, double height) {
		getGraphics().setStroke(Color.color(0.1, 0.40, 0.8));
		getGraphics().strokeLine(posX, posY, posX + width, posY);
		getGraphics().strokeLine(posX, posY, posX, posY + height);
		getGraphics().strokeLine(posX + width, posY + height, posX, posY + height);
		getGraphics().strokeLine(posX + width, posY + height, posX + width, posY);
		getGraphics().setFill(Color.color(0.2, 0.6, 0.9));
		getGraphics().fillArc(posX - 3, posY - 3, ARC_RADIUS, ARC_RADIUS, 0, 360, ArcType.ROUND);
		getGraphics().fillArc(posX - 3, posY + height - 3, ARC_RADIUS, ARC_RADIUS, 0, 360, ArcType.ROUND);
		getGraphics().fillArc(posX + width - 3, posY - 3, ARC_RADIUS, ARC_RADIUS, 0, 360, ArcType.ROUND);
		getGraphics().fillArc(posX + width - 3, posY + height - 3, ARC_RADIUS, ARC_RADIUS, 0, 360, ArcType.ROUND);
	}
}
