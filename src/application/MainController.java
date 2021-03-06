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
import javafx.scene.control.ComboBox;
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
	
	static DatosImagen datosImagenActiva;  //DATOS DE LA IMAGEN QUE ABRIMOS, SE GUARDAN AQUI COMO ESTATICOS PARA TRABAJAR CON ELLOS.
	static ArrayList<DatosImagen> imagenes = new ArrayList<DatosImagen>(); //por si creamos muchas imagenes
	
	@FXML
	ComboBox<String> cbImagenActiva;
	
	@FXML
	Label auxLabel;
	@FXML
	TextField auxTextField;
	@FXML
	Button auxButton;
	
	public static enum TipoInterpolacion {VECINO_MAS_PROXIMO, BILINEAL};
	
	/**
	 * @param event evento que se manda al boton para abrir la nueva ventana y el 2� Controller con la imagen dentro
	 * @throws IOException Por si explota
	 */
	public void abrirVentana(ActionEvent event) throws IOException {
		FileChooser fileChooser = new FileChooser();
		FileChooser.ExtensionFilter extFilterTiff = new FileChooser.ExtensionFilter("Archivos tiff", "*.tiff", "*.TIF");
		FileChooser.ExtensionFilter extFilterJPG = new FileChooser.ExtensionFilter("Archivos JPG", "*.jpg");
		fileChooser.getExtensionFilters().addAll(extFilterTiff);
		fileChooser.getExtensionFilters().addAll(extFilterJPG);
		File file = fileChooser.showOpenDialog(null);		
		/// Hasta aqu� es para filtrar los jpg, y tiff y poder elegirlos y guardarlos en el file
		try {
			BufferedImage img1 = ImageIO.read(file);
			Image image = SwingFXUtils.toFXImage(img1, null);
			datosImagenActiva = new DatosImagen(image);
			cbImagenActiva.getItems().add(datosImagenActiva.titulo);
			imagenes.add(datosImagenActiva); //metemos la imagen abierta en el arrayList
			/// Se lee la imagen como ImageIO y se convierte a Image porque es tiff.

		} catch(IOException ex) {
			System.out.println(ex.getMessage());
		}		
				
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("SecondController.fxml"));
			Parent root = (Parent)loader.load();
			SecondController secController = loader.getController();
			secController.nuevaImagen(datosImagenActiva.imagen);
			secController.mostrarInfo(datosImagenActiva.imagen); // Hacer que el controlador de la imagen muestre la info
			secController.addMainController(this);
			Stage stage = new Stage();
			stage.setScene(new Scene(root));
			stage.setTitle(datosImagenActiva.titulo);
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
	 * @param event	Evento asociado a un bot�n en el que si le damos se abre un filechooser donde podemos elegir el sitio donde guardar la imagen (por ahora solo en .tiff)
	 * @throws IOException Por si explota
	 */
	public void guardarImagen(ActionEvent event) throws IOException {
		String ruta;
		FileChooser filechooser = new FileChooser();
		filechooser.setTitle("Guardar Imagen");
		File file = filechooser.showSaveDialog(null);
		ruta = file.getAbsolutePath();
		/// Pillar donde se quiere guardar
		if(!ruta.endsWith(".tiff") && !ruta.endsWith(".TIF")) {
			file = new File(ruta + ".tiff");
			ruta = ruta + ".tiff";
		}
		/// Si no se le pone la extensi�n se la pongo yo
		if(file != null) {
			ImageOutputStream ios = ImageIO.createImageOutputStream(file);
			ImageIO.write(SwingFXUtils.fromFXImage(datosImagenActiva.imagen,null), "tiff", ios);
		/// Se guarda la imagen
                    										
		}
	}
	
	public void abrirImagen(Image imagen) throws IOException {
		datosImagenActiva = new DatosImagen(imagen);
		cbImagenActiva.getItems().add(datosImagenActiva.titulo);
		imagenes.add(datosImagenActiva);
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("SecondController.fxml"));
		Parent root = (Parent) loader.load();
		SecondController secController = loader.getController();
		secController.nuevaImagen(imagen);
		secController.mostrarInfo(imagen); // Hacer que el controlador de la imagen muestre la info
		secController.addMainController(this);
		Stage stage = new Stage();
		stage.setScene(new Scene(root));
		stage.setTitle(datosImagenActiva.titulo);
		stage.show();
	}
	
	public void seleccionarImagenActiva(ActionEvent event) {
		String tituloImagenActiva = cbImagenActiva.getValue();
		for(DatosImagen datosImagen: imagenes) {
			if(datosImagen.titulo.equals(tituloImagenActiva))
				datosImagenActiva = datosImagen;
		}
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
		String info = "Rango de valores: " + datosImagenActiva.grisMin + " - " + datosImagenActiva.grisMax + "\n";
		info += "Brillo: " + df.format(datosImagenActiva.brillo) + "\n";
		info += "Contraste: " + df.format(datosImagenActiva.contraste) + "\n";
		info += "Entrop�a: " + df.format(datosImagenActiva.entropia) + "\n";
		abrirMensaje(info);
	}
	
	public void mostrarHistograma(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("HistogramController.fxml"));
		Parent root = (Parent) loader.load();
		HistogramController histogramController = loader.getController();
		histogramController.mostrarHistograma(datosImagenActiva.histograma);
		Stage stage = new Stage();
		stage.setScene(new Scene(root));
		stage.show();
	}
	
	public void mostrarHistogramaAcumulativo(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("HistogramController.fxml"));
		Parent root = (Parent) loader.load();
		HistogramController histogramController = loader.getController();
		histogramController.mostrarHistograma(datosImagenActiva.hAcumulativo);
		Stage stage = new Stage();
		stage.setScene(new Scene(root));
		stage.show();
	}
	
	public void escalaDeGrises(ActionEvent event) {
		int width = (int) datosImagenActiva.imagen.getWidth();
		int height = (int) datosImagenActiva.imagen.getHeight();
		PixelReader reader = datosImagenActiva.imagen.getPixelReader();
		
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
		
		// En principio todos los niveles de gris se mapean igual, donde est� definida la t. lineal cambiar�
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
		bcc.brilloContrasteActuales(datosImagenActiva.brillo, datosImagenActiva.contraste);
		Stage stage = new Stage();
		stage.setScene(new Scene(root));
		stage.show();
	}
	
	public void actualizarBrilloContraste(float nuevoBrillo, float nuevoContraste) {
		Integer[] LUT = new Integer[256];
		
		// Vout = m * Vin + n
		double m = nuevoContraste / datosImagenActiva.contraste;
		double n = nuevoBrillo - m * datosImagenActiva.brillo;
		
		for(int i = 0; i <= 255; i++) {
			LUT[i] = (int) Math.min(Math.round(m * i + n), 255);
			if(LUT[i] < 0)
				LUT[i] = 0;
		}
		
		aplicarLUT(LUT);
	}
	
	public void ecualizar(ActionEvent event) throws IOException {
		Integer[] LUT = new Integer[256];
		ArrayList<Integer> hAcumulativo = datosImagenActiva.hAcumulativo.numPixels;
		
		for(int i = 0; i <= 255; i++) {
			LUT[i] = Math.max(0, Math.round(256F/datosImagenActiva.size * hAcumulativo.get(i)) - 1);
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
			hAcActual.add((double) datosImagenActiva.hAcumulativo.getNumPixels(i) / datosImagenActiva.size);
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
	
	public void diferenciaImagenes(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("DiferenciaImagenesController.fxml"));
		Parent root = (Parent) loader.load();
		DiferenciaImagenesController dic = loader.getController();
		dic.addMainController(this);
		dic.addListaImagenes(cbImagenActiva);
		Stage stage = new Stage();
		stage.setScene(new Scene(root));
		stage.show();
	}
	
	public void aplicarDiferenciaImagenes(String imagen1, String imagen2) throws IOException {
		DatosImagen datosImagen1 = null, datosImagen2 = null;
		for(DatosImagen datosImagen: imagenes) {
			if(datosImagen.titulo.equals(imagen1))
				datosImagen1 = datosImagen;
		}
		for(DatosImagen datosImagen: imagenes) {
			if(datosImagen.titulo.equals(imagen2))
				datosImagen2 = datosImagen;
		}
		
		int width1 = (int) datosImagen1.imagen.getWidth();
		int height1 = (int) datosImagen1.imagen.getHeight();
		int width2 = (int) datosImagen2.imagen.getWidth();
		int height2 = (int) datosImagen2.imagen.getHeight();
		
		if((width1 != width2) || (height1 != height2)) {
			abrirMensaje("Mismo tama�o requerido");
		}
		else {
			PixelReader reader1 = datosImagen1.imagen.getPixelReader();
			PixelReader reader2 = datosImagen2.imagen.getPixelReader();
			
			WritableImage img = new WritableImage(width1, height1);
			PixelWriter writer = img.getPixelWriter();
				
			int color1, color2, r, g, b;
			for(int i = 0; i < width1; i++) {
				for(int j = 0; j < height1; j++) {
					color1 = reader1.getArgb(i, j);
					color2 = reader2.getArgb(i, j);
					
					r = Math.abs(argbToRed(color1) - argbToRed(color2));
					b = Math.abs(argbToBlue(color1) - argbToBlue(color2));
					g = Math.abs(argbToGreen(color1) - argbToGreen(color2));
					writer.setArgb(i, j, rgbToArgb(r, g, b));
				}
			}
			
				try {
					abrirImagen(img);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
	public void detectarCambios(String imagen1, String imagen2) throws IOException {
		DatosImagen datosImagen1 = null, datosImagen2 = null;
		for(DatosImagen datosImagen: imagenes) {
			if(datosImagen.titulo.equals(imagen1))
				datosImagen1 = datosImagen;
		}
		for(DatosImagen datosImagen: imagenes) {
			if(datosImagen.titulo.equals(imagen2))
				datosImagen2 = datosImagen;
		}
		
		int width1 = (int) datosImagen1.imagen.getWidth();
		int height1 = (int) datosImagen1.imagen.getHeight();
		int width2 = (int) datosImagen2.imagen.getWidth();
		int height2 = (int) datosImagen2.imagen.getHeight();
		
		if((width1 != width2) || (height1 != height2)) {
			abrirMensaje("Mismo tama�o requerido");
		}
		else {
			PixelReader reader1 = datosImagen1.imagen.getPixelReader();
			PixelReader reader2 = datosImagen2.imagen.getPixelReader();
			
			WritableImage img = new WritableImage(width1, height1);
			PixelWriter writer = img.getPixelWriter();
			
			int[][] originalImg = new int[width1][height1];
			int[][] greyDiff = new int[width1][height1];
			int color1, color2, r, g, b;
			for(int i = 0; i < width1; i++) {
				for(int j = 0; j < height1; j++) {
					color1 = reader1.getArgb(i, j);
					color2 = reader2.getArgb(i, j);
					originalImg[i][j] = color1;
					
					// Media de las diferencias por banda ponderadas tal como
					// se hace para pasar una imagen en color a escala de grises
					//
					// De esta forma el histograma de la imagen diferencia es una
					// buena referencia para elegir un umbral
					r = Math.abs(argbToRed(color1) - argbToRed(color2));
					b = Math.abs(argbToBlue(color1) - argbToBlue(color2));
					g = Math.abs(argbToGreen(color1) - argbToGreen(color2));
					greyDiff[i][j] = argbToGrey(rgbToArgb(r, g, b));
					if(greyDiff[i][j] <= VisualizarCambiosController.UMBRAL_INICIAL)
						writer.setArgb(i, j, originalImg[i][j]);	 // P�xel de la imagen original
					else
						writer.setArgb(i, j, rgbToArgb(255, 0, 0));  // Rojo
				}
			}
			
			try {
				datosImagenActiva = new DatosImagen(img);
				cbImagenActiva.getItems().add(datosImagenActiva.titulo);
				imagenes.add(datosImagenActiva);
				
				FXMLLoader loader = new FXMLLoader(getClass().getResource("VisualizarCambiosController.fxml"));
				Parent root = (Parent) loader.load();
				VisualizarCambiosController vcc = loader.getController();
				vcc.nuevaImagen(img);
				vcc.mostrarInfo(img);
				vcc.addMainController(this);
				vcc.addImagenOriginalyDiferencias(originalImg, greyDiff);
				Stage stage = new Stage();
				stage.setScene(new Scene(root));
				stage.setTitle(datosImagenActiva.titulo);
				stage.show();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void perfil(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("PerfilController.fxml"));
		Parent root = (Parent) loader.load();
		PerfilController perfilController = loader.getController();
		perfilController.addMainController(this);
		Stage stage = new Stage();
		stage.setScene(new Scene(root));
		stage.show();
	}
	
	public ArrayList<Par> bresenham(int x0, int y0, int x1, int y1) {
		ArrayList<Par> puntos = new ArrayList<Par>();
		
		int x, y, dx, dy, p, incE, incNE, stepx, stepy;
		dx = (x1 - x0);
		dy = (y1 - y0);

		/* determinar que punto usar para empezar, cual para terminar */
		if (dy < 0) {
			dy = -dy;
			stepy = -1;
		} else {
			stepy = 1;
		}

		if (dx < 0) {
			dx = -dx;
			stepx = -1;
		} else {
			stepx = 1;
		}

		x = x0;
		y = y0;
		//g.drawLine(x0, y0, x0, y0);
		puntos.add(new Par(x0, y0));
		/* se cicla hasta llegar al extremo de la l�nea */
		if (dx > dy) {
			p = 2 * dy - dx;
			incE = 2 * dy;
			incNE = 2 * (dy - dx);
			while (x != x1) {
				x = x + stepx;
				if (p < 0) {
					p = p + incE;
				} else {
					y = y + stepy;
					p = p + incNE;
				}
				//g.drawLine(x, y, x, y);
				puntos.add(new Par(x, y));
			}
		} else {
			p = 2 * dx - dy;
			incE = 2 * dx;
			incNE = 2 * (dx - dy);
			while (y != y1) {
				y = y + stepy;
				if (p < 0) {
					p = p + incE;
				} else {
					x = x + stepx;
					p = p + incNE;
				}
				//g.drawLine(x, y, x, y);
				puntos.add(new Par(x, y));
			}
		}
		
		return puntos;
	}
	
	public void aplicarPerfil(int x0, int y0, int x1, int y1) throws IOException {
		ArrayList<Par> puntos = bresenham(x0, y0, x1, y1);
		PixelReader reader = datosImagenActiva.imagen.getPixelReader();
		
		ArrayList<Integer> nivelesGris = new ArrayList<Integer>();
		for(Par p: puntos) {
			nivelesGris.add(argbToGrey(reader.getArgb(p.x, p.y)));
		}
		
		ArrayList<Integer> diferencias = new ArrayList<Integer>();
		for(int i = 1; i < nivelesGris.size(); i++) {
			diferencias.add(nivelesGris.get(i) - nivelesGris.get(i-1));
		}
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("VerPerfilController.fxml"));
		Parent root = (Parent) loader.load();
		VerPerfilController verPerfilController = loader.getController();
		verPerfilController.mostrarNivelesGris(nivelesGris);
		verPerfilController.mostrarDiferencias(diferencias);
		Stage stage = new Stage();
		stage.setScene(new Scene(root));
		stage.show();
	}
	
	public void aplicarPerfilSuavizado(int x0, int y0, int x1, int y1) throws IOException {
		ArrayList<Par> puntos = bresenham(x0, y0, x1, y1);
		PixelReader reader = datosImagenActiva.imagen.getPixelReader();
		
		ArrayList<Integer> nivelesGris = new ArrayList<Integer>();
		for(Par p: puntos) {
			nivelesGris.add(argbToGrey(reader.getArgb(p.x, p.y)));
		}
		for(int i = 2; i < nivelesGris.size() - 2; i++) {
			nivelesGris.set(i, (nivelesGris.get(i-2) + nivelesGris.get(i-1) + nivelesGris.get(i)
							+ nivelesGris.get(i+1) + nivelesGris.get(i+2)) / 5);
		}
		
		ArrayList<Integer> diferencias = new ArrayList<Integer>();
		for(int i = 1; i < nivelesGris.size(); i++) {
			diferencias.add(nivelesGris.get(i) - nivelesGris.get(i-1));
		}
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("VerPerfilController.fxml"));
		Parent root = (Parent) loader.load();
		VerPerfilController verPerfilController = loader.getController();
		verPerfilController.mostrarNivelesGris(nivelesGris);
		verPerfilController.mostrarDiferencias(diferencias);
		Stage stage = new Stage();
		stage.setScene(new Scene(root));
		stage.show();
	}
	
	public void digitalizacion(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("DigitalizacionController.fxml"));
		Parent root = (Parent) loader.load();
		DigitalizacionController digitalizacionController = loader.getController();
		digitalizacionController.addMainController(this);
		Stage stage = new Stage();
		stage.setScene(new Scene(root));
		stage.show();
	}
	
	public void aplicarDigitalizacion(int tamano, int numBits) {
		int width = (int) datosImagenActiva.imagen.getWidth();
		int height = (int) datosImagenActiva.imagen.getHeight();
		PixelReader reader = datosImagenActiva.imagen.getPixelReader();
		
		// Truncamos con la divisi�n entera para quitar los pixels que no puedan completar una regi�n
		int newWidth = width / tamano * tamano;
		int newHeight = height / tamano * tamano;
		Integer[][] resultado = new Integer[newWidth][newHeight];
		
		int media = 0;
		int mediaR = 0;
		int mediaG = 0;
		int mediaB = 0;
		for(int i = 0; i <= newWidth - tamano; i += tamano) {
			for(int j = 0; j <= newHeight - tamano; j += tamano) {
				mediaR = 0;
				mediaG = 0;
				mediaB = 0;
				for(int k = 0; k < tamano; k++) {
					for(int s = 0; s < tamano; s++) {
						mediaR += argbToRed(reader.getArgb(i + k, j + s));
						mediaG += argbToGreen(reader.getArgb(i + k, j + s));
						mediaB += argbToBlue(reader.getArgb(i + k, j + s));
					}
				}
				mediaR = Math.round((float) (mediaR) / (tamano * tamano));
				mediaG = Math.round((float) (mediaG) / (tamano * tamano));
				mediaB = Math.round((float) (mediaB) / (tamano * tamano));
				media = rgbToArgb(mediaR, mediaG, mediaB);
				for(int k = 0; k < tamano; k++) {
					for(int l = 0; l < tamano; l++) {
						resultado[i + k][j + l] = media;
					}
				}
			}
		}

		
		WritableImage img = new WritableImage(newWidth, newHeight);
		PixelWriter writer = img.getPixelWriter();
		
		// Escalamos a un intervalo de tama�o numBits^2 - 1, redondeamos y reescalamos
		int r, g, b;
		int maxRelativeVal = (int) Math.pow(numBits, 2) - 1;
		for(int i = 0; i < newWidth; i++) {
			for(int j = 0; j < newHeight; j++) {
				if(numBits > 1) {
					r = Math.round(argbToRed(resultado[i][j]) * maxRelativeVal / 255F) * 255 / maxRelativeVal;
					g = Math.round(argbToGreen(resultado[i][j]) * maxRelativeVal / 255F) * 255 / maxRelativeVal;
					b = Math.round(argbToBlue(resultado[i][j]) * maxRelativeVal / 255F) * 255 / maxRelativeVal;
				}
				else {
					r = Math.round(argbToRed(resultado[i][j]) / 255F) * 255;
					g = Math.round(argbToGreen(resultado[i][j]) / 255F) * 255;
					b = Math.round(argbToBlue(resultado[i][j]) / 255F) * 255;
				}
				writer.setArgb(i, j, rgbToArgb(r, g, b));
			}
		}
		
		try {
			abrirImagen(img);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void aplicarLUT(Integer[] LUT) {
		int width = (int) datosImagenActiva.imagen.getWidth();
		int height = (int) datosImagenActiva.imagen.getHeight();
		PixelReader reader = datosImagenActiva.imagen.getPixelReader();
		
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
	
	////// ----- Operaciones geom�tricas ----- ///////
	public Image espejoVertical() {
		int width = (int) datosImagenActiva.imagen.getWidth();
		int height = (int) datosImagenActiva.imagen.getHeight();
		PixelReader reader = datosImagenActiva.imagen.getPixelReader();
		
		WritableImage img = new WritableImage(width, height);
		PixelWriter writer = img.getPixelWriter();
			
		int color;
		for(int i = 0; i < width; i++) {
			for(int j = 0; j < height; j++) {
				color = reader.getArgb(i, j);
				writer.setArgb(i, height - 1 - j, color);
			}
		}
		
		return img;
	}
	
	public void espejoVertical(ActionEvent event) {
		try {
			abrirImagen(espejoVertical());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Image espejoHorizontal() {
		int width = (int) datosImagenActiva.imagen.getWidth();
		int height = (int) datosImagenActiva.imagen.getHeight();
		PixelReader reader = datosImagenActiva.imagen.getPixelReader();
		
		WritableImage img = new WritableImage(width, height);
		PixelWriter writer = img.getPixelWriter();
			
		int color;
		for(int i = 0; i < width; i++) {
			for(int j = 0; j < height; j++) {
				color = reader.getArgb(i, j);
				writer.setArgb(width - 1 - i, j, color);
			}
		}
		
		return img;
	}
	
	public void espejoHorizontal(ActionEvent event) {
		try {
			abrirImagen(espejoHorizontal());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Image traspuesta() {
		int width = (int) datosImagenActiva.imagen.getWidth();
		int height = (int) datosImagenActiva.imagen.getHeight();
		PixelReader reader = datosImagenActiva.imagen.getPixelReader();
		
		WritableImage img = new WritableImage(height, width);
		PixelWriter writer = img.getPixelWriter();
			
		int color;
		for(int i = 0; i < width; i++) {
			for(int j = 0; j < height; j++) {
				color = reader.getArgb(i, j);
				writer.setArgb(j, i, color);
			}
		}
		
		return img;
	}
	
	public void traspuesta(ActionEvent event) {
		try {
			abrirImagen(traspuesta());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void rotacion90(ActionEvent event) {
		datosImagenActiva.imagen = traspuesta();
		try {
			abrirImagen(espejoHorizontal());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void rotacion180(ActionEvent event) {
		datosImagenActiva.imagen = espejoVertical();
		try {
			abrirImagen(espejoHorizontal());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void rotacion270(ActionEvent event) {
		datosImagenActiva.imagen = espejoHorizontal();
		try {
			abrirImagen(traspuesta());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void escalado(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("EscaladoController.fxml"));
		Parent root = (Parent) loader.load();
		EscaladoController escaladoController = loader.getController();
		escaladoController.addMainController(this);
		Stage stage = new Stage();
		stage.setScene(new Scene(root));
		stage.show();
	}
	
	public void aplicarPorcentajeEscalado(double cambioX, double cambioY, TipoInterpolacion tipoInterpolacion) {
		aplicarEscalado(cambioX, cambioY, (int) Math.floor(datosImagenActiva.imagen.getWidth() * cambioX),
						(int) Math.floor(datosImagenActiva.imagen.getHeight() * cambioY), tipoInterpolacion);
	}
	
	public void aplicarDimensionesEscalado(int newWidth, int newHeight, TipoInterpolacion tipoInterpolacion) {
		aplicarEscalado((double) newWidth / datosImagenActiva.imagen.getWidth(),
						(double) newHeight / datosImagenActiva.imagen.getHeight(), 
						newWidth, newHeight, tipoInterpolacion);
	}

	public void aplicarEscalado(double cambioX, double cambioY, int newWidth, int newHeight, TipoInterpolacion tipoInterpolacion) {
		int width = (int) datosImagenActiva.imagen.getWidth();
		int height = (int) datosImagenActiva.imagen.getHeight();
		PixelReader reader = datosImagenActiva.imagen.getPixelReader();
		
		WritableImage img = new WritableImage(newWidth, newHeight);
		PixelWriter writer = img.getPixelWriter();
				
		for(int i = 0; i < newWidth; i++) {
			for(int j = 0; j < newHeight; j++) {
				writer.setArgb(i, j, interpolar(tipoInterpolacion, i / cambioX, j / cambioY, width, height, reader));
			}
		}
		
		try {
			abrirImagen(img);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void rotacionLibre(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("RotacionController.fxml"));
		Parent root = (Parent) loader.load();
		RotacionController rotacionController = loader.getController();
		rotacionController.addMainController(this);
		Stage stage = new Stage();
		stage.setScene(new Scene(root));
		stage.show();
	}
	
	public void aplicarRotarYPintar(double angulo) {
		PixelReader reader = datosImagenActiva.imagen.getPixelReader();
		int width = (int) datosImagenActiva.imagen.getWidth();
		int height = (int) datosImagenActiva.imagen.getHeight();
		RectanguloExterior r = new RectanguloExterior(width, height, angulo);
		
		WritableImage img = new WritableImage(r.ancho, r.alto);
		PixelWriter writer = img.getPixelWriter();
			
		int color;
		for(int i = 0; i < width; i++) {
			for(int j = 0; j < height; j++) {
				color = reader.getArgb(i, j);
				writer.setArgb((int) Math.round(xTD(i, j, angulo)) - r.despX1, (int) Math.round(yTD(i, j, angulo)) - r.despY1, color);
			}
		}
		
		try {
			abrirImagen(img);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void aplicarRotacionLibre(double angulo, TipoInterpolacion tipoInterpolacion) {
		PixelReader reader = datosImagenActiva.imagen.getPixelReader();
		int width = (int) datosImagenActiva.imagen.getWidth();
		int height = (int) datosImagenActiva.imagen.getHeight();
		RectanguloExterior r = new RectanguloExterior(width, height, angulo);
		
		//System.out.println(r.ancho + "x" + r.alto);
		//System.out.println(r.despX1 + " " + r.despX2 + " " + r.despY1 + " " + r.despY2);
		
		WritableImage img = new WritableImage(r.ancho, r.alto);
		PixelWriter writer = img.getPixelWriter();
			
		int color;
		double x, y;
		for(int i = 0; i < r.ancho; i++) {
			for(int j = 0; j < r.alto; j++) {
				x = xTI(i + r.despX1, j + r.despY1, angulo);
				y = yTI(i + r.despX1, j + r.despY1, angulo);
				if(x >= 0 && x <= width - 1 && y >= 0 && y <= height - 1) {
					color = interpolar(tipoInterpolacion, x, y, width, height, reader);
					writer.setArgb(i, j, color);
				}
				else
					writer.setArgb(i, j, 0);	// Transparente
			}
		}
		
		try {
			abrirImagen(img);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void aplicarRoi(int x1, int y1, int x2, int y2) {
		int width = (int) datosImagenActiva.imagen.getWidth();
		int height = (int) datosImagenActiva.imagen.getHeight();
		PixelReader reader = datosImagenActiva.imagen.getPixelReader();
		
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
	
	public static int interpolar(TipoInterpolacion tipoInterpolacion, double x, double y, int width, int height, PixelReader reader) {
		switch(tipoInterpolacion) {
			case VECINO_MAS_PROXIMO:
				return interpolacionVecinoMasProximo(x, y, width, height, reader);
			case BILINEAL:
				return interpolacionBilineal(x, y, width, height, reader);
			default:
				return interpolacionBilineal(x, y, width, height, reader);
		}
	}
	
	// Transformacion de coordenadas en las rotaciones
	
	public static double xTD(int x, int y, double angulo) {
		return x * Math.cos(angulo) - y * Math.sin(angulo);
	}
	
	public static double yTD(int x, int y, double angulo) {
		return x * Math.sin(angulo) + y * Math.cos(angulo);
	}
	
	public static double xTI(int xPrima, int yPrima, double angulo) {
		return xPrima * Math.cos(angulo) + yPrima * Math.sin(angulo);
	}
	
	public static double yTI(int xPrima, int yPrima, double angulo) {
		return -xPrima * Math.sin(angulo) + yPrima * Math.cos(angulo);
	}
	
	public static int interpolacionVecinoMasProximo(double x, double y, int width, int height, PixelReader reader) {
		return reader.getArgb((int) Math.min(Math.round(x), width - 1), (int) Math.min(Math.round(y), height - 1));
	}
	
	public static int interpolacionBilineal(double x, double y, int width, int height, PixelReader reader) {
		/*
		 * A        B
		 * 
		 *    P
		 * 
		 * C        D
		 */
		int xA, yA, xB, yB, xC, yC, xD, yD;
		xA = xC = (int) Math.floor(x);
		xB = xD = Math.min((int) Math.ceil(x), width - 1);
		yA = yB = Math.min((int) Math.ceil(y), height - 1);
		yC = yD = (int) Math.floor(y);
		
		// (p,q) --> posici�n de P relativa a C
		double p = x - xC;
		double q = y - yC;
		
		// Colores de A, B, C and D
		int A, B, C, D;
		A = reader.getArgb(xA, yA);
		B = reader.getArgb(xB, yB);
		C = reader.getArgb(xC, yC);
		D = reader.getArgb(xD, yD);
		
		// Si alguno de los p�xeles es transparente, que sea transparente
		if(argbToA(A) == 0 || argbToA(B) == 0 || argbToA(C) == 0 || argbToA(D) == 0)
			return 0;
		
		int red = (int) Math.round(argbToRed(C) + (argbToRed(D) - argbToRed(C)) * p + (argbToRed(A) - argbToRed(C)) * q
				+ (argbToRed(B) - argbToRed(A) - argbToRed(D) + argbToRed(C)) * p * q);
		int green = (int) Math.round(argbToGreen(C) + (argbToGreen(D) - argbToGreen(C)) * p + (argbToGreen(A) - argbToGreen(C)) * q
				  + (argbToGreen(B) - argbToGreen(A) - argbToGreen(D) + argbToGreen(C)) * p * q);
		int blue = (int) Math.round(argbToBlue(C) + (argbToBlue(D) - argbToBlue(C)) * p + (argbToBlue(A) - argbToBlue(C)) * q
				 + (argbToBlue(B) - argbToBlue(A) - argbToBlue(D) + argbToBlue(C)) * p * q);
				
		return rgbToArgb(red, green, blue);
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
	
	public static int argbToA(int argb) {
		return argb >> 24;
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
