package de.dis;

import de.dis.data.Makler;
import de.dis.data.Haus;
import de.dis.data.Apartment;
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
						System.out.println("Datensatz gefunden! Bitte Passwort für " + login + " eingeben:");
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
					newEstate();
					break;
				case DEL_ESTATE:
					delEstate();
					break;
				case UPDATE_ESTATE:
					updateEstate();
					break;
				case BACK:
					return;
			}
		}
	}

	public static void newEstate() {
		String estateType = FormUtil.readString("Bitte Art der Immobilie eingeben (Haus, Apartment)");

		if (estateType.equals("Haus")) {
			Haus e = new Haus();
			e.setFloors(FormUtil.readInt("Stockwerke"));
			e.setPrice(FormUtil.readInt("Kaufpreis"));
			String garden = FormUtil.readString("Garten? (y/n)");
			if (garden.equals("y")) {
				e.setGarden(true);
			} else {
				e.setGarden(false);
			}
			e.setCity(FormUtil.readString("Stadt"));
			e.setPostalcode(FormUtil.readInt("Postleitzahl"));
			e.setStreet(FormUtil.readString("Straße"));
			e.setStreetnumber(FormUtil.readInt("Hausnummer"));
			e.setSquarearea(FormUtil.readInt("Fläche"));
			e.setLogin(FormUtil.readString("Zuständiger Makler (Login)"));
			e.save();

			System.out.println("Haus mit ID "+e.getHouseId()+" wurde erstellt.");
		} else if (estateType.equals("Apartment")){
			Apartment e = new Apartment();
			e.setFloors(FormUtil.readInt("Stockwerke"));
			e.setRent(FormUtil.readInt("Miete"));
			e.setRooms(FormUtil.readInt("Anzahl Räume"));
			String kitchen = FormUtil.readString("Küche? (y/n)");
			if (kitchen.equals("y")) {
				e.setKitchen(true);
			} else {
				e.setKitchen(false);
			}
			String balcony = FormUtil.readString("Balkon? (y/n)");
			if (balcony.equals("y")) {
				e.setKitchen(true);
			} else {
				e.setKitchen(false);
			}
			e.setCity(FormUtil.readString("Stadt"));
			e.setPostalcode(FormUtil.readInt("Postleitzahl"));
			e.setStreet(FormUtil.readString("Straße"));
			e.setStreetnumber(FormUtil.readInt("Hausnummer"));
			e.setSquarearea(FormUtil.readInt("Fläche"));
			e.setLogin(FormUtil.readString("Zuständiger Makler (Login)"));
			e.save();

			System.out.println("Apartment mit ID "+e.getApartmentId()+" wurde erstellt.");
		} else {
			System.out.println("Immobilientyp "+ estateType +" existiert nicht! Kehre zum Immobilienmenü zurück...");
		}
	}

	public static void delEstate() {
		System.out.println("Bitte Immobilientyp (Haus, Apartment) und ID der zu löschenden Immobilie eingeben:");
		String type = FormUtil.readString("Typ");
		int id = FormUtil.readInt("ID");

		if (type.equals("Haus")) {
			Haus e = Haus.load(id);
			if (e != null) {
				e.delete();
				System.out.println("Erfolg! "+ type + " mit der ID "+id+ " wurde gelöscht.\n Kehre zum Immobilienmenü zurück...");
			}
		} else if (type.equals("Apartment")) {
			Apartment e = Apartment.load(id);
			if (e != null) {
				e.delete();
				System.out.println("Erfolg! "+ type + " mit der ID "+id+ " wurde gelöscht.\n Kehre zum Immobilienmenü zurück...");
			}
		} else {
			System.out.println("Der Typ: "+type+" existiert nicht.");
		}
	}

	public static void updateEstate() {
		System.out.println("Bitte Immobilientyp und ID eingeben");
		String type = FormUtil.readString("Typ");
		int id = FormUtil.readInt("ID");

		if (type.equals("Haus")) {
			Haus e = Haus.load(id);
			System.out.println("Datensatz für Haus mit ID: "+e.getHouseId()+" gefunden!\n");
			System.out.println("Welches Attribut soll geändert werden? Mögliche:\n Stadt, Postleitzahl, Straße, Hausnummer, Fläche, Makler, Stockwerke, Preis, Garten");
			String input = FormUtil.readString("Attribut");

			switch(input) {
				case "Stadt":
					String newCity = FormUtil.readString("Neue Stadt");
					e.setCity(newCity);
					break;
				case "Postleitzahl":
					int newPostalcode = FormUtil.readInt("Neue Postleitzahl");
					e.setPostalcode(newPostalcode);
					break;
				case "Straße":
					String newStreet = FormUtil.readString("Neue Straße");
					e.setStreet(newStreet);
					break;
				case "Hausnummer":
					int newStreetnumber = FormUtil.readInt("Neue Hausnummer");
					e.setStreetnumber(newStreetnumber);
					break;
				case "Fläche":
					int newSquarearea = FormUtil.readInt("Neue Fläche");
					e.setSquarearea(newSquarearea);
					break;
				case "Makler":
					String newLogin = FormUtil.readString("Neuer Makler");
					e.setLogin(newLogin);
					break;
				case "Stockwerke":
					int newFloors = FormUtil.readInt("Neue Anzahl Stockwerke");
					e.setFloors(newFloors);
					break;
				case "Preis":
					int newPrice = FormUtil.readInt("Neuer Preis");
					e.setPrice(newPrice);
					break;
				case "Garten":
					String newGarden = FormUtil.readString("Neuer Garten (y/n)?");
					if (newGarden.equals("y")) {
						e.setGarden(true);
					} else {
						e.setGarden(false);
					}
				default:
					System.out.println("Attribut "+input+" existiert nicht!");
			}
			System.out.println("Attribut "+input+" von Haus mit ID: "+e.getHouseId()+" wurde aktualisiert!");
			e.save();
		} else {
			Apartment e = Apartment.load(id);
			System.out.println("Datensatz für Apartment mit ID: "+e.getApartmentId()+" gefunden!\n");
			String input = FormUtil.readString("Attribut");
			System.out.println("Welches Attribut soll geändert werden? Mögliche:\n Stadt, Postleitzahl, Straße, Hausnummer, Fläche, Makler, Stockwerke, Miete, Zimmer, Balkon, Einbauküche");
			
			switch(input) {
				case "Stadt":
					String newCity = FormUtil.readString("Neue Stadt");
					e.setCity(newCity);
					break;
				case "Postleitzahl":
					int newPostalcode = FormUtil.readInt("Neue Postleitzahl");
					e.setPostalcode(newPostalcode);
					break;
				case "Straße":
					String newStreet = FormUtil.readString("Neue Straße");
					e.setStreet(newStreet);
					break;
				case "Hausnummer":
					int newStreetnumber = FormUtil.readInt("Neue Hausnummer");
					e.setStreetnumber(newStreetnumber);
					break;
				case "Fläche":
					int newSquarearea = FormUtil.readInt("Neue Fläche");
					e.setSquarearea(newSquarearea);
					break;
				case "Makler":
					String newLogin = FormUtil.readString("Neuer Makler");
					e.setLogin(newLogin);
					break;
				case "Stockwerke":
					int newFloors = FormUtil.readInt("Neue Anzahl Stockwerke");
					e.setFloors(newFloors);
					break;
				case "Miete":
					int newRent = FormUtil.readInt("Neue Miete");
					e.setRent(newRent);
					break;
				case "Zimmer":
					int newRooms = FormUtil.readInt("Neue Anzahl Zimmer");
					e.setRooms(newRooms);
					break;
				case "Küche":
					String newKitchen = FormUtil.readString("Neue Küche (y/n)?");
					if (newKitchen.equals("y")) {
						e.setKitchen(true);
					} else {
						e.setKitchen(false);
					}
				case "Balkon":
					String newBalcony = FormUtil.readString("Neuer Balkon (y/n)?");
					if (newBalcony.equals("y")) {
						e.setKitchen(true);
					} else {
						e.setKitchen(false);
					}
				default:
					System.out.println("Attribut "+input+" existiert nicht!");
			}
		System.out.println("Attribut "+input+" von Apartment mit ID: "+e.getApartmentId()+" wurde aktualisiert!");
		e.save();
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
