import javafx.scene.layout.GridPane;
import jdk.tools.jlink.internal.Platform;

import java.io.Serializable;
import java.util.ArrayList;

public class BattleshipGame {
    BattleshipPlayer player;
    BattleshipPlayer enemy;

    boolean online = false;

    public BattleshipGame() {
        player = new BattleshipPlayer();
        enemy = new BattleshipPlayer();
    }

    public String winCheck() {
        if(player.checkHealth() == 0) {
            return "p2";
        }
        if(enemy.checkHealth() == 0) {
            return "p1";
        }
        return "none";
    }
    public String playerCheckPoint(int x, int y){return player.pointStatus(x,y);}

    public String enemyCheckShip(int x, int y){return enemy.pointStatus(x,y);}
    public void setOnline(){online = true;}

    public boolean isOnline() {return online;}

    public boolean setShipLocation(String ship, String location) {return player.setShipLocations(ship,location);}
    public void setShot(int x, int y) {player.setShot(x,y);}



}
