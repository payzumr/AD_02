/**
 * @author Gruppe3!
 * @date 19.04.2013
 */
package model;

/**
 * Enum Klasse zur vereinfachung des Umgangs mit den Bots/Packstationen zwecks Uebersichtlichkeit/Vergleichbarkeit.
 * 
 * @version 1.0 -
 * Diese Klasse befindet in der Entwicklung und kann dementsprechend noch angepasst/veraendert werden!
 * 
 * @IDLE Bot/Packstation wartet auf eine Aufgabe.
 * @BUSY Bot/Packstation ist mit einer Aufgabe beschaeftigt.
 * @BOXING Bot/Packstation entlaedt sich. 
 * @MOVING Bot bewegt sich.
 * @LOADING Bot belaedt sich.
 */
public enum Status {

    IDLE,
    BUSY,
    BOXING,
    MOVING,
    LOADING

}