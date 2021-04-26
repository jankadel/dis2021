package de.dis.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class PurchaseContract extends Contract {

	private int no_installments;
	private double interest_rate;
	
	public int getNoInstallments() {
		return no_installments;
	}

	public void setNoInstallments(int no_installments) {
		this.no_installments = no_installments;
	}

	public double getInterestRate() {
		return interest_rate;
	}

	public void setInterestRate(double interest_rate) {
		this.interest_rate = interest_rate;
	}


	public static boolean contractNoExists(int no) {
		try {
			// Hole Verbindung
			Connection con = DbConnectionManager.getInstance().getConnection();
			
			// Erzeuge Anfrage
			String selectSQL = "SELECT * FROM purchase_contract WHERE contract_no = ?";
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
			String selectSQL = "SELECT * FROM purchase_contract WHERE contract_no = ?";
			PreparedStatement pstmt = con.prepareStatement(selectSQL);
			pstmt.setInt(1, no);

			// FÃ¼hre Anfrage aus
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				PurchaseContract ts = new PurchaseContract();
				ts.setContractNo(rs.getInt("contract_no"));
				ts.setDate(rs.getDate("date"));
				ts.setPlace(rs.getString("place"));
				ts.setEstateId(rs.getInt("estate_id"));
				ts.setPersonId(rs.getInt("person_id"));
				ts.setNoInstallments(rs.getInt("no_installments"));
				ts.setInterestRate(rs.getDouble("interest_rate"));
				
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
				String updateSQL = "UPDATE purchase_contract SET date = ?, place = ?, estate_id = ?, person_id = ?,"
						+ " no_installments = ?, interest_rate = ? WHERE contract_no = ?";
				PreparedStatement pstmt = con.prepareStatement(updateSQL);

				// Setze Anfrage Parameter und fC<hre Anfrage aus
				pstmt.setDate(1, getDate());
				pstmt.setString(2, getPlace());
				pstmt.setInt(3, getEstateId());
				pstmt.setInt(4, getPersonId());
				pstmt.setInt(5, getNoInstallments());
				pstmt.setDouble(6, getInterestRate());
				pstmt.setInt(7, getContractNo());
				pstmt.executeUpdate();

				pstmt.close();
				
			} else {
				//Füge neues Element hinzu, wenn der Login noch nicht existiert.
				String insertSQL = "INSERT INTO purchase_contract (date, place, estate_id, person_id, no_installments, interest_rate) VALUES (?, ?, ?, ?, ?, ?)";

				PreparedStatement pstmt = con.prepareStatement(insertSQL,
						Statement.RETURN_GENERATED_KEYS);

				// Setze Anfrageparameter und fC<hre Anfrage aus
				pstmt.setDate(1, getDate());
				pstmt.setString(2, getPlace());
				pstmt.setInt(3, getEstateId());
				pstmt.setInt(4, getPersonId());
				pstmt.setInt(5, getNoInstallments());
				pstmt.setDouble(6, getInterestRate());
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
				String updateSQL = "DELETE FROM purchase_contract WHERE contract_no = ?";
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
