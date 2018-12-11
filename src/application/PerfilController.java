package application;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;

public class PerfilController {

	@FXML
	TextField tfPunto1x;
	
	@FXML
	TextField tfPunto1y;
	
	@FXML
	TextField tfPunto2x;
	
	@FXML
	TextField tfPunto2y;
	
	@FXML
	CheckBox perfilSuavizado;
	
	MainController mc;
	
	
	public void puntosIntroducidos(ActionEvent event) throws IOException {
		int punto1x = new Integer(tfPunto1x.getText());
		int punto1y = new Integer(tfPunto1y.getText());
		int punto2x = new Integer(tfPunto2x.getText());
		int punto2y = new Integer(tfPunto2y.getText());
		if(perfilSuavizado.isSelected())
			mc.aplicarPerfilSuavizado(punto1x, punto1y, punto2x, punto2y);
		else
			mc.aplicarPerfil(punto1x, punto1y, punto2x, punto2y);
	}

	public void addMainController(MainController mc) {
		this.mc = mc;
	}

}
