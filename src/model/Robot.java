package model;

import java.text.DecimalFormat;
import java.util.*;
import java.util.Map.Entry;

public class Robot implements IRobot {
    private final int id;
    private final int startPosX;
    private final int startPosY;
    private int currentPosX;
    private int currentPosY;
    private boolean busy;
    private final IField[][] field;
    private int blockCounter = 0;
    private int loadedTime;
    private int[] target;

    private Order order;
    private final DecimalFormat df = new DecimalFormat("00");

    public Robot(int id, int startPosX, int startPosY, IField[][] field) {
        this.id = id;
        this.startPosX = startPosX;
        this.startPosY = startPosY;
        this.currentPosX = startPosX;
        this.currentPosY = startPosY;
        this.field = field;
        busy = false;
    }

    public void receiveOrder(Order order) {
        this.order = order;
    }

    /**
     * Bewegt den Robot zum naechsten Ziel und wenn die Order leer ist zu seiner
     * BoxingPlant zurueck.
     */
    public char processOrder() {
        char direction;
        if (order != null) {

                this.target = destination();
 
            busy = true;

            // Speichert in target das naechste Feld
            this.target = destination();

            direction = findWay(this.target[0], this.target[1]);

            // Bewegung zum naechsten Feld
            switch (direction) {
            case 'N':
                moveTo(currentPosY - 1, currentPosX);
                break;
            case 'S':
                moveTo(currentPosY + 1, currentPosX);
                break;
            case 'W':
                moveTo(currentPosY, currentPosX - 1);
                break;
            case 'E':
                moveTo(currentPosY, currentPosX + 1);
                break;
            case 'A':
                if (order != null && !order.isEmpty()) {
                    if (ItemLoadTime() > 0) {
                        loadedTime++;
                        System.out.println("Robot [" + df.format(this.id()) + "]: Lade Item bei Y: " + df.format(currentPosY) + " X: " + df.format(currentPosX) + " Timeleft: " + df.format(ItemLoadTime()));
                    } else {
                        remove(); // Eintrag entfernen, nachdem der Robot angekommen ist                       
                    }
                } else {
                    busy = false;
                }
                break;
            default:
                break;
            }
            return direction;
        }
        return 'F';
    }

    /**
     * Speichert die Koordinaten des ersten Elements aus der Map und die
     * Startposition des Robots.
     * 
     * @return Wenn die Liste leer ist, wird die Startposition zurueckgegeben,
     *         ansonsten die Koordinaten des ersten Elements aus der Liste.
     */
    int[] destination() {

        // Solange noch Items uebrig sind, hole das erste Item aus der TreeMap
        if (!order.Map().isEmpty()) {
            int[] itemPos = { ((TreeMap<Item, Integer>) order.Map()).firstKey().productPosY(),
                    ((TreeMap<Item, Integer>) order.Map()).firstKey().productPosX() };
            return itemPos;

            // Wenn keine Items mehr da sind, kehre zur Startposition zurueck
        } else {
            int[] startPos = { startPosY, startPosX };
            return startPos;
        }
 
    }

    
    /**
     * Findet die Richtung, in die sich der Robot auf dem Weg zum Item bewegt
     * Der Robot bewegt sich zu erst auf der x Achse wenn moeglich danach erst
     * auf der y Achse in Richtung ziel Wenn kein Zug mehr moeglich ist prueft
     * er ob die Robots auf den Feld bzw. den Felder auf die er normalerweise
     * ziehen wuerde auf das Feld wollen auf den er steht wenn ja weicht er aus
     * Wenn der Robot sich zulange nicht bewegen kann weicht er ebenfalls aus
     * 
     * @param positionY
     *            Y-Ziel-Koordinate des uebergebenen Items+ * @param positionX
     *            X-Ziel-Koordinate des uebergebenen Items
     * @return Gefundene Richtung (N, S, W, E) oder A (arrived at item)
     */    
    private char findWay(int destinationY, int destinationX) {
        if (destinationX != currentPosX && fieldFree(currentPosY, currentPosX + Integer.compare(destinationX, currentPosX))) {
            if(currentPosY + 1 < field.length &&  currentPosY + 1 < destinationY && field[currentPosY + 1][currentPosX].Target()[1] == destinationX
                    && field[currentPosY + 1][currentPosX].Target()[0] < destinationY){
                return 0;
            }
            
            if (destinationX < currentPosX) {
                return 'W';
            } else if (destinationX > currentPosX) {
                return 'E';
            }
        } else if (destinationY != currentPosY && fieldFree(currentPosY + Integer.compare(destinationY, currentPosY), currentPosX)) {
            if(currentPosX + 1 < field[0].length &&  currentPosX + 1 < destinationX && field[currentPosY][currentPosX + 1].Target()[0] == destinationY
                    && field[currentPosY][currentPosX + 1].Target()[1] < destinationX){
                return 0;
            }
            
            if (destinationY < currentPosY) {
                return 'N';
            } else if (destinationY > currentPosY) {
                return 'S';
            }
        } else if (destinationY == currentPosY && destinationX == currentPosX) {
            return 'A';
        } else if ((currentPosY == destinationY || field[currentPosY + Integer.compare(destinationY, currentPosY)][currentPosX].Target()[0] == currentPosY
                && field[currentPosY + Integer.compare(destinationY, currentPosY)][currentPosX].Target()[1] == currentPosX)
                && (currentPosX == destinationX || field[currentPosY][currentPosX + Integer.compare(destinationX, currentPosX)].Target()[0] == currentPosY
                && field[currentPosY][currentPosX + Integer.compare(destinationX, currentPosX)].Target()[1] == currentPosX)) {
            return evade(destinationY, destinationX);
        }
        if (blockCounter >= 5) {
            blockCounter = 0;
            return evade(destinationY, destinationX);
        } else {
            blockCounter++;
        }
        return 0;
    }

