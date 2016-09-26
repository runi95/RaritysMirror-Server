package javafx.model;

import javafx.scene.image.Image;

public class CanvasObject {
	private double x, y, width, height;
	private int size;
	private boolean selected;
	private Image img = null;
	private String s = null;

	public CanvasObject(double x, double y, Image img) {
		this.x = x;
		this.y = y;
		this.img = img;
		
		width = img.getWidth();
		height = img.getHeight();
	}

	public CanvasObject(double x, double y, String s, int size) {
		this.x = x;
		this.y = y;
		this.s = s;
		this.size = size;
		
		width = s.length() * (size / 2);
		height = size;
	}
	
	public boolean hitboxCheck(double x, double y) {
		return (((x > this.x) && (y > this.y)) && ((x < (this.x + width)) && (y < (this.y + height))));
	}
	
	public void setX(double x) {
		this.x = x;
	}
	
	public void setY(double y) {
		this.y = y;
	}
	
	public void setSelected(boolean b) {
		selected = b;
	}
	
	public double getSize() {
		return size;
	}
	
	public boolean isSelected() {
		return selected;
	}
	
	public double getWidth() {
		return width;
	}
	
	public double getHeight() {
		return height;
	}
	
	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public Image getImage() {
		return img;
	}

	public String getText() {
		return s;
	}
}
