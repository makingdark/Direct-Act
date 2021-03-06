package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import DBUtil.GoogleCon;
import DBUtil.SQLCon;
import domein.Antwoord;
import domein.Vraag;

public class ToetsDAO {

	public Vraag getVraagByNr(int nr) {
		Connection conn = null;
		ResultSet rSet;
		Vraag v = null;

		try {
			conn = SQLCon.getConnection();
			PreparedStatement pStmt = conn
					.prepareStatement("SELECT * FROM vraag WHERE vraagNummer = ?");
			pStmt.setInt(1, nr);
			rSet = pStmt.executeQuery();
			while (rSet.next()) {
				v = new Vraag(rSet.getBoolean("heeftRekenmachine"),
						rSet.getInt("vraagNummer"), rSet.getString("context"),
						rSet.getString("afbeelding"),
						rSet.getString("categorie"), rSet.getString("opgave"),
						rSet.getString("antwoord1"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			GoogleCon.closeConnection(conn);
		}
		return v;
	}

	public void addAntwoord(Antwoord a) {
		Connection conn = null;
		int opgaveNummer = 1;
		try {
			conn = SQLCon.getConnection();
			PreparedStatement pStmt = conn
					.prepareStatement("select MAX(opgaveNummer) from antwoord");
			ResultSet rSet = pStmt.executeQuery();
			while (rSet.next()) {
				opgaveNummer = rSet.getInt("MAX(opgaveNummer)");
			}
			PreparedStatement pStmt2 = conn
					.prepareStatement("insert into antwoord values (?,?,?,?,?,?)");
			pStmt2.setInt(1, opgaveNummer + 1);
			pStmt2.setString(2, a.getAntwoord());
			pStmt2.setInt(3, a.getTijd());
			System.out.println("ToetsDao seconden: " + a.getTijd());
			pStmt2.setBoolean(4, a.getHeeftRekenmachineGebruikt());
			pStmt2.setInt(5, a.getToetsNummer());
			pStmt2.setInt(6, a.getVraagNummer());
			pStmt2.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			GoogleCon.closeConnection(conn);
		}

	}

	public int getHuidigToetsNummer() {
		Connection conn = null;
		int toetsNummer = 0;
		try {
			conn = SQLCon.getConnection();
			PreparedStatement pStmt = conn
					.prepareStatement("select MAX from toets");
			ResultSet rSet = pStmt.executeQuery();
			toetsNummer = rSet.getInt("toetsNummer");
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			GoogleCon.closeConnection(conn);
		}
		if (toetsNummer == 0)
			return 1;
		else
			return toetsNummer;
	}

	public int getVolgendToetsNummer(boolean newToets, int stNr) {
		Connection conn = null;
		int toetsNummer = 0;
		try {
			conn = SQLCon.getConnection();
			PreparedStatement pStmt = conn
					.prepareStatement("select MAX(toetsNummer) from toets");
			ResultSet rSet = pStmt.executeQuery();
			while (rSet.next()) {
				toetsNummer = rSet.getInt("MAX(toetsNummer)");
			}
			if (newToets) {
				PreparedStatement pStmt2 = conn
						.prepareStatement("insert into toets values(?,?)");
				pStmt2.setInt(1, toetsNummer + 1);
				pStmt2.setInt(2, stNr);
				pStmt2.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			GoogleCon.closeConnection(conn);
		}
		if (toetsNummer == 0)
			return 1;
		else
			return toetsNummer + 1;
	}
}
