package gui;

import java.util.Set;

import model.*;


public interface IHauptFrame {
	
	 /**
     * Zeugt den State des Warehouses.
     * 
     * @param fields: Die Anzahl der Felder.
     * @param stations: Die Anzahl der Stationen.
     * @param robots: Die Anzahl der Roboter.
     * @param item: Welches Item-Set genommen wird.
     */
	public void showWarehouseState(IWarehouse whouse,IRobot rob, Set<Item> item, int loadingTime, int xZiel, int yZiel, final int packingTime);

	/**
     * Gibt Fehlermeldungen bei etwaigtem Fehlverhalten aus.
     * 
     * @param fehlermeldung: Ein String mit der Fehlermeldung.
     */
    public void showErrors(String fehlermeldung);
      
    /**
     * Zeigt eine Übersicht nach erfolgreichem Abarbeiten der Aufträge.
     * 
     * @param takte: Die aktuelle Taktanzahl.
     * @param anzahltAuftraege: Die aktuelle Anzahl der Aufträge.   
     */
    public void showFinalOverview(int takte, int anzahlAuftraege);
    
    /**
     * Erstellt ein neues Warehouse.
     * 
     * @param fields: Die Anzahl der Felder.
     * @param stations: Die Anzahl der Stationen.
     * @param robots: Die Anzahl der Roboter.
     * @param item: Welches Item-Set genommen wird.
     */
    public void newWarehouse(int fields, int stations,int robots, Set<Item> item);

}

