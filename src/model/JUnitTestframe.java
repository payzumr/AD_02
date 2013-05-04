package model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Test;

/**
 * JUnit Testklasse die Basisfunktionalitäten sicherstellt (Konfektionierung der zur verfuegung stehenden Strukturen).
 * 
 * @version 1.8 -
 * Diese Klasse befindet in der Entwicklung und kann/sollte dementsprechend noch angepasst/veraendert werden!
 * 
 * @testhouse Fuer den Test verwendetes Warehouse Objekt.
 * @testItemList Fuer den Test verwendetes ItemList Objekt.
 * @testOrderMap Fuer den Test verwendetes Order Objekt.
 * @maxTestFieldLength Ergibt im quadrat die goesse des Lagerhauses bis zu der getestet werden soll.
 */
public class JUnitTestframe {
    private Warehouse testHouse;
    private List<Item> testItemList;
    private Order testOrder;

    private final int maxTestFieldLength = 50; // Skalierung

    public static int N;
    public static int NUMBOXINGPLANTS;
    public static int ORDERMAXSIZE;
    private static int MAXCAPACITY;
    private static int NUMROBOTS;
    public static int PPTIME;
    public static int CLTIME;


    /**
     * Setzt {@link aufgabe2_1.Simulation.TEST} = true (Testmodus) und 
     * erzeugt ein neues Warehouse Objekt mit der lokalen variablen {@link aufgabe2_1.JUnitTestfram.testHouse} durch die factory von WarehouseImpl 
     * {@link aufgabe2_1.WarehouseImpl.factory()}.
     * 
     * @return Warehouse Objekt mit {@link aufgabe2_1.JUnitTestfram.testHouse}.
     */
    private void makeTestHouse(int n, int numboxingplants, int ordermaxsize, int numrobots, int pptime){
        Simulation.TEST = true;
        JUnitTestframe.N = n;
        JUnitTestframe.NUMBOXINGPLANTS = numboxingplants; 
        JUnitTestframe.ORDERMAXSIZE = ordermaxsize;
        JUnitTestframe.MAXCAPACITY = ordermaxsize;
        JUnitTestframe.NUMROBOTS = numrobots;
        JUnitTestframe.PPTIME = pptime;
        JUnitTestframe.CLTIME = 0;
        this.testItemList = Item.factory();
        this.testHouse = new WarehouseImpl(this.testItemList);
        //this.testOrder = new Order();
    }

    /**
     * Erzeugung eines zweidimensionalen Arrays ueber Field-Objekte des Typs StorageArea 
     * koennen mit beliebiger Groesse erzeugt werden, die Koordinaten sind nachvollziehbar.
     * 
     * Field Array[][] erzeugen, mit StorageAreas fuellen. 
     * Testen des Arrays auf size(), der einzelnen Objekte auf Koordinaten.
     */
    @Test
    public void testFieldSizeKoord() {
        // Field[N][N] = [i][i]
        for (int i = 1; i < maxTestFieldLength; i++) {
            makeTestHouse(i, 0, 0, 0, 0);
            assertEquals(N, testHouse.getWarehouseArr().length);
            assertEquals(N, testHouse.getWarehouseArr()[0].length);

            // mit j=Y, k=X - durchlaufen des jew. arrays und test der aufsteigend vergebenen koordinaten der jew. StorageArea Objekte.
            for (int j = 0; j < i-1; j++) {
                for (int k = 0; k < i-1; k++) {
                    assertEquals(k, testHouse.getWarehouseArr()[j][k].coordinateX());
                    assertEquals(j, testHouse.getWarehouseArr()[j][k].coordinateY());
                }
            }
        }
    }

