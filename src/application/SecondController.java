package application;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

/**
 * Segundo controlador, asociado a la segunda ventana se necesita para poder abrir la imagen, sino
 * el hilo de la primera ventana peta con NullPointerException porque no encuentra donde abrir el ImageView de aquí.
 *
 */
public class SecondController {
	
	private Image img;
	@FXML
	private ImageView imgView;
	
	@FXML
	private Label infoTamTipo = new Label();	// tamaño y tipo de la imagen
	@FXML
	private Label infoPos = new Label();	// posicion del raton
	@FXML
	private Label infoColor = new Label();	// color en la ubicacion del raton

	
	
	public void nuevaImagen(Image imagen) {
		imgView.setFitWidth(imagen.getWidth());
		imgView.setFitHeight(imagen.getHeight());
		imgView.setImage(imagen);
	}
	
	public void mostrarInfo(Image imagen) {
		img = imagen;
		String info = "Tamaño: " + (int) imgView.getImage().getWidth() + "x" +  (int) imgView.getImage().getHeight();
		info += "";	// Falta tipo de la imagen
		infoTamTipo.setText(info);
	}
	
	public void moverRaton(MouseEvent event) {
		infoPosicion(event);
		infoColor(event);
	}
	
	public void infoPosicion(MouseEvent event) {
		infoPos.setText("PosX: " + (int) event.getX() + " PosY: " + (int) event.getY());
	}
	
	public void infoColor(MouseEvent event) {
		int argb = img.getPixelReader().getArgb((int) event.getX(), (int) event.getY());
		infoColor.setText("R: " + MainController.argbToRed(argb) + " G: " + MainController.argbToGreen(argb)
							+ " B: " + MainController.argbToBlue(argb));
	}
}
