package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;

public class EscaladoController {

	@FXML
	TextField tfX;
	
	@FXML
	TextField tfY;
	
	@FXML
	CheckBox cbPorcentajes;
	
	@FXML
	CheckBox cbInterpolacionBilineal;
	
	MainController mc;
	
	
	public void parametrosIntroducidos(ActionEvent event) {
		MainController.TipoInterpolacion tipoInterpolacion;
		if(cbInterpolacionBilineal.isSelected())
			tipoInterpolacion = MainController.TipoInterpolacion.BILINEAL;
		else
			tipoInterpolacion = MainController.TipoInterpolacion.VECINO_MAS_PROXIMO;
		
		if(cbPorcentajes.isSelected())
			mc.aplicarPorcentajeEscalado(new Double(tfX.getText()) / 100, new Double(tfY.getText()) / 100, tipoInterpolacion);
		else
			mc.aplicarDimensionesEscalado(new Integer(tfX.getText()), new Integer(tfY.getText()), tipoInterpolacion);
	}

	public void addMainController(MainController mc) {
		this.mc = mc;
	}
}
