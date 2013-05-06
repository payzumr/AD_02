package model;

public interface IRobot {

    /**			Wenn der Robot eine Order hat Arbeitet er alle Items in der liste ab und 
     * 	         wenn die Order leer ist fährt er zuruck zu seiner Startposition
     * 	    	 Setzt busy auf true wenn er eine Order hat und auf false wenn er
     * 	    	 seine Startposition wieder erreicht hat
     */
    public char processOrder();

    /**
     * Setzt den neuen Auftrag.
     * 
     * @param order - Eine Order mit dem neuen Auftrag
     */
    public void receiveOrder(Order order);

    
    // Getterkrams? Was brauchen wir davon, was muss kommentiert werden? ~Sven
    
    
    /**
     * Ergaenzug team2
     * Gibt die Item map des Robots nach aussen um seine Auftraege abgreifbar zu machen
     * @return Item MAP
     */
    public Order Order();
    
    //getter
    public int StartPosX();
    public int StartPosY();
    public int CurrentPosX();
    public int CurrentPosY();
    public boolean isBusy();
    public int id();
    
    /**
     * getter fuer die momentane Destination des Robots
     * @return Zielkoordinaten (gedreht [Y][X])
     */
    public int[] Target();
    
    public String OrderInfos();

    public int ItemLoadTime();
}
