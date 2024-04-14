import java.util.ArrayList;
import java.util.HashMap;

public class BattleshipPlayer {

    ArrayList<Ship> playerShips;
    HashMap<String, ArrayList<BattleshipGame.Move>> shipLocations;
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
    public boolean isShip(int x, int y) {return grid.isShip(x,y);}

    public boolean setShipLocations(String ship, String location) {
        int length = 0;
        if (ship.startsWith("Destroyer")) {
            length = 2;
        } else if (ship.startsWith("Cruiser") || ship.startsWith("Submarine")) {
            length = 3;
        } else if (ship.startsWith("Battleship")) {
            length = 4;
        } else if (ship.startsWith("Carrier")) {
            length = 5;
        }
        int x = location.indexOf('-');
        String firstSpot = location.substring(0, x);
        String lastSpot = location.substring(x + 1);
        if (!isValidLocation(firstSpot, lastSpot, length)) {
            return false;
        }
        int y1 = firstSpot.charAt(0) - 'A';
        int y2 = lastSpot.charAt(0) - 'A';
        int x1 = Integer.parseInt(firstSpot.substring(1)) - 1;
        int x2 = Integer.parseInt(lastSpot.substring(1)) - 1;
        System.out.println("("+x1+","+y1+")");
        System.out.println("("+x2+","+y2+")");
        if(y1 == y2) {
            if(x1 > x2) {int temp = x1; x1=x2; x2=temp;}
            for(int i = x1; i <= x2; i++) {
                if(grid.isShip(i,y1)){return false;}
            }
            for(int i = x1; i <= x2; i++) {grid.setShip(i, y1);}
        }
        else if(x1 == x2) {
            if(y1 > y2) {int temp = y1; y1=y2; y2=temp;}
            for (int i = y1; i <= y2; i++) {
                if (grid.isShip(x1, i)) {return false;}
            }
            for (int i = y1; i <= y2; i++) {grid.setShip(x1, i);}
        }
        return true;
    }


    private boolean isValidLocation(String firstSpot, String lastSpot, int length) {
        char startRow = firstSpot.charAt(0);
        char endRow = lastSpot.charAt(0);
        int startCol = Integer.parseInt(firstSpot.substring(1));
        int endCol = Integer.parseInt(lastSpot.substring(1));

        if (startRow < 'A' || startRow > 'J' || endRow < 'A' || endRow > 'J' ||
                startCol < 1 || startCol > 10 || endCol < 1 || endCol > 10) {return false;}
        if (startRow == endRow && Math.abs(startCol - endCol) == length - 1) {return true;}
        if (startCol == endCol && Math.abs(startRow - endRow) == length - 1) {return true;}
        return false;
    }
}
