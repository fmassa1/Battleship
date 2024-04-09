
import java.util.HashMap;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class GuiClient extends Application{

	
	TextField c1;
	Button b1;
	HashMap<String, Scene> sceneMap;
	VBox clientBox;
	Client clientConnection;
	
	ListView<String> listItems2;
	private Button[][] gridButtons = new Button[10][10];
	GridPane gridPane = new GridPane();
	
	
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

		listItems2 = new ListView<String>();
		
		c1 = new TextField();
		b1 = new Button("Send");
		b1.setOnAction(e->{clientConnection.send(c1.getText()); c1.clear();});
		
		sceneMap = new HashMap<String, Scene>();

		sceneMap.put("client",  createClientGui());
		
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
				Button button = new Button();
				button.setMinSize(40, 40); // Set button size
				final int r = row;
				final int c = col;
				button.setOnAction(event -> handleButtonClick(r, c)); // Set button click handler
				gridButtons[row][col] = button;
				gridPane.add(button, col, row);
			}
		}


		primaryStage.setScene(sceneMap.get("client"));
		primaryStage.setTitle("Client");
		primaryStage.show();
		
	}
	private void handleButtonClick(int row, int col) {
		Button clickedButton = gridButtons[row][col];
		clickedButton.setStyle("-fx-background-color: red; -fx-text-fill: white; -fx-border-color: black;");
		clickedButton.setDisable(true); // Disable the button so it cannot be clicked again
		System.out.println("Button clicked at: " + row + ", " + col);
		// Add your game logic here
	}

	
	public Scene createClientGui() {
		
		clientBox = new VBox(10, gridPane);
		clientBox.setStyle("-fx-font-family: 'serif';");
		return new Scene(clientBox, 400, 300);
		
	}

}
