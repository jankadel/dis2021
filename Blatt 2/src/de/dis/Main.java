package de.dis;

import de.dis.data.EstateAgent;
import de.dis.data.House;
import de.dis.data.Person;
import de.dis.data.PurchaseContract;
import de.dis.data.TenancyContract;
import de.dis.data.Apartment;
import de.dis.data.Estate;

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
		final int MENU_ESTATEAGENT = 0;
		final int MENU_ESTATE = 1;
		final int MENU_CONTRACT = 2;
		final int QUIT = 3;
		
		//Erzeuge Menü
		Menu mainMenu = new Menu("Main Menu");
		mainMenu.addEntry("Estate Agent Administration", MENU_ESTATEAGENT);
		mainMenu.addEntry("Estate Administration", MENU_ESTATE);
		mainMenu.addEntry("Contract Administration", MENU_CONTRACT);
		mainMenu.addEntry("Quit", QUIT);
		
		//Verarbeite Eingabe
		while(true) {
			int response = mainMenu.show();
			
			switch(response) {
				case MENU_ESTATEAGENT:
					showAgentMenu();
					break;
				case MENU_ESTATE:
					showEstateMenu();
					break;
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
	public static void showAgentMenu() {
		// TODO
		if(!FormUtil.readString("Please enter the access code").equals("Moin")) {
			
			System.out.println("That's not the one. Sorry. Please try again.\n");
			return;
			
		}
		
		//Menüoptionen
		final int NEW_ESTATEAGENT = 0;
		final int CHANGE = 1;
		final int DELETE = 2;
		final int BACK = 3;
		
		//Maklerverwaltungsmenü
		Menu maklerMenu = new Menu("Estate Agent Administration");
		maklerMenu.addEntry("New Estate Agent", NEW_ESTATEAGENT);
		maklerMenu.addEntry("Change Existing Estate Agent", CHANGE);
		maklerMenu.addEntry("Delete Estate Agent", DELETE);
		maklerMenu.addEntry("Back to Main Menu", BACK);
		
		//Verarbeite Eingabe
		while(true) {
			int response = maklerMenu.show();
			
			switch(response) {
				case NEW_ESTATEAGENT:
					newAgent();
					break;
				case CHANGE:
					changeAgent();
					break;
				case DELETE:
					deleteAgent();
					break;
				case BACK:
					return;
			}
		}
	
	}
	

	/**
	 * Legt einen neuen Estate Agent an, nachdem der Benutzer
	 * die entprechenden Daten eingegeben hat.
	 */
	public static void newAgent() {
		EstateAgent m = new EstateAgent();
		
		m.setName(FormUtil.readString("Name"));
		m.setAddress(FormUtil.readString("Adresse"));
		m.setLogin(FormUtil.readString("Login"));
		while (EstateAgent.loginExists(m.getLogin())) {
			System.out.println("Sorry, but an estate agent with login "+m.getLogin()+" already exists. :-( \nPlease choose another one. ;-)\n");
			m.setLogin(FormUtil.readString("Login"));
		}
		m.setPassword(FormUtil.readString("Passwort"));
		m.save();
		
		System.out.println("Estate Agent with login "+m.getLogin()+" created.\n");
	}
	
	/**
	 * �ndert die Daten eines existierenden Estate Agents.
	 */
	private static void changeAgent() {
		String login = FormUtil.readString("Login");
		// TODO
		if (!EstateAgent.loginExists(login)) {
			System.out.println("Your Login does't seem to exist. Please try again or else.\n");
			return;
		}
		
		EstateAgent m = EstateAgent.load(login);			
		
		String name = FormUtil.readString("Name ["+m.getName()+"]");
		String address = FormUtil.readString("Address ["+m.getAddress()+"]");
		String password = FormUtil.readString("Password ["+m.getPassword()+"]");
		
		if (!name.isEmpty()) {
			m.setName(name);
		}
		
		if (!address.isEmpty()) {
			m.setAddress(address);
		}
		
		if (!password.isEmpty()) {
			m.setPassword(password);
		}

		m.save();
		
		System.out.println("Update successful!\n");
		
	}
	
	/**
	 * L�scht einen existierenden Estate Agent.
	 */
	private static void deleteAgent() {
		String login = FormUtil.readString("Login");
		// TODO
		if (!EstateAgent.loginExists(login)) {
			System.out.println("Your Login does't seem to exist. Please try again or else.\n");
			return;
		}
		
		EstateAgent.delete(login);

		System.out.println("Estate Agent "+login+" successfully deleted.\n");
		
	}
	

	// TODO
	private static void showEstateMenu() {
		// TODO
		String login = FormUtil.readString("Please enter your login");
		Boolean correctPassword = EstateAgent.CheckPassword(login, FormUtil.readString("Please enter your password"));
		
		if(!correctPassword) {
			System.out.println("Your login data is incorrect.\n");
			return;
		}
		
		//Menüoptionen
		final int NEW_ESTATE = 0;
		final int CHANGE_ESTATE = 1;
		final int DELETE_ESTATE = 2;
		final int BACK = 3;
		
		//Maklerverwaltungsmenü
		Menu maklerMenu = new Menu("Estate Administration");
		maklerMenu.addEntry("New Estate", NEW_ESTATE);
		maklerMenu.addEntry("Change Estate", CHANGE_ESTATE);
		maklerMenu.addEntry("Delete Estate", DELETE_ESTATE);
		maklerMenu.addEntry("Back to Main Menu", BACK);
		
		//Verarbeite Eingabe
		while(true) {
			int response = maklerMenu.show();
			
			switch(response) {
				case NEW_ESTATE:
					newEstate(login);
					break;
				case CHANGE_ESTATE:
					changeEstate(login);
					break;
				case DELETE_ESTATE:
					deleteEstate(login);
					break;
				case BACK:
					return;
			}
		}
		
	}
	
	// TODO
	private static void deleteEstate(String login) {

		int estate_id = FormUtil.readInt("Estate ID");
		
		int estate_type = FormUtil.readInt("Please enter the type of the estate [1: house / 2: apartment]");
		
		if (estate_type == 1) {
			// TODO
			if (!House.idExists(estate_id)) {
				System.out.println("There is no house listed under this Estate ID.\n");
				return;
			}
			
			if(!Estate.checkAgent(login, estate_id)) {
				System.out.println("You don't have permission to delete this house, because you are not listed as the manager.\n");
				return;			
			}
			
			House.delete(estate_id);
		}
		
		else if(estate_type == 2) {
			//TODO
			if (!Apartment.idExists(estate_id)) {
				System.out.println("There is no apartment listed under this Estate ID.\n");
				return;
			}
			
			if(!Estate.checkAgent(login, estate_id)) {
				System.out.println("You don't have permission to delete this apartment, because you are not listed as the manager.\n");
				return;			
			}
			
			Apartment.delete(estate_id);
		}
		
		else {
			System.out.println("Invalid input. Please enter a '1' or a '2' next time.\n");
		}
		
		return;
	}

	// TODO
	private static void changeEstate(String login) {

		int estate_type = FormUtil.readInt("Please enter the type of the estate [1: house / 2: apartment]");
		int estate_id = FormUtil.readInt("Estate ID");
		
		if (estate_type == 1) {
			if (!House.idExists(estate_id)) {
				System.out.println("There is no house listed under this Estate ID.\n");
				return;
			}
			
			if(!Estate.checkAgent(login, estate_id)) {
				System.out.println("You don't have permission to change this house, because you are not listed as the manager.\n");
				return;			
			}
			
			House m = House.load(estate_id);			
			
			String city = FormUtil.readString("City ["+m.getCity()+"]");
			String postal_code = FormUtil.readString("Postal Code ["+m.getPostalCode()+"]");
			String Street = FormUtil.readString("Street ["+m.getStreet()+"]");
			int StreetNo = FormUtil.readInt("StreetNo ["+m.getStreetNo()+"]");
			double SquareArea = FormUtil.readDouble("SquareArea ["+m.getSquareArea()+"]");
			String Login = FormUtil.readString("Login ["+m.getLogin()+"]");
			int Floors = FormUtil.readInt("Floors ["+m.getFloors()+"]");
			double Price = FormUtil.readDouble("Price ["+m.getPrice()+"]");
			
			// Garden
			String garden = FormUtil.readString("garden [y / n]");
			while (!garden.equals("y") && !garden.equals("n"))
			{
				System.out.println("Invalid input. Please enter 'y' or 'n'.\n");
				garden = FormUtil.readString("garden [y / n]");
			}

			if (garden.equals("y")) {
				m.setGarden(true);
			}
			else if (garden.equals("n")) {
				m.setGarden(false);
			}

			
			if (!city.isEmpty()) {
				m.setCity(city);
			}
			
			if (!postal_code.isEmpty()) {
				m.setPostalCode(postal_code);
			}
			
			if (!Street.isEmpty()) {
				m.setStreet(Street);
			}

			if (StreetNo != 0) {
				m.setStreetNo(StreetNo);
			}
			
			if (SquareArea != 0) {
				m.setSquareArea(SquareArea);
			}
			
			if (!Login.isEmpty()) {
				m.setLogin(login);
			}
			
			if (Floors != 0) {
				m.setFloors(Floors);
			}
			
			if (Price != 0) {
				m.setPrice(Price);
			}
			m.save();
			
			System.out.println("Update successful!\n");
		}
		
		else if (estate_type == 2) {
			//TODO
			if (!Apartment.idExists(estate_id)) {
				System.out.println("There is no apartment listed under this Estate ID.\n");
				return;
			}
			
			if(!Estate.checkAgent(login, estate_id)) {
				System.out.println("You don't have permission to change this apartment, because you are not listed as the manager.\n");
				return;			
			}
			
			Apartment m = Apartment.load(estate_id);			
			
			String city = FormUtil.readString("City ["+m.getCity()+"]");
			String postal_code = FormUtil.readString("Postal Code ["+m.getPostalCode()+"]");
			String Street = FormUtil.readString("Street ["+m.getStreet()+"]");
			int StreetNo = FormUtil.readInt("StreetNo ["+m.getStreetNo()+"]");
			double SquareArea = FormUtil.readDouble("SquareArea ["+m.getSquareArea()+"]");
			String Login = FormUtil.readString("Login ["+m.getLogin()+"]");
			int Floor = FormUtil.readInt("Floor ["+m.getFloor()+"]");
			double Rent = FormUtil.readDouble("Rent ["+m.getRent()+"]");
			int Rooms = FormUtil.readInt("Rooms ["+m.getRooms()+"]");
			
			// Balcony
			String balcony = FormUtil.readString("balcony [y / n]");
			while (!balcony.equals("y") && !balcony.equals("n"))
			{
				System.out.println("Invalid input. Please enter 'y' or 'n'.\n");
				balcony = FormUtil.readString("balcony [y / n]");
			}

			if (balcony.equals("y")) {
				m.setBalcony(true);
			}
			else if (balcony.equals("n")) {
				m.setBalcony(false);
			}

			
			if (!city.isEmpty()) {
				m.setCity(city);
			}
			
			if (!postal_code.isEmpty()) {
				m.setPostalCode(postal_code);
			}
			
			if (!Street.isEmpty()) {
				m.setStreet(Street);
			}

			if (StreetNo != 0) {
				m.setStreetNo(StreetNo);
			}
			
			if (SquareArea != 0) {
				m.setSquareArea(SquareArea);
			}
			
			if (!Login.isEmpty()) {
				m.setLogin(login);
			}
			
			if (Floor != 0) {
				m.setFloor(Floor);
			}
			
			if (Rent != 0) {
				m.setRent(Rent);
			}

			if (Rooms != 0) {
				m.setRooms(Rooms);
			}
			
			m.save();
			
			System.out.println("Update successful!\n");
		}
		
		else {
			System.out.println("Invalid input. Please enter a '1' or a '2' next time.\n");
		}
		
		return;
	}

	//TODO
	private static void newEstate(String login) {
		// TODO Auto-generated method stub
		
		int estate_type = FormUtil.readInt("Please enter the type of the estate [1: house / 2: apartment]");
		
		if (estate_type == 1) {
			House a = new House();
			
			a.setCity(FormUtil.readString("city"));
			a.setPostalCode(FormUtil.readString("postal_code"));
			a.setStreet(FormUtil.readString("street"));
			a.setStreetNo(FormUtil.readInt("street_no"));
			a.setSquareArea(FormUtil.readDouble("square_area"));
			a.setLogin(FormUtil.readString("login"));
			a.setFloors(FormUtil.readInt("floors"));
			a.setPrice(FormUtil.readDouble("price"));
			
			String garden = FormUtil.readString("garden [y / n]");
			while (!garden.equals("y") && !garden.equals("n"))
			{
				System.out.println("Invalid input. Please enter 'y' or 'n'.\n");
				garden = FormUtil.readString("garden [y / n]");
			}

			if (garden.equals("y")) {
				a.setGarden(true);
			}
			else if (garden.equals("n")) {
				a.setGarden(false);
			}

			a.save();
			
			System.out.println("Estate with ID "+a.getId()+" created.\n");
			
			return;
		}
		
		else if (estate_type == 2)
		{
			Apartment a = new Apartment();
			
			a.setCity(FormUtil.readString("city"));
			a.setPostalCode(FormUtil.readString("postal_code"));
			a.setStreet(FormUtil.readString("street"));
			a.setStreetNo(FormUtil.readInt("street_no"));
			a.setSquareArea(FormUtil.readDouble("square_area"));
			a.setLogin(FormUtil.readString("login"));
			a.setFloor(FormUtil.readInt("floor"));
			a.setRent(FormUtil.readDouble("rent"));
			a.setRooms(FormUtil.readInt("rooms"));

			//Balcony
			String balcony = FormUtil.readString("balcony [y / n]");
			while (!balcony.equals("y") && !balcony.equals("n"))
			{
				System.out.println("Invalid input. Please enter 'y' or 'n'.\n");
				balcony = FormUtil.readString("balcony [y / n]");
			}

			if (balcony.equals("y")) {
				a.setBalcony(true);
			}
			else if (balcony.equals("n")) {
				a.setBalcony(false);
			}
			
			//Kitchen
			String kitchen = FormUtil.readString("kitchen [y / n]");
			while (!kitchen.equals("y") && !kitchen.equals("n"))
			{
				System.out.println("Invalid input. Please enter 'y' or 'n'.\n");
				kitchen = FormUtil.readString("kitchen [y / n]");
			}

			if (kitchen.equals("y")) {
				a.setKitchen(true);
			}
			else if (kitchen.equals("n")) {
				a.setKitchen(false);
			}
	
			a.save();
			System.out.println("Estate with ID "+a.getId()+" created.\n");
			
			return;
		}
		
		else {
			System.out.println("Invalid input. Please enter a '1' or a '2' next time.\n");
		}
		
		return;
	}

	// TODO
	private static void showContractMenu() {
		//Menüoptionen
		final int NEW_PERSON = 0;
		final int NEW_CONTRACT = 1;
		final int VIEW_CONTRACTS = 2;
		final int BACK = 3;
		
		//Maklerverwaltungsmenü
		Menu maklerMenu = new Menu("Contract Administration");
		maklerMenu.addEntry("New Person", NEW_PERSON);
		maklerMenu.addEntry("New Contract", NEW_CONTRACT);
		maklerMenu.addEntry("View Contracts", VIEW_CONTRACTS);
		maklerMenu.addEntry("Back to Main Menu", BACK);
		
		//Verarbeite Eingabe
		while(true) {
			int response = maklerMenu.show();
			
			switch(response) {
				case NEW_PERSON:
					newPerson();
					break;
				case NEW_CONTRACT:
					newContract();
					break;
				case VIEW_CONTRACTS:
					viewContracts();
					break;
				case BACK:
					return;
			}
		}

		
	}

	//TODO
	private static void newPerson() {
		Person m = new Person();
		
		m.setFirstName(FormUtil.readString("First Name"));
		m.setName(FormUtil.readString("Name"));
		m.setAddress(FormUtil.readString("Adresse"));
		m.save();
		
		System.out.println("Person with id "+m.getPersonId()+" created.\n");
	
		
	}
	
	//TODO
	private static void newContract() {
		int contract_type = FormUtil.readInt("Please enter the type of the contract [1: purchase / 2: tenancy]");
		
		if (contract_type == 1) {
			PurchaseContract a = new PurchaseContract();
			
			a.setDate(FormUtil.readDate("Date"));
			a.setPlace(FormUtil.readString("Place"));
			a.setEstateId(FormUtil.readInt("Estate ID"));
			a.setPersonId(FormUtil.readInt("Person ID"));
			
			a.setNoInstallments(FormUtil.readInt("Number of Installments"));
			a.setInterestRate(FormUtil.readDouble("Interest Rate"));
			
			a.save();
			
			System.out.println("Contract No "+a.getContractNo()+" created.\n");
			
			return;
		}
		
		else if (contract_type == 2)
		{
			TenancyContract a = new TenancyContract();

			a.setDate(FormUtil.readDate("Date"));
			a.setPlace(FormUtil.readString("Place"));
			a.setEstateId(FormUtil.readInt("Estate ID"));
			a.setPersonId(FormUtil.readInt("Person ID"));
			
			a.setStartDate(FormUtil.readDate("Start Date"));
			a.setDuration(FormUtil.readInt("Duration"));
			a.setAdditionalCosts(FormUtil.readDouble("Additional Costs"));
			
			a.save();
			
			System.out.println("Contract No "+a.getContractNo()+" created.\n");
						
			return;
		}
		
		else {
			System.out.println("Invalid input. Please enter a '1' or a '2' next time.\n");
		}
		
		return;
		
	}

	//TODO
	private static void viewContracts() {
		
		TenancyContract.printTenancyContract();		
	}


		
}
