package gui;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import java.io.File;
import java.text.DecimalFormat;

import javax.swing.filechooser.FileFilter;
import javax.swing.plaf.IconUIResource;

import model.*;

public class HauptFrame extends JFrame implements HauptFrame_interface {

	/* --------- Variablen ------------ */
	private Simulation controller;
	private JMenuItem startEntry;
	private JPanel hauptpane;
	JLabel[][] feld;
	textFenster textFenster;
	private JToggleButton pauseButton;
	private boolean paused = false;
	static HauptFrame myMainWindow;
	int[] RobotPosX;
	int[] RobotPosY;
	private int fields;
	// Festlegen der max Fenstergrï¿½ï¿½e
	private Dimension screen = new Dimension((int) ((Toolkit
			.getDefaultToolkit().getScreenSize().width) * 0.75),
			(int) ((Toolkit.getDefaultToolkit().getScreenSize().height) * 0.75));

	public HauptFrame(Simulation controller) {
		this.controller = controller;
		myMainWindow = this;

		setTitle("Warehouse 13.37");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocation(200, 200);
		setMinimumSize(new Dimension(400, 400));
		setMaximumSize(screen);
		erzeugeMenus();
		erzeugeAnzeige();
		pack();

		setVisible(true);
		// aendereParameter();
	}

	// Erzeugt die Menï¿½leiste und weist ActionListener zu
	private void erzeugeMenus() {
		/* Menï¿½leiste erzeugen */
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		/* Menï¿½ Datei erzeugen */
		JMenu menuDatei = new JMenu("Menü");
		menuBar.add(menuDatei);

		/* Ende */
		JMenuItem endeEntry = new JMenuItem("Beenden");
		endeEntry.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		menuDatei.add(endeEntry);
	}

//	// neu Einlesen der CSV Datei.
//	public void aendereParameter() {
//		JFileChooser fc = new JFileChooser();
//		// Filter damit nur CSV Dateien angezeigt werden
//		fc.setFileFilter(new FileFilter() {
//			@Override
//			public boolean accept(File f) {
//				return f.isDirectory()
//						|| f.getName().toLowerCase().endsWith(".ini");
//			}
//
//			@Override
//			public String getDescription() {
//				return "INI";
//			}
//		});
//
//		int state = fc.showOpenDialog(null);
//		// Wenn "ï¿½ffnen" gedrï¿½ckt
//		if (state == JFileChooser.APPROVE_OPTION) {
//			File file = fc.getSelectedFile();
//			controller.readCSV(file.getAbsolutePath());
//		}
//		// Wenn "Abbrechen" gedrï¿½ckt
//		else
//			System.out.println("Auswahl abgebrochen");
//
//	}

