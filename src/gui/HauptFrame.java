package gui;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import java.util.Map.Entry;
import java.util.Set;

import model.*;
import model.IRobot;

public class HauptFrame extends JFrame implements IHauptFrame {

    /**
	 * 
	 */
	private static final long serialVersionUID = -268172097860439460L;
	/* --------- Variablen ------------ */
    private final Simulation controller;
    private JPanel hauptpane;
    private JLabel[][] feld;
    private TextFenster textFenster;
    private JToggleButton pauseButton;
    private boolean paused = false;
    private static HauptFrame myMainWindow;
    private int[] RobotPosX;
    private int[] RobotPosY;
    private int fields;

    public HauptFrame(Simulation controller) {
        this.controller = controller;
        myMainWindow = this;

        setTitle("Warehouse 13.37");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocation(200, 200);
        setMinimumSize(new Dimension(400, 400));
        Dimension screen = new Dimension((int) ((Toolkit
                .getDefaultToolkit().getScreenSize().width) * 0.75),
                (int) ((Toolkit.getDefaultToolkit().getScreenSize().height) * 0.75));
        setMaximumSize(screen);
        erzeugeMenus();
        erzeugeAnzeige();
        pack();

        setVisible(true);
        // aendereParameter();
    }

    // Erzeugt die Men�leiste und weist ActionListener zu
    private void erzeugeMenus() {
        /* Men�leiste erzeugen */
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        /* Men� Datei erzeugen */
        JMenu menuDatei = new JMenu("Men�");
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


    void erzeugeAnzeige() {
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

    /* Erstellt Ansicht f�r neues Lager */
    public void newWarehouse(int fields, int stations, int robots, Set<Item> item) {
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
                    feld[x][y].setBorder(BorderFactory.createLineBorder(Color.orange, 3));
                    feld[x][y].setIcon(new ImageIcon(getClass().getResource(("/paket_klein.jpg"))));
                }
                gridPane.add(feld[x][y]);
            }

        }

        for (Item elem : item) {
            feld[elem.productPosX()][elem.productPosY()].setText(String.valueOf(elem.id()));
        }

