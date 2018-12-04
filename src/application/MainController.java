package application;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;

import com.sun.istack.internal.logging.Logger;

	
/**
 * Controlador de la ventana principal donde se abre la imagen en otra ventana y se puede escoger donde guardarla
 *
 */
public class MainController { //esto permite usar el objeto en el scene Builder
	
	static DatosImagen datosImagen;  //DATOS DE LA IMAGEN QUE ABRIMOS, SE GUARDAN AQUI COMO ESTATICOS PARA TRABAJAR CON ELLOS.
	static ArrayList<DatosImagen> imagenes = new ArrayList<DatosImagen>(); //por si creamos muchas imagenes
	
	@FXML
	Label auxLabel;
	@FXML
	TextField auxTextField;
	@FXML
	Button auxButton;
	
	/**
	 * @param event evento que se manda al boton para abrir la nueva ventana y el 2º Controller con la imagen dentro
	 * @throws IOException Por si explota
	 */
	public void abrirVentana(ActionEvent event) throws IOException {
		FileChooser fileChooser = new FileChooser();
		FileChooser.ExtensionFilter extFilterTiff = new FileChooser.ExtensionFilter("Archivos tiff", "*.tiff", "*.TIF");
		FileChooser.ExtensionFilter extFilterJPG = new FileChooser.ExtensionFilter("Archivos JPG", "*.jpg");
		fileChooser.getExtensionFilters().addAll(extFilterTiff);
		fileChooser.getExtensionFilters().addAll(extFilterJPG);
		File file = fileChooser.showOpenDialog(null);		
		//////////////////////////// Hasta aquí es para filtrar los jpg, y tiff y poder elegirlos y guardarlos en el file
		try {
			BufferedImage img1 = ImageIO.read(file);
			Image image = SwingFXUtils.toFXImage(img1, null);
			datosImagen = new DatosImagen(image);
			imagenes.add(datosImagen); //metemos la imagen abierta en el arrayList
			/////////////////////////////////// Se lee la imagen como ImageIO y se convierte a Image porque es tiff.

		} catch(IOException ex) {
			System.out.println(ex.getMessage());
		}		
				
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("SecondController.fxml"));
			Parent root = (Parent)loader.load();
			SecondController secController = loader.getController();
			secController.nuevaImagen(datosImagen.imagen);
			secController.mostrarInfo(datosImagen.imagen); // Hacer que el controlador de la imagen muestre la info
			secController.addMainController(this);
			Stage stage = new Stage();
			stage.setScene(new Scene(root));
			stage.show();
			///// Esto es para mandar la imagen elegida a la segunda ventana, se necesita si o si el segundo controlador
		} catch(IOException e) {
			System.out.println(e.getMessage());
		} ////Fin de evento
		for(DatosImagen i: imagenes) {
			System.out.println(i);
		}
	}
	
	/**
	 * @param event	Evento asociado a un botón en el que si le damos se abre un filechooser donde podemos elegir el sitio donde guardar la imagen (por ahora solo en .tiff)
	 * @throws IOException Por si explota
	 */
	public void guardarImagen(ActionEvent event) throws IOException {
		String ruta;
		FileChooser filechooser = new FileChooser();
		filechooser.setTitle("Guardar Imagen");
		File file = filechooser.showSaveDialog(null);
		ruta = file.getAbsolutePath();
		//////////////////////Pillar donde se quiere guardar
		if(!ruta.endsWith(".tiff") && !ruta.endsWith(".TIF")) {
			file = new File(ruta + ".tiff");
			ruta = ruta + ".tiff";
		}
		/////////////////////Si no se le pone la extensión se la pongo yo
		if(file != null) {
			ImageOutputStream ios = ImageIO.createImageOutputStream(file);
			ImageIO.write(SwingFXUtils.fromFXImage(datosImagen.imagen,null), "tiff", ios);
		////////////////////Se guarda la imagen
                    										
		}
	}
	
	public void abrirImagen(Image imagen) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("SecondController.fxml"));
		Parent root = (Parent) loader.load();
		SecondController secController = loader.getController();
		secController.nuevaImagen(imagen);
		secController.mostrarInfo(imagen); // Hacer que el controlador de la imagen muestre la info
		secController.addMainController(this);
		Stage stage = new Stage();
		stage.setScene(new Scene(root));
		stage.show();
		
		datosImagen = new DatosImagen(imagen);
		imagenes.add(datosImagen);
	}
	
	public void abrirMensaje(String texto) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("SimpleMessageController.fxml"));
		Parent root = (Parent) loader.load();
		SimpleMessageController smc = loader.getController();
		smc.setMessage(texto);
		Stage stage = new Stage();
		stage.setScene(new Scene(root));
		stage.show();
	}
	
	public void verInfoBasica(ActionEvent event) throws IOException {
		DecimalFormat df = new DecimalFormat("#.00");
		String info = "Rango de valores: " + datosImagen.grisMin + " - " + datosImagen.grisMax + "\n";
		info += "Brillo: " + df.format(datosImagen.brillo) + "\n";
		info += "Contraste: " + df.format(datosImagen.contraste) + "\n";
		info += "Entropía: " + df.format(datosImagen.entropia) + "\n";
		abrirMensaje(info);
	}
	
	public void mostrarHistograma(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("HistogramController.fxml"));
		Parent root = (Parent) loader.load();
		HistogramController histogramController = loader.getController();
		histogramController.mostrarHistograma(datosImagen.histograma);
		Stage stage = new Stage();
		stage.setScene(new Scene(root));
		stage.show();
	}
	
	public void mostrarHistogramaAcumulativo(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("HistogramController.fxml"));
		Parent root = (Parent) loader.load();
		HistogramController histogramController = loader.getController();
		histogramController.mostrarHistograma(datosImagen.hAcumulativo);
		Stage stage = new Stage();
		stage.setScene(new Scene(root));
		stage.show();
	}
	
	public void escalaDeGrises(ActionEvent event) {
		int width = (int) datosImagen.imagen.getWidth();
		int height = (int) datosImagen.imagen.getHeight();
		PixelReader reader = datosImagen.imagen.getPixelReader();
		
		WritableImage img = new WritableImage(width, height);
		PixelWriter writer = img.getPixelWriter();
		
		int nivelGris;
		for(int i = 0; i < width; i++) {
			for(int j = 0; j < height; j++) {
				nivelGris = argbToGrey(reader.getArgb(i, j));
				writer.setArgb(i, j, rgbToArgb(nivelGris, nivelGris, nivelGris));
			}
		}
		
		try {
			abrirImagen(img);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void transformacionLinealTramos(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("LinealTransformController.fxml"));
		Parent root = (Parent) loader.load();
		LinealTransformController ltc = loader.getController();
		ltc.addMainController(this);
		Stage stage = new Stage();
		stage.setScene(new Scene(root));
		stage.show();
	}
	
	public void aplicarTransformacionLinealTramos(Integer[][] puntos) {
		Integer[] LUT = new Integer[256];
		
		// En principio todos los niveles de gris se mapean igual, donde esté definida la t. lineal cambiará
		for(int i = 0; i <= 255; i++) {
			LUT[i] = i;
		}
		
		double m, n; // Recta = mx + n
		for(int i = 1; i < puntos.length; i++) {
			m = pendienteRecta(puntos[i-1][0], puntos[i-1][1], puntos[i][0], puntos[i][1]);
			n = origenRecta(puntos[i-1][0], puntos[i-1][1], m);
			for(int j = puntos[i-1][0]; j <= puntos[i][0]; j++)
				LUT[j] = (int) Math.round(j * m + n);
		}
		
		aplicarLUT(LUT);
	}
	
	public void cambiarBrilloContraste(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("BrilloContrasteController.fxml"));
		Parent root = (Parent) loader.load();
		BrilloContrasteController bcc = loader.getController();
		bcc.addMainController(this);
		bcc.brilloContrasteActuales(datosImagen.brillo, datosImagen.contraste);
		Stage stage = new Stage();
		stage.setScene(new Scene(root));
		stage.show();
	}
	
	public void actualizarBrilloContraste(float nuevoBrillo, float nuevoContraste) {
		Integer[] LUT = new Integer[256];
		
		// Vout = m * Vin + n
		double m = nuevoContraste / datosImagen.contraste;
		double n = nuevoBrillo - m * datosImagen.brillo;
		
		for(int i = 0; i <= 255; i++) {
			LUT[i] = (int) Math.min(Math.round(m * i + n), 255);
			if(LUT[i] < 0)
				LUT[i] = 0;
		}
		
		aplicarLUT(LUT);
	}
	
	public void ecualizar(ActionEvent event) throws IOException {
		Integer[] LUT = new Integer[256];
		ArrayList<Integer> hAcumulativo = datosImagen.hAcumulativo.numPixels;
		
		for(int i = 0; i <= 255; i++) {
			LUT[i] = Math.max(0, Math.round(256F/datosImagen.size * hAcumulativo.get(i)) - 1);
		}
		
		aplicarLUT(LUT);
	}
	
	public void especificarHistograma(ActionEvent event) throws IOException {
		DatosImagen imagenEscogida = null;
		FileChooser fileChooser = new FileChooser();
		FileChooser.ExtensionFilter extFilterTiff = new FileChooser.ExtensionFilter("Archivos tiff", "*.tiff", "*.TIF");
		FileChooser.ExtensionFilter extFilterJPG = new FileChooser.ExtensionFilter("Archivos JPG", "*.jpg");
		fileChooser.getExtensionFilters().addAll(extFilterTiff);
		fileChooser.getExtensionFilters().addAll(extFilterJPG);
		File file = fileChooser.showOpenDialog(null);		
		try {
			BufferedImage img1 = ImageIO.read(file);
			Image image = SwingFXUtils.toFXImage(img1, null);
			imagenEscogida = new DatosImagen(image);
		}
		catch(IOException ex) {
			System.out.println(ex.getMessage());
		}
		
		Integer[] LUT = new Integer[256];
		ArrayList<Double> hAcActual = new ArrayList<Double>();
		ArrayList<Double> hAcDeseado = new ArrayList<Double>();
		
		// Obtener histogramas normalizados
		for(int i = 0; i <= 255; i++) {
			hAcActual.add((double) datosImagen.hAcumulativo.getNumPixels(i) / datosImagen.size);
			hAcDeseado.add((double) imagenEscogida.hAcumulativo.getNumPixels(i) / imagenEscogida.size);
		}
		
		double numPixels;
		int nivelGris = 0;
		for(int i = 0; i <= 255; i++) {
			numPixels = hAcActual.get(i);
			while(numPixels > hAcDeseado.get(nivelGris)) {
				nivelGris++;
			}
			if(nivelGris == 0)
				LUT[i] = 0;
			else if((hAcDeseado.get(nivelGris) - numPixels) < (numPixels - hAcDeseado.get(nivelGris - 1)))
				LUT[i] = nivelGris;
			else
				LUT[i] = nivelGris - 1;
		}
		
		aplicarLUT(LUT);
	}
	
	public void correccionGamma(ActionEvent event) throws IOException {
		auxLabel.setText("Factor gamma");
		auxButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
	        @Override public void handle(MouseEvent e) {
	        	auxLabel.setVisible(false);
	    		auxTextField.setVisible(false);
	    		auxButton.setVisible(false);
	            aplicarCorreccionGamma(new Double(auxTextField.getText()));
	            auxButton.removeEventHandler(MouseEvent.MOUSE_CLICKED, this);
	        }
		});
		
		auxLabel.setVisible(true);
		auxTextField.setVisible(true);
		auxButton.setVisible(true);
	}
	
	public void aplicarCorreccionGamma(double gamma) {
		Integer[] LUT = new Integer[256];
		
		double a, b;
		for(int i = 0; i <= 255; i++) {
			a = (double) i / 255;
			b = Math.pow(a, gamma);
			LUT[i] = (int) Math.round(b * 255);
		}
		
		aplicarLUT(LUT);
	}
	
	public void aplicarLUT(Integer[] LUT) {
		int width = (int) datosImagen.imagen.getWidth();
		int height = (int) datosImagen.imagen.getHeight();
		PixelReader reader = datosImagen.imagen.getPixelReader();
		
		WritableImage img = new WritableImage(width, height);
		PixelWriter writer = img.getPixelWriter();
			
		int color, r, g, b;
		for(int i = 0; i < width; i++) {
			for(int j = 0; j < height; j++) {
				color = reader.getArgb(i, j);
				r = LUT[argbToRed(color)];
				b = LUT[argbToBlue(color)];
				g = LUT[argbToGreen(color)];
				writer.setArgb(i, j, rgbToArgb(r, g, b));
			}
		}
		
		try {
			abrirImagen(img);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void aplicarRoi(int x1, int y1, int x2, int y2) {
		int width = (int) datosImagen.imagen.getWidth();
		int height = (int) datosImagen.imagen.getHeight();
		PixelReader reader = datosImagen.imagen.getPixelReader();
		
		WritableImage img = new WritableImage(x2 - x1, y2 - y1);
		PixelWriter writer = img.getPixelWriter();
			
		
		for(int i = x1; i < Math.min(x2, width); i++) {
			for(int j = y1; j < Math.min(y2, height); j++) {
				writer.setArgb(i - x1, j - y1, reader.getArgb(i, j));
			}
		}
		
		try {
			abrirImagen(img);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static int argbToRed(int argb) {
		return (argb >> 16) & 0xff;
	}
	
	public static int argbToGreen(int argb) {
		return (argb >> 8) & 0xff;
	}
	
	public static int argbToBlue(int argb) {
		return argb & 0xff;
	}
	
	public static int argbToGrey(int argb) {
		return (int) Math.min(Math.round(0.299 * argbToRed(argb) + 0.587 * argbToGreen(argb) + 0.114 * argbToBlue(argb)), 255); 
	}
	
	public static int rgbToArgb(int r, int g, int b) {
		return (255 << 24) + (r << 16) + (g << 8) + b;
	}
	
	public static double pendienteRecta(double x1, double y1, double x2, double y2) {
		return (y2 - y1) / (x2 - x1);
	}
	
	public static double origenRecta(double x, double y, double m) {
		return y - m * x;
	}
}
