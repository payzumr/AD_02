package model;

/*
 * Diese Klasse soll nicht zum fertigen Programm gehoeren.
 * Ihr koennte hier schnell mal Quick'N'Dirty eure Methoden testen.
 */

import java.util.*;
import java.util.Map.Entry;

public class Test {
    private static void testItem_Order_factory() {
        System.out.println("\ntestItem_Order_factory()\n");

        System.out.println("N: " + Simulation.N);
        System.out.println("NUMBOXINGPLANTS: " + Simulation.NUMBOXINGPLANTS);
        System.out.println("ORDERMAXSIZE: " + Simulation.ORDERMAXSIZE);

        System.out.println("\nItem-Factory:");

        List<Item> items = Item.factory();

        for(Item i: items) {
            System.out.println(i);
        }

        System.out.println("\nOrder-Factory:");

        Map<Item, Integer> order = Order.factory(items);

        for (Entry<Item, Integer> element : order.entrySet()) {
            System.out.println(element.getKey() + " , amount = " + element.getValue());
        }
    }

    private static void testWarehouseImpl_factory() {
        System.out.println("\ntestWarehouseImpl_factory()\n");

//        Warehouse wh = new WarehouseImpl();
//        System.out.println(wh);
    }

    public static void main(String[] args) {
        testItem_Order_factory();
        testWarehouseImpl_factory();
    }

}
