package model;

import java.util.*;

public class Warehouse implements IWarehouse {
    private IField[][] warehouse;
    private Queue<Order> orderQueue;
    private final List<Order> orderCopy = new ArrayList<>(); // Liste mit den Orders vom Anfang, dient zur Darstellung im TextFrame
    private IBoxingPlant[] bplants;
    private boolean done;
    private boolean firstcall = true;

    public Warehouse() {
        new Warehouse(Item.factory());
    }

    public Warehouse(List<Item> itemList) {
        int temp_N = Simulation.N;
        int temp_NUMBOXINGPLANTS = Simulation.NUMBOXINGPLANTS;

        warehouse = new IField[temp_N][temp_N];
        orderQueue = new LinkedList<>();
        bplants = new IBoxingPlant[temp_NUMBOXINGPLANTS];
        done = false;

        // Alle vorgesehene Fields mit StorageAreaImpl initialisieren und Items zuweisen
        for(Item i : itemList) {
            warehouse[i.productPosY()][i.productPosX()] = new Field(i);
        }

        // Alle vorgesehene Fields mit BoxingPlantImpl initialisieren und RobotImpl zuweisen
        IRobot tmpBot;
        int count = 1;

        // temp_N-1 = Letzte Zeile
        for (int i = 0; i < warehouse[temp_N - 1].length; i++) {

            // Nur Zuweisungen bei null vornehmen
            if (warehouse[temp_N - 1][i] == null) {

                if (i < temp_NUMBOXINGPLANTS) {

                    // Robot erstellen
                    tmpBot = new Robot(count, i, temp_N - 1, warehouse);

                    // boxingplant erstellen
                    warehouse[temp_N - 1][i] = new BoxingPlant(count, i, temp_N - 1, tmpBot);

                    // Extra Referenz fuer schnelleren Zugriff auf boxingplant erstellen
                    bplants[count - 1] = (IBoxingPlant) warehouse[temp_N - 1][i];

                } else {

                    // Fake BoxingPlant erstellen (unterste Zeile nur fuer BoxingPlants reservieren)
                    warehouse[temp_N - 1][i] = new Field(count, i, temp_N - 1);
                }
                count++;
            }
        }
    }

    public void processOrder() {
    	if(firstcall){
    		firstcall = false;
    		for(int i = 0;i<orderQueue.size();i++){
    			orderCopy.add((Order) orderQueue.toArray()[i]);
    		}
    	}
    	
        // Index fuer eine bPlant, die idle ist
        int idle;

        // Wenn alle bPlants fertig sind und keine weiteren Bestelungen vorliegen
        if (bPlantsDone() && orderQueue.isEmpty()) {
            done = true;
        }

        // Wenn Bestellungen vorliegen
        for (IBoxingPlant bplant : bplants) {
            if (!orderQueue.isEmpty()) {
                // freie bPlant suchen
                idle = findIdleBPlant();

                // dieser die Bestellung zuweisen
                if (idle != 0) {
                    bplants[idle - 1].receiveOrder(orderQueue.remove());

                }
            }

            // allen bplants nacheinander ein action-Signal geben
            bplant.processOrder();
        }
    }

    public void takeOrder(Order order) {
        orderQueue.add(order);
        done = false;
    }

    /*
     * Der return wert ist der index + 1
     * Der return wert ist 0, falls es keine idle bplant gibt
     */
    private int findIdleBPlant() {
        int ret = 0;

        for (int i = 0; i < bplants.length; i++) {
            if (!bplants[i].isBusy()) {
                return i + 1;
            }
        }

        return ret;
    }

    /*
     * Kontrolliert ob alle bplants fertig sind
     */
    private boolean bPlantsDone() {
        for (IBoxingPlant bplant : bplants) {
            if (bplant.isBusy()) {
                return false;
            }
        }

        return true;
    }

    public boolean notDone() {
        return !done;
    }
   

    @Override
    public IField[][] WarehouseArr() {
        return this.warehouse;
    }

    @Override
    public Queue<Order> OrderQueue() {
        return this.orderQueue;
    }
    
    public List<Order> Order() {
    	return this.orderCopy;
    }

    @Override
    public IBoxingPlant[] Bplants() {
        return this.bplants;
    }



}
