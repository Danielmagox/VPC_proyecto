package application;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
			Stage stage = new Stage();
			stage.setScene(new Scene(root));
			stage.show();
			////////////////////////////////// Esto es para mandar la imagen elegida a la segunda ventana, se necesita si o si el segundo controlador
		} catch(IOException e) {
			System.out.println(e.getMessage());
		}/////Fin de evento
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
		Stage stage = new Stage();
		stage.setScene(new Scene(root));
		stage.show();
		
		datosImagen = new DatosImagen(imagen);
		imagenes.add(datosImagen);
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
		Stage stage = new Stage();
		stage.setScene(new Scene(root));
		stage.show();
	}
	
	public static void aplicarTransformacionLinealTramos(Integer[][] puntos) {
		
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
		return (int) Math.round(0.299 * argbToRed(argb) + 0.587 * argbToGreen(argb) + 0.114 * argbToBlue(argb)) % 256; 
	}
	
	public static int rgbToArgb(int r, int g, int b) {
		return (255 << 24) + (r << 16) + (g << 8) + b;
	}
}
