package application;

import java.util.ArrayList;

import javafx.scene.image.Image;

public class Histograma {
	ArrayList<Integer> numPixels;
	
	public Histograma(Image img) {
		numPixels = new ArrayList<Integer>();
		for(int i = 0; i <= 255; i++)
			numPixels.add(0);
		
		for(int i = 0; i < img.getWidth(); i++) {
			for(int j = 0; j < img.getHeight(); j++) {
				if(MainController.argbToA(img.getPixelReader().getArgb(i, j)) != 0)
						numPixels.set(MainController.argbToGrey(img.getPixelReader().getArgb(i, j)),
								numPixels.get(MainController.argbToGrey(img.getPixelReader().getArgb(i, j))) + 1);
			}
		}
	}
	
	public int getNumPixels(int nivel) {
		return numPixels.get(nivel);
	}
	
	public String toString() {
		String cadena = "";
		for(int i = 0; i <= 255; i++)
			cadena += i + " " + numPixels.get(i) + "\n";
		return cadena;
	}
}
