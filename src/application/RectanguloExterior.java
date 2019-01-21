package application;

public class RectanguloExterior {
	public int despX1, despX2, despY1, despY2;
	public int ancho, alto;
	
	public RectanguloExterior(int width, int height, double angulo) {
		/*
		 * Esquinas antes de rotarlas:
		 * 
		 * A ---------- B
		 * |            |
		 * |            |
		 * C----------- D
		 * 
		 */
		// Versiones rotadas:
		double xa = 0;
		double ya = 0;
		double xb = MainController.xTD(width - 1, 0, angulo);
		double yb = MainController.yTD(width - 1, 0, angulo);
		double xc = MainController.xTD(0, height - 1, angulo);
		double yc = MainController.yTD(0, height - 1, angulo);
		double xd = MainController.xTD(width - 1, height - 1, angulo);
		double yd = MainController.yTD(width - 1, height - 1, angulo);
		
		despX1 = (int) Math.floor(Math.min(Math.min(xa, xb), Math.min(xc, xd)));
		despX2 = (int) Math.ceil(Math.max(Math.max(xa, xb), Math.max(xc, xd)));
		despY1 = (int) Math.floor(Math.min(Math.min(ya, yb), Math.min(yc, yd)));
		despY2 = (int) Math.ceil(Math.max(Math.max(ya, yb), Math.max(yc, yd)));
		
		ancho = despX2 - despX1 + 1;
		alto = despY2 - despY1 + 1;
	}
}
