package de.dis.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Estate-Bean TODO
 * 
 * Beispiel-Tabelle:
 * CREATE TABLE estate (
 * name varchar(255), 
 * address varchar(255), 
 * login varchar(40) UNIQUE, 
 * password varchar(40), 
 * id serial primary key);
 */
public class Estate {
	private int id;
	private String city;
	private String postal_code;
	private String street;
	private int street_no;
	private double square_area;
	private String login;
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getCity() {
		return city;
	}
	
	public void setCity(String city) {
		this.city = city;
	}
	
	public String getPostalCode() {
		return postal_code;
	}
	
	public void setPostalCode(String postal_code) {
		this.postal_code = postal_code;
	}
	
	public String getStreet() {
		return street;
	}
	
	public void setStreet(String street) {
		this.street = street;
	}
	
	public int getStreetNo() {
		return street_no;
	}
	
	public void setStreetNo(int street_no) {
		this.street_no = street_no;
	}
	
	public double getSquareArea() {
		return square_area;
	}
	
	public void setSquareArea(double square_area) {
		this.square_area = square_area;
	}
	
	public String getLogin() {
		return login;
	}
	
	public void setLogin(String login) {
		this.login = login;
	}
	
	/**
	 * Prüft, ob ein Estate mit der gegebenen ID bereits exitsiert.
	 * @return existiert oder nicht
	 */
	public static boolean idExists(int id) {
		try {
			// Hole Verbindung
			Connection con = DbConnectionManager.getInstance().getConnection();
			
			// Erzeuge Anfrage
			String selectSQL = "SELECT * FROM estate WHERE estate_id = ?";
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
		
		return true;
		
		}
	
	// TODO
	public static boolean checkAgent(String login, int estate_id) {
		try {
			// Hole Verbindung
			Connection con = DbConnectionManager.getInstance().getConnection();
			
			// Erzeuge Anfrage
			String selectSQL = "SELECT login FROM estate WHERE estate_id = ?";
			PreparedStatement pstmt = con.prepareStatement(selectSQL);
			pstmt.setInt(1, estate_id);
		
			// FÃ¼hre Anfrage aus
			ResultSet rs = pstmt.executeQuery();
			
			boolean result = false;
			
			if(rs.next()) {
				result = (rs.getString("login")).equals(login);			
				
			}
			
			rs.close();
			pstmt.close();
			
			return (result);

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * LÃ¤dt ein Estate aus der Datenbank
	 * @param id ID des zu ladenden Estates
	 * @return Estate-Instanz
	 */
	public static Estate load(int id) {
		try {
			// Hole Verbindung
			Connection con = DbConnectionManager.getInstance().getConnection();

			// Erzeuge Anfrage
			String selectSQL = "SELECT * FROM estate WHERE estate_id = ?";
			PreparedStatement pstmt = con.prepareStatement(selectSQL);
			pstmt.setInt(1, id);

			// FÃ¼hre Anfrage aus
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				Estate ts = new Estate();
				ts.setId(rs.getInt("estate_id"));
				ts.setCity(rs.getString("city"));
				ts.setPostalCode(rs.getString("postal_code"));
				ts.setStreet(rs.getString("street"));
				ts.setStreetNo(rs.getInt("street_no"));
				ts.setSquareArea(rs.getDouble("square_area"));
				ts.setLogin(rs.getString("login"));

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
	 * Speichert das Estate in der Datenbank. Falls die ID schon in der Datenbank existiert, 
	 * wird der Eintrag nur geupdated, sonst ein neuer angelegt.
	 */
	public void save() {
		// Hole Verbindung
		Connection con = DbConnectionManager.getInstance().getConnection();

		try {		
	
			if (idExists(getId())) {				
				// Falls der Login schon vorhanden ist, mache ein Update...
				String updateSQL = "UPDATE estate SET city = ?, postal_code = ?, street = ?, street_no = ?, square_area = ?, "
						+ "login = ? WHERE estate_id = ?";
				PreparedStatement pstmt = con.prepareStatement(updateSQL);

				// Setze Anfrage Parameter und fC<hre Anfrage aus
				pstmt.setString(1, getCity());
				pstmt.setString(2, getPostalCode());
				pstmt.setString(3, getStreet());
				pstmt.setInt(4, getStreetNo());
				pstmt.setDouble(5, getSquareArea());
				pstmt.setString(6, getLogin());
				pstmt.setInt(7, getId());
				pstmt.executeUpdate();

				pstmt.close();
				
			} else {
				//Füge neues Element hinzu, wenn der Login noch nicht existiert.
				String insertSQL = "INSERT INTO estate (city, postal_code, street, street_no, square_area, login) VALUES (?, ?, ?, ?, ?, ?)";

				PreparedStatement pstmt = con.prepareStatement(insertSQL,
						Statement.RETURN_GENERATED_KEYS);

				// Setze Anfrageparameter und fC<hre Anfrage aus
				pstmt.setString(1, getCity());
				pstmt.setString(2, getPostalCode());
				pstmt.setString(3, getStreet());
				pstmt.setInt(4, getStreetNo());
				pstmt.setDouble(5, getSquareArea());
				pstmt.setString(6, getLogin());
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
				String updateSQL = "DELETE FROM estate WHERE estate_id = ?";
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

}