        pack();
        textFenster = new TextFenster(robots);
        textFenster.setBounds(myMainWindow.getX() + myMainWindow.getWidth(), myMainWindow.getY(), 100, myMainWindow.getHeight());

    }


    /* Aktualisiert Warenhaus ansicht */
    public void showWarehouseState( final IWarehouse whouse, final IRobot rob, final Set<Item> item, 
            final int loadTime, final int xZiel, final int yZiel, final int packingTime ) {
       
    	String output;
        output = "OrderId   ItemId   Gewicht   Menge   Robot\n";
        //�bersicht aktualisieren
        if (!whouse.Order().isEmpty()) {
            //System.out.println("-----------------------Auftraege-------------------");
            for ( Order map : whouse.Order()  ) {
                for (Entry<Item, Integer> entry : map.getMap().entrySet()) {
                    String id = "na";
                	for(int i = 0; i < whouse.Bplants().length; i++)
                	{
                		if((whouse.Bplants()[i].getRobot().getOrder() != null) && (whouse.Bplants()[i].getRobot().getOrder().OrderId().equals(map.OrderId())))
                		{
                			id = String.valueOf(whouse.Bplants()[i].getRobot().id());
                		}
                	}
                	//System.out.println(entry.getKey().id() + "#####"+ entry.getValue().toString());
                    output += String.format("%4s %8s %8s %8s %8s" , map.OrderId() , entry.getKey().id() , entry.getKey().size() , entry.getValue().toString(), id);
                    output += "\n";
                }
            }
        }

        //System.out.println("-----------------------End Auftraege-------------------");
        if(rob.getOrder() != null) if (!rob.getOrder().getMap().keySet().isEmpty()) {

            Item realitem = (rob.getOrder().getMap().keySet()).iterator().next();
            int menge   = rob.getOrder().getMap().values().iterator().next();
            int gewicht = realitem.size();
            textFenster.refresh(rob.id(), rob.CurrentPosX(),
                    rob.CurrentPosY(), xZiel, yZiel,
                    String.valueOf(realitem.id()), String.valueOf(gewicht),String.valueOf(menge), output);
        } 
        else
        {
            textFenster.refresh(rob.id(), rob.CurrentPosX(),
                    rob.CurrentPosY(), xZiel, yZiel,
                    "","","",  output);

        }
        
        feld[RobotPosX[rob.id()]][RobotPosY[rob.id()]].setText("");
        feld[RobotPosX[rob.id()]][RobotPosY[rob.id()]].setForeground(Color.BLACK);
        for (Item elem : item) {
        	if(feld[elem.productPosX()][elem.productPosY()] == feld[RobotPosX[rob.id()]][RobotPosY[rob.id()]])
        	{
        		feld[elem.productPosX()][elem.productPosY()].setText(String.valueOf(elem.id()));
        	}
        }

        // Ziel? --> verpacken Bildchen
        if (rob.CurrentPosX() == xZiel && rob.CurrentPosY() == yZiel && yZiel != fields-1 && loadTime != 0) {
            feld[rob.CurrentPosX()][rob.CurrentPosY()].setText("R" + rob.id() + ":" + loadTime );
            feld[rob.CurrentPosX()][rob.CurrentPosY()].setForeground(Color.RED);
        }else if(rob.CurrentPosX() == rob.StartPosX() && rob.CurrentPosY() == rob.StartPosY() && packingTime != 0 ) {
        	feld[rob.CurrentPosX()][rob.CurrentPosY()].setForeground(Color.RED);
        	feld[rob.CurrentPosX()][rob.CurrentPosY()].setText("R" + rob.id() + ":" + packingTime );
        }else{
        	// Neue Position eintragen
        	feld[rob.CurrentPosX()][rob.CurrentPosY()].setText("R" + rob.id());
        	feld[rob.CurrentPosX()][rob.CurrentPosY()].setForeground(Color.RED);
        }
        RobotPosX[rob.id()] = rob.CurrentPosX();
        RobotPosY[rob.id()] = rob.CurrentPosY(); 
        
        // Anzeige aktualisieren
        EventQueue.invokeLater(new Runnable() {
            public void run() {
            	
                feld[RobotPosX[rob.id()]][RobotPosY[rob.id()]].setText("");
                feld[RobotPosX[rob.id()]][RobotPosY[rob.id()]].setForeground(Color.BLACK);

                // Ziel? --> verpacken Bildchen
                if (rob.CurrentPosX() == xZiel && rob.CurrentPosY() == yZiel && yZiel != fields-1 && loadTime != 0) {
                    feld[rob.CurrentPosX()][rob.CurrentPosY()].setText("R" + rob.id() + ":" + loadTime );
                    feld[rob.CurrentPosX()][rob.CurrentPosY()].setForeground(Color.RED);
                }else if(rob.CurrentPosX() == rob.StartPosX() && rob.CurrentPosY() == rob.StartPosY() && packingTime != 0) {
                	feld[rob.CurrentPosX()][rob.CurrentPosY()].setForeground(Color.RED);
                	feld[rob.CurrentPosX()][rob.CurrentPosY()].setText("R" + rob.id() + ":" + packingTime );
                }else{
                	// Neue Position eintragen
                	feld[rob.CurrentPosX()][rob.CurrentPosY()].setText("R" + rob.id());
                	feld[rob.CurrentPosX()][rob.CurrentPosY()].setForeground(Color.RED);
                }

        // Position zum sp�teren l�lschen zwischenspeichern
        RobotPosX[rob.id()] = rob.CurrentPosX();
        RobotPosY[rob.id()] = rob.CurrentPosY();             

            }
        });
      
    }

    /* Zeigt Fehlermeldung bei Fehlverhalten */
    public void showErrors(String fehlermeldung) {
        textFenster.abbruch(fehlermeldung);
    }

    /* Zeigt �bersicht nach erfolgreichem Abarbeiten der Auftraege */
    public void showFinalOverview(int benoetigteTakte, int summeAuftraege) {
        textFenster.beendet(benoetigteTakte, summeAuftraege);
    }
}
