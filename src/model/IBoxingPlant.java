package model;

public interface IBoxingPlant extends IField {

    /**
     * @param  keine 
     * @return int -  Die ID des BoxingPlant
     */
    public int id();

    /**
     * @param  keine  
     * @return  boolean - true wenn BoxingPlant busy ist.
     */
    public boolean isBusy();

    /**
     * Weist der BoxingPlant eine Bestellung zu, setzt sie auf busy und berechnet die gesamte Packzeit 
     *           abhängig von der Anzahl an Items
     * @param  order - Ein Auftrage vom Typ Order         
     * @return keine     
     */
    public void receiveOrder(Order order);

    /**
     *  Es wird geprüft, ob ein Robot frei ist, um die Order abzuarbeiten. Falls ja, wird ihm diese Order
     *  zugewiesen.
     */
    public void processOrder();

    //JUnit
    public IRobot getRobot();

    public int coordinateX();
    public int coordinateY();
    public int getTemp_CLTIME_cnt();
    public int getPackingTime();
    public int getLoadTime();
}
