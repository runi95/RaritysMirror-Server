package javafx.model;

public class Slide {
	String name = "default";
	CanvasObjectList canvas = new CanvasObjectList();
	
	public Slide(String name) {
		this.name = name;
	}
	
	public void setName(String name) { this.name = name; }
	public String getName() { return name; }
	public CanvasObjectList getCanvasObjectList()  { return canvas; }
	
}
