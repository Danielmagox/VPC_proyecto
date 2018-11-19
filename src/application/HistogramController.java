package application;

import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

public class HistogramController {
	@FXML
	BarChart<String,Number> barChart;
	
	public void mostrarHistograma(Histograma h) {    
        XYChart.Series<String, Number> series = new XYChart.Series<String, Number>();
        series.setName("Píxeles por nivel");
        
        for(int i = 0; i < h.numPixels.size(); i++) {
        	series.getData().add(new XYChart.Data<String, Number>("" + i, h.getNumPixels(i)));
        }
        
        barChart.getData().add(series);
	}
}
