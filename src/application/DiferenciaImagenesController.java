package application;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;

public class DiferenciaImagenesController {

	@FXML
	ComboBox<String> cbImagen1;
	@FXML
	ComboBox<String> cbImagen2;
	
	MainController mc;
	
	
	public void addListaImagenes(ComboBox<String> cb) {
		cbImagen1.setItems(cb.getItems());
		cbImagen2.setItems(cb.getItems());
	}
	
	public void imagenDiferencia(ActionEvent event) throws IOException {
		mc.aplicarDiferenciaImagenes(cbImagen1.getValue(), cbImagen2.getValue());
	}
	
	public void visualizarCambios(ActionEvent event) throws IOException {
		mc.detectarCambios(cbImagen1.getValue(), cbImagen2.getValue());
	}

	public void addMainController(MainController mc) {
		this.mc = mc;
	}
}
