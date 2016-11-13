package javafx.model;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritablePixelFormat;

public class ImageObject extends CanvasObject {

	private Image img;
	
	public ImageObject(GraphicsContext gc, double x, double y, Image img) { super(gc); setX(x); setY(y); this.img = img; setWidth(img.getWidth()); setHeight(img.getHeight()); }
	
	public Image getImage() { return img; }
	
	@Override
	public void draw() {
//		System.out.println();
		toString();
		getGraphics().drawImage(img, getX(), getY());
		
		if(isSelected())
			drawSelected(getX(), getY(), img.getWidth(), img.getHeight());
	}
	
	@Override
	public String toString() {
		/*
		PixelReader pixelReader = img.getPixelReader();
		WritablePixelFormat<ByteBuffer> pixform = PixelFormat.getByteBgraInstance();
		int width = (int)img.getWidth();
		int height = (int)img.getHeight();
		byte[] buffer = new byte[width * height * 4];
		pixelReader.getPixels(
		        0,
		        0,
		        width,
		        height,
		        pixform,
		        buffer,
		        0,
		        width * 4
		);
		
		/*
		 * TODO: THIS IS HOW TO RECREATE THE IMAGES FROM THE BYTES!
		WritableImage image = new WritableImage(width, height);
		
		PixelWriter pixw = image.getPixelWriter();
		pixw.setPixels(0, 0, width, height, pixform, buffer, 0, width * 4);
		
		String bufferString = "[";
		System.out.println(buffer.length);
		for(int i = 0; i < buffer.length; i++)
			if(i < (buffer.length - 1))
				bufferString += buffer[i] + ",";
			else
				bufferString += buffer[i] + "]";
		System.out.println("Done looping!");
		*/
		
		PixelReader pixelReader = img.getPixelReader();
		WritablePixelFormat<IntBuffer> pixform = PixelFormat.getIntArgbPreInstance();
		int width = (int)img.getWidth();
		int height = (int)img.getHeight();
		int[] buffer = new int[width * height];
		pixelReader.getPixels(
		        0,
		        0,
		        width,
		        height,
		        pixform,
		        buffer,
		        0,
		        width
		);
		
//		ArrayList<String> list = new ArrayList<>();
		StringBuilder stringb = new StringBuilder();
		for(int i = 0; i < buffer.length; i++)
			if(i < (buffer.length - 1))
				stringb.append(buffer[i] + ",");
			else
				stringb.append(buffer[i] + "]");
		
		return super.toString() + stringb;
	}
}
