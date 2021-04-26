package de.dis.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Apartment-Bean TODO
 * 
 * Beispiel-Tabelle:
 * CREATE TABLE estate (
 * name varchar(255), 
 * address varchar(255), 
 * login varchar(40) UNIQUE, 
 * password varchar(40), 
 * id serial primary key);
 */
public class Apartment extends Estate{
	private int floor;
	private double rent;
	private int rooms;
	private boolean balcony;
	private boolean kitchen;
	
	public int getFloor() {
		return floor;
	}
	
	public void setFloor(int floor) {
		this.floor = floor;
	}
	
	public double getRent() {
		return rent;
	}
	
	public void setRent(double rent) {
		this.rent = rent;
	}
	
	public int getRooms() {
		return rooms;
	}
	
	public void setRooms(int rooms) {
		this.rooms = rooms;
	}
	
	public boolean getBalcony() {
		return balcony;
	}
	
	public void setBalcony(boolean balcony) {
		this.balcony = balcony;
	}
	
	public boolean getKitchen() {
		return kitchen;
	}
	
	public void setKitchen(boolean kitchen) {
		this.kitchen = kitchen;
	}
	
	
	/**
	 * LÃ¤dt ein Apartment aus der Datenbank
	 * @param id ID des zu ladenden Apartments
	 * @return Apartment-Instanz
	 */
	public static Apartment load(int id) {
		try {
			// Hole Verbindung
			Connection con = DbConnectionManager.getInstance().getConnection();

			// Erzeuge Anfrage
			String selectSQL = "SELECT * FROM apartment WHERE estate_id = ?";
			PreparedStatement pstmt = con.prepareStatement(selectSQL);
			pstmt.setInt(1, id);

			// FÃ¼hre Anfrage aus
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				Apartment ts = new Apartment();
				ts.setId(rs.getInt("estate_id"));
				ts.setCity(rs.getString("city"));
				ts.setPostalCode(rs.getString("postal_code"));
				ts.setStreet(rs.getString("street"));
				ts.setStreetNo(rs.getInt("street_no"));
				ts.setSquareArea(rs.getDouble("square_area"));
				ts.setLogin(rs.getString("login"));
				ts.setFloor(rs.getInt("floor"));
				ts.setRent(rs.getDouble("rent"));
				ts.setRooms(rs.getInt("rooms"));
				ts.setBalcony(rs.getBoolean("balcony"));
				ts.setKitchen(rs.getBoolean("kitchen"));				

				rs.close();
				pstmt.close();
				return ts;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Speichert das Apartment in der Datenbank. Falls die ID schon in der Datenbank existiert, 
	 * wird der Eintrag nur geupdated, sonst ein neuer angelegt.
	 */
	@Override public void save() {
		// Hole Verbindung
		Connection con = DbConnectionManager.getInstance().getConnection();

		try {		
	
			if (idExists(getId())) {				
				// Falls der Login schon vorhanden ist, mache ein Update...
				String updateSQL = "UPDATE apartment SET city = ?, postal_code = ?, street = ?, street_no = ?, square_area = ?, "
						+ "login = ?, floor = ?, rent = ?, rooms = ?, balcony = ?, kitchen = ? WHERE estate_id = ?";
				PreparedStatement pstmt = con.prepareStatement(updateSQL);

				// Setze Anfrage Parameter und fC<hre Anfrage aus
				pstmt.setString(1, getCity());
				pstmt.setString(2, getPostalCode());
				pstmt.setString(3, getStreet());
				pstmt.setInt(4, getStreetNo());
				pstmt.setDouble(5, getSquareArea());
				pstmt.setString(6, getLogin());
				pstmt.setInt(7, getFloor());
				pstmt.setDouble(8, getRent());
				pstmt.setInt(9, getRooms());
				pstmt.setBoolean(10, getBalcony());
				pstmt.setBoolean(11, getKitchen());
				pstmt.setInt(12, getId());
				pstmt.executeUpdate();

				pstmt.close();
				
			} else {
				//Füge neues Element hinzu, wenn der Login noch nicht existiert.
				String insertSQL = "INSERT INTO apartment (city, postal_code, street, street_no, square_area, login, floor, rent, rooms, balcony, kitchen)"
						+ " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

				PreparedStatement pstmt = con.prepareStatement(insertSQL,
						Statement.RETURN_GENERATED_KEYS);

				// Setze Anfrageparameter und fC<hre Anfrage aus
				pstmt.setString(1, getCity());
				pstmt.setString(2, getPostalCode());
				pstmt.setString(3, getStreet());
				pstmt.setInt(4, getStreetNo());
				pstmt.setDouble(5, getSquareArea());
				pstmt.setString(6, getLogin());
				pstmt.setInt(7, getFloor());
				pstmt.setDouble(8, getRent());
				pstmt.setInt(9, getRooms());
				pstmt.setBoolean(10, getBalcony());
				pstmt.setBoolean(11, getKitchen());
				pstmt.executeUpdate();
				
				// Hole die Id des engefC<gten Datensatzes
				ResultSet rs = pstmt.getGeneratedKeys();
				if (rs.next()) {
					setId(rs.getInt(1));
				}

				rs.close();
				pstmt.close();
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	// TODO comment
	public static void delete(int estate_id) {
		// Hole Verbindung
		Connection con = DbConnectionManager.getInstance().getConnection();

		try {		
	
			if (idExists(estate_id)) {				
				// Falls die ID schon vorhanden ist, lösche den Eintrag.
				String updateSQL = "DELETE FROM apartment WHERE estate_id = ?";
				PreparedStatement pstmt = con.prepareStatement(updateSQL);

				// Setze Anfrage Parameter
				pstmt.setInt(1, estate_id);
				pstmt.executeUpdate();

				pstmt.close();
			} 
			
		} catch (SQLException e) {
			e.printStackTrace();
		}

		}

	//TODO
	public static boolean idExists(int id) {
		try {
			// Hole Verbindung
			Connection con = DbConnectionManager.getInstance().getConnection();
			
			// Erzeuge Anfrage
			String selectSQL = "SELECT * FROM apartment WHERE estate_id = ?";
			PreparedStatement pstmt = con.prepareStatement(selectSQL);
			pstmt.setInt(1, id);
		
			// FÃ¼hre Anfrage aus
			ResultSet rs = pstmt.executeQuery();
			
			boolean result = rs.next();
			
			rs.close();
			pstmt.close();
			
			return (result);

		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return false;
		}
}
