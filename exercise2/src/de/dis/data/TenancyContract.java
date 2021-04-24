package de.dis.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.sql.Date;

public class TenancyContract {
    private int contract_id = -1;
    private Date contract_date;
    private String place;

    private Date start_date;
    private int duration;
    private int additional_cost;

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

    public int getAdditionalCost() {
        return additional_cost;
    }

    public void setAdditionalCost(int additional_cost) {
        this.additional_cost = additional_cost;
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
                String insertSQLtenancy = "INSERT INTO tenancy_contract(contract_id, start_date, duration, additional_cost) VALUES ((SELECT contract_id FROM contract WHERE contract_id = ?), ?, ?, ?)";
                String insertSQLrents = "INSERT INTO rents(contract_id, estate_id, person_id) VALUES ((SELECT contract_id FROM contract WHERE contract_id = ?), (SELECT estate_id FROM estate WHERE estate_id = ?), (SELECT person_id FROM person WHERE person_id = ?))";
				
                PreparedStatement pstmtC = con.prepareStatement(insertSQLcontract, Statement.RETURN_GENERATED_KEYS);
                PreparedStatement pstmtT = con.prepareStatement(insertSQLtenancy, Statement.RETURN_GENERATED_KEYS);
                PreparedStatement pstmtR = con.prepareStatement(insertSQLrents, Statement.RETURN_GENERATED_KEYS);

				pstmtC.setDate(1, getDate());
                pstmtC.setString(2, getPlace());
                pstmtC.executeUpdate();

				// Hole die Id des engefC<gten Datensatzes
				ResultSet rsC = pstmtC.getGeneratedKeys();
				
                if (rsC.next()) {
					setId(rsC.getInt(1));
				}
				
                pstmtT.setInt(1, getId());
                pstmtT.setDate(2, getStartDate());
                pstmtT.setInt(3, getDuration());
                pstmtT.setInt(4, getAdditionalCost());
                pstmtT.executeUpdate();

                pstmtR.setInt(1, getId());
                pstmtR.setInt(2, getEid());
                pstmtR.setInt(3, getPid());
                pstmtR.executeUpdate();

				// Setze Anfrageparameter und fC<hre Anfrage aus
			
                rsC.close();
                pstmtC.close();
                pstmtT.close();
                pstmtR.close();
			} else {
				// Falls schon eine ID vorhanden ist, mache ein Update...
                String updateSQLcontract = "UPDATE contract SET contract_date = ?, place = ? WHERE contract_id = ?";
                String updateSQLtenancy = "UPDATE tenancy_contract SET start_date = ?, duration = ?, additional_cost = ? WHERE contract_id = ?";
                String updateSQLrents = "UPDATE rents SET estate_id = ?, person_id = ? WHERE contract_id = ?";
                
                PreparedStatement pstmtC = con.prepareStatement(updateSQLcontract);
                PreparedStatement pstmtT = con.prepareStatement(updateSQLtenancy);
                PreparedStatement pstmtR = con.prepareStatement(updateSQLrents);

				// Setze Anfrage Parameter
				pstmtC.setDate(1, getDate());
                pstmtC.setString(2, getPlace());
                pstmtC.setInt(3, getId());
                pstmtC.executeUpdate();

                pstmtT.setInt(4, getId());
                pstmtT.setDate(1, getStartDate());
                pstmtT.setInt(2, getDuration());
                pstmtT.setInt(3, getAdditionalCost());
                pstmtT.executeUpdate();

                pstmtR.setInt(3, getId());
                pstmtR.setInt(1, getEid());
                pstmtR.setInt(2, getPid());
                pstmtR.executeUpdate();

                pstmtC.close();
                pstmtT.close();
                pstmtR.close();
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
			String selectSQL = "SELECT * FROM contract c JOIN tenancy_contract p on c.contract_id = p.contract_id JOIN rents s on c.contract_id = s.contract_id";
			PreparedStatement pstmt = con.prepareStatement(selectSQL);
			// Führe Anfrage aus
			ResultSet rs = pstmt.executeQuery();
            System.out.println(" --- Mietverträge ---");
            while(rs.next()){
                System.out.println(
                    "Vertrags-ID: "+rs.getString("contract_id")+"\nVertragsdatum: "+rs.getDate("contract_date")+"\nOrt: "+rs.getString("place")+"\nStartdatum: "+rs.getDate("start_date")+"\nMietdauer: "+rs.getInt("duration")+"\nNebenkosten: "+rs.getInt("additional_cost")+"\nPersonen ID: "+rs.getInt("person_id")+"\nHaus-ID: "+rs.getInt("estate_id")
                );
                System.out.println("----------");
            } 
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }
}
