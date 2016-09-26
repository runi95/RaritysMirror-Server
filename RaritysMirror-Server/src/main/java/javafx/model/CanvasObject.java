package javafx.model;

import javafx.scene.image.Image;

public class CanvasObject {

	private double x, y, width, height;
	private Object n;
	private int size;
	private CanvasObject next = null, previous = null, tail = this;
	private boolean selected = false;

	public CanvasObject(String s, double x, double y, int size) {
		// this.head = root;
		this.n = s;
		this.x = x;
		this.y = y;
		this.size = size;

		width = s.length() * (size / 2);
		height = size;
	}

	public CanvasObject(Image img, double x, double y) {
		// this.head = root;
		this.n = img;
		this.x = x;
		this.y = y;
		width = img.getWidth();
		height = img.getHeight();
	}

	public void setX(double x) {
		this.x = x;
	}

	public void setY(double y) {
		this.y = y;
	}

	public void setPrevious(CanvasObject previous) {
		this.previous = previous;
	}
	
	public void add(CanvasObject n) {
		tail = n;
		if (next == null) {
			n.setPrevious(this);
			next = n;
		}else
			next.add(n);
	}

	public void setSelected(boolean b) {
		selected = b;
	}

	public void deselectAllBut(CanvasObject co) {
		if (this.equals(co))
			setSelected(true);
		else
			setSelected(false);
		
		if (next() != null)
			next().deselectAllBut(co);
	}

	public CanvasObject selectFirstInHitbox(double x, double y) {
		CanvasObject ret = tail.hitboxLoop(x, y);
		deselectAllBut(ret);
		return ret;
	}

	public CanvasObject hitboxLoop(double x, double y) {
		if (this.hitboxCheck(x, y))
			return this;
		else
			setSelected(false);
		
		if (previous() != null) 
			return previous().hitboxLoop(x, y);
		
		return null;
	}

	public boolean hitboxCheck(double x, double y) {
		return (((x > this.x) && (y > this.y)) && ((x < (this.x + width)) && (y < (this.y + height))));
	}

	public boolean isSelected() {
		return selected;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getWidth() {
		return width;
	}

	public double getHeight() {
		return height;
	}

	public int getSize() {
		return size;
	}

	public Object getNode() {
		return n;
	}

	public CanvasObject next() {
		return next;
	}

	public CanvasObject previous() {
		return previous;
	}
	
	@Override
	public String toString() {
		return Double.toString(((Image) n).getWidth());
	}
}
