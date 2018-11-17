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
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

import com.sun.istack.internal.logging.Logger;

//donde van los eventos de los botones
public class MainController { //esto permite usar el objeto en el scene Builder
	@FXML  
	private Label myMessage;
	@FXML
	private ImageView myImageView;
	@FXML
	private Label prueba;
	
	public void abrirVentana(ActionEvent event) throws IOException {
//		Parent root = FXMLLoader.load(getClass().getResource("Ventana2.fxml"));
//		Scene secondScene = new Scene(root,800,800);
//		Stage newWindow = new Stage();
//		newWindow.setTitle("Imagen");
//		newWindow.setScene(secondScene);
//		newWindow.setX(100);
//		newWindow.setY(100);
//		newWindow.show();
		/////// Hasta aquí abrir nueva ventana que se opera en el Ventana2.fxml //////
		FileChooser fileChooser = new FileChooser();
		FileChooser.ExtensionFilter extFilterTiff = new FileChooser.ExtensionFilter("Archivos tiff", "*.tiff");
		FileChooser.ExtensionFilter extFilterJPG = new FileChooser.ExtensionFilter("Archivos JPG", "*.jpg");
		fileChooser.getExtensionFilters().addAll(extFilterTiff);
		fileChooser.getExtensionFilters().addAll(extFilterJPG);
		File file = fileChooser.showOpenDialog(null);
		
		try {
			BufferedImage img1 = ImageIO.read(file);
			Image image = SwingFXUtils.toFXImage(img1, null);
			myImageView.setImage(image);
			

		}catch(IOException ex) {
			System.out.println(ex.getMessage());
		}
		
		
	}
	
}
