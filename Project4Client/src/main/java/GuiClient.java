// Ricky Massa
// 04-25-2024
// UIC Spring 2024
// GuiClient.java
// UI for the battleship game on the client side

import java.util.ArrayList;
import java.util.HashMap;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.scene.control.ComboBox;
import javafx.scene.text.Font;


public class GuiClient extends Application{

	TextField c1, c2;
	Text title, turn, opMove;
	Button b1, b2, b3, b4, b5, b6, b7, b8;
	HashMap<String, Scene> sceneMap;
	VBox clientBox, chatBox, optionBox;
	HBox hBox, hBox2;
	Client clientConnection;
	BorderPane borderPane;
	ComboBox<String> shipDropDown;
	ListView<String> chat;


	private Button[][] enemyGridButtons = new Button[10][10];;
	private Button[][] playerGridButtons = new Button[10][10];;
	GridPane player = new GridPane();
	GridPane enemy = new GridPane();
	BattleshipGame game;
	boolean myTurn;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		clientConnection = new Client(data->{
			Platform.runLater(()->{
				if(data instanceof Move) {
					Move nextMove = (Move) data;
					if(game.setShotPlayer(nextMove.getX(), nextMove.getY())) {
						opMove.setText(game.playerMoveChecker(nextMove));
					}
					else {
						opMove.setText("Opponent has missed");
					}
					updatePlayerGrid();
					myTurn = !myTurn;
					turn.setText("Your turn");
					winner();
				}
				else if (data instanceof String) {
					String word = data.toString();
					if(word.equals("begin") && game.isOnline()) {
						primaryStage.setScene(mainGame());
					}
				}
				else if (data instanceof Grid) {
					Grid enemyGrid = (Grid) data;
					if(game.isOnline()) {
						game.enemy.setBoard(enemyGrid);
						myTurn = true;
						turn.setText("Your turn");
					}
				}
				else if (data instanceof ArrayList) {
					ArrayList enemyShips = (ArrayList) data;
					if(game.isOnline()) {
						game.enemy.copyShips(enemyShips);
					}
				}
				else if (data instanceof Message) {
					Message mess = (Message) data;
					if(game.isOnline()) {
						chat.getItems().add("Enemy: " + mess.getMessage());
					}
				}

			});
		});

		clientConnection.start();
		chat = new ListView<>();
		chat.setPrefHeight(200);
		chat.setPrefWidth(300);
		b1 = new Button("Play AI");
		b2 = new Button("Play Person");
		b3 = new Button("Rules");
		b4 = new Button("Set Position");
		b5 = new Button("Begin Game");
		b6 = new Button("Back to Menu");
		b7 = new Button("Random");
		b8 = new Button("Send Message");
		turn = new Text("");
		opMove = new Text("");


		b1.setOnAction(e->{resetGame();primaryStage.setScene(mainGame()); game.generateEnemyLocation();});
		b2.setOnAction(e->{resetGame();game.setOnline(); clientConnection.send("queue");primaryStage.setScene(queueScreen());});

		b4.setOnAction(e->{
			String selectedShip = shipDropDown.getValue();
			String placement = c1.getText();
			if (selectedShip != null) {
				if(game.setShipLocation(selectedShip, placement)) {
					shipDropDown.getItems().remove(selectedShip);
					updatePlayerGrid();
					c1.clear();
					if(shipDropDown.getItems().isEmpty()) {
						hBox.getChildren().add(b5);
						b4.setDisable(true);
						c1.setDisable(true);
						shipDropDown.setDisable(true);
					}
				}
				else {
					c1.setPromptText("Error, try again");
				}
			} else {
				c1.setPromptText("Error, try again");
			}
		});
		b5.setOnAction(e->{
			optionBox.getChildren().clear();
			optionBox.getChildren().add(opMove);
			optionBox.getChildren().add(turn);
			optionBox.getChildren().add(title);
			title.setText("");
			turn.setText("Your turn");
			if(game.isOnline()) {
				turn.setText("Waiting for opponent");
				Grid sending = new Grid(game.getPlayerGrid());
				clientConnection.send(sending);
				ArrayList<Ship> ships = new ArrayList<>();
				for(Ship x: game.player.getShips()) {
					ships.add(new Ship(x));
				}
				clientConnection.send(ships);
				turn.setText("Opponent's turn");
				myTurn = false;
			}
			else {
				myTurn = true;
			}
		});

