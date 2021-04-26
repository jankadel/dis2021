package de.dis.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Makler-Bean
 * 
 * Beispiel-Tabelle:
 * CREATE TABLE makler (
 * name varchar(255), 
 * address varchar(255), 
 * login varchar(40) UNIQUE, 
 * password varchar(40), 
 * id serial primary key);
 */
public class EstateAgent {
	private String name;
	private String address;
	private String login;
	private String password;
	
	
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
	
	public String getLogin() {
		return login;
	}
	
	public void setLogin(String login) {
		this.login = login;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	/**
	 * Prüft, ob ein Estate Agent mit dem Login bereits exitsiert.
	 * @return existiert oder nicht
	 */
	public static boolean loginExists(String login) {
		try {
			// Hole Verbindung
			Connection con = DbConnectionManager.getInstance().getConnection();
			
			// Erzeuge Anfrage
			String selectSQL = "SELECT * FROM estate_agent WHERE login = ?";
			PreparedStatement pstmt = con.prepareStatement(selectSQL);
			pstmt.setString(1, login);
		
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
	public static Boolean CheckPassword(String login, String password) {
		try {
			// Hole Verbindung
			Connection con = DbConnectionManager.getInstance().getConnection();
			
			// Erzeuge Anfrage
			String selectSQL = "SELECT password FROM estate_agent WHERE login = ?";
			PreparedStatement pstmt = con.prepareStatement(selectSQL);
			pstmt.setString(1, login);
		
			// FÃ¼hre Anfrage aus
			ResultSet rs = pstmt.executeQuery();
			
			boolean result = false;
			
			if(rs.next()) {
				result = (rs.getString("password")).equals(password);			
				
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
	 * LÃ¤dt einen Estate Agent aus der Datenbank
	 * @param login Login des zu ladenden Estate Agents
	 * @return Estate Agent-Instanz
	 */
	public static EstateAgent load(String login) {
		try {
			// Hole Verbindung
			Connection con = DbConnectionManager.getInstance().getConnection();

			// Erzeuge Anfrage
			String selectSQL = "SELECT * FROM estate_agent WHERE login = ?";
			PreparedStatement pstmt = con.prepareStatement(selectSQL);
			pstmt.setString(1, login);

			// FÃ¼hre Anfrage aus
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				EstateAgent ts = new EstateAgent();
				ts.setName(rs.getString("name"));
				ts.setAddress(rs.getString("address"));
				ts.setLogin(rs.getString("login"));
				ts.setPassword(rs.getString("password"));

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
	 * Speichert den Estate Agent in der Datenbank. Falls der Login schon in der Datenbank existiert, 
	 * wird der Eintrag nur geupdated, sonst ein neuer angelegt.
	 */
	public void save() {
		// Hole Verbindung
		Connection con = DbConnectionManager.getInstance().getConnection();

		try {		
	
			if (loginExists(getLogin())) {				
				// Falls der Login schon vorhanden ist, mache ein Update...
				String updateSQL = "UPDATE estate_agent SET name = ?, address = ?, password = ? WHERE login = ?";
				PreparedStatement pstmt = con.prepareStatement(updateSQL);

				// Setze Anfrage Parameter
				pstmt.setString(1, getName());
				pstmt.setString(2, getAddress());
				pstmt.setString(3, getPassword());
				pstmt.setString(4, getLogin());
				pstmt.executeUpdate();

				pstmt.close();
			} else {
				//Füge neues Element hinzu, wenn der Login noch nicht existiert.
				String insertSQL = "INSERT INTO estate_agent(name, address, login, password) VALUES (?, ?, ?, ?)";

				PreparedStatement pstmt = con.prepareStatement(insertSQL);

				// Setze Anfrageparameter und fC<hre Anfrage aus
				pstmt.setString(1, getName());
				pstmt.setString(2, getAddress());
				pstmt.setString(3, getLogin());
				pstmt.setString(4, getPassword());
				pstmt.executeUpdate();

				pstmt.close();
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// TODO comment
	public static void delete(String login) {
		// Hole Verbindung
		Connection con = DbConnectionManager.getInstance().getConnection();

		try {		
	
			if (loginExists(login)) {				
				// Falls der Login schon vorhanden ist, lösche den Eintrag.
				String updateSQL = "DELETE FROM estate_agent WHERE login = ?";
				PreparedStatement pstmt = con.prepareStatement(updateSQL);

				// Setze Anfrage Parameter
				pstmt.setString(1, login);
				pstmt.executeUpdate();

				pstmt.close();
			} 
			
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

}
