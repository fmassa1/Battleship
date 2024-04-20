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