package javafx.model;

import java.util.Iterator;
import java.util.LinkedList;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;

public class CanvasObjectList {

	private LinkedList<CanvasObject> list = new LinkedList<>();
	private final ObjectProperty<CanvasObject> objectProperty = new SimpleObjectProperty<CanvasObject>(this, "selected", null);

	public CanvasObjectList() {
		// Create empty list.
	}
	
	public void addListener(ChangeListener<CanvasObject> listener) {
		objectProperty.addListener(listener);
	}

	public void deselectAllBut(CanvasObject co) {
		objectProperty.setValue(co);
		for (CanvasObject c : list) {
			if (c.equals(co))
				c.select();
			else
				c.deselect();
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
		if(objectProperty.getValue() != null && !objectProperty.getValue().isSelected())
			objectProperty.setValue(null);
		
		return objectProperty.getValue();
	}
	
	public void setSelected(CanvasObject selected) {
		objectProperty.setValue(selected);
	}
	
	public LinkedList<CanvasObject> getList() {
		return list;
	}
}
