package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class BrilloContrasteController {

	@FXML
	TextField nuevoBrillo;
	
	@FXML
	TextField nuevoContraste;
	
	MainController mc;
	
	public void brilloContrasteActuales(float brilloActual, float contrasteActual) {
		nuevoBrillo.setText("" + brilloActual);
		nuevoContraste.setText("" + contrasteActual);
	}
	
	public void brilloContrasteIntroducidos(ActionEvent event) {
		mc.actualizarBrilloContraste(new Float(nuevoBrillo.getText()), new Float(nuevoContraste.getText()));
	}

	public void addMainController(MainController mc) {
		this.mc = mc;
	}
}
