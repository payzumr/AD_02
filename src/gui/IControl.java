package gui;

public interface IControl {
	
	/**
	 * Führt den nächsten Simulationsschritt sofort aus
	 */
	public void next_simuation();
	
	/**
	 * Pausiert den Simulationszyklus
	 */
	public void pause_simuation();
	
	/**
	 * Setzt die Simulation im Standard Interval fort
	 */
	public void run_simuation();
}
