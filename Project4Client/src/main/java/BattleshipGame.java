import javafx.scene.layout.GridPane;

import java.util.ArrayList;

public class BattleshipGame {
    private Grid playerBoard;
    private Grid enemyBoard;

    private BattleshipLogic logic;

    private ArrayList<Ship> playerShips;
    private ArrayList<Ship> enemyShips;


    public BattleshipGame() {
        playerBoard = new Grid();
        enemyBoard = new Grid();
        logic = new BattleshipLogic();
        playerShips = generateShips();
        enemyShips = generateShips();
    }

    private ArrayList<Ship> generateShips() {
        ArrayList<Ship> ships = new ArrayList<>();
        ships.add(new Ship("Carrier", 5));
        ships.add(new Ship("Battleship", 4));
        ships.add(new Ship("Cruiser", 3));
        ships.add(new Ship("Submarine", 3));
        ships.add(new Ship("Destroyer", 2));
        return ships;
    }
    public Grid getEnemyBoard() {return enemyBoard;}

    public void setEnemyBoard(Grid enemyBoard) {this.enemyBoard = enemyBoard;}

    public Grid getPlayerBoard() {return playerBoard;}

    public void setPlayerBoard(Grid playerBoard) {this.playerBoard = playerBoard;}
}