    /**
     * Erzeugen von Item-Objekten mit bestimmten Eigenschaften (ID, Position & Size) 
     * und deren Zuweisung an bestimmte StorageAreas ist moeglich.
     * 
     * Fuellen der StorageAreas mit Items (durchnummeriert). --> passiert automatisch in der factory!
     * Testen auf ID, Position und size der einzelnen Item-Objekte.
     */
    @Test
    public void testItems() {
        // Field[N][N] = [i][i] - start bei i=2, da unterste zeile immer Boxingplants vorbehalten ist,
        // ORDERMAXSIZE = 1
        for (int i = 2; i <= maxTestFieldLength; i++) {
            makeTestHouse(i, 0, 1, 0, 0);

            // ID= 1 bis maxTestFieldLength, poductPos: k=X, j=Y (aufsteigend), 
            // size des item immer =1, da ORDERMAXSIZE=1 festgelegt wird.
            int idCnt = 1;
            for (int j = 0; j < i-1; j++) {
                for (int k = 0; k < i; k++) {
                    assertEquals(idCnt, ((StorageArea) testHouse.getWarehouseArr()[j][k]).item().id());
                    idCnt++;

                    assertEquals(k, ((StorageArea) testHouse.getWarehouseArr()[j][k]).item().productPosX());
                    assertEquals(j, ((StorageArea) testHouse.getWarehouseArr()[j][k]).item().productPosY());

                    assertEquals(1, ((StorageArea) testHouse.getWarehouseArr()[j][k]).item().size());
                }
            }
        }
    }

    /**
     * Das Befüllen des Arrays mit einer bestimmten Anzahl von StorageAreas und BoxingPlants 
     * die einem bestimmten Muster entsprechen (BoxingPlants werden hinten im Array eingefügt) 
     * wird sichergestellt.
     * 
     * Konfektionierung des Field Arrays[][] mit StorageAreas und BoxingPlants (<=N). 
     * Testen der Objekte auf Koordinaten.
     */
    @Test
    public void testAreaPlantKoord() {
        // Field[N][N] = [i][i] (angefangen mit 2x2),
        // NUMBOXINGPLANTS = j (1 bis N)
        for (int i = 2; i <= maxTestFieldLength; i++) {
            for (int j = 1; j <= N; j++) {
                makeTestHouse(i, j, 0, 0, 0);

                // Durchlaufen der BoxingPlants
                for (int k = 1; k < j; k++) {
                    assertTrue((testHouse.getWarehouseArr()[i - 1][k]).isBoxingPlant());
                    assertEquals(k,	(testHouse.getWarehouseArr()[i - 1][k]).coordinateX());
                    assertEquals(i - 1,	(testHouse.getWarehouseArr()[i - 1][i - 1]).coordinateY());

                    // Folgend auskommentiertes wird ben�tigt, 
                    // sollte es jemals doch wieder m�glich sein StorageAreas in die unterste Zeile zu setzen...! 

                    //if ((i - (k + 1)) >= 0) {
                    assertEquals(StorageAreaImpl.class, (testHouse.getWarehouseArr()[i - (k + 1)][i - 1]).getClass());
                    //} else {
                    //assertEquals(StorageAreaImpl.class, (testHouse.getWarehouseArr()[i-2][i - 1]).getClass());
                    //System.out.println("Sonderfall Zeilenwechsel:" + (testHouse.getWarehouseArr()[i-2][i - 1]).coordinateX()+","+(testHouse.getWarehouseArr()[i-2][i - 1]).coordinateY());
                    //System.out.println(testHouse);
                    //}
                }
            }
        }
    }

