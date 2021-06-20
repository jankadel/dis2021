package de.dis;

public class DataWarehouse {
    private UserInterface ui;

	/**
	 * Erzeugt neues Data-Warehouse
	 */
	public DataWarehousing() {
	}

	/**
	 * @return user interface
	 */
	public UserInterface getUi() {
		return ui;
	}

	/**
	 * @param ui
	 *            the ui to set
	 */
	public void setUi(UserInterface ui) {
		this.ui = ui;
	}

	/**
	 * Hauptprogramm
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		DataWarehousing dw = new DataWarehousing();
		CsvParser csvp = new CsvParser();
		DbParser dbp = new DbParser();
		DataCube dc = new DataCube();
		StarScheme s = new StarScheme();
		UserInterface ui = new UserInterface(csvp, dbp, dc, s);
		dw.setUi(ui);
		ui.start();
	}
}
