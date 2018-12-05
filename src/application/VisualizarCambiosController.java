package application;

import java.net.URL;
import java.text.NumberFormat;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;

public class VisualizarCambiosController implements Initializable {
	private Image img;
	@FXML
	private ImageView imgView;
	
	@FXML
	private Label infoTamTipo = new Label();	// tamaño y tipo de la imagen
	@FXML
	private Label infoPos = new Label();	// posicion del raton
	@FXML
	private Label infoColor = new Label();	// color en la ubicacion del raton

	int x1Roi, y1Roi;
	@FXML
	private Rectangle roi;
	
	MainController mc;
	
	public static final int UMBRAL_INICIAL = 10; 
	
	int[][] imgOriginal;
	int[][] diferencias;
	
	@FXML
	Slider slider;
	
	@FXML
	TextField valorSlider;
	
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		slider.setValue(UMBRAL_INICIAL);
		valorSlider.setText("" + UMBRAL_INICIAL);
		valorSlider.textProperty().bindBidirectional(slider.valueProperty(), NumberFormat.getNumberInstance());
	}
	
	public void addImagenOriginalyDiferencias(int[][] imgOriginal, int[][] diferencias) {
		this.imgOriginal = imgOriginal;
		this.diferencias = diferencias;
	}
	
	public void sliderMovido() {
		int width = (int) imgView.getImage().getWidth();
		int height = (int) imgView.getImage().getHeight();
		PixelWriter writer = ((WritableImage) imgView.getImage()).getPixelWriter();

		for(int i = 0; i < width; i++) {
			for(int j = 0; j < height; j++) {
				if(diferencias[i][j] <= slider.getValue())
					writer.setArgb(i, j, imgOriginal[i][j]);	 // Píxel de la imagen original
				else
					writer.setArgb(i, j, MainController.rgbToArgb(255, 0, 0));  // Rojo
			}
		}
	}
	
	public void nuevaImagen(Image imagen) {
		imgView.setFitWidth(imagen.getWidth());
		imgView.setFitHeight(imagen.getHeight());
		imgView.setImage(imagen);
	}
	
	public void mostrarInfo(Image imagen) {
		img = imagen;
		String info = "Tamaño: " + (int) imgView.getImage().getWidth() + "x" +  (int) imgView.getImage().getHeight();
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
