package application;

import java.util.ArrayList;

import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;

public class VerPerfilController {
	@FXML
	BarChart<String,Number> barChartNivelGris;
	@FXML
	BarChart<String,Number> barChartDerivada;
	
	
	public void mostrarNivelesGris(ArrayList<Integer> nivelesGris) {    
        XYChart.Series<String, Number> series = new XYChart.Series<String, Number>();
        series.setName("Nivel de gris por píxel");
        
        for(int i = 0; i < nivelesGris.size(); i++) {
        	series.getData().add(new XYChart.Data<String, Number>("" + i, nivelesGris.get(i)));
        }
        
        barChartNivelGris.getData().add(series);
	}
	
	public void mostrarDiferencias(ArrayList<Integer> diferencias) {    
        XYChart.Series<String, Number> series = new XYChart.Series<String, Number>();
        series.setName("Diferencia entre cada píxel y el anterior");
        
        for(int i = 0; i < diferencias.size(); i++) {
        	series.getData().add(new XYChart.Data<String, Number>("" + i, diferencias.get(i)));
        }
        
        barChartDerivada.getData().add(series);
	}
}
