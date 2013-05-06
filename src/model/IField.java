package model;

public interface IField {

	/**
	 * Gibt die Anzahl von Robots auf einem Field als Integer-Wert zurück.
	 * 
	 * @return int - Anzahl der Robots auf einem Field.
	 */
    public int robotCount();

    /**
     * Die uebergebene Robot ID wird in der Objektvariable robotId von
     * Field gespeichert. amountOfRobots wird inkrementiert.
     * 
     * @param bot - Ein Roboter zum registrieren. 
     */
    public void registerRobot(IRobot bot);

    /**
     * Anzahl der Roboter wird dekrementiert. Der Robot in dem Field wird auf NULL gesetzt.
     */
    public void unregisterRobot();

    /**
     * Gibt die aktuelle robotID als Integer-Wert zurück.
     * 
     * @return int - Die aktuelle robotID
     */
    public int robotID();

    /**
     * Gibt die aktuelle x-Koordinate als Integer-Wert zurück.
     * 
     * @return int - Die aktuelle x-Koordinate
     */
    public int coordinateX();

    /**
     * Gibt die aktuelle y-Koordinate als Integer-Wert zurück.
     * 
     * @return int - Die aktuelle y-Koordinate
     */
    public int coordinateY();

    /**
     * Gibt als Boolean zurück, ob das aktuelle Field ein BoxingPlant
     * ist oder nicht.
     * 
     * @return boolean - true wenn es ein BoxingPlant ist. 
     */
    public boolean isBoxingPlant();

    /**
     * Gibt das Target des Robots auf dem Feld zurück.
     * 
     * @return int[] - das aktuelle Ziel des Robots auf dem Field.
     */
    public int[] getTarget();
}
