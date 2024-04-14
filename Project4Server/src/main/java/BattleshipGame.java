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
    public boolean playerCheckShip(int x, int y){return player.isShip(x,y);}
    public boolean enemyCheckShip(int x, int y){return enemy.isShip(x,y);}
    public void setOnline(){online = true;}

    public boolean isOnline() {return online;}

    public boolean setShipLocation(String ship, String location) {return player.setShipLocations(ship,location);}



}
