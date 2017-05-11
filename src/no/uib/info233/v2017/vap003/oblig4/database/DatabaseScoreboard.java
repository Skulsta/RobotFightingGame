package no.uib.info233.v2017.vap003.oblig4.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import no.uib.info233.v2017.vap003.oblig4.player.Player;

public class DatabaseScoreboard {

	public void updateDatabseRanking(Player p) {
		
		String playerName = p.getName();
		float playerScore = p.getScore();
		

		// Using "try" so the connection closes itself.
		try (
				// Allocating a database "Connection" object.
				Connection connect = DriverManager.getConnection("jdbc:mysql://wildboy.uib.no/Dina?useSSL=false",
						"Dina", "d+W<YaB.QZ>\"6,q5");

				// Allocating a database "Statement" object
				Statement statementSelect = connect.createStatement();
				Statement statementUpdate = connect.createStatement();
				
			) {

			// Execute a SQL SELECT query, the query result is
			// returned in a "ResultSet" object.
			String stringSelect = "select player, score from ranking";

			// Used to register new players if they are not already in the
			// database.

			ResultSet resultsetSelect = statementSelect.executeQuery(stringSelect);

			// Process the ResultSet by scrolling the cursor forward via next().
			// For each row, retrieve the contents of the cells with get 
			// methods.
			String updateScore = null;
			boolean playerFound = false;
			while (resultsetSelect.next()) { // Move the cursor to the next row,
												// return false if no more rows.
				
				
				String player = resultsetSelect.getString("player");
				// If the player is already registered in the database, update
				// his score by adding the score from this game.
				updateScore = null;
				if (player.equals(playerName)) {
					updateScore = "update ranking set score = score + " + playerScore + " where player = " + "'"
							+ playerName + "'";
					statementUpdate.executeUpdate(updateScore);
					playerFound = true;
				}	
			} // End while loop.
			
			// If the player was not found, add him to the database.
			if (!playerFound) {
				updateScore = "insert into ranking " + "values ('" + playerName + "', " + playerScore + ")";
				statementUpdate.executeUpdate(updateScore);
			}	
		}

		catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	// Displays the updated ranking table. Separated into it's own method so
	// it can be reused in future implementations, for example if we were to
	// implement a remove method and want to make sure the player has been removed.
	public void displayScoreboard() {

		try (
				// Allocating a database "Connection" object.
				Connection connect = DriverManager.getConnection("jdbc:mysql://wildboy.uib.no/Dina?useSSL=false",
						"Dina", "d+W<YaB.QZ>\"6,q5");

				Statement statementAfter = connect.createStatement();

		) {
			String stringSelect = "select player, score from ranking";
			ResultSet resultsetAfter = statementAfter.executeQuery(stringSelect);
			while (resultsetAfter.next()) {
				String updatedPlayer = resultsetAfter.getString("player");
				float updatedScore = resultsetAfter.getFloat("score");
				System.out.println(updatedPlayer + " - score: " + updatedScore);
			}
		}

		catch (SQLException ex) {
			ex.printStackTrace();
		}
	}
}
