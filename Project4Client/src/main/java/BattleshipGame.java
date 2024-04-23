import javafx.scene.layout.GridPane;
import jdk.tools.jlink.internal.Platform;

import java.io.Serializable;
import java.util.ArrayList;

public class BattleshipGame {
    BattleshipPlayer player;
    BattleshipPlayer enemy;

    private boolean online = false;

    public BattleshipGame() {
        player = new BattleshipPlayer();
        enemy = new BattleshipPlayer();
    }

    public String winCheck() {
        if(player.checkHealth() == 0) {
            return "You have lost!";
        }
        if(enemy.checkHealth() == 0) {
            return "Congratulations you have won! ";
        }
        return "none";
    }
    public void generateEnemyLocation() {
        String[] shipTypes = {"Destroyer", "Cruiser", "Submarine", "Battleship", "Carrier"};
        int[] shipLengths = {2, 3, 3, 4, 5};

        for (int i = 0; i < shipTypes.length; i++) {
            enemy.randomShipLocation(shipTypes[i], shipLengths[i]);
        }
    }
    public void playerRandom(String ship) {
        int length = 0;
        String name = "";
        if (ship.startsWith("Destroyer")) {
            length = 2;
            name = "Destroyer";
        } else if (ship.startsWith("Cruiser")) {
            length = 3;
            name = "Cruiser";
        }
        else if (ship.startsWith("Submarine")) {
            length = 3;
            name = "Submarine";
        }
        else if (ship.startsWith("Battleship")) {
            length = 4;
            name = "Battleship";
        } else if (ship.startsWith("Carrier")) {
            length = 5;
            name = "Carrier";
        }
        player.randomShipLocation(name, length);
    }

    public String playerMoveChecker(Move x) {

        return "Your " + player.checkShot(x);
    }
    public String enemyMoveChecker(Move x) {
        return "Opponent's " + enemy.checkShot(x);
    }

    public String playerCheckPoint(int x, int y){return player.pointStatus(x,y);}
    public Grid getPlayerGrid() {return player.getGrid();}

    public String enemyCheckPoint(int x, int y){return enemy.pointStatus(x,y);}
    public void setOnline(){online = true;}

    public boolean isOnline() {return online;}

    public boolean setShipLocation(String ship, String location) {return player.setShipLocations(ship,location);}
    public boolean setShotPlayer(int x, int y) {return player.setShot(x,y);}
    public void setShotEnemy(int x, int y) {enemy.setShot(x,y);}

}