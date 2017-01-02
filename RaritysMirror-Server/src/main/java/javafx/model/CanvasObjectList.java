package javafx.model;

import java.util.LinkedList;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CanvasObjectList {

	private ObservableList<CanvasObject> list = FXCollections.observableList(new LinkedList<>());
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
		for(int i = list.size() - 1; i >= 0; i--)
			if(list.get(i).hitboxCheck(x,y)) {
				ret = list.get(i);
				break;
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
	
	public ObservableList<CanvasObject> getList() {
		return list;
	}
}
