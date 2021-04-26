package de.dis.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * House-Bean TODO
 * 
 * Beispiel-Tabelle:
 * CREATE TABLE estate (
 * name varchar(255), 
 * address varchar(255), 
 * login varchar(40) UNIQUE, 
 * password varchar(40), 
 * id serial primary key);
 */
public class House extends Estate{
	private int floors;
	private double price;
	private boolean garden;
	
	public int getFloors() {
		return floors;
	}
	
	public void setFloors(int floors) {
		this.floors = floors;
	}
	
	public double getPrice() {
		return price;
	}
	
	public void setPrice(double price) {
		this.price = price;
	}
	
	public boolean getGarden() {
		return garden;
	}
	
	public void setGarden(boolean garden) {
		this.garden = garden;
	}
	
	
	/**
	 * Lädt ein Haus aus der Datenbank
	 * @param id ID des zu ladenden Hauses
	 * @return Haus-Instanz
	 */
	public static House load(int id) {
		try {
			// Hole Verbindung
			Connection con = DbConnectionManager.getInstance().getConnection();

			// Erzeuge Anfrage
			String selectSQL = "SELECT * FROM house WHERE estate_id = ?";
			PreparedStatement pstmt = con.prepareStatement(selectSQL);
			pstmt.setInt(1, id);

			// Führe Anfrage aus
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				House ts = new House();
				ts.setId(rs.getInt("estate_id"));
				ts.setCity(rs.getString("city"));
				ts.setPostalCode(rs.getString("postal_code"));
				ts.setStreet(rs.getString("street"));
				ts.setStreetNo(rs.getInt("street_no"));
				ts.setSquareArea(rs.getDouble("square_area"));
				ts.setLogin(rs.getString("login"));
				ts.setFloors(rs.getInt("floors"));
				ts.setPrice(rs.getDouble("price"));
				ts.setGarden(rs.getBoolean("garden"));			

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
	 * Speichert das Haus in der Datenbank. Falls die ID schon in der Datenbank existiert, 
	 * wird der Eintrag nur geupdated, sonst ein neuer angelegt.
	 */
	@Override public void save() {
		// Hole Verbindung
		Connection con = DbConnectionManager.getInstance().getConnection();

		try {		
	
			if (idExists(getId())) {				
				// Falls der Login schon vorhanden ist, mache ein Update...
				String updateSQL = "UPDATE house SET city = ?, postal_code = ?, street = ?, street_no = ?, square_area = ?, "
						+ "login = ?, floors = ?, price = ?, garden = ? WHERE estate_id = ?";
				PreparedStatement pstmt = con.prepareStatement(updateSQL);

				// Setze Anfrage Parameter und fC<hre Anfrage aus
				pstmt.setString(1, getCity());
				pstmt.setString(2, getPostalCode());
				pstmt.setString(3, getStreet());
				pstmt.setInt(4, getStreetNo());
				pstmt.setDouble(5, getSquareArea());
				pstmt.setString(6, getLogin());
				pstmt.setInt(7, getFloors());
				pstmt.setDouble(8, getPrice());
				pstmt.setBoolean(9, getGarden());
				pstmt.setInt(10, getId());
				pstmt.executeUpdate();

				pstmt.close();
				
			} else {
				//Füge neues Element hinzu, wenn der Login noch nicht existiert.
				String insertSQL = "INSERT INTO house (city, postal_code, street, street_no, square_area, login, floors, price, garden)"
						+ " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

				PreparedStatement pstmt = con.prepareStatement(insertSQL,
						Statement.RETURN_GENERATED_KEYS);

				// Setze Anfrageparameter und führe Anfrage aus
				pstmt.setString(1, getCity());
				pstmt.setString(2, getPostalCode());
				pstmt.setString(3, getStreet());
				pstmt.setInt(4, getStreetNo());
				pstmt.setDouble(5, getSquareArea());
				pstmt.setString(6, getLogin());
				pstmt.setInt(7, getFloors());
				pstmt.setDouble(8, getPrice());
				pstmt.setBoolean(9, getGarden());
				pstmt.executeUpdate();
				
				// Hole die Id des engefügten Datensatzes
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
				String updateSQL = "DELETE FROM house WHERE estate_id = ?";
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
			String selectSQL = "SELECT * FROM house WHERE estate_id = ?";
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
