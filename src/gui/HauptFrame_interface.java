package gui;

import model.*;


public interface HauptFrame_interface {
	/* Aktualisiert Warenhaus ansicht*/  
    public void showRobotState(int robotName, int xPos,int yPos, Item[] ladung, int xZiel, int yZiel, Status status);
    
    /* Zeigt Fehlermeldung bei Fehlverhalten*/
    public void abbruch(String Fehlermeldung);
      
    /* Zeigt Übersicht nach erfolgreichem Abarbeiten der Auftraege*/
    public void beendet(int takte, int anzahlAuftraege);
    
    
    public void newWarehouse(int fields, int stations,int robots);

}

