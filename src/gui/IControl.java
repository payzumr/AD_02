package gui;

public interface IControl {
	
	/**
	 * F�hrt den n�chsten Simulationsschritt sofort aus
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
