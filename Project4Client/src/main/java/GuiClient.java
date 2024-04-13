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

public class GuiClient extends Application{

	TextField c1;
	Text title;
	Button b1, b2, b3;
	HashMap<String, Scene> sceneMap;
	VBox clientBox;
	Client clientConnection;
	BorderPane borderPane;

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

		b1.setOnAction(e->{primaryStage.setScene(sceneMap.get("game"));});
		b2.setOnAction(e->{game.setOnline(); primaryStage.setScene(sceneMap.get("game"));});

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
				Button button1 = new Button(); // Grid 1
				Button button2 = new Button(); // Grid 2
				button1.setMinSize(30, 30); // Set button size
				button2.setMinSize(30, 30); // Set button size
				final int r = row;
				final int c = col;
				button1.setOnAction(event -> enemyButtonClick2(r, c)); // Set button click handler for Grid 1
				button2.setOnAction(event -> playerButtonClick(r, c)); // Set button click handler for Grid 2
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
	private void playerButtonClick(int row, int col) {
		// No action needed as buttons in grid 1 should not be clickable
	}

	private void enemyButtonClick2(int row, int col) {
		Button clickedButton = enemyGridButtons[row][col];
		clickedButton.setStyle("-fx-background-color: blue;-fx-border-color: black;");
		if(game.playerCheckShip(row, col)) {
			clickedButton.setStyle("-fx-background-color: red;-fx-border-color: black;");
		}
		clickedButton.setDisable(true); // Disable the button so it cannot be clicked again
		System.out.println("Button clicked at: " + row + ", " + col);
		// Add your game logic here
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
		borderPane = new BorderPane();
		borderPane.setTop(enemy);
		borderPane.setBottom(player);
		enemy.setAlignment(Pos.CENTER);
		player.setAlignment(Pos.CENTER);
		return  new Scene(borderPane, 600, 700);
	}

}