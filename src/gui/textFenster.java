/* 
 * Klasse zum Erzeugen der Robot Übersicht
 */

package gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import java.io.File;
import javax.swing.filechooser.FileFilter;
import javax.swing.plaf.IconUIResource;

import model.*;

public class textFenster extends JFrame implements textFenster_interface  {

	int robots;
	JLabel[][] zeile;
	JPanel hauptpane;
	JPanel gridPane;
	JPanel ueberschriftPane;

	private Dimension screen = new Dimension((int) ((Toolkit
			.getDefaultToolkit().getScreenSize().width) * 0.75),
			(int) ((Toolkit.getDefaultToolkit().getScreenSize().height) * 0.75));

	public textFenster(int robots) {
		this.robots = robots;
		
		setTitle("Robot Uebersicht");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocation(700, 200);
		setMinimumSize(new Dimension(300, 300));
		setMaximumSize(screen);

		hauptpane = new JPanel();
		getContentPane().add(hauptpane);
		hauptpane.setLayout(new BorderLayout(0, 0));
		
		ueberschriftPane = new JPanel();
		ueberschriftPane.setLayout(new GridLayout(1, 4));
		hauptpane.add(ueberschriftPane, BorderLayout.NORTH);
		
		gridPane = new JPanel();
		gridPane.setLayout(new GridLayout(robots, 4));
		hauptpane.add(gridPane, BorderLayout.CENTER);

		ueberschriftPane.add(new JLabel("Robot:"));
		ueberschriftPane.add(new JLabel("Position:"));
		ueberschriftPane.add(new JLabel("Ziel"));
		ueberschriftPane.add(new JLabel("Ladung"));
		
		// Zeilen durchnummerieren
		zeile = new JLabel[robots+1][4];
		for (int y = 1; y < robots+1; y++) {
			//for (int x = 0; x < 3; x++) {
				zeile[y][0] = (new JLabel(""+y));
				zeile[y][1] = (new JLabel());
				zeile[y][2] = (new JLabel());
				zeile[y][3] = (new JLabel());
				gridPane.add(zeile[y][0]);		
				gridPane.add(zeile[y][1]);	
				gridPane.add(zeile[y][2]);	
				gridPane.add(zeile[y][3]);	
		}
		
		pack();
		setVisible(true);

	}
	
	/* Das ganze Fenster zeigt eine "Tabelle" aus JLabels in nem 4xAnzRobots GridLayout
	 * Die Felder(JLabels) erreicht man wie folgt:
	 * 										zeile[zeile][spalte]
	 * wobei [zeile] = RobotID
	 * und die Spalten wie folgt "benannt" sind: 	[0] RobotID
	 * 												[1] Position
	 * 												[2]	Ziel
	 * 												[3] Ladung
	 * 
	 */
	public void refresh(int robot,int xPos,int yPos, int xZiel,int yZiel, Item[] ladung){
		zeile[robot][1].setText("X:"+xPos + " Y:" + yPos);		// Position aktualisieren
		zeile[robot][2].setText("X:"+xZiel + " Y:" + yZiel);	// Ziel aktualisieren
		if (ladung == null) {		
			zeile[robot][3].setText("tba");						// ToDo! Noch gibts immer nur null übergeben....
		}
		
		
	}
	
	// Tabelle verwerfen und Endstatus anzeigen
	public void beendet(int takte, int summeAuftraege){
		hauptpane.remove(gridPane);
		hauptpane.remove(ueberschriftPane);
		JLabel temp = new JLabel("Order abgearbeitet. Auftraege:" + summeAuftraege +" Takte:"+ takte);
		hauptpane.add(temp);
		pack();
		
	}
	// Fehlermeldung anzeigen
	public void abbruch(String fehlermeldung){
		hauptpane.remove(gridPane);
		hauptpane.remove(ueberschriftPane);
		JLabel temp = new JLabel(fehlermeldung);
		hauptpane.add(temp);
		pack();
		
	}

}
