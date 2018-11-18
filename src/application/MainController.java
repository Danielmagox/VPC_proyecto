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
	
	static Image imagen;  //IMAGEN QUE ABRIMOS, SE GUARDA AQUI COMO ESTATICA PARA TRABAJAR CON ELLA.
	static ArrayList<Image> imagenes = new ArrayList<Image>(); //por si creamos muchas imagenes
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
			imagen = image;
			imagenes.add(imagen); //metemos la imagen abierta en el arrayList
			/////////////////////////////////// Se lee la imagen como ImageIO y se convierte a Image porque es tiff.

		} catch(IOException ex) {
			System.out.println(ex.getMessage());
		}		
				
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("SecondController.fxml"));
			Parent root = (Parent)loader.load();
			SecondController secController = loader.getController();
			secController.nuevaImagen(imagen);
			secController.mostrarInfo(imagen); // Hacer que el controlador de la imagen muestre la info
			Stage stage = new Stage();
			stage.setScene(new Scene(root));
			stage.show();
			////////////////////////////////// Esto es para mandar la imagen elegida a la segunda ventana, se necesita si o si el segundo controlador
		} catch(IOException e) {
			System.out.println(e.getMessage());
		}/////Fin de evento				
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
			ImageIO.write(SwingFXUtils.fromFXImage(imagen,null), "tiff", ios);
		////////////////////Se guarda la imagen
                    										
		}
	}
}
