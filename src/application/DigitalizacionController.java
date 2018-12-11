package application;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class DigitalizacionController {

	@FXML
	TextField tfTamano;
	
	@FXML
	TextField tfNumBits;
	
	MainController mc;
	
	
	public void parametrosIntroducidos(ActionEvent event) {
		int tamano = new Integer(tfTamano.getText());
		int numBits = new Integer(tfNumBits.getText());
		mc.aplicarDigitalizacion(tamano, numBits);
	}

	public void addMainController(MainController mc) {
		this.mc = mc;
	}

}
