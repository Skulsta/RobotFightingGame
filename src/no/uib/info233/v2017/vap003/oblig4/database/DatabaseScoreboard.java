package no.uib.info233.v2017.vap003.oblig4.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import GUI.ConsoleGUI;
import no.uib.info233.v2017.vap003.oblig4.game.GameMaster;
import no.uib.info233.v2017.vap003.oblig4.player.Player;

public class DatabaseScoreboard {

	private volatile Thread blinker;

	private GameMaster gameMaster;
	private Player player1;
	private Player player2;
	private String enteredPlayer1id;
	private boolean playerFound = false;

	// Temp
	private int i;

	public DatabaseScoreboard(GameMaster gameMaster) {
		this.gameMaster = gameMaster;
	}

	public void updateDatabseRanking(String playerName, float playerScore) {


		// Using "try" so the connection closes itself.
		try (
				// Allocating a database "Connection" object.
				Connection connect = DriverManager.getConnection("jdbc:mysql://wildboy.uib.no/oblig4?useSSL=false",
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
			while (resultsetSelect.next() && !playerFound) { // Move the cursor to the next row,
				// return false if no more rows.

				String player = resultsetSelect.getString("player");
				// If the player is already registered in the database, update
				// his score by adding the score from this game.
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
				Connection connect = DriverManager.getConnection("jdbc:mysql://wildboy.uib.no/oblig4?useSSL=false",
						"Dina", "d+W<YaB.QZ>\"6,q5");

				Statement statementAfter = connect.createStatement();

				) {
			int highScore = 0;
			String stringSelect = "select player, score from ranking order by score desc";
			ResultSet resultsetAfter = statementAfter.executeQuery(stringSelect);
			while (resultsetAfter.next()) {
				if (highScore < 5) {
					String updatedPlayer = resultsetAfter.getString("player");
					float updatedScore = resultsetAfter.getFloat("score");
					ConsoleGUI.sendToConsole(updatedPlayer + " - score: " + updatedScore);
					highScore++;
				}
			}
		}

		catch (SQLException ex) {
			ex.printStackTrace();
		}
	}


	public void saveGame (GameMaster gameMaster) {

		player1 = gameMaster.getPlayer1();
		player2 = gameMaster.getPlayer2();


		try (
				// Allocating a database "Connection" object.
				Connection connect = DriverManager.getConnection("jdbc:mysql://wildboy.uib.no/oblig4?useSSL=false",
						"Dina", "d+W<YaB.QZ>\"6,q5");

				Statement saveStatement = connect.createStatement();

				) {

			String stringSelect = "insert into saved_games values " + "('" + gameMaster.getGameid() + "', " + "'" +
					player1.getName() + "', '" + player2.getName() + "', " + gameMaster.getPosition() + ", " +
					player1.getEnergy() + ", " + player2.getEnergy() + ")";
			saveStatement.executeUpdate(stringSelect);

			ConsoleGUI.sendToConsole("\nThe game has been saved with gameid: " + gameMaster.getGameid());
		}

		catch (SQLException ex) {
			ex.printStackTrace();
		}
	}


	public void loadGame(String gameid) {
		try (
				// Allocating a database "Connection" object.
				Connection connect = DriverManager.getConnection("jdbc:mysql://wildboy.uib.no/oblig4?useSSL=false",
						"Dina", "d+W<YaB.QZ>\"6,q5");

				Statement listStatement = connect.createStatement();

				) {
			boolean gameFound = false;
			String stringSelect = "select * from saved_games";
			ResultSet resultsetAfter = listStatement.executeQuery(stringSelect);
			while (resultsetAfter.next()) {
				String loadedGameid = resultsetAfter.getString("game_id");
				String player1 = resultsetAfter.getString("player_1");
				String player2 = resultsetAfter.getString("player_2");
				int position = resultsetAfter.getInt("game_position");
				int playerOneEnergy = resultsetAfter.getInt("player_1_energy");
				int playerTwoEnergy = resultsetAfter.getInt("player_2_energy");
				if (gameid.equals(loadedGameid)) {
					gameFound = true;
					gameMaster.getLoadedGame(loadedGameid, player1, player2, position, playerOneEnergy, playerTwoEnergy);
				}
			}
			if (!gameFound)
				ConsoleGUI.sendToConsole("The game id was not found. Please try again.");
		}

		catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	// Shows all saved games.
	public void listSavedGames() {
		try (
				// Allocating a database "Connection" object.
				Connection connect = DriverManager.getConnection("jdbc:mysql://wildboy.uib.no/oblig4?useSSL=false",
						"Dina", "d+W<YaB.QZ>\"6,q5");

				Statement listStatement = connect.createStatement();

				) {
			String stringSelect = "select game_id, player_1, player_2 from saved_games";
			ResultSet resultsetAfter = listStatement.executeQuery(stringSelect);
			while (resultsetAfter.next()) {
				String gameid = resultsetAfter.getString("game_id");
				String player1 = resultsetAfter.getString("player_1");
				String player2 = resultsetAfter.getString("player_2");
				ConsoleGUI.sendToConsole(player1 + " vs " + player2 + " - Game ID: " + gameid);
			}
		}

		catch (SQLException ex) {
			ex.printStackTrace();
		}
	}


	public void listOpenGames() {
		try (
				// Allocating a database "Connection" object.
				Connection connect = DriverManager.getConnection("jdbc:mysql://wildboy.uib.no/oblig4?useSSL=false",
						"Dina", "d+W<YaB.QZ>\"6,q5");

				Statement listStatement = connect.createStatement();

				) {
			String stringSelect = "select * from open_games";
			ResultSet resultsetAfter = listStatement.executeQuery(stringSelect);
			while (resultsetAfter.next()) {
				String player1 = resultsetAfter.getString("player_1");
				String player1id = resultsetAfter.getString("player_1_random");
				ConsoleGUI.sendToConsole(player1 + " - player id: " + player1id);
			}
		}

		catch (SQLException ex) {
			ex.printStackTrace();
		}
	}


	public void loadOpenGame(String playerId, String addPlayerTwo) {

		enteredPlayer1id = playerId;

		try (
				// Allocating a database "Connection" object.
				Connection connect = DriverManager.getConnection("jdbc:mysql://wildboy.uib.no/oblig4?useSSL=false",
						"Dina", "d+W<YaB.QZ>\"6,q5");

				Statement listStatement = connect.createStatement();
				Statement addPlayer2Statement = connect.createStatement();

				) {
			String stringSelect = "select * from open_games";
			ResultSet resultsetAfter = listStatement.executeQuery(stringSelect);
			while (resultsetAfter.next()) {
				String player1id = resultsetAfter.getString("player_1_random");
				String player2 = resultsetAfter.getString("player_2");
				if (player1id.equals(playerId)) {
					if (player2 == null) {
						ConsoleGUI.sendToConsole("\nThe game was available.");
						addPlayer2Statement.executeUpdate(addPlayerTwo);
					}
					else
						ConsoleGUI.sendToConsole("---------------------\n" +
								"Game no longer avaiable. Try another game." + 
								"\n--------------------------");
				}
			}
		}

		catch (SQLException ex) {
			ex.printStackTrace();
		}
	}


	public void startOnlineGame () {

		ConsoleGUI.sendToConsole("Waiting for game to start.\n");

		// Create a delay to give the database some time, and look for the game multiple times if not found.

		player2 = gameMaster.getPlayer2();

		Thread waitingForGame = new Thread(new Runnable() {
			public void run() {
				while (!playerFound)
					try {
						Thread.sleep((long) 5000);


				try (
						// Allocating a database "Connection" object.
						Connection connect = DriverManager.getConnection("jdbc:mysql://wildboy.uib.no/oblig4?useSSL=false",
								"Dina", "d+W<YaB.QZ>\"6,q5");

						Statement joinStatement = connect.createStatement();

						) {

					String gameInProgress = "select * from game_in_progress";
					ResultSet result = joinStatement.executeQuery(gameInProgress);
					// ConsoleGUI.sendToConsole("Your game id is: " + enteredPlayer1id + player2.getPlayerRandom());
					ConsoleGUI.sendToConsole("...");
					while (result.next()) {
						String gameid = result.getString("game_id");
						if (gameid.equals(enteredPlayer1id + player2.getPlayerRandom())) {
							ConsoleGUI.sendToConsole("\nFound the game!");
							playerFound = true;
							gameMaster.getIntoOnlineGame(result);
						}
					}

				}

				catch (SQLException ex) {
					ex.printStackTrace();
				}
				
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} finally {
						ConsoleGUI.sendToConsole("");
					}

			}
		});

		// lookForGameInProgress.start();

		// Create a scheduled pool with 10 threads. 5 second delay between every execution.
		ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(10);
		executor.execute(waitingForGame);
	}


	public void createOpenGame (String createOpenGame) {

		try (
				// Allocating a database "Connection" object.
				Connection connect = DriverManager.getConnection("jdbc:mysql://wildboy.uib.no/oblig4?useSSL=false",
						"Dina", "d+W<YaB.QZ>\"6,q5");

				Statement listStatement = connect.createStatement();

				) {
			listStatement.executeUpdate(createOpenGame);
			ConsoleGUI.sendToConsole("Online game created.");

		}

		catch (SQLException ex) {
			ex.printStackTrace();
		}

		createGameInProgress();
	}


	public void createGameInProgress () {
		
		ConsoleGUI.sendToConsole("Waiting for player 2");

		player1 = gameMaster.getPlayer1();

		Thread lookForPlayer2 = new Thread(new Runnable() {
			public void run() {
				while (!playerFound)
					try {
						Thread.sleep((long) 5000);

					try (
							// Allocating a database "Connection" object.
							Connection connect = DriverManager.getConnection("jdbc:mysql://wildboy.uib.no/oblig4?useSSL=false",
									"Dina", "d+W<YaB.QZ>\"6,q5");

							Statement lookStatement = connect.createStatement();
							Statement insertStatement = connect.createStatement();

							) {

						String stringSelect = "select * from open_games where player_1_random = '" + player1.getPlayerRandom() +
								"'";
						ResultSet resultsetAfter = lookStatement.executeQuery(stringSelect);
						while (resultsetAfter.next()) {
							String playerTwoName = resultsetAfter.getString("player_2");
							if (playerTwoName != null) {
								ConsoleGUI.sendToConsole("\nA player joined!");
								player2 = new Player (playerTwoName);
								gameMaster.setPlayers(player1, player2);
								playerFound = true;
								insertStatement.executeUpdate(gameMaster.startOnlineGame());
								ConsoleGUI.sendToConsole("Will it work?!");
							}
						}
						if (!playerFound)
							ConsoleGUI.sendToConsole("...");
					}
					

					catch (SQLException ex) {
						ex.printStackTrace();
					}
					
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					ConsoleGUI.sendToConsole("");
				}
			}
		});

		ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(10);
		// executor.scheduleAtFixedRate(lookForPlayer2, 5, 5, TimeUnit.SECONDS);
		
		executor.execute(lookForPlayer2);

		
	}
}
