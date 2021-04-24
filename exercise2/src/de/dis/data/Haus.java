package de.dis.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Haus {
    private int house_id = -1;
	private String city;
	private String street;
    private int streetnumber;
    private int squarearea;
	private String login;
	private int postalcode;

	private int floors;
	private int price;
	private boolean garden;
	
	public int getHouseId() {
		return house_id;
	}
	
	public void setHouseId(int house_id) {
		this.house_id = house_id;
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

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public boolean getGarden() {
		return garden;
	}

	public void setGarden(boolean garden) {
		this.garden = garden;
	}

	public int getPostalcode() {
		return postalcode;
	}
    
	public void setPostalcode(int postalcode) {
		this.postalcode = postalcode;
	}

	public static Haus load(int house_id) {
		try {
			// Hole Verbindung
			Connection con = DbConnectionManager.getInstance().getConnection();

			// Erzeuge Anfrage
			String selectSQL = "SELECT * FROM house JOIN estate USING (estate_id) WHERE estate_id = ?";
			PreparedStatement pstmt = con.prepareStatement(selectSQL);
			pstmt.setInt(1, house_id);

			// Führe Anfrage aus
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				Haus ts = new Haus();
				ts.setHouseId(house_id);
				ts.setCity(rs.getString("city"));
				ts.setStreet(rs.getString("street"));
                ts.setStreetnumber(rs.getInt("streetnumber"));
                ts.setSquarearea(rs.getInt("squarearea"));
				ts.setLogin(rs.getString("login"));
                ts.setFloors(rs.getInt("floors"));
                ts.setPrice(rs.getInt("price"));
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

	public void save() {
		// Hole Verbindung
		Connection con = DbConnectionManager.getInstance().getConnection();

		try {
			// FC<ge neues Element hinzu, wenn das Objekt noch keine ID hat.
			if (getHouseId() == -1) {
				// Achtung, hier wird noch ein Parameter mitgegeben,
				// damit spC$ter generierte IDs zurC<ckgeliefert werden!
				String insertSQLestate = "INSERT INTO estate(city, postalcode, street, streetnumber, squarearea, login) VALUES (?, ?, ?, ?, ?, (SELECT login FROM estate_agent WHERE login = ?))";
				String insertSQLhouse = "INSERT INTO house(estate_id, floors, price, garden) VALUES ((SELECT estate_id FROM estate WHERE estate_id = ?), ?, ?, ?)";

				PreparedStatement pstmtHouse = con.prepareStatement(insertSQLhouse,
						Statement.RETURN_GENERATED_KEYS);

                PreparedStatement pstmtEstate = con.prepareStatement(insertSQLestate, Statement.RETURN_GENERATED_KEYS);

				pstmtEstate.setString(1, getCity());
                pstmtEstate.setInt(2, getPostalcode());
                pstmtEstate.setString(3, getStreet());
                pstmtEstate.setInt(4, getStreetnumber());
                pstmtEstate.setInt(5, getSquarearea());
                pstmtEstate.setString(6, getLogin());
                pstmtEstate.executeUpdate();

				// Hole die Id des engefC<gten Datensatzes
				ResultSet rsEstate = pstmtEstate.getGeneratedKeys();
				
                if (rsEstate.next()) {
					setHouseId(rsEstate.getInt(1));
				}
				
				// Setze Anfrageparameter und fC<hre Anfrage aus
				pstmtHouse.setInt(2, getFloors());
				pstmtHouse.setInt(3, getPrice());
				pstmtHouse.setBoolean(4, getGarden());
				pstmtHouse.setInt(1, getHouseId());
				pstmtHouse.executeUpdate();
				
				pstmtHouse.close();
                rsEstate.close();
                pstmtEstate.close();
			} else {
				// Falls schon eine ID vorhanden ist, mache ein Update...
				String updateSQLhouse = "UPDATE house SET floors = ?, price = ?, garden = ? WHERE estate_id = ?";
				String updateSQLestate = "UPDATE estate SET city = ?, postalcode = ?, street = ?, streetnumber = ?, squarearea = ?, login = ? WHERE estate_id = ?";

                PreparedStatement pstmtHouse = con.prepareStatement(updateSQLhouse);
                PreparedStatement pstmtEstate = con.prepareStatement(updateSQLestate);

				// Setze Anfrage Parameter
				pstmtHouse.setInt(1, getFloors());
				pstmtHouse.setInt(2, getPrice());
				pstmtHouse.setBoolean(3, getGarden());
				pstmtHouse.setInt(4, getHouseId());
				pstmtHouse.executeUpdate();

                pstmtEstate.setString(1, getCity());
                pstmtEstate.setInt(2, getPostalcode());
                pstmtEstate.setString(3, getStreet());
                pstmtEstate.setInt(4, getStreetnumber());
                pstmtEstate.setInt(5, getSquarearea());
                pstmtEstate.setString(6, getLogin());
                pstmtEstate.setInt(7, getHouseId());
                pstmtEstate.executeUpdate();

				pstmtHouse.close();
                pstmtEstate.close();;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void delete() {
		// Hole Verbindung
		Connection con = DbConnectionManager.getInstance().getConnection();

		try {
			String updateSQL = "DELETE FROM estate WHERE estate_id = ?";
			PreparedStatement pstmt = con.prepareStatement(updateSQL);

			// Setze Anfrage Parameter
			pstmt.setInt(1, getHouseId());
			pstmt.executeUpdate();

			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}		
	}
}
