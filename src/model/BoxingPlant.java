package model;

import java.text.DecimalFormat;
import java.util.Map.Entry;

public class BoxingPlant implements IBoxingPlant {
    private int amountOfRobots;
    private final int coordinateX;
    private final int coordinateY;
    private final int ID;
    private final IRobot assignedrobot;
    private IRobot robotOnField;
    private Order order;
    private boolean busy;
    private int packingTime;
    private final int temp_CLTIME =  Simulation.CLTIME;
    private int temp_CLTIME_cnt = temp_CLTIME;
    private final DecimalFormat df = new DecimalFormat("00");

    public BoxingPlant(int id, int x, int y, IRobot bot) {
        assignedrobot = bot;
        amountOfRobots = 1;
        busy = false;
        coordinateX = x;
        coordinateY = y;
        ID = id;
    }

    public void processOrder() {

        // Wenn eine bestellung vorliegt und der Robot nicht unterwegs ist
        if(order != null && !assignedrobot.isBusy()) {
            // gib dem Robot bestellung
            // und loesche die Bestellliste
            System.out.println("BoxingPlant [" + df.format(this.id()) + "]: Bekomme Order " + order.toString());
            assignedrobot.receiveOrder(order);
            order = null;
        }

        // Wenn der Roboter unterwegs ist, wird nur eine action 
        // nach Ablauf des Takt counters ausgeloest
        if(order == null && assignedrobot.isBusy() && temp_CLTIME_cnt-1 != 0) {
            temp_CLTIME_cnt--;
        } else {
            assignedrobot.processOrder();

            temp_CLTIME_cnt = temp_CLTIME;
        } 

        // Wenn keine Bestelliste vorliegt, robot nicht (mehr) unterwegs ist,
        // aber packingTime > 0 ist, muss noch eine Vestellung verpackt werden 
        if(order == null && !assignedrobot.isBusy() && packingTime != 0) {
            System.out.println("BoxingPlant [" + df.format(this.id()) + "]: Verpacke Order");

            packingTime--;
        } 

        // Nach dem Verpacken ist die bplant fertig
        if(order == null && !assignedrobot.isBusy() && packingTime == 0){
            busy = false;
        }
    }

    public void receiveOrder(Order order) {
        // Bestellung entgegennehmen
        this.order = order;
        
      
        // Gesamtgewicht merken
        for (Entry<Item, Integer> element : order.Map().entrySet()) {
            packingTime += (element.getValue() * element.getKey().size());
        }

        // Zustand von busy auf true setzen
        busy = true;
    }
    
    /**
     * @return Anzahl vorhandener Roboter.
     */
    public int robotCount() {
        return amountOfRobots;
    }

    /**
     * 
     */
    public int coordinateX() {
        return coordinateX;
    }

    public int coordinateY() {
        return coordinateY;
    }

    public boolean isBoxingPlant() {
        return true;
    }

    public int id() {
        return ID;
    }

    public boolean isBusy() {
        return busy;
    }

    public void registerRobot(IRobot bot) {
        amountOfRobots++;
        robotOnField = bot;
    }

    public void unregisterRobot() {
        amountOfRobots--;
        robotOnField = null;
    }

    public int robotID() {
        if(robotOnField == null){
            return 0;
        }
        return robotOnField.id();
    }
    
    public int[] Target(){
        if(robotOnField != null){
            return robotOnField.Target();
        }
        int i[] = {-1,-1};
        return i;
    }

    public IRobot getRobot() {
        return assignedrobot;
    }

    public int Temp_CLTIME_cnt()
    {
    	return this.temp_CLTIME_cnt;
    }
    
    public int packingTime()
    {
    	return this.packingTime;
    }

    public int loadTime() {
        return assignedrobot.ItemLoadTime();
    }
}
