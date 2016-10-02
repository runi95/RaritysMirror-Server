package javafx.model;

import java.util.Iterator;
import java.util.LinkedList;

public class CanvasObjectList {

	private LinkedList<CanvasObject> list = new LinkedList<>();
	private CanvasObject selected = null;

	public CanvasObjectList() {
		// Create empty list.
	}

	public void deselectAllBut(CanvasObject co) {
		selected = co;
		for (CanvasObject c : list) {
			if (c.equals(co))
				c.setSelected(true);
			else
				c.setSelected(false);
		}
	}

	public CanvasObject selectFirstInHitbox(double x, double y) {
		CanvasObject ret = null;
		Iterator<CanvasObject> iterator = list.descendingIterator();
		
		while(iterator.hasNext() && ret == null) {
			CanvasObject c = iterator.next();
			if(c.hitboxCheck(x, y))
				ret = c;
		}

		deselectAllBut(ret);

		return ret;
	}

	public CanvasObject getSelected() {
		return selected;
	}
	
	public void setSelected(CanvasObject selected) {
		this.selected = selected;
	}
	
	public LinkedList<CanvasObject> getList() {
		return list;
	}
}
