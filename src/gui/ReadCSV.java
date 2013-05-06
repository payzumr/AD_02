package gui;

import java.io.*;
import java.util.*;

import model.*;

public class ReadCSV {
    private String delimiter;
    private final ArrayList<String> liste;
    private List<String> orderArr;
	private final Set<Item> itemSet = new TreeSet<>();
	private String zeile = null;

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
		tmpFile.close();
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
		
		tmpFile.close();
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
		tmpFile.close();
		return itemList;

	}

	// Order einlesen
	public List<Order> readOrder(List<Item> item) {
		
		//Diese Liste beinhaltet Listenpaare von den ids und Mengen der items
		//index 0 und 1 zusammen,Index 2 und 3 zusammen ... etc. pp.
		List<List<Integer>> listpairs = new ArrayList<>();
		//Jonas und philipp reworked start
        for (Object anOrderArr : orderArr) {
            List<Integer> tmpids = new ArrayList<>();
            List<Integer> mengen = new ArrayList<>();
            zeile = (String) anOrderArr;

            String ids = (zeile.split(";")[0]);
            String mengenstrings = (zeile.split(";")[1]);

            for (int j = 0; j < ids.split(",").length; j++) {
                tmpids.add(Integer.parseInt(ids.split(",")[j]));
            }

            for (int k = 0; k < mengenstrings.split(",").length; k++) {
                mengen.add(Integer.parseInt(mengenstrings.split(",")[k]));
            }
            listpairs.add(tmpids);
            listpairs.add(mengen);


        }
		
		for(int i = 0;i<listpairs.size();i++){
			int gewichtgesamt = 0;
			for(int j = 0;j<listpairs.get(i).size();j++){
                for (Item anItem : item) {
                    if (anItem.id() == listpairs.get(i).get(j)) {
                        gewichtgesamt += anItem.size() * listpairs.get(i + 1).get(j);
                    }
                }
		}
			if(gewichtgesamt > Simulation.ORDERMAXSIZE){
				//Hier wird "ein Auftrag entfernt" wenn das Gewicht zu hoch ist
				String idlisten = "";
				String mengenausgabe = "";
				for(int y = 0;y<listpairs.get(i).size();y++){
					idlisten = idlisten + listpairs.get(i).get(y) + ",";
				}
				for(int y = 0;y<listpairs.get(i+1).size();y++){
					mengenausgabe = mengenausgabe + listpairs.get(i+1).get(y) + ",";
				}
				System.out.println("Auftrag mit den Items: " + idlisten + "und den Mengen: " + mengenausgabe + "ist zu schwer!   Gewicht" + gewichtgesamt + "/" + Simulation.ORDERMAXSIZE);
				listpairs.remove(i);
				listpairs.remove(i);
				i--;
			} else {
				i++;
			}
		}
		
		//Alle relevanten Items dem ItemSet hinzufügen,diese sind später auf dem Field sichtbar
		for(int i = 0;i<listpairs.size();i=i+2){
			for(int j = 0;j<listpairs.get(i).size();j++){
                for (Item anItem : item) {
                    if (listpairs.get(i).get(j) == anItem.id()) {
                        itemSet.add(anItem);
                    }
                }
			}
		}
		List<TreeMap<Item,Integer>> retmap = new ArrayList<>();
		for(int i = 0;i<listpairs.size();i=i+2){
			TreeMap<Item,Integer> tmp = new TreeMap<>();
			for(int j = 0;j<listpairs.get(i).size();j++){
                for (Item anItem : item) {
                    if (anItem.id() == listpairs.get(i).get(j)) {
                        tmp.put(anItem, listpairs.get(i + 1).get(j));
                    }
                }
		}
			retmap.add(tmp);
		}
	
		List<Order> orderList = new ArrayList<>();

        for (TreeMap<Item, Integer> aRetmap : retmap) {
            orderList.add(new Order(aRetmap));
        }
		
		return orderList;
	}
	
	public Set<Item> getItemSet()
	{
		return this.itemSet;
	}

}