	public void erzeugeAnzeige() {
		getContentPane().setLayout(new GridLayout(1, 0, 0, 0));

		hauptpane = new JPanel();
		getContentPane().add(hauptpane);
		hauptpane.setLayout(new BorderLayout(0, 0));

		JPanel buttonPanel = new JPanel();
		hauptpane.add(buttonPanel, BorderLayout.NORTH);
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));

		JButton btnStart = new JButton("Start");
		buttonPanel.add(btnStart);
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.run_simuation();
			}
		});
	
		JButton btnNchsterTakt = new JButton("N\u00E4chster Takt");
		buttonPanel.add(btnNchsterTakt);
		btnNchsterTakt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.next_simuation();
			}
		});
		//Enterbutton funktioniert ohne Fokus
		this.getRootPane().setDefaultButton(btnNchsterTakt);
		
		

		pauseButton = new JToggleButton();
		pauseButton.setText("Pause");		
		pauseButton.addActionListener(new ActionListener() {
		      public void actionPerformed(ActionEvent e) {
		        if(paused)
		        {
		        	paused = false;
		        	pauseButton.setText("Pause");
		        	controller.run_simuation();		        	
		        }
		        else
		        {
		        	paused = true;
		        	pauseButton.setText("Fortfahren");		        	
		        	controller.pause_simuation();
		        }
		      }
		 });
		buttonPanel.add(pauseButton);

	}

	/* Erstellt Ansicht fï¿½r neues Lager */
	public void newWarehouse(int fields, int stations, int robots) {
		this.fields = fields;
		
		RobotPosX = new int[robots+1];
		RobotPosY = new int[robots+1];
		JPanel gridPane = new JPanel();
		gridPane.setLayout(new GridLayout(fields, fields));
		hauptpane.add(gridPane, BorderLayout.CENTER);

		feld = new JLabel[fields][fields];

		for (int y = 0; y < fields; y++) {
			for (int x = 0; x < fields; x++) {
				feld[x][y] = (new JLabel("", JLabel.CENTER));
				feld[x][y].setBorder(BorderFactory.createLineBorder(Color.black));
				if (y == fields - 1 && x < stations) {
					feld[x][y].setText("");
					feld[x][y].setIcon(new ImageIcon(getClass().getResource(("/paket_klein.jpg"))));
				}
				gridPane.add(feld[x][y]);
			}

		}
		pack();
		textFenster = new textFenster(robots);
		textFenster.setBounds(myMainWindow.getX() + myMainWindow.getWidth(), myMainWindow.getY(), 100, myMainWindow.getHeight());

	};

	
	/* Aktualisiert Warenhaus ansicht */
	public void showRobotState(final int robotName, final int xPos, final int yPos,
			Item[] ladung, final int xZiel, final int yZiel, final Status status) {
		
		//ï¿½bersicht aktualisieren
		textFenster.refresh(robotName,xPos,yPos,xZiel,yZiel,ladung);

		// Anzeige aktualisieren
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				
				// Alte Position lï¿½schen
				if (RobotPosY[robotName] != fields-1) {
					feld[RobotPosX[robotName]][RobotPosY[robotName]].setIcon(null);
				}
				feld[RobotPosX[robotName]][RobotPosY[robotName]].setText("");
				feld[RobotPosX[robotName]][RobotPosY[robotName]].setForeground(Color.BLACK);

				
				// Ziel? --> verpacken Bildchen
				if (xPos == xZiel && yPos == yZiel && yZiel != fields-1) {
					feld[RobotPosX[robotName]][RobotPosY[robotName]].setText("");
					feld[xPos][yPos].setIcon(new ImageIcon(getClass().getResource("/einkaufswagen_klein.jpg")));
					
				}else{
					// Neue Position eintragen
					feld[xPos][yPos].setText("["+robotName+"]");
				}
				
//				switch (status) {
//				case IDLE:
//					feld[xPos][yPos].setForeground(Color.GREEN);
//					break;
//				case BUSY:
//					feld[xPos][yPos].setForeground(Color.YELLOW);
//					break;
//				case BOXING:
//					feld[xPos][yPos].setForeground(Color.BLUE);
//					break;
//				case MOVING:
//					feld[xPos][yPos].setForeground(Color.WHITE);
//					break;
//				case LOADING:
//					feld[xPos][yPos].setForeground(Color.RED);
//					break;
//				default:
//					feld[xPos][yPos].setForeground(Color.PINK);
//					break;
//				}

				
				// Position zum spï¿½teren lï¿½lschen zwischenspeichern
				RobotPosX[robotName] = xPos;
				RobotPosY[robotName] = yPos;				
				
			}
		});
		//pack();
		
	}

	/* Zeigt Fehlermeldung bei Fehlverhalten */
	public void abbruch(String fehlermeldung) {
		textFenster.abbruch(fehlermeldung);
	};

	/* Zeigt ï¿½bersicht nach erfolgreichem Abarbeiten der Auftraege */
	public void beendet(int benoetigteTakte, int summeAuftraege) {
		textFenster.beendet(benoetigteTakte, summeAuftraege);
	};
}
