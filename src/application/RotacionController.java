package application;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;

public class RotacionController {

	@FXML
	TextField tfAnguloRotacion;
	
	@FXML
	CheckBox cbRotarYPintar;
	
	@FXML
	CheckBox cbInterpolacionBilineal;
	
	MainController mc;
	
	
	public void puntosIntroducidos(ActionEvent event) throws IOException {
		if(cbRotarYPintar.isSelected())
			mc.aplicarRotarYPintar(Math.toRadians(new Double(tfAnguloRotacion.getText())));
		else {
			MainController.TipoInterpolacion tipoInterpolacion;
			if(cbInterpolacionBilineal.isSelected())
				tipoInterpolacion = MainController.TipoInterpolacion.BILINEAL;
			else
				tipoInterpolacion = MainController.TipoInterpolacion.VECINO_MAS_PROXIMO;
			
			mc.aplicarRotacionLibre(Math.toRadians(new Double(tfAnguloRotacion.getText())), tipoInterpolacion);
		}
	}

	public void addMainController(MainController mc) {
		this.mc = mc;
	}
}