    /**
     * Prueft, ob ein Feld frei ist, oder sich ein Robot darauf befindet
     * 
     * @param positionY
     *            Y-Koordinate des Feldes, welches geprueft werden soll
     * @param positionX
     *            X-Koordinate des Feldes, welches geprueft werden soll
     * @return TRUE bei freiem Feld, sonst FALSE
     */
    private boolean fieldFree(int positionY, int positionX) {
        return positionY < Simulation.N && positionX < Simulation.N && positionY > -1 && positionX > -1
                && (!field[positionY][positionX].isBoxingPlant() || (positionY == startPosY && positionX == startPosX))
                && field[positionY][positionX].robotID() == 0;

    }

    /**
     * Bewegt den Robot auf die Position der uebergebenen XY-Koordinaten
     * 
     * @param positionY
     *            Y-Koordinate des uebergebenen Feldes
     * @param positionX
     *            X-Koordinate des uebergebenen Feldes
     * @return Anzahl der Robots auf dem uebergebenem Feld
     */
    private int moveTo(int positionY, int positionX) {
        System.out.println("Robot [" + df.format(this.id()) + "]: " + "Gehe zu Position Y: " + df.format(currentPosY) + " X: "
                + df.format(currentPosX));
        // Robot vom aktuellen Feld abmelden
        field[this.currentPosY][this.currentPosX].unregisterRobot();

        // Uebergebene Koordinaten zuweisen
        this.currentPosY = positionY;
        this.currentPosX = positionX;

        // Robot auf uebergebenem Feld anmelden
        field[positionY][positionX].registerRobot(this);
    	loadedTime = 0; 
        return field[positionY][positionX].robotCount();
    }

    /**
     * Ausweichroutine fuer Robot
     * 
     * Prueft je nachdem in welche Richtung der Robot sich bewegen wuerde, wenn
     * kein umliegendes Feld blockiert waere, alle umliegende Felder in einer
     * festen Reihenfolge, wenn kein freies Feld gefunden wurde wird 0
     * zurueckgegeben Reihenfolge x-Achse N,S,W,E Reihenfolge y-Achse W,E,N,S
     */
    private char evade(int destinationY, int destinationX) {
        if (currentPosX != destinationX) {
            if (fieldFree(currentPosY - 1, currentPosX)) {
                return 'N';
            } else if (fieldFree(currentPosY + 1, currentPosX)) {
                return 'S';
            } else if (fieldFree(currentPosY, currentPosX - 1)) {
                return 'W';
            } else if (fieldFree(currentPosY, currentPosX + 1)) {
                return 'E';
            } else {
                return 0;
            }
        } else {
            if (fieldFree(currentPosY, currentPosX - 1)) {
                return 'W';
            } else if (fieldFree(currentPosY, currentPosX + 1)) {
                return 'E';
            } else if (fieldFree(currentPosY - 1, currentPosX)) {
                return 'N';
            } else if (fieldFree(currentPosY + 1, currentPosX)) {
                return 'S';
            } else {
                return 0;
            }
        }
    }

    /**
     * Entfernt den ersten Eintrag aus der Map, nachdem das Item geholt wurde.
     * 
     * @return Gibt das entfernte Item zurueck.
     */
    Entry<Item, Integer> remove() {
        return ((TreeMap<Item, Integer>) order.Map()).pollFirstEntry();
    }

    public int id() {
        return id;
    }

    public boolean isBusy() {
        return busy;
    }

    @Override
    public int StartPosX() {
        return this.startPosX;
    }

    @Override
    public int StartPosY() {
        return this.startPosY;
    }

    @Override
    public int CurrentPosX() {
        return this.currentPosX;
    }

    @Override
    public int CurrentPosY() {
        return this.currentPosY;
    }

    public String OrderInfos() {
        return order.toString();
    }

    @Override
    public Order Order() {
        return this.order;
    }

    @Override
    public int[] Target() {
        return this.target;
    }

    public int ItemLoadTime() {
        if(order == null || order.isEmpty()) {
            return 0;
        }
        Entry<Item, Integer> orderEntry = ((TreeMap<Item, Integer>)order.Map()).firstEntry();
        Item item = orderEntry.getKey();
        Integer itemCount = orderEntry.getValue();
        int loadtime = item.size() * itemCount;
        if(loadtime - loadedTime > 0) {
            return loadtime - loadedTime;
        } else {
            return 0;
        }
    }

}
