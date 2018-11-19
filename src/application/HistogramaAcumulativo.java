package application;

import java.util.ArrayList;


public class HistogramaAcumulativo {
	ArrayList<Integer> numPixels;
	
	public HistogramaAcumulativo(Histograma h) {
		numPixels = new ArrayList<Integer>();
		
		int suma = 0;
		for(int i = 0; i < h.numPixels.size(); i++) {
			suma += h.getNumPixels(i);
			numPixels.add(suma);
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
