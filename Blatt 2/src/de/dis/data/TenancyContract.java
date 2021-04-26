package de.dis.data;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

public class TenancyContract extends Contract{
	
	private Date start_date;
	private int duration;
	private double additional_costs;
	
	public Date getStartDate() {
		return start_date;
	}
	
	public void setStartDate(Date start_date) {
		this.start_date = start_date;
	}
	
	public int getDuration() {
		return duration;
	}
	
	public void setDuration(int duration) {
		this.duration = duration;
	}
	
	public double getAdditionalCosts() {
		return additional_costs;
	}
	
	public void setAdditionalCosts(double additional_costs) {
		this.additional_costs = additional_costs;
	}
	

	public static boolean contractNoExists(int no) {
		try {
			// Hole Verbindung
			Connection con = DbConnectionManager.getInstance().getConnection();
			
			// Erzeuge Anfrage
			String selectSQL = "SELECT * FROM tenancy_contract WHERE contract_no = ?";
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
			String selectSQL = "SELECT * FROM tenancy_contract WHERE contract_no = ?";
			PreparedStatement pstmt = con.prepareStatement(selectSQL);
			pstmt.setInt(1, no);

			// FÃ¼hre Anfrage aus
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				TenancyContract ts = new TenancyContract();
				ts.setContractNo(rs.getInt("contract_no"));
				ts.setDate(rs.getDate("date"));
				ts.setPlace(rs.getString("place"));
				ts.setEstateId(rs.getInt("estate_id"));
				ts.setPersonId(rs.getInt("person_id"));
				ts.setStartDate(rs.getDate("start_date"));
				ts.setDuration(rs.getInt("duration"));;
				ts.setAdditionalCosts(rs.getDouble("additional_costs"));
				
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
				String updateSQL = "UPDATE tenancy_contract SET date = ?, place = ?, estate_id = ?, person_id = ?,"
						+ " start_date = ?, duration = ?, additional_costs = ? WHERE contract_no = ?";
				PreparedStatement pstmt = con.prepareStatement(updateSQL);

				// Setze Anfrage Parameter und fC<hre Anfrage aus
				pstmt.setDate(1, getDate());
				pstmt.setString(2, getPlace());
				pstmt.setInt(3, getEstateId());
				pstmt.setInt(4, getPersonId());
				pstmt.setDate(5, getStartDate());
				pstmt.setInt(6, getDuration());
				pstmt.setDouble(7, getAdditionalCosts());
				pstmt.setInt(8, getContractNo());
				pstmt.executeUpdate();

				pstmt.close();
				
			} else {
				//Füge neues Element hinzu, wenn der Login noch nicht existiert.
				String insertSQL = "INSERT INTO tenancy_contract (date, place, estate_id, person_id, start_date, duration, additional_costs) VALUES (?, ?, ?, ?, ?, ?, ?)";

				PreparedStatement pstmt = con.prepareStatement(insertSQL,
						Statement.RETURN_GENERATED_KEYS);

				// Setze Anfrageparameter und fC<hre Anfrage aus
				pstmt.setDate(1, getDate());
				pstmt.setString(2, getPlace());
				pstmt.setInt(3, getEstateId());
				pstmt.setInt(4, getPersonId());
				pstmt.setDate(5, getStartDate());
				pstmt.setInt(6, getDuration());
				pstmt.setDouble(7, getAdditionalCosts());
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
				String updateSQL = "DELETE FROM tenancy_contract WHERE contract_no = ?";
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
	
	//TODO
	public static void printTenancyContract() {	
		try {
			// Hole Verbindung
			Connection con = DbConnectionManager.getInstance().getConnection();
			
			// Erzeuge Anfrage
			String selectSQL = "SELECT * FROM tenancy_contract";
			PreparedStatement pstmt = con.prepareStatement(selectSQL);

			// FÃ¼hre Anfrage aus
			ResultSet rs = pstmt.executeQuery();
			
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnsNumber = rsmd.getColumnCount();
			while (rs.next()) {
				for (int i = 1; i <= columnsNumber; i++) {
					if (i > 1) System.out.print(",  ");
					String columnValue = rs.getString(i);
					System.out.print(columnValue + " " + rsmd.getColumnName(i));
				}
				System.out.println("");
			}
			
			rs.close();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}


}
