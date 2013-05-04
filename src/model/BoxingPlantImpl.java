package model;

import java.text.DecimalFormat;
import java.util.*;
import java.util.Map.Entry;

public class BoxingPlantImpl implements BoxingPlant {
    private int amountOfRobots;
    private int coordinateX;
    private int coordinateY;
    private final int ID;
    private int robotId;
    private Robot robot;
    private Order order;
    private boolean busy;
    private int packingTime;
    private final int temp_PPTIME = (Simulation.TEST) ? JUnitTestframe.PPTIME : Simulation.PPTIME;
    private final int temp_CLTIME = (Simulation.TEST) ? JUnitTestframe.CLTIME : Simulation.CLTIME;
    private int temp_CLTIME_cnt = temp_CLTIME;
    private DecimalFormat df = new DecimalFormat("00");
    private Status status;

    public BoxingPlantImpl(int id, int x, int y, Robot bot) {
        robot = bot;
        robotId = bot.id();
        amountOfRobots = 1;
        busy = false;
        coordinateX = x;
        coordinateY = y;
        ID = id;
        this.status = Status.IDLE;
    }

    public void action() {

        // Wenn eine bestellung vorliegt und der Robot nicht unterwegs ist
        if(order != null && !robot.isBusy()) {
            // gib dem Robot bestellung
            // und loesche die Bestellliste
            System.out.println("BoxingPlant [" + df.format(this.id()) + "]: Bekomme Order " + order.toString());
            robot.receiveOrder(order);
            order = null;
        }

        // Wenn der Roboter unterwegs ist, wird nur eine action 
        // nach Ablauf des Takt counters ausgeloest
        if(order == null && robot.isBusy() && temp_CLTIME_cnt-1 != 0) {
            temp_CLTIME_cnt--;
        } else {
            robot.action();

            temp_CLTIME_cnt = temp_CLTIME;
        } 

        // Wenn keine Bestelliste vorliegt, robot nicht (mehr) unterwegs ist,
        // aber packingTime > 0 ist, muss noch eine Vestellung verpackt werden 
        if(order == null && !robot.isBusy() && packingTime != 0) {
            System.out.println("BoxingPlant [" + df.format(this.id()) + "]: Verpacke Order");

            packingTime--;
        } 

        // Nach dem Verpacken ist die bplant fertig
        if(order == null && !robot.isBusy() && packingTime == 0){
            busy = false;
        }
    }

    public void receiveOrder(Order order) {
        // Bestellung entgegennehmen
        this.order = order;
        
      
        // Gesamtgewicht merken
        for (Entry<Item, Integer> element : order.getMap().entrySet()) {
            packingTime += element.getValue();
        }

        // Reale Packzeit berechnen
        packingTime *= temp_PPTIME;

        // Zustand von busy auf true setzen
        busy = true;
    }

    public int hasRobots() {
        return amountOfRobots;
    }

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

    public void reg(Robot bot) {
        amountOfRobots++;
        this.robotId = bot.id();
    }

    public void unReg() {
        amountOfRobots--;
        this.robotId = 0;
    }

    public int robotID() {
        return robotId;
    }

    public Robot getRobot() {
        return robot;
    }

    public int getAmountOfRobots() {
        return amountOfRobots;
    }

    public Status getStatus() {
        return status;
    }
    
    public int getTemp_CLTIME_cnt()
    {
    	return this.temp_CLTIME_cnt;
    }
    
    public int getPackingTime()
    {
    	return this.packingTime;
    }
}
