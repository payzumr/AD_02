package model;

import java.util.Map;

public interface IOrder {

    /*
     * Eingabe:  keine
     *
     * Ausgabe:  boolean ob die Map der Order leer ist
     */
    public boolean  isEmpty();

    /*
     * Eingabe:  keine
     * 
     * Ausgabe:  Die Artikel der Order in einer Map mit dem Item als Key und der Menge als Value
     */
    public Map<Item, Integer> Map();

    /*
     * Eingabe:  keine
     * 
     * Ausgabe:  Die Id der Order als String
     */
    public String OrderId();
    
}
