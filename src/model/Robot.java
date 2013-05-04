package model;

import java.util.Map;

public interface Robot {

    /* 
     * Eingabe:  keine
     * 
     * Ausgabe:  keine
     *   
     * Funktion: Gibt die ID des Robot zuruck
     */
    public int id();

    /*
     * Eingabe:  eine Map die Item und Iteger enthalt
     *
     * Ausgabe:  keine
     *
     * Funktion: Wenn der Robot eine Order hat Arbeitet er alle Items in der liste ab und 
     * 	         wenn die Order leer ist fahrt er zuruck zu seiner Startposition
     * 	    	 Setzt busy auf true wenn er eine Order hat und auf false wenn er
     * 	    	 seine Startposition wieder erreicht hat
     */
    public void action();

    /* 
     * Eingabe:  keine
     *
     * Ausgabe:  keine
     * 
     * Funktion: Zum abfragen ob der Robot beschaftigt ist
     */
    public boolean isBusy();

    /*
     * Eingabe:  eine Map die Item und Iteger enthalt
     *
     * Ausgabe:  keine
     *
     * Funktion: gibt dem Robot eine neue Order
     */
    public void receiveOrder(Order order);

    /**
     * Erg�nzug team2
     * Gibt die Item map des Robots nach au�en um seine Auftr�ge abgreifbar zu machen
     * @return Item MAP
     */
    public Order getOrder();
    
    //JUnit
    public int getStartPosX();
    public int getStartPosY();
    public int getCurrentPosX();
    public int getCurrentPosY();
    public Status getStatus();
    
    /**
     * getter für die momentane Destination des Robots
     * @return Zielkoordinaten (gedreht [Y][X])
     */
    public int[] getTarget();
    
    public String getOrderInfos();
}
