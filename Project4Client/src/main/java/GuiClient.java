import java.util.ArrayList;
import java.util.HashMap;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
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
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.scene.control.ComboBox;
import javafx.util.Duration;

public class GuiClient extends Application{

	TextField c1;
	Text title, turn;
	Button b1, b2, b3, b4, b5, b6;
	HashMap<String, Scene> sceneMap;
	VBox clientBox;
	HBox hBox;
	Client clientConnection;
	BorderPane borderPane;
	ComboBox<String> shipDropDown;

	Timeline delay;

	private Button[][] enemyGridButtons;
	private Button[][] playerGridButtons;
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
						title.setText(game.playerMoveChecker(nextMove));
					}
					else {
						title.setText("Opponent has missed");
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

			});
		});

		clientConnection.start();

		b1 = new Button("Play AI");
		b2 = new Button("Play Person");
		b3 = new Button("Rules");
		b4 = new Button("Set Position");
		b5 = new Button("Begin Game");
		b6 = new Button("Back to Menu");
		turn = new Text("");

//		delay = new Timeline(new KeyFrame(Duration.seconds(2), actionEvent -> {
//
//		}));


		b1.setOnAction(e->{resetGame();primaryStage.setScene(sceneMap.get("game"));});
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
			clientBox.getChildren().clear();
			clientBox.getChildren().add(turn);
			clientBox.getChildren().add(title);
			title.setText("");
			turn.setText("Your move");
			if(game.isOnline()) {
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

		b6.setOnAction(e->{clientConnection.send("queue");primaryStage.setScene(createStart());});




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
			clickedButton.setStyle("-fx-background-color: blue;-fx-border-color: black;");
			if (game.enemyCheckPoint(col, row).equals("ship")) {
				clickedButton.setStyle("-fx-background-color: red;-fx-border-color: black;");
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
					playerGridButtons[row][col].setStyle("-fx-background-color: red;");
					playerGridButtons[row][col].setDisable(true);
					playerGridButtons[row][col].setText("");
				}
				else if (game.playerCheckPoint(col, row).equals("ship")) {
					playerGridButtons[row][col].setStyle("-fx-background-color: gray;");
					playerGridButtons[row][col].setDisable(true);
					playerGridButtons[row][col].setText("");
				}
				else if(game.playerCheckPoint(col, row).equals("shot")) {
					playerGridButtons[row][col].setStyle("-fx-background-color: blue;");
					playerGridButtons[row][col].setDisable(true);
					playerGridButtons[row][col].setText("");
				}

			}
		}
	}
	private void winner() {
		if(!game.winCheck().equals("none")) {
			myTurn = false;
			turn.setVisible(false);
			title.setText(game.winCheck());
			clientBox.getChildren().add(b6);
		}
	}
	private void generateButtons() {
		enemyGridButtons = new Button[10][10];
		playerGridButtons = new Button[10][10];
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
	}
	private void resetGame() {
		game = new BattleshipGame();
		generateButtons();
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

		title = new Text("Place your ships");
		c1 = new TextField();
		c1.setPromptText("Enter ship location, by smaller position to bigger. Example; A1-A5");
		shipDropDown = new ComboBox<>();
		shipDropDown.getItems().add("Carrier (length 5)");
		shipDropDown.getItems().add("Battleship (length 4)");
		shipDropDown.getItems().add("Cruiser (length 3)");
		shipDropDown.getItems().add("Submarine (length 3)");
		shipDropDown.getItems().add("Destroyer (length 2)");
		hBox = new HBox(shipDropDown, b4);
		hBox.setAlignment(Pos.CENTER);
		clientBox = new VBox(10, title, c1, hBox);
		clientBox.setAlignment(Pos.CENTER);

		borderPane = new BorderPane();
		borderPane.setPadding(new Insets(10));
		borderPane.setTop(enemy);
		borderPane.setCenter(clientBox);
		borderPane.setBottom(player);
		enemy.setAlignment(Pos.CENTER);

		player.setAlignment(Pos.CENTER);
		return  new Scene(borderPane, 600, 700);
	}

}