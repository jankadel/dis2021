package de.dis;

import de.dis.data.Makler;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

/**
 * Hauptklasse
 */
public class Main {
	/**
	 * Startet die Anwendung
	 */
	public static void main(String[] args) {
		showMainMenu();
	}
	
	/**
	 * Zeigt das Hauptmenü
	 */
	public static void showMainMenu() {
		//Menüoptionen
		final int MENU_MAKLER = 0;
		final int MENU_ESTATE = 1;
		final int MENU_CONTRACT = 2;
		final int QUIT = 3;
		
		//Erzeuge Menü
		Menu mainMenu = new Menu("Hauptmenü");
		mainMenu.addEntry("Makler-Verwaltung", MENU_MAKLER);
		mainMenu.addEntry("Immobilien-Verwaltung", MENU_ESTATE);
		mainMenu.addEntry("Vertragsverwaltung", MENU_CONTRACT);
		mainMenu.addEntry("Beenden", QUIT);
		
		BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));

		//Verarbeite Eingabe
		while(true) {
			int response = mainMenu.show();
			
			switch(response) {
				case MENU_MAKLER:
					System.out.println("Bitte Master-Passwort zur Makler-Verwaltung eingeben:");
					try {
						String input = stdin.readLine();
						if (input.equals("passwort")) {
							showMaklerMenu();
							break;
						} else {
							System.out.println("Falsches Passwort! Kehre zum Hauptmenü zurück.");
							break;
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				case MENU_ESTATE:
					System.out.println("Bitte Makler Login eingeben:");

					try {
						String login = stdin.readLine();
						Makler m = Makler.load(login);
						System.out.println("Datensatz gefunden! Bitte Passwort für " + login + "eingeben:");
						String password = m.getPassword();
						String input = stdin.readLine();
						if (input.equals(password)) {
							showEstateMenu();
							break;
						} else {
							System.out.println("Falsches Makler-Passwort! Kehre zum Hauptmenü zurück.");
							break;
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				case MENU_CONTRACT:
					showContractMenu();
					break;
				case QUIT:
					return;
			}
		}
	}
	
	/**
	 * Zeigt die Maklerverwaltung
	 */
	public static void showMaklerMenu() {
		//Menüoptionen
		final int NEW_MAKLER = 0;
		final int UPDATE_MAKLER = 1;
		final int DEL_MAKLER = 2;
		final int BACK = 3;
		
		//Maklerverwaltungsmenü
		Menu maklerMenu = new Menu("Makler-Verwaltung");
		maklerMenu.addEntry("Neuen Makler anlegen", NEW_MAKLER);
		maklerMenu.addEntry("Bestehenden Makler ändern", UPDATE_MAKLER);
		maklerMenu.addEntry("Bestehenden Makler löschen", DEL_MAKLER);
		maklerMenu.addEntry("Zurück zum Hauptmenü", BACK);

		//Verarbeite Eingabe
		while(true) {
			int response = maklerMenu.show();

			switch(response) {
				case NEW_MAKLER:
					newMakler();
					break;
				case UPDATE_MAKLER:
					updateMakler();
					break;
				case DEL_MAKLER:
					deleteMakler();
					break;
				case BACK:
					return;
			}
		}
	}
	
	/**
	 * Legt einen neuen Makler an, nachdem der Benutzer
	 * die entprechenden Daten eingegeben hat.
	 */
	public static void newMakler() {
		Makler m = new Makler();
		
		m.setName(FormUtil.readString("Name"));
		m.setAddress(FormUtil.readString("Adresse"));
		m.setLogin(FormUtil.readString("Login"));
		m.setPassword(FormUtil.readString("Passwort"));
		m.save();
		
		System.out.println("Makler "+m.getLogin()+" mit der ID "+m.getId()+" wurde erzeugt.");
	}

	/*
		Ändert Attribute eines bestehenden Maklers
	*/
	public static void updateMakler() {
		System.out.println("Bitte Makler-Login des zu ändernden Maklers eingeben:");
		String login = FormUtil.readString("Login");
		Makler m = Makler.load(login);
		System.out.println("Datensatz für Makler: "+m.getLogin()+" mit ID: "+m.getId()+" gefunden!\n");
		System.out.println("Welches Attribut soll geändert werden? Mögliche: Name, Adresse, Login, Passwort\n");
		String input = FormUtil.readString("Attribut");

		if (input.equals("Name")) {
			String newName = FormUtil.readString("Neuer Name");
			m.setName(newName);
		} else if(input.equals("Adresse")) {
			String newAddr = FormUtil.readString("Neue Adresse");
			m.setAddress(newAddr);
		} else if(input.equals("Login")) {
			String newLogin = FormUtil.readString("Neuer Login");
			m.setLogin(newLogin);
		} else if(input.equals("Passwort")) {
			String newPass = FormUtil.readString("Neues Passwort");
			m.setPassword(newPass);
		} else {
			System.out.println("Fehler! Das Attribut "+input+" existiert nicht.\n Kehre zum Makler-Menü zurück...");
		}
		m.save();
		System.out.println("Erfolg! Das Attribut "+input+" von "+m.getLogin()+" wurde aktualisiert.\n Kehre zum Makler-Menü zurück...");
	}

	public static void deleteMakler() {
		System.out.println("Bitte Makler-Login des zu löschenden Maklers eingeben:");
		String login = FormUtil.readString("Login");
		Makler m = Makler.load(login);
		if (m != null) {
			m.delete();
			System.out.println("Erfolg! Makler "+login+" wurde gelöscht.\n Kehre zum Makler-Menü zurück...");
		} else {
			System.out.println("Makler "+login+" existiert nicht.\n Kehre zum Makler-Menü zurück...");
		}
	}

	/* 
		Zeigt die Immobilienverwaltung an.
	*/
	public static void showEstateMenu() {
		//Menüoptionen
		final int NEW_ESTATE = 0;
		final int DEL_ESTATE = 1;
		final int UPDATE_ESTATE = 2;
		final int BACK = 3;
		
		//Maklerverwaltungsmenü
		Menu estateMenu = new Menu("Immobilien-Verwaltung");
		estateMenu.addEntry("Neue Immobilie", NEW_ESTATE);
		estateMenu.addEntry("Immobilie entfernen", DEL_ESTATE);
		estateMenu.addEntry("Immobilie bearbeiten", UPDATE_ESTATE);
		estateMenu.addEntry("Zurück zum Hauptmenü", BACK);
		
		//Verarbeite Eingabe
		while(true) {
			int response = estateMenu.show();
			
			switch(response) {
				case NEW_ESTATE:
					//newEstate();
					break;
				case DEL_ESTATE:
					//delEstate();
					break;
				case UPDATE_ESTATE:
					//updateEstate();
					break;
				case BACK:
					return;
			}
		}
	}

	public static void showContractMenu() {
		//Menüoptionen
		final int NEW_PERSON = 0;
		final int SIGN_CONTRACT = 1;
		final int SHOW_CONTRACTS = 2;
		final int BACK = 3;
		
		//Maklerverwaltungsmenü
		Menu estateMenu = new Menu("Immobilien-Verwaltung");
		estateMenu.addEntry("Neue Immobilie", NEW_PERSON);
		estateMenu.addEntry("Immobilie entfernen", SIGN_CONTRACT);
		estateMenu.addEntry("Immobilie bearbeiten", SHOW_CONTRACTS);
		estateMenu.addEntry("Zurück zum Hauptmenü", BACK);
		
		//Verarbeite Eingabe
		while(true) {
			int response = estateMenu.show();
			
			switch(response) {
				case NEW_PERSON:
					//newPerson();
					break;
				case SIGN_CONTRACT:
					//signContract();
					break;
				case SHOW_CONTRACTS:
					//showContracts();
					break;
				case BACK:
					return;
			}
		}
	}
}
