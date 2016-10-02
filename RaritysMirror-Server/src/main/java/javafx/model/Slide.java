package javafx.model;

public class Slide {
	String name = "default";
	
	public Slide(String name) {
		this.name = name;
	}
	
	public void setName(String name) { this.name = name; }
	public String getName() { return name; }
	
}