		b6.setOnAction(e->{clientConnection.send("dequeue");primaryStage.setScene(createStart()); chat.getItems().clear();});

		b7.setOnAction(e->{
			String selectedShip = shipDropDown.getValue();
			if (selectedShip != null) {
				game.playerRandom(selectedShip);
				shipDropDown.getItems().remove(selectedShip);
				updatePlayerGrid();
				c1.clear();
				if(shipDropDown.getItems().isEmpty()) {
					hBox.getChildren().add(b5);
					b4.setDisable(true);
					b7.setDisable(true);
					c1.setDisable(true);
					shipDropDown.setDisable(true);
				}
			}
		});

		b8.setOnAction(e->{
			Message message = new Message(c2.getText());
			chat.getItems().add("You: " + message.getMessage());
			if(game.isOnline()) {
				clientConnection.send(message);
			}
			else {
				message.getAI();
				chat.getItems().add("Enemy: " + message.getMessage());
			}
			c2.clear();
		});

		for (int row = 0; row < 10; row++) {
			for (int col = 0; col < 10; col++) {
				Button button1 = new Button(Character.toString((char)('A' + row))+(col+1)); // Grid 1
				Button button2 = new Button(Character.toString((char)('A' + row))+(col+1)); // Grid 2
				button1.setStyle("-fx-font-size:9;" + "-fx-text-fill: rgba(0, 0, 0, 0.5);");
				button2.setStyle("-fx-font-size:9;" + "-fx-text-fill: rgba(0, 0, 0, 0.5);");
				button1.setPrefSize(30, 30);
				button2.setPrefSize(30, 30);
				final int r = row;
				final int c = col;
				button1.setOnAction(event -> enemyButtonClick(r, c));
				enemyGridButtons[row][col] = button1;
				playerGridButtons[row][col] = button2;
				enemy.add(button1, col, row);
				player.add(button2, col, row);
			}
		}


		sceneMap = new HashMap<String, Scene>();

