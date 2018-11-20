package application;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class LinealTransformController {
	@FXML
	TextField numeroTramos;
	
	@FXML
	GridPane datosTramos;
	
	int nTramos;
	TextField[][] datosPuntos;
	Integer[][] puntos;
	
	@FXML
	Button verGrafico;
	
	@FXML
	LineChart<String,Number> lineChart;
	
	@FXML
	Button aplicarFiltro;
	
	MainController mc;
	
	
	public void numeroTramosIntroducido(ActionEvent event) {
		nTramos = new Integer(numeroTramos.getText());
		datosTramos.add(new Label("\tx"), 1, 0);
		datosTramos.add(new Label("\ty"), 2, 0);
		
		datosPuntos = new TextField[nTramos+1][2];
		for(int i = 0; i <  nTramos + 1; i++) {
			for(int j = 0; j < 2; j++) {
				datosPuntos[i][j] = new TextField();
			}
		}
		
		for(int i = 1; i <= nTramos + 1; i++) {
			datosTramos.addRow(i, new Label("Punto " + i), datosPuntos[i-1][0], datosPuntos[i-1][1]);
		}
		
		verGrafico.setVisible(true);
	}
	
	public void hacerGrafico(ActionEvent event) {
		puntos = new Integer[nTramos+1][2];
		for(int i = 0; i <  nTramos + 1; i++) {
			for(int j = 0; j < 2; j++) {
				puntos[i][j] = new Integer(datosPuntos[i][j].getText());
			}
		}
		
		XYChart.Series<String, Number> series = new XYChart.Series<String, Number>();
        
        for(int i = 0; i < puntos.length; i++) {
        	series.getData().add(new XYChart.Data<String, Number>("" + puntos[i][0], puntos[i][1]));
        }
        
        lineChart.getData().add(series);
        aplicarFiltro.setVisible(true);
	}
	
	public void aplicarFiltro(ActionEvent event) {
		mc.aplicarTransformacionLinealTramos(puntos);
	}
	
	public void addMainController(MainController mc) {
		this.mc = mc;
	}
}
