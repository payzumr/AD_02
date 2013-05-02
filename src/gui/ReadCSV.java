package gui;

import java.io.*;
import java.util.*;
import model.*;

public class ReadCSV {
    private String delimiter;
    private final ArrayList<String> liste;
    private ArrayList orderArr;
	private String zeile = null;
	private int counter = 0;

	// Konstruktor
	// bekommt als parameter den pfad der ini datei als string übergeben.
	public ReadCSV(String inipfad) throws IOException {
		// liste initialisieren
		liste = new ArrayList<>();
		// ini datei einlesen und die einzelnen pfade speichern,.
		try (BufferedReader bReader = new BufferedReader(
				new FileReader(inipfad))) {
			String line;
			while ((line = bReader.readLine()) != null) {
				liste.add(line);
			}

		}

		// orderpfad hollen und in einem bufferedReader speichern.
        File orderFile = new File(liste.get(2).split("=")[1].trim());
		BufferedReader tmpFile = new BufferedReader(new FileReader(orderFile));

		orderArr = new ArrayList<String>();

		// Header überspringen
		tmpFile.readLine();

		// Zeilen einlesen und in einem liste speichern.
		while ((zeile = tmpFile.readLine()) != null) {
			// if(zeile.length()!=1)
			orderArr.add(zeile);
		}
	}

	// config.csv laden und die Variablen initialisieren
	public void readConfig() throws IOException {
		/* liste inhalt zugehörigen variable zuweisen */
		// pfade von config und Delimeter wird gespeichert
        File configFile = new File(liste.get(0).split("=")[1].trim());
		this.delimiter = liste.get(3).split(" ")[2].trim();

		BufferedReader tmpFile = new BufferedReader(new FileReader(configFile));

		// header überspringen
		tmpFile.readLine();

		// die Zeile mit den einstellungen von der config einlesen und in zeile
		// speichern
		String zeile = tmpFile.readLine();

		// Variable zeile splitten und die statics initialisieren
		Simulation.N = Integer.parseInt(zeile.split(delimiter)[0]);
		Simulation.NUMBOXINGPLANTS = Simulation.N;
		Simulation.ORDERMAXSIZE = Integer.parseInt(zeile.split(delimiter)[1]);
		Simulation.MAXCAPACITY = Simulation.ORDERMAXSIZE;
		Simulation.NUMROBOTS = Simulation.N;
		Simulation.CLTIME = Integer.parseInt(zeile.split(delimiter)[2]);
		Simulation.PPTIME = Integer.parseInt(zeile.split(delimiter)[3]);
	}

	// generiert eine Liste mit der vorgegeben lagergröße
	public void writeItems() throws IOException {

		File outfile = new File(liste.get(4).split("=")[1].trim());

		int maxSize;

		int id = 1;

		// Schreibt die items in die csv datei
		try (PrintWriter erzeugteDatei = new PrintWriter(new BufferedWriter(
				new FileWriter(outfile)))) {
			erzeugteDatei.println("item_id" + ";" + "productPosX" + ";"
					+ "productPosY" + ";" + "productSize");

			for (int y = 0; y < Simulation.N - 1; y++) {
				for (int x = 0; x < Simulation.N; x++) {

					if ((x >= Simulation.N - Simulation.NUMBOXINGPLANTS)
							&& y == Simulation.N - 1) {
						break;
					}

					maxSize = (int) ((Math.random()) * Simulation.ORDERMAXSIZE + 1);

					erzeugteDatei.println(id + ";" + x + ";" + y + ";"
							+ maxSize);
					id++;

				}
			}
		}
	}

