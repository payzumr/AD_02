package model;

import java.util.*;

public interface IWarehouse {
 
	/**
	 * Die Bestellung von "außerhalb" wird an das Warehouse übergeben.
     *          
	 * 
	 * @param order - ein Auftrag vom Typ Order
	 */
    public void takeOrder(Order order);

    /**		Mit dieser Methode wird dem Warehouse 
     *      ein Taktsignal gegeben. Es wird geprüft, ob ein BoxingPlant frei ist. Falls ja, wird
     *      eine Order dort plaziert.
     */
    public void processOrder();

    /**
     * True, wenn das Warehouse fertig ist, d.h. wenn alle
     *           Bestellungen abgearbeitet sind. False, 
     *           wenn das Gegenteil der falls ist.
     * 
     * @return boolean - true wenn alle Bestellungen abgearbeitet sind.
     */
    public boolean notDone();

    //JUnit
    public IField[][] WarehouseArr();
    public Queue<Order> OrderQueue();
    public List<Order> Order();
    public IBoxingPlant[] Bplants();
}