		sceneMap.put("start",  createStart());
		sceneMap.put("queue",  queueScreen());
		sceneMap.put("game", mainGame());

		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent t) {
				Platform.exit();
				System.exit(0);
			}
		});



		primaryStage.setScene(sceneMap.get("start"));
		primaryStage.setTitle("Battleship");
		primaryStage.show();

	}

	private void enemyButtonClick(int row, int col) {
		if(myTurn) {
			Button clickedButton = enemyGridButtons[row][col];
			Move clicked = new Move(row, col);
			clickedButton.setStyle("-fx-background-color: blue;-fx-border-color: black;-fx-font-size:9;");
			if (game.enemyCheckPoint(col, row).equals("ship")) {
				clickedButton.setStyle("-fx-background-color: red;-fx-border-color: black;-fx-font-size:9;");
				title.setText(game.enemyMoveChecker(clicked));
			}
			else {title.setText("You have missed");}
			clickedButton.setDisable(true);
			System.out.println("Button clicked at: " + row + ", " + col);
			clientConnection.send(new Move(row, col));
			turn.setText("Opponents turn");
			myTurn = false;
			winner();
		}

	}
	private void updatePlayerGrid() {
		// Iterate over the ship coordinates and update the corresponding buttons on the grid
		for (int row = 0; row < 10; row++) {
			for (int col = 0; col < 10; col++) {
				if (game.playerCheckPoint(col, row).equals("shot ship")) {
					playerGridButtons[row][col].setStyle("-fx-background-color: red;"+"-fx-font-size:9;");
					playerGridButtons[row][col].setDisable(true);
				}
				else if (game.playerCheckPoint(col, row).equals("ship")) {
					playerGridButtons[row][col].setStyle("-fx-background-color: gray;"+"-fx-font-size:9;");
					playerGridButtons[row][col].setDisable(true);
				}
				else if(game.playerCheckPoint(col, row).equals("shot")) {
					playerGridButtons[row][col].setStyle("-fx-background-color: blue;"+"-fx-font-size:9;");
					playerGridButtons[row][col].setDisable(true);
				}

			}
		}
	}
	private void winner() {
		if(!game.winCheck().equals("none")) {
			myTurn = false;
			title.setText("");
			opMove.setText("");
			turn.setText(game.winCheck());
			optionBox.getChildren().add(b6);
		}
	}
	public void resetButtons() {
		for (int row = 0; row < 10; row++) {
			for (int col = 0; col < 10; col++) {
				Button button1 = enemyGridButtons[row][col];
				Button button2 = playerGridButtons[row][col];
				button1.setStyle("-fx-font-size:9;" + "-fx-text-fill: rgba(0, 0, 0, 0.5);");
				button2.setStyle("-fx-font-size:9;" + "-fx-text-fill: rgba(0, 0, 0, 0.5);");
				button1.setDisable(false);
				button2.setDisable(false);
			}
		}
	}
	private void resetGame() {
		game = new BattleshipGame();
		resetButtons();
	}
	public Scene queueScreen() {
		title = new Text("Waiting for another player");
		clientBox = new VBox(20, title);
		clientBox.setAlignment(Pos.CENTER);
		borderPane = new BorderPane();
		borderPane.setPadding(new Insets(20));
		borderPane.setCenter(clientBox);
		clientBox.setStyle("-fx-background-radius: 50;" + "-fx-background-color: #F2EFE5;");
		borderPane.setStyle("-fx-background-color: #C7C8CC;");
		return new Scene(borderPane, 400, 300);
	}
	public Scene createStart() {
		title = new Text("Battleship");
		clientBox = new VBox(20, title, b1, b2, b3);
		clientBox.setAlignment(Pos.CENTER);
		borderPane = new BorderPane();
		borderPane.setPadding(new Insets(20));
		borderPane.setCenter(clientBox);
		clientBox.setStyle("-fx-background-radius: 50;" + "-fx-background-color: #F2EFE5;");
		borderPane.setStyle("-fx-background-color: #C7C8CC;");
		return new Scene(borderPane, 400, 300);
	}
	public Scene mainGame() {
		Text opBoard = new Text("Opponents Board");
		Text playerBoard = new Text("Your Board");
		opBoard.setFont(Font.font("Arial", FontWeight.BOLD, 20)); //
		playerBoard.setFont(Font.font("Arial", FontWeight.BOLD, 20));
		title = new Text("Place your ships");
		c1 = new TextField();
		c1.setPromptText("Enter ship location, by smaller position to bigger. Example; A1-A5");
		c2 = new TextField();
		c2.setPromptText("Message to opponent");
		b4.setDisable(false);
		b7.setDisable(false);
		shipDropDown = new ComboBox<>();
		shipDropDown.getItems().add("Carrier (length 5)");
		shipDropDown.getItems().add("Battleship (length 4)");
		shipDropDown.getItems().add("Cruiser (length 3)");
		shipDropDown.getItems().add("Submarine (length 3)");
		shipDropDown.getItems().add("Destroyer (length 2)");

		hBox = new HBox(10, shipDropDown, b4, b7);
		hBox.setAlignment(Pos.CENTER);

		optionBox = new VBox(10, title, c1, hBox);
		optionBox.setAlignment(Pos.CENTER);
		optionBox.setStyle("-fx-background-color: #E5E1DA;-fx-background-radius: 15; -fx-padding: 10px;");

		clientBox = new VBox(10, opBoard, enemy, optionBox, player, playerBoard);
		clientBox.setAlignment(Pos.CENTER);
		clientBox.setStyle("-fx-background-color: #FBF9F1;-fx-background-radius: 25; -fx-padding: 10px;");

		chatBox = new VBox(10, chat, c2, b8);
		chatBox.setAlignment(Pos.CENTER);
		chatBox.setStyle("-fx-background-color: #AAD7D9;-fx-background-radius: 25; -fx-padding: 10px;");

		borderPane = new BorderPane();
		borderPane.setPadding(new Insets(10));
		borderPane.setCenter(clientBox);
		borderPane.setRight(chatBox);
		borderPane.setStyle("-fx-background-color: #92C7CF;-fx-padding: 10px;");
		enemy.setAlignment(Pos.CENTER);
		player.setAlignment(Pos.CENTER);
		return  new Scene(borderPane, 800, 700);
	}

}