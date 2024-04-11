import javafx.scene.layout.GridPane;

import java.util.ArrayList;

public class BattleshipGame {
    BattleshipPlayer player;
    BattleshipPlayer enemy;

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
}
