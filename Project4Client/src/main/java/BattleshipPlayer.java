import java.util.ArrayList;

public class BattleshipPlayer {

    ArrayList<Ship> playerShips;
    Grid grid;

    BattleshipPlayer() {
        grid = new Grid();
        playerShips = new ArrayList<>();
        playerShips.add(new Ship("Carrier", 5));
        playerShips.add(new Ship("Battleship", 4));
        playerShips.add(new Ship("Cruiser", 3));
        playerShips.add(new Ship("Submarine", 3));
        playerShips.add(new Ship("Destroyer", 2));
    }
    public Grid getGrid() {return grid;}

    public void setBoard(Grid grid) {this.grid = grid;}

    public int checkHealth() {
        int health = 0;
        for(Ship ship : playerShips)  {
            health += ship.getHealth();
        }
        return health;
    }

}
