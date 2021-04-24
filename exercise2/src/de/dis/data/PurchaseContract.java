package de.dis.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.sql.Date;

public class PurchaseContract {
    private int contract_id = -1;
    private Date contract_date;
    private String place;

    private int installments;
    private int interest_rate;

    private int person_id;
    private int estate_id;

    public int getId() {
        return contract_id;
    }

    public void setId(int contract_id) {
        this.contract_id = contract_id;
    }

    public Date getDate() {
        return contract_date;
    }

    public void setDate(Date contract_date) {
        this.contract_date = contract_date;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public int getInstallments() {
        return installments;
    }

    public void setInstallments(int installments) {
        this.installments = installments;
    }

    public int getInterest() {
        return interest_rate;
    }

    public void setInterest(int interest_rate) {
        this.interest_rate = interest_rate;
    }

    public int getPid() {
        return person_id;
    }

    public void setPid(int person_id) {
        this.person_id = person_id;
    }

    public int getEid() {
        return estate_id;
    }

    public void setEid(int estate_id) {
        this.estate_id = estate_id;
    }

    public void save() {
		// Hole Verbindung
		Connection con = DbConnectionManager.getInstance().getConnection();

		try {
			// FC<ge neues Element hinzu, wenn das Objekt noch keine ID hat.
			if (getId() == -1) {
				// Achtung, hier wird noch ein Parameter mitgegeben,
				// damit spC$ter generierte IDs zurC<ckgeliefert werden!
				String insertSQLcontract = "INSERT INTO contract(contract_date, place) VALUES (?, ?)";
                String insertSQLpurchase = "INSERT INTO purchase_contract(contract_id, installments, interest_rate) VALUES ((SELECT contract_id FROM contract WHERE contract_id = ?), ?, ?)";
                String insertSQLsells = "INSERT INTO sells(contract_id, estate_id, person_id) VALUES ((SELECT contract_id FROM contract WHERE contract_id = ?), (SELECT estate_id FROM estate WHERE estate_id = ?), (SELECT person_id FROM person WHERE person_id = ?))";
				
                PreparedStatement pstmtC = con.prepareStatement(insertSQLcontract, Statement.RETURN_GENERATED_KEYS);
                PreparedStatement pstmtP = con.prepareStatement(insertSQLpurchase, Statement.RETURN_GENERATED_KEYS);
                PreparedStatement pstmtS = con.prepareStatement(insertSQLsells, Statement.RETURN_GENERATED_KEYS);

				pstmtC.setDate(1, getDate());
                pstmtC.setString(2, getPlace());
                pstmtC.executeUpdate();

				// Hole die Id des engefC<gten Datensatzes
				ResultSet rsC = pstmtC.getGeneratedKeys();
				
                if (rsC.next()) {
					setId(rsC.getInt(1));
				}
				
                pstmtP.setInt(1, getId());
                pstmtP.setInt(2, getInstallments());
                pstmtP.setInt(3, getInterest());
                pstmtP.executeUpdate();

                pstmtS.setInt(1, getId());
                pstmtS.setInt(2, getEid());
                pstmtS.setInt(3, getPid());
                pstmtS.executeUpdate();

				// Setze Anfrageparameter und fC<hre Anfrage aus
			
                rsC.close();
                pstmtC.close();
                pstmtP.close();
                pstmtS.close();
			} else {
				// Falls schon eine ID vorhanden ist, mache ein Update...
                String updateSQLcontract = "UPDATE contract SET contract_date = ?, place = ? WHERE contract_id = ?";
                String updateSQLpurchase = "UPDATE purchase_contract SET installments = ?, interest_rate = ? WHERE contract_id = ?";
                String updateSQLsells = "UPDATE sells SET estate_id = ?, person_id = ? WHERE contract_id = ?";
                
                PreparedStatement pstmtC = con.prepareStatement(updateSQLcontract);
                PreparedStatement pstmtP = con.prepareStatement(updateSQLpurchase);
                PreparedStatement pstmtS = con.prepareStatement(updateSQLsells);

				// Setze Anfrage Parameter
				pstmtC.setDate(1, getDate());
                pstmtC.setString(2, getPlace());
                pstmtC.setInt(3, getId());
                pstmtC.executeUpdate();

                pstmtP.setInt(3, getId());
                pstmtP.setInt(1, getInstallments());
                pstmtP.setInt(2, getInterest());
                pstmtP.executeUpdate();

                pstmtS.setInt(3, getId());
                pstmtS.setInt(1, getEid());
                pstmtS.setInt(2, getPid());
                pstmtS.executeUpdate();

                pstmtC.close();
                pstmtP.close();
                pstmtS.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

    public static void printContracts() {
        try {
			// Hole Verbindung
			Connection con = DbConnectionManager.getInstance().getConnection();

			// Erzeuge Anfrage
			String selectSQL = "SELECT * FROM contract c JOIN purchase_contract p on c.contract_id = p.contract_id JOIN sells s on c.contract_id = s.contract_id";
			PreparedStatement pstmt = con.prepareStatement(selectSQL);
			// Führe Anfrage aus
			ResultSet rs = pstmt.executeQuery();
            System.out.println(" --- Kaufverträge ---");
            while(rs.next()){
                System.out.println(
                    "Vertrags-ID: "+rs.getString("contract_id")+"\nVertragsdatum: "+rs.getDate("contract_date")+"\nOrt: "+rs.getString("place")+"\nRatenanzahl: "+rs.getInt("installments")+"\nZinssatz: "+rs.getInt("interest_rate")+"\nPersonen ID: "+rs.getInt("person_id")+"\nHaus-ID: "+rs.getInt("estate_id")
                );
                System.out.println("----------");
            } 
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }
}
