package gui;

/**
 * @author Gruppe 1 
 *Implementierende Klassen verwalten die Simulation.
 *(Folgendes in die implementierede Klasse)
 *Geplant ist das Einladen von Konfigdaten aus einer CSV Datei.
 *(Syntax wird im file und Methodenrumpf beschrieben)
 *Aus den Konfigurationsdaten wird dann die Simulation
 *initialisiert.
 *Die Simulation kann von außen wärend des laufens 
 *beeinflusst werden , hierfür sind die folgenden Methoden des 
 *Interfaces vorgesehen 
 */
public interface Control {
	
	/**
	 * Lässt den nächsten Simulationsschritt sofort ausführen
	 */
	public void next_simuation();
	
	/**
	 * Hällt den Simulationszyklus an
	 */
	public void pause_simuation();
	
	/**
	 * Setzt die Simulation im Standard Interwall fort
	 */
	public void run_simuation();
	
	
	//Optional
	//public void restart_simulation();
	
	//Vermerk notwengig an gui???
	//Aufbereitetes Feld in Format auf das wir  uns einigen müssen
	//Aktuelle Informationen über Roboter und Auftragsabarbeitung + (Urauftrag?)
		
	//Vermerk von GUI ??
	//Konstruktor mit notwendigen Initdaten
}
