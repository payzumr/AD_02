package gui;

public interface ITextFenster  {
	
	/**
	 *  Refresht die Anzeige mit aktuellen Werten.
	 * 
	 * @param robot - Die ID des Robots
	 * @param xPos - Die X-Position des Robots
	 * @param yPos - Die Y-Position des Robots
	 * @param xZiel - Das X-Ziel des Robots
	 * @param yZiel - Das Y-Ziel des Robots
	 * @param orderId - Die ID des aktuellen Auftrags.
	 * @param gewicht - Das Gewicht des Items
	 * @param menge - Anzahl der Items
	 * @param status - Aktueller Auftrag
	 */
	public void refresh(int robot, int xPos,int yPos,int xZiel,int yZiel, String orderId, String gewicht, String menge, String status);

}