    /**
     * Erzeugen von Item-Objekten mit bestimmten Eigenschaften (ID, Position & Size) 
     * und deren Zuweisung an bestimmte StorageAreas ist mit der Einschränkung möglich, 
     * dass sich hier nun auch Boxingplants im Array befinden bis zu denen nur getestet werden darf.
     * 
     * Befuellte StorageAreas mit Items (durchnummeriert). 
     * Testen auf ID, Position und size der einzelnen Item-Objekte (durchlaufen des Arrays nur bis zu den BoxingPlants).
     */
    @Test
    public void testItemsPlants() {
        // Field[N][N] = [i][i] (angefangen mit 2x2), 
        // NUMBOXINGPLANTS = j (1 bis N), 
        // ORDERMAXSIZE = 1
        for (int i = 2; i <= maxTestFieldLength; i++) {
            for (int j = 1; j <= N; j++) {
                makeTestHouse(i, j, 1, 0, 0);

                // ID= 1 bis maxTestFieldLength, poductPos: k=X, j=Y (aufsteigend), 
                // size des item immer =1, da ORDERMAXSIZE=1 festgelegt wird.
                int idCnt = 1;
                for (int x = 0; x < i; x++) {
                    for (int k = 0; k < i; k++) {
                        // Items bis zur ersten BoxingPlant prüfen 
                        if ((testHouse.getWarehouseArr()[x][k]).isBoxingPlant()) {
                            assertTrue((testHouse.getWarehouseArr()[x][k]).isBoxingPlant());
                        } else {
                            assertEquals(idCnt, ((StorageArea) testHouse.getWarehouseArr()[x][k]).item().id());
                            idCnt++;

                            assertEquals(k, ((StorageArea) testHouse.getWarehouseArr()[x][k]).item().productPosX());
                            assertEquals(x, ((StorageArea) testHouse.getWarehouseArr()[x][k]).item().productPosY());

                            assertEquals(1, ((StorageArea) testHouse.getWarehouseArr()[x][k]).item().size());
                        }
                    }
                }
            }
        }
    }


    /**
     * Erzeugen von Robot-Objekten und deren feste Zuweisung an bestimmte BoxingPlants ist moeglich. 
     * Die Uebereinstimmende Position beider Objekte wird sichergestellt.
     * 
     * Fuellen der BoxingPlants mit Robots. 
     * Testen der RobotID (=BoxingPlantID), der Koordinaten (Start=currentPos).
     * Testen der BoxingPlants.amountOfRobots(=1), BoxingPlants.busy(=false).
     * Testen des BoxingPlantStatus (=idle), RobotStatus(=idle) [siehe enums].
     */
    @Test
    public void testBotPlantKoordStat() {
        // Field[N][N] = [i][i] (angefangen mit 2x2), 
        // NUMBOXINGPLANTS = j (1 bis N), 
        // NUMROBOTS = h (1 bis Anz.BoxinPlants)

        for (int i = 2; i <= maxTestFieldLength; i++) {
            for (int j = 1; j <= N; j++) {
                for (int h = 1; h <= j ; h++) {
                    makeTestHouse(i, j, 0, h, 0);

                    // Durchlaufen der BoxingPlants
                    for (int k = 0; k < j; k++) {
                        //BoxPlant id == Robot ID
                        assertEquals(((BoxingPlant) testHouse.getWarehouseArr()[i - 1][k]).id(), testHouse.getWarehouseArr()[i - 1][k].robotID());

                        //startpos == currPos
                        assertEquals(((BoxingPlant) testHouse.getWarehouseArr()[i - 1][k]).getRobot().getStartPosX(), ((BoxingPlant) testHouse.getWarehouseArr()[i - 1][k]).getRobot().getCurrentPosX());
                        assertEquals(((BoxingPlant) testHouse.getWarehouseArr()[i - 1][k]).getRobot().getStartPosY(), ((BoxingPlant) testHouse.getWarehouseArr()[i - 1][k]).getRobot().getCurrentPosY());
                        //System.out.println("startpos:" + ((BoxingPlant) testHouse.getWarehouseArr()[i - 1][i - k]).getRobot().getStartPosX()+","+((BoxingPlant) testHouse.getWarehouseArr()[i - 1][i - k]).getRobot().getStartPosY() + " CurrPos:" + ((BoxingPlant) testHouse.getWarehouseArr()[i - 1][i - k]).getRobot().getCurrentPosX() + "," + ((BoxingPlant) testHouse.getWarehouseArr()[i - 1][i - k]).getRobot().getCurrentPosY());

                        // amountOfRobots = 1 pro BoxingPlant
                        assertEquals(1, ((BoxingPlant) testHouse.getWarehouseArr()[i - 1][k]).getAmountOfRobots());

                        // BoxingPlant busy = false
                        assertTrue(!((BoxingPlant) testHouse.getWarehouseArr()[i - 1][k]).isBusy());

                        // BoxingPlant/Robot Status = IDLE
                        assertEquals(Status.IDLE, ((BoxingPlant) testHouse.getWarehouseArr()[i - 1][k]).getStatus());
                        assertEquals(Status.IDLE, ((BoxingPlant) testHouse.getWarehouseArr()[i - 1][k]).getRobot().getStatus());

                    }
                }
            }
        }
    }

