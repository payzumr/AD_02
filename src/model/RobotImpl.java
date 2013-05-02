package model;

import java.text.DecimalFormat;
import java.util.*;
import java.util.Map.Entry;

public class RobotImpl implements Robot {
    private final int id;
    private final int startPosX;
    private final int startPosY;
    private int lastPosX;
    private int lastPosY;
    private int currentPosX;
    private int currentPosY;
    private boolean busy;
    private Status status;
    private Field[][] field;
    
    private int[] target;
    
    private Map<Item, Integer> order;
    private Random random = new Random();
    private DecimalFormat df = new DecimalFormat("00");

    public RobotImpl(int id, int startPosX, int startPosY, Field[][] field) {
        this.id = id;
        this.startPosX = startPosX;
        this.startPosY = startPosY;
        this.currentPosX = startPosX;
        this.currentPosY = startPosY;
        this.field = field;
        this.status = Status.IDLE;
        busy = false;
    }

    public void receiveOrder(Map<Item, Integer> order) {
        this.order = order;
    }

    /**
     * Bewegt den Robot zum naechsten Ziel und wenn die Order leer ist zu seiner BoxingPlant zurueck.
     */
    public void action() {

        // Wenn die Order nicht leer ist, wird das naechste Feld gesucht
        if (order != null && !order.isEmpty()) {

            busy = true;

            // Speichert in target das naechste Feld
            this.target = destination();

            // Bewegung zum naechsten Feld
            switch (findWay(this.target[0], this.target[1])) {
            case 'N':
                tryMove(-1, 0);
                fetch();
                break;
            case 'S':
                tryMove(+1, 0);
                fetch();
                break;
            case 'W':
                tryMove(0, -1);
                fetch();
                break;
            case 'E':
                tryMove(0, +1);
                fetch();
                break;
            case 'A':
                load(); // Load Ausgabe auf der Konsole
                remove(); // Eintrag entfernen, nachdem der Robot angekommen ist
                break;
            }

        // Wenn die Order leer ist, bewegt sich der Robot zurueck zu seiner BoxingPlant
        } else {
            switch (findWay(startPosY, startPosX)) {
            case 'N':
                tryMove(-1, 0);
                break;
            case 'S':
                tryMove(+1, 0);
                break;
            case 'W':
                tryMove(0, -1);
                break;
            case 'E':
                tryMove(0, +1);
                break;
            case 'A':
                busy = false; // Robot ist bei seiner BoxingPlant angekommen
                break;
            }
        }
    }

    /**
     * Speichert die Koordinaten des ersten Elements aus der Map
     * und die Startposition des Robots.
     * @return Wenn die Liste leer ist, wird die Startposition zurueckgegeben,
     *         ansonsten die Koordinaten des ersten Elements aus der Liste.
     */
    public int[] destination() {

        int[] result;
        int[] startPos = { startPosY, startPosX };
        int[] itemPos = { ((TreeMap<Item, Integer>) order).firstKey().productPosY(),
                ((TreeMap<Item, Integer>) order).firstKey().productPosX() };

        // Solange noch Items uebrig sind, hole das erste Item aus der TreeMap
        if (!order.isEmpty()) {
            result = itemPos;

        // Wenn keine Items mehr da sind, kehre zur Startposition zurueck
        } else {
            result = startPos;
        }
        return result;
    }

    /**
     * Findet die Richtung, in die sich der Robot auf dem Weg zum Item bewegt
     * 
     * @param positionY Y-Ziel-Koordinate des uebergebenen Items
     * @param positionX X-Ziel-Koordinate des uebergebenen Items
     * @return Gefundene Richtung (N, S, W, E) oder A (arrived at item)
     */
    private char findWay(int destinationY, int destinationX) {
        if (destinationY < currentPosY) {
            return 'N';
        } else if (destinationY > currentPosY) {
            return 'S';
        } else if (destinationX < currentPosX) {
            return 'W';
        } else if (destinationX > currentPosX) {
            return 'E';
        } else {
            return 'A';
        }
    }