	// Item liste einlesen
	public List<Item> readItems() throws IOException {

        List<Item> itemList = new ArrayList<>();
		// items.csv pfad speichern
        File itemsFile = new File(liste.get(1).split("=")[1].trim());

		BufferedReader tmpFile = new BufferedReader(new FileReader(itemsFile));

		String zeile;

		// header überspringen
		tmpFile.readLine();

		// solange zeile vorhanden...
		while ((zeile = tmpFile.readLine()) != null) {

			// wenn zeilenlänge größer als 0
			if (zeile.length() > 0) {

				String[] arr = zeile.split(delimiter);

				int id = Integer.parseInt(arr[0]);
				int x = Integer.parseInt(arr[1]);
				int y = Integer.parseInt(arr[2]);
				int size = Integer.parseInt(arr[3]);

				// speicher items von der CSV datein in liste
				itemList.add(new Item(x, y, size, id));

			}

		}

		return itemList;

	}

	// Order einlesen
	public Map<Item, Integer> readOrder(List<Item> item) {

		int tmpId;
		int menge;

		Map<Item, Integer> retMap = new TreeMap<>();

		// flag zum setzten ob order fertig oder nicht
		boolean orderComplete = false;

		Item tempItem = null;
		// den maximal gewicht in einer tmp variable speichern (kapazität)
		int currentMaxSize = Simulation.ORDERMAXSIZE;
		int countSameItem; // zählt wie oft das selbe item beladen werden muss.
		int gw = 0; // gewicht wird hier addiert und darf nicht über kapazität
					// sein

		// solange order "pro" robot noch nicht abgearbeitet wurde
		while (!orderComplete) {

			countSameItem = 0;

			// counter = anzahl an items die aus der orderliste abgearbeitet
			// wurden sind.
			// wenn ich nicht beim item letzten item bin....
			if (counter < (orderArr.size())) {

				// holt die zeile der orderliste (n�chstes item)
				zeile = (String) orderArr.get(counter);

				// hollt die Item id und die menge in tmp variable
				tmpId = Integer.parseInt(zeile.split(";")[0]);
				menge = Integer.parseInt(zeile.split(";")[1]);

				// Solange das selbe item noch eine menge hat und in kapazit�t
				// noch gewicht passt...
				for (int i = 0; i < menge; i++) {

					// items durch laufen
					for (Item element : item) {

						// �berpr�ft item id vom jetzigen mit der von der Item
						// liste
						if (element.id() == tmpId) {
							// speicher elemet (das item ) in die tmp
							tempItem = element;
							// gewicht zu addieren
							gw = gw + tempItem.size();
							// wie oft wurde das selbe item abgearbeitet.
							countSameItem++;
							// verl�sst die schleifen wenn item gefunden
							break;
						}
					}

					//falls das selbe item nicht mehr rein passt
					// dann..
                    if (tempItem != null) {
                        if ((gw + tempItem.size()) > currentMaxSize) {
                            // entfernt item aus mit der Alten menge und f�gt das
                            // item mit der neuen menge hinzu
                            orderArr.remove(counter);

                            orderArr.add(counter, tmpId + ";"
                                    + (menge - countSameItem));
                            retMap.put(tempItem, countSameItem);
                            if ((menge - countSameItem) == 0) {
                                // counter z�hlen um n�chstes item zu hollen
                                counter++;
                            }
                            orderComplete = true;
                            break;
                        }
                    }

                }

			
				if (!orderComplete) {
					// aktuelle item hat komplett gepasst und wird in die orderMap
					// gespeichert
					retMap.put(tempItem, menge);

					int i = counter + 1;
					counter++;

					// �berpr�fe ob das n�chste item noch vom robot zu tragen
					// w�re, ansonsten return die orderMap
					if (i < orderArr.size()) {
						zeile = (String) orderArr.get(i);
						tmpId = Integer.parseInt(zeile.split(";")[0]);

						for (Item element : item) {

							if (element.id() == tmpId) {
								tempItem = element;
								break;
							}
						}

                        if (tempItem != null) {
                            if ((gw + tempItem.size()) > currentMaxSize) {
                                orderComplete = true;
                            }
                        }
                    } else {
						orderComplete = true;
					}

				}

			} else {
				orderComplete = true;
			}

		}
		return retMap;
	}

}
