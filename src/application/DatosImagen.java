package application;

import javafx.scene.image.Image;

public class DatosImagen {
	private static int nImagenes = 0;
	
	String titulo;
	Image imagen;
	int size;
	Histograma histograma;
	HistogramaAcumulativo hAcumulativo;
	int grisMin;
	int grisMax;
	float brillo;
	float contraste;
	float entropia;

	
	public DatosImagen(Image img) {
		this.titulo = "Imagen " + ++nImagenes;
		imagen = img;
		update();
	}
	
	public void update() {
		calcularSize();
		histograma = new Histograma(imagen);
		hAcumulativo = new HistogramaAcumulativo(histograma);
		calcularRangoValores();
		calcularBrillo();
		calcularContraste();
		calcularEntropia();
	}
	
	public void calcularSize() {
		size = 0;
		for(int i = 0; i < imagen.getWidth(); i++) {
			for(int j = 0; j < imagen.getHeight(); j++) {
				if(MainController.argbToA(imagen.getPixelReader().getArgb(i, j)) != 0)
					size++;
			}
		}
	}
	
	public void calcularRangoValores() {
		int min = 255;
		int i = 0;
		while(i <= 255) {
			if(histograma.getNumPixels(i) > 0) {
				min = i;
				break;
			}
			i++;
		}
		grisMin = min;
			
		int max = 0;
		i = 255;
		while(i >= 0) {
			if(histograma.getNumPixels(i) > 0) {
				max = i;
				break;
			}
			i--;
		}
		grisMax = max;
	}
	
	public void calcularBrillo() {
		float suma = 0;
		for(int i = 0; i <= 255; i++) {
			suma += histograma.getNumPixels(i) * i;
		}
		
		brillo = suma / size;
	}
	
	public void calcularContraste() {
		float suma = 0;
		for(int i = 0; i <= 255; i++) {
			suma += histograma.getNumPixels(i) * Math.pow(i - brillo, 2);
		}
		
		contraste = (float) Math.sqrt(suma / size);
	}
	
	public void calcularEntropia() {
		double suma = 0;
		double p;
		for(int i = 0; i <= 255; i++) {
			p = histograma.getNumPixels(i) / (double) size;
			if(p != 0)
				suma += p * (Math.log(p) / Math.log(2));
		}
		
		entropia = (float) - suma;
	}
	
	public String toString() {
		String cadena = titulo + ":\n";
		cadena = "Size: " + size + "\nHistograma: " + histograma + "\nHistograma acumulativo: " + hAcumulativo;
		cadena += "Rango de valores: " + grisMin + " - " + grisMax;
		cadena += "\nBrillo: " + brillo + "\nContraste: " + contraste + "\nEntropia: " + entropia + "\n";
		return cadena;
	}
}
