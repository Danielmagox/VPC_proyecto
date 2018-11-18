package application;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Segundo controlador, asociado a la segunda ventana se necesita para poder abrir la imagen, sino
 * el hilo de la primera ventana peta con NullPointerException porque no encuentra donde abrir el ImageView de aquí.
 *
 */
public class SecondController implements Initializable {
	
	@FXML
	private ImageView img1;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
	}
	
	public void nuevaImagen(Image imagen) {
		img1.setImage(imagen);
	}
}