    /**
     * Prueft, ob der Robot an die uebergebenen XY-Koordinaten bewegt werden kann.
     * Wenn der Robot sich in der Zeile ueber den BoxingPlants befindet, wird
     * zusaetzlich geprueft, ob er sich zu seiner eigenen BoxingPlant nach links
     * oder rechts bewegen muss.
     * Ist eine Bewegung nicht moeglich, wird die Ausweichroutine ausgefuehrt.
     * @param positionY Y-Koordinate fuer die Bewegungsrichtung (-1 Norden / +1 Sueden)
     * @param positionY Y-Koordinate fuer die Bewegungsrichtung (-1 Westen / +1 Osten)
     * @return Gibt TRUE zurueck, wenn eine Bewegung moeglich war, sonst FALSE.
     */
    private boolean tryMove(int positionY, int positionX) {

        // Prueft, ob das Feld in Richtung der uebergebenen Koordinaten frei ist und bewegt den Robot dorthin
        if (fieldFree(currentPosY + positionY, currentPosX + positionX)) {
            moveTo(currentPosY + positionY, currentPosX + positionX);
            return true;

        // Prueft, ob der Robot sich genau in der Zeile ueber den BoxingPlants befindet und noch im Feld-Array ist
        } else if (currentPosY + 1 < Simulation.N && field[currentPosY + 1][currentPosX].isBoxingPlant()) {

            // Wenn die BoxingPlant des Robot links ist, bewegt er sich nach links
            if (id - 1 < currentPosX && fieldFree(currentPosY, currentPosX - 1)) {
                moveTo(currentPosY, currentPosX - 1);
                return true;

            // Wenn die Boxing Plant des Robot rechts ist, bewegt er sich nach rechts
            } else if (id - 1 > currentPosX && fieldFree(currentPosY, currentPosX + 1)) {
                moveTo(currentPosY, currentPosX + 1);
                return true;
            }
        }

        // Wenn der Robot sich nicht auf die uebergebenen Koordinaten bewegen kann,
        // fuehrt er eine Ausweichroutine aus
        evade();

        return false;
    }

    /**
     * Prueft, ob ein Feld frei ist, oder sich ein Robot darauf befindet
     * 
     * @param positionY Y-Koordinate des Feldes, welches geprueft werden soll
     * @param positionX X-Koordinate des Feldes, welches geprueft werden soll
     * @return TRUE bei freiem Feld, sonst FALSE
     */
    private boolean fieldFree(int positionY, int positionX) {
        if (positionY < Simulation.N && positionX < Simulation.N
                && positionY > -1 && positionX > -1
                && (!field[positionY][positionX].isBoxingPlant()
                        || (positionY == startPosY && positionX == startPosX)) && field[positionY][positionX].robotID() == 0) {
            return true;
        }

        return false;
    }

    /**
     * Bewegt den Robot auf die Position der uebergebenen XY-Koordinaten
     * 
     * @param positionY Y-Koordinate des uebergebenen Feldes
     * @param positionX X-Koordinate des uebergebenen Feldes
     * @return Anzahl der Robots auf dem uebergebenem Feld
     */
    private int moveTo(int positionY, int positionX) {

        // Robot vom aktuellen Feld abmelden
        field[this.currentPosY][this.currentPosX].unReg();

        // Aktuelle Koordianten speichern
        lastPosY = this.currentPosY;
        lastPosX = this.currentPosX;

        // Uebergebene Koordinaten zuweisen
        this.currentPosY = positionY;
        this.currentPosX = positionX;

        // Robot auf uebergebenem Feld anmelden
        field[positionY][positionX].reg(this);

        return field[positionY][positionX].hasRobots();
    }

