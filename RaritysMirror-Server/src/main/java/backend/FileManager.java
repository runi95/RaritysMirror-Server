package backend;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

import javafx.model.CanvasObject;

public class FileManager {
	
	public void saveFile(String location, ArrayList<String> text) {
		try(BufferedWriter w = new BufferedWriter(new FileWriter(new File(location)))) {
			for(String s : text)
				w.write(s);
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList<String> readFile(String location) {
		ArrayList<String> text = new ArrayList<>();
		
		try(BufferedReader r = new BufferedReader(new FileReader(new File(location)))) {
			String l = null;
			while((l = r.readLine()) != null)
				text.add(l);
		}catch(IOException e) {
			e.printStackTrace();
		}
		return text;
	}
	
	public ArrayList<String> convertCanvasObjectListToText(LinkedList<CanvasObject> canvasList) {
		ArrayList<String> text = new ArrayList<>();
		
		for(CanvasObject c : canvasList) {
			text.add(c.toString());
		}
		
		return text;
	}
	
}
