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
import javafx.scene.shape.Rectangle;

/**
 * Segundo controlador, asociado a la segunda ventana se necesita para poder abrir la imagen, sino
 * el hilo de la primera ventana peta con NullPointerException porque no encuentra donde abrir el ImageView de aqu�.
 *
 */
public class SecondController {
	
	private Image img;
	@FXML
	private ImageView imgView;
	
	@FXML
	private Label infoTamTipo = new Label();	// tama�o y tipo de la imagen
	@FXML
	private Label infoPos = new Label();	// posicion del raton
	@FXML
	private Label infoColor = new Label();	// color en la ubicacion del raton

	int x1Roi, y1Roi;
	@FXML
	private Rectangle roi;
	
	MainController mc;
	
	
	public void nuevaImagen(Image imagen) {
		imgView.getParent().maxWidth(imagen.getWidth());
		imgView.getParent().maxHeight(imagen.getHeight());
		imgView.setFitWidth(imagen.getWidth());
		imgView.setFitHeight(imagen.getHeight());
		imgView.setImage(imagen);
	}
	
	public void mostrarInfo(Image imagen) {
		img = imagen;
		String info = "Tama�o: " + (int) imgView.getImage().getWidth() + "x" +  (int) imgView.getImage().getHeight();
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
	
	public void comenzarRoi(MouseEvent event) {
		x1Roi = (int) event.getX();
		y1Roi = (int) event.getY();
		roi.setX(x1Roi);
		roi.setY(y1Roi);
	}
	
	public void acabarRoi(MouseEvent event) {
		int x2Roi = (int) event.getX();
		int y2Roi = (int) event.getY();
		mc.aplicarRoi(x1Roi, y1Roi, x2Roi, y2Roi);
	}
	
	public void dibujarRoi(MouseEvent event) {
		roi.setWidth(event.getX() - x1Roi);
		roi.setHeight(event.getY() - y1Roi);
	}
	
	public void addMainController(MainController mc) {
		this.mc = mc;
	}
}
