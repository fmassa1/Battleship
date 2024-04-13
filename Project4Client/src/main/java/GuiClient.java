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
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.scene.control.ComboBox;

public class GuiClient extends Application{

	TextField c1;
	Text title;
	Button b1, b2, b3, b4;
	HashMap<String, Scene> sceneMap;
	VBox clientBox;
	HBox hBox;
	Client clientConnection;
	BorderPane borderPane;
	ComboBox<String> shipDropDown;

	ListView<String> listItems2;
	private Button[][] enemyGridButtons = new Button[10][10]; // Grid 1
	private Button[][] playerGridButtons = new Button[10][10]; // Grid 2
	GridPane player = new GridPane();
	GridPane enemy = new GridPane();
	BattleshipGame game = new BattleshipGame();


	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		clientConnection = new Client(data->{
			Platform.runLater(()->{listItems2.getItems().add(data.toString());
			});
		});

		clientConnection.start();

		b1 = new Button("Play AI");
		b2 = new Button("Play Person");
		b3 = new Button("Rules");
		b4 = new Button("Set Position");


		b1.setOnAction(e->{primaryStage.setScene(sceneMap.get("game"));});
		b2.setOnAction(e->{game.setOnline(); primaryStage.setScene(sceneMap.get("game"));});

		b4.setOnAction(e->{
			String selectedShip = shipDropDown.getValue();
			String placement = c1.getText();
			if (selectedShip != null) {
				if(game.setShipLocation(selectedShip, placement)) {
					shipDropDown.getItems().remove(selectedShip);
					updatePlayerGrid();
				}
				System.out.println("ship selected!");
			} else {
				// Handle case when no ship is selected
				System.out.println("No ship selected!");
			}
		});


		sceneMap = new HashMap<String, Scene>();

		sceneMap.put("start",  createStart());
		sceneMap.put("game", mainGame());

		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent t) {
				Platform.exit();
				System.exit(0);
			}
		});

		// Populate the grid with buttons
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

		primaryStage.setScene(sceneMap.get("start"));
		primaryStage.setTitle("Battleship");
		primaryStage.show();

	}

	private void enemyButtonClick(int row, int col) {
		Button clickedButton = enemyGridButtons[row][col];
		clickedButton.setStyle("-fx-background-color: blue;-fx-border-color: black;");
		if(game.playerCheckShip(row, col)) {
			clickedButton.setStyle("-fx-background-color: red;-fx-border-color: black;");
		}
		clickedButton.setDisable(true); // Disable the button so it cannot be clicked again
		System.out.println("Button clicked at: " + row + ", " + col);
		// Add your game logic here
	}
	private void updatePlayerGrid() {
		// Iterate over the ship coordinates and update the corresponding buttons on the grid
		for (int row = 0; row < 10; row++) {
			for (int col = 0; col < 10; col++) {
				if (game.playerCheckShip(row, col)) {
					// If the ship is placed at this position, update the button style
					playerGridButtons[row][col].setStyle("-fx-background-color: gray;");
					playerGridButtons[row][col].setDisable(true); // Disable the button
				}
			}
		}
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
		c1.setPromptText("Enter ship location. Example; A0-A4");
		shipDropDown = new ComboBox<>();
		shipDropDown.getItems().add("Carrier");
		shipDropDown.getItems().add("Battleship");
		shipDropDown.getItems().add("Cruiser");
		shipDropDown.getItems().add("Submarine");
		shipDropDown.getItems().add("Destroyer");
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