    /**
     * Erzeugen von einzelnen Order-Objekten, die allen erlaubten Vorgaben genügen wird sichergestellt.
     * 
     * Erstellung von Order-Object Positionen mit legalen Größen (Item-Anzahl durchnummeriert). 
     * Testen auf die jeweilige Anzahl der Items.
     */
    @Test
    public void testOrderPos() {
        // Field[k][k],
        // NUMBOXINGPLANTS = k,
        // ORDERMAXSIZE = k (1 bis N), 
        // MAXCAPACITY = k*k),
        // NUMROBOTS = NUMBOXINGPLANTS,
        for (int k = 2; k <= maxTestFieldLength; k++) {
            makeTestHouse(k, k, 1, k*k, k); // items erstellen mit ORDERMAXSIZE = 1, um rnd zu vermeiden!
            ORDERMAXSIZE = k; // Um mehr als 1 Item zur order hinzuzufuegen zu koennen

            for (int j = 0; j < ORDERMAXSIZE; j++) {
                testOrder.addItem(testItemList.get(j), j+1); // bei 0 beginnen und qunatity +1 => id==Quantity
            }	 

            // sortierbare keylist aus der map erstellen (item ID)
            List<Integer> keyList = new ArrayList<>();
            for (Item i : testOrder.getMap().keySet()) {
                keyList.add(i.id());
            }
            Collections.sort(keyList);

            // sortierbare valuelist aus der map erstellen (quantity)
            List<Integer> valueList = new ArrayList<>();
            for (Integer i : testOrder.getMap().values()) {
                valueList.add(i);
            }
            Collections.sort(valueList);

            // fertig sortierte listen index für index vergleichen --> id==quantity
            for (int j = 0; j < testOrder.getMap().size(); j++) {
                assertEquals(keyList.get(j), valueList.get(j));
            }
        }
    }

    /**
     * Sicherstellung, dass Listen, die Order-Objekte enthalten, den erlaubten Größen entsprechen.
     * 
     * Erstellung von Order-Object Listen mit legalen Größen. 
     * Testen der Order-Size und "sum(itemAnzahlen)<=MAXCAPACITY".
     */
    @Test
    public void testOrderSizes() {
        // Field[k][k],
        // NUMBOXINGPLANTS = k,
        // ORDERMAXSIZE = k (1 bis N),
        // MAXCAPACITY = k,
        // NUMROBOTS = NUMBOXINGPLANTS,
        for (int k = 2; k <= maxTestFieldLength; k++) {
            makeTestHouse(k, k, 1, k, 0); // items erstellen mit ORDERMAXSIZE = 1, um rnd zu vermeiden!
            ORDERMAXSIZE = k; // Um mehr als 1 Item zur order hinzuzufuegen zu koennen

            for (int j = 0; j < ORDERMAXSIZE; j++) {
                testOrder.addItem(testItemList.get(j), j+1); // bei 0 beginnen und quantity +1 => id==Quantity
            }	 

            // Size test
            assertEquals(k, testOrder.getMap().size());

            // summen test
            ORDERMAXSIZE = k*k; 
            Map<Item, Integer> rndTestOrder = Order.factory(testItemList);
            int sum = 0;
            // alle values aufsummieren
            for (Entry<Item, Integer> element : rndTestOrder.entrySet()) {
                sum += element.getValue();
            }

            // summe aller values mit OrdmaxSize vergleichen
            assertTrue(sum <= ORDERMAXSIZE);
        }
    }



    /**
     * Bsp. Fehlerfall
     */
    @Test (expected = NegativeArraySizeException.class)
    public void doofesInit() {
        makeTestHouse(-1, 0, 0, 0, 0);
    }

}
