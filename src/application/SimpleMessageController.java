package application;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class SimpleMessageController {
	@FXML
	Label message;
	
	public void setMessage(String text) {
		message.setText(text);
	}

}
