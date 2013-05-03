package gui;

import java.util.List;
import java.util.Set;

import model.*;


public interface HauptFrame_interface {
	/* Aktualisiert Warenhaus ansicht*/  
    //public void showRobotState(int robotName, int xPos,int yPos, Item[] ladung, int xZiel, int yZiel, Status status, Set<Item> item, int loadTime);
	public void showRobotState(Robot rob, Set<Item> item, int loadingTime, int xZiel, int yZiel, final int packingTime);
    /* Zeigt Fehlermeldung bei Fehlverhalten*/
    public void abbruch(String Fehlermeldung);
      
    /* Zeigt Übersicht nach erfolgreichem Abarbeiten der Auftraege*/
    public void beendet(int takte, int anzahlAuftraege);
    
    
    public void newWarehouse(int fields, int stations,int robots, Set<Item> item);

}

