package de.dis.data;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Contract {

	private int contract_no;
	private Date date;
	private String place;
	private int estate_id;
	private int person_id;
	
	public int getContractNo() {
		return contract_no;
	}
	
	public void setContractNo(int contract_no) {
		this.contract_no = contract_no;
	}
	
	public Date getDate() {
		return date;
	}
	
	public void setDate(Date date) {
		this.date = date;
	}
	
	public String getPlace() {
		return place;
	}
	
	public void setPlace(String place) {
		this.place = place;
	}
	
	public int getEstateId() {
		return estate_id;
	}
	
	public void setEstateId(int estate_id) {
		this.estate_id = estate_id;
	}
	
	public int getPersonId() {
		return person_id;
	}
	
	public void setPersonId(int person_id) {
		this.person_id = person_id;
	}
	
	public static boolean contractNoExists(int no) {
		try {
			// Hole Verbindung
			Connection con = DbConnectionManager.getInstance().getConnection();
			
			// Erzeuge Anfrage
			String selectSQL = "SELECT * FROM contract WHERE contract_no = ?";
			PreparedStatement pstmt = con.prepareStatement(selectSQL);
			pstmt.setInt(1, no);
		
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
	
	// TODO comment
	public static Contract load(int no) {
		try {
			// Hole Verbindung
			Connection con = DbConnectionManager.getInstance().getConnection();

			// Erzeuge Anfrage
			String selectSQL = "SELECT * FROM contract WHERE contract_no = ?";
			PreparedStatement pstmt = con.prepareStatement(selectSQL);
			pstmt.setInt(1, no);

			// FÃ¼hre Anfrage aus
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				Contract ts = new Contract();
				ts.setContractNo(rs.getInt("contract_no"));
				ts.setDate(rs.getDate("date"));
				ts.setPlace(rs.getString("place"));
				ts.setEstateId(rs.getInt("estate_id"));
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
	
	// TODO comment
	public void save() {
		// Hole Verbindung
		Connection con = DbConnectionManager.getInstance().getConnection();

		try {		
	
			if (contractNoExists(getContractNo())) {				
				// Falls die contract number schon vorhanden ist, mache ein Update...
				String updateSQL = "UPDATE contract SET date = ?, place = ?, estate_id = ?, person_id = ? WHERE contract_no = ?";
				PreparedStatement pstmt = con.prepareStatement(updateSQL);

				// Setze Anfrage Parameter und fC<hre Anfrage aus
				pstmt.setDate(1, getDate());
				pstmt.setString(2, getPlace());
				pstmt.setInt(3, getEstateId());
				pstmt.setInt(4, getPersonId());
				pstmt.setInt(5, getContractNo());
				pstmt.executeUpdate();

				pstmt.close();
				
			} else {
				//Füge neues Element hinzu, wenn der Login noch nicht existiert.
				String insertSQL = "INSERT INTO contract (date, place, estate_id, person_id) VALUES (?, ?, ?, ?)";

				PreparedStatement pstmt = con.prepareStatement(insertSQL,
						Statement.RETURN_GENERATED_KEYS);

				// Setze Anfrageparameter und fC<hre Anfrage aus
				pstmt.setDate(1, getDate());
				pstmt.setString(2, getPlace());
				pstmt.setInt(3, getEstateId());
				pstmt.setInt(4, getPersonId());
				pstmt.executeUpdate();
				
				// Hole die Id des engefC<gten Datensatzes
				ResultSet rs = pstmt.getGeneratedKeys();
				if (rs.next()) {
					setContractNo(rs.getInt(1));
				}

				rs.close();
				pstmt.close();
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	// TODO comment
	public static void delete(int contract_no) {
		// Hole Verbindung
		Connection con = DbConnectionManager.getInstance().getConnection();

		try {		
	
			if (contractNoExists(contract_no)) {				
				// Falls die contract_no schon vorhanden ist, lösche den Eintrag.
				String updateSQL = "DELETE FROM contract WHERE contract_no = ?";
				PreparedStatement pstmt = con.prepareStatement(updateSQL);

				// Setze Anfrage Parameter
				pstmt.setInt(1, contract_no);
				pstmt.executeUpdate();

				pstmt.close();
			} 
			
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

}