    /**
     * Generiert eine Random Zahl, die entweder den Wert 1 oder -1 hat
     * @return Random mit dem zufaelligen Wert 1 oder -1
     */
    private int random() {
        return random.nextBoolean() ? 1 : -1;
    }

    /**
     * Ausweichroutine fuer Robot
     * 
     * Ist der Weg eines Robot blockiert, weicht dieser dem Hindernis anhand von
     * zufaelligen Werten fuer random1 und random2 aus.
     * random1 und random2 stehen mit ihren moeglichen Werten -1 und 1 fuer eine
     * Bewegung nach links oder rechts, bzw. oben oder unten auf den Field Array.
     * 
     * Zuerst wird in X-Richtung geprueft, ob ein Feld links oder rechts frei ist.
     * Dann wird in Y-Richtung geprueft, ob ein Feld oben oder unten frei ist.
     * Findet sich kein freies Feld, bleibt der Robot bis zum naechsten Takt auf der aktuellen Position.
     */
    private void evade() {

        int random1 = random();
        int random2 = (random1 == -1) ? 1 : -1;

        // Abhaengig von random1 nach links oder rechts ausweichen
        if ((currentPosY + currentPosX + random1 - lastPosY - lastPosX != 0) && fieldFree(currentPosY, currentPosX + random1)) {
            moveTo(currentPosY, currentPosX + random1);

        // Fuehrt random1 nicht zum Erfolg, wird versucht mit random2 in die Gegenrichtung auszuweichen
        } else if ((currentPosY + currentPosX + random2 - lastPosY - lastPosX != 0) && fieldFree(currentPosY, currentPosX + random2)) {
            moveTo(currentPosY, currentPosX + random2);

        // Abhaengig von random1 nach unten oder oben ausweichen
        } else if ((currentPosY + random1 + currentPosX - lastPosY - lastPosX != 0) && fieldFree(currentPosY + random1, currentPosX)) {
            moveTo(currentPosY + random1, currentPosX);

        // Fuehrt random1 nicht zum Erfolg, wird versucht mit random2 in die Gegenrichtung auszuweichen
        } else if ((currentPosY + currentPosX + random2 - lastPosY - lastPosX != 0) && fieldFree(currentPosY + random2, currentPosX)) {
            moveTo(currentPosY + random2, currentPosX);

        // Wenn keine Bewegung moeglich ist, bleibt der Robot bis zum naechsten Takt stehen 
        } else if (fieldFree(lastPosY, lastPosX)) {
            moveTo(lastPosY, lastPosX);
        }
    }

    /**
     * Entfernt den ersten Eintrag aus der Map, nachdem das Item geholt wurde.
     * @return Gibt das entfernte Item zurueck.
     */
    public Entry<Item, Integer> remove() {
        return ((TreeMap<Item, Integer>) order).pollFirstEntry();
    }

    private void fetch() {
        System.out.println("Robot [" + df.format(this.id()) + "]: " + "Gehe zu Position Y: " + df.format(currentPosY) + " X: " + df.format(currentPosX));
    }

    private void load() {
        System.out.println("Robot [" + df.format(this.id()) + "]: Lade Item bei Y: " + df.format(currentPosY) + " X: " + df.format(currentPosX));
    }

    public int id() {
        return id;
    }

    public boolean isBusy() {
        return busy;
    }

    @Override
    public int getStartPosX() {
        return this.startPosX;
    }

    @Override
    public int getStartPosY() {
        return this.startPosY;
    }

    @Override
    public int getCurrentPosX() {
        return this.currentPosX;
    }

    @Override
    public int getCurrentPosY() {
        return this.currentPosY;
    }

    @Override
    public Status getStatus() {
        return this.status;
    }
    
    public String getOrderInfos(){
    	System.out.println(order.toString());
    	return order.toString();
    }

	@Override
	public Map<Item, Integer> getOrder() {
		return this.order;
	}

    @Override
    public int[] getTarget() {
        return this.target;
    }



}
