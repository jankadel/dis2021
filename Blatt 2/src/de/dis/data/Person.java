package de.dis.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Person {
	
	private String first_name;
	private String name;
	private String address;
	private int person_id;
	
	public String getFirstName() {
		return first_name;
	}
	
	public void setFirstName(String first_name) {
		this.first_name = first_name;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getAddress() {
		return address;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}
	
	public int getPersonId() {
		return person_id;
	}
	
	public void setPersonId(int person_id) {
		this.person_id = person_id;
	}
	
	/**
	 * Prüft, ob eine Person mit der gegebenen ID bereits exitsiert.
	 * @return existiert oder nicht
	 */
	public static boolean idExists(int id) {
		try {
			// Hole Verbindung
			Connection con = DbConnectionManager.getInstance().getConnection();
			
			// Erzeuge Anfrage
			String selectSQL = "SELECT * FROM person WHERE person_id = ?";
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
	
	/**
	 * LÃ¤dt eine Person aus der Datenbank
	 * @param id ID der zu ladenden Person
	 * @return Estate-Instanz
	 */
	public static Person load(int id) {
		try {
			// Hole Verbindung
			Connection con = DbConnectionManager.getInstance().getConnection();

			// Erzeuge Anfrage
			String selectSQL = "SELECT * FROM person WHERE person_id = ?";
			PreparedStatement pstmt = con.prepareStatement(selectSQL);
			pstmt.setInt(1, id);

			// FÃ¼hre Anfrage aus
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				Person ts = new Person();
				ts.setFirstName(rs.getString("first_name"));
				ts.setName(rs.getString("name"));
				ts.setAddress(rs.getString("address"));
				ts.setPersonId(rs.getInt("person_id"));
				
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
	 * Speichert die Person in der Datenbank. Falls die ID schon in der Datenbank existiert, 
	 * wird der Eintrag nur geupdated, sonst ein neuer angelegt.
	 */
	public void save() {
		// Hole Verbindung
		Connection con = DbConnectionManager.getInstance().getConnection();

		try {		
	
			if (idExists(getPersonId())) {				
				// Falls die person_id schon vorhanden ist, mache ein Update...
				String updateSQL = "UPDATE person SET first_name = ?, name = ?, address = ? WHERE person_id = ?";
				PreparedStatement pstmt = con.prepareStatement(updateSQL);

				// Setze Anfrage Parameter und fC<hre Anfrage aus
				pstmt.setString(1, getFirstName());
				pstmt.setString(2, getName());
				pstmt.setString(3, getAddress());
				pstmt.setInt(4, getPersonId());
				pstmt.executeUpdate();

				pstmt.close();
				
			} else {
				//Füge neues Element hinzu, wenn der Login noch nicht existiert.
				String insertSQL = "INSERT INTO person (first_name, name, address) VALUES (?, ?, ?)";

				PreparedStatement pstmt = con.prepareStatement(insertSQL,
						Statement.RETURN_GENERATED_KEYS);

				// Setze Anfrageparameter und fC<hre Anfrage aus
				pstmt.setString(1, getFirstName());
				pstmt.setString(2, getName());
				pstmt.setString(3, getAddress());
				pstmt.executeUpdate();
				
				// Hole die Id des engefC<gten Datensatzes
				ResultSet rs = pstmt.getGeneratedKeys();
				if (rs.next()) {
					setPersonId(rs.getInt(4));
				}

				rs.close();
				pstmt.close();
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	// TODO comment
	public static void delete(int person_id) {
		// Hole Verbindung
		Connection con = DbConnectionManager.getInstance().getConnection();

		try {		
	
			if (idExists(person_id)) {				
				// Falls die ID schon vorhanden ist, lösche den Eintrag.
				String updateSQL = "DELETE FROM person WHERE person_id = ?";
				PreparedStatement pstmt = con.prepareStatement(updateSQL);

				// Setze Anfrage Parameter
				pstmt.setInt(1, person_id);
				pstmt.executeUpdate();

				pstmt.close();
			} 
			
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

}
