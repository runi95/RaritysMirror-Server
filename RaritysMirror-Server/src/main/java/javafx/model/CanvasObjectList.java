package javafx.model;

import java.util.LinkedList;

public class CanvasObjectList {

	private LinkedList<CanvasObject> list = new LinkedList<>();

	public CanvasObjectList() {
		// Create empty list.
	}

	public void deselectAllBut(CanvasObject co) {
		for (CanvasObject c : list) {
			if (c.equals(co))
				c.setSelected(true);
			else
				c.setSelected(false);
		}
	}

	public CanvasObject selectFirstInHitbox(double x, double y) {
		CanvasObject ret = null;
		for (CanvasObject c : list)
			ret = hitboxLoop(c, x, y);

		if (ret != null)
			deselectAllBut(ret);
		return ret;
	}

	public CanvasObject hitboxLoop(CanvasObject c, double x, double y) {
		if (c.hitboxCheck(x, y))
			return c;
		else
			return null;
	}

	public LinkedList<CanvasObject> getList() {
		return list;
	}
}
