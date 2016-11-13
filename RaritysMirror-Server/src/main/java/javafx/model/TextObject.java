package javafx.model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class TextObject extends CanvasObject {
	
	private String text;
	private Font font;
	private int pointerPosition;
	
	public TextObject(GraphicsContext gc, double x, double y, String text) { super(gc); setX(x); setY(y); this.text = text; font = Font.getDefault(); }
	public TextObject(GraphicsContext gc, double x, double y, String text, Font font) { super(gc); setX(x); setY(y); this.text = text; this.font = font; }
	public TextObject(GraphicsContext gc, double x, double y, String text, String fontName, int fontSize) { super(gc); setX(x); setY(y); this.text = text; font = new Font(fontName, fontSize); }
	
	public void setFont(Font font) { this.font = font; }
	public void setText(String text) { this.text = text; }
	public void removeChar(int index) {
		if(text == null)
			return;
		
		if(index < 0 || index >= text.length())
			return;
		
		text = text.substring(0, index) + text.substring(index + 1, text.length());
		
		setPointerPosition(getPointerPosition() - 1);
	}
	public void appendText(String c) {
		if(text == null)
			return;
		
		text = text.substring(0, getPointerPosition()) + c + text.substring(getPointerPosition());
		
		setPointerPosition(getPointerPosition() + 1);
	}
	public Font getFont() { return font; }
	public String getText() { return text; }
	public int getPointerPosition() { return pointerPosition; }
	
	@Override
	public void draw() {
		getGraphics().setFont(font);
		getGraphics().setStroke(Color.color(0.0, 0.0, 0.0));
		getGraphics().strokeText(text, getX(), getY() + font.getSize());
		
		 if(isSelected()) {
			Text t = new Text(text.substring(0,pointerPosition));
			t.setFont(getFont());
			double width = t.getLayoutBounds().getWidth(), height = t.getLayoutBounds().getHeight();
			
			getGraphics().strokeLine(getX() + width, getY(), getX() + width, getY() + height);
			
			Text t2 = new Text(text);
			t2.setFont(getFont());
			double width2 = t2.getLayoutBounds().getWidth();
			
			drawSelected(getX(), getY(), width2, height);
		}
	}
	
	@Override
	public void keyPressed(KeyEvent keyevent) {
		KeyCode keycode = keyevent.getCode();
		if(keycode == KeyCode.RIGHT|| keycode == KeyCode.KP_RIGHT)
			setPointerPosition(getPointerPosition() + 1);
		else if(keycode == KeyCode.LEFT || keycode == KeyCode.KP_LEFT)
			setPointerPosition(getPointerPosition() - 1);
		else if(keycode == KeyCode.BACK_SPACE)
			removeChar(getPointerPosition() - 1);
		else if (keycode == KeyCode.ENTER)
			deselect();
		else if(keycode.isLetterKey() || keycode.isDigitKey() || keycode.isWhitespaceKey())
			appendText(keyevent.getText());
	}
	
	@Override
	public double getWidth() {
		Text t = new Text(text);
		t.setFont(getFont());
		
		return t.getLayoutBounds().getWidth();
	}
	
	@Override
	public double getHeight() {
		Text t = new Text(text);
		t.setFont(font);
		
		return t.getLayoutBounds().getHeight();
	}
	
	private void setPointerPosition(int i) {
		if(i <= text.length() && i >= 0)
			pointerPosition = i;
	}
	
	public Color getColor() {
		return Color.BLACK;
	}
	
	@Override
	public String toString() {
		return super.toString() + " f(" + font.getName() + "," + font.getSize() + ") c(" + getColor() + ")";
	}
}
