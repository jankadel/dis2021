package de.dis.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Apartment {
    private int apartment_id = -1;
	private String city;
	private String street;
    private int streetnumber;
    private int squarearea;
	private String login;
    private int postalcode;
    
    private int floors;
    private int rent;
    private int rooms;
    private boolean balcony;
    private boolean kitchen;

    public int getApartmentId() {
		return apartment_id;
	}
	
	public void setApartmentId(int apartment_id) {
		this.apartment_id = apartment_id;
	}
	
	public String getCity() {
		return city;
	}
	
	public void setCity(String city) {
		this.city = city;
	}
	
	public String getStreet() {
		return street;
	}
	
	public void setStreet(String street) {
		this.street = street;
	}

    public int getStreetnumber() {
        return streetnumber;
    }

    public void setStreetnumber(int streetnumber) {
        this.streetnumber = streetnumber;
    }
	
	public String getLogin() {
		return login;
	}
	
	public void setLogin(String login) {
		this.login = login;
	}
	
    public int getSquarearea() {
        return squarearea;
    }

    public void setSquarearea(int squarearea) {
        this.squarearea = squarearea;
    }

    public int getFloors() {
        return floors;
    }

    public void setFloors(int floors) {
        this.floors = floors;
    }

    public int getRent() {
        return rent;
    }

    public void setRent(int rent) {
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

    public int getPostalcode() {
        return postalcode;
    }

    public void setPostalcode(int postalcode) {
        this.postalcode = postalcode;
    }

    public static Apartment load(int apartment_id) {
		try {
			// Hole Verbindung
			Connection con = DbConnectionManager.getInstance().getConnection();

			// Erzeuge Anfrage
			String selectSQL = "SELECT * FROM apartment JOIN estate USING (estate_id) WHERE estate_id = ?";
			PreparedStatement pstmt = con.prepareStatement(selectSQL);
			pstmt.setInt(1, apartment_id);

			// Führe Anfrage aus
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				Apartment ts = new Apartment();
				ts.setApartmentId(apartment_id);
				ts.setCity(rs.getString("city"));
				ts.setStreet(rs.getString("street"));
                ts.setStreetnumber(rs.getInt("streetnumber"));
                ts.setSquarearea(rs.getInt("squarearea"));
				ts.setLogin(rs.getString("login"));
                ts.setFloors(rs.getInt("floors"));
                ts.setRent(rs.getInt("rent"));
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

    public void save() {
		// Hole Verbindung
		Connection con = DbConnectionManager.getInstance().getConnection();

		try {
			// FC<ge neues Element hinzu, wenn das Objekt noch keine ID hat.
			if (getApartmentId() == -1) {
				// Achtung, hier wird noch ein Parameter mitgegeben,
				// damit spC$ter generierte IDs zurC<ckgeliefert werden!
				String insertSQLapartment = "INSERT INTO apartment(estate_id, floors, rent, rooms, balcony, kitchen) VALUES ((SELECT estate_id FROM estate WHERE estate_id = ?), ?, ?, ?, ?, ?)";
                String insertSQLestate = "INSERT INTO estate(city, postalcode, street, streetnumber, squarearea, login) VALUES (?, ?, ?, ?, ?, (SELECT login FROM estate_agent WHERE login = ?))";

				PreparedStatement pstmtApartment = con.prepareStatement(insertSQLapartment,
						Statement.RETURN_GENERATED_KEYS);

                PreparedStatement pstmtEstate = con.prepareStatement(insertSQLestate, Statement.RETURN_GENERATED_KEYS);

				// Setze Anfrageparameter und fC<hre Anfrage aus
				pstmtEstate.setString(1, getCity());
                pstmtEstate.setInt(2, getPostalcode());
                pstmtEstate.setString(3, getStreet());
                pstmtEstate.setInt(4, getStreetnumber());
                pstmtEstate.setInt(5, getSquarearea());
                pstmtEstate.setString(6, getLogin());
                pstmtEstate.executeUpdate();

                ResultSet rsEstate = pstmtEstate.getGeneratedKeys();
				
                if (rsEstate.next()) {
					setApartmentId(rsEstate.getInt(1));
				}
                
                pstmtApartment.setInt(2, getFloors());
				pstmtApartment.setInt(3, getRent());
				pstmtApartment.setInt(4, getRooms());
				pstmtApartment.setBoolean(5, getBalcony());
                pstmtApartment.setBoolean(6, getKitchen());
                pstmtApartment.setInt(1, getApartmentId());
				pstmtApartment.executeUpdate();

				pstmtApartment.close();
                rsEstate.close();
                pstmtEstate.close();
			} else {
				// Falls schon eine ID vorhanden ist, mache ein Update...
				String updateSQLapartment = "UPDATE apartment SET floors = ?, rent = ?, rooms = ?, balcony = ?, kitchen = ? WHERE estate_id = ?";
				String updateSQLestate = "UPDATE estate SET city = ?, postalcode = ?, street = ?, streetnumber = ?, squarearea = ?, login = ? WHERE estate_id = ?";

                PreparedStatement pstmtApartment = con.prepareStatement(updateSQLapartment);
                PreparedStatement pstmtEstate = con.prepareStatement(updateSQLestate);

				// Setze Anfrage Parameter
				pstmtApartment.setInt(1, getFloors());
				pstmtApartment.setInt(2, getRent());
				pstmtApartment.setInt(3, getRooms());
				pstmtApartment.setBoolean(4, getBalcony());
                pstmtApartment.setBoolean(5, getKitchen());
				pstmtApartment.setInt(6, getApartmentId());
				pstmtApartment.executeUpdate();

                pstmtEstate.setString(1, getCity());
                pstmtEstate.setInt(2, getPostalcode());
                pstmtEstate.setString(3, getStreet());
                pstmtEstate.setInt(4, getStreetnumber());
                pstmtEstate.setInt(5, getSquarearea());
                pstmtEstate.setString(6, getLogin());
                pstmtEstate.setInt(7, getApartmentId());
                pstmtEstate.executeUpdate();

				pstmtApartment.close();
                pstmtEstate.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

    public void delete() {
		// Hole Verbindung
		Connection con = DbConnectionManager.getInstance().getConnection();

		try {
			String updateSQL = "DELETE CASCADE FROM estate WHERE estate_id = ?";
			PreparedStatement pstmt = con.prepareStatement(updateSQL);

			// Setze Anfrage Parameter
			pstmt.setInt(1, getApartmentId());
			pstmt.executeUpdate();

			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}		
	}
}
