// Ricky Massa
// 04-25-2024
// UIC Spring 2024
// BattleshipPlayer.java
// Contains the information for a player

import java.util.ArrayList;
import java.util.Random;

public class BattleshipPlayer{
    private ArrayList<Ship> playerShips;
    private Grid grid;

    BattleshipPlayer() {
        grid = new Grid();
        playerShips = new ArrayList<>();
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
    public String pointStatus(int x, int y) {return grid.contains(x,y);}

    public boolean setShipLocations(String ship, String location) {
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
        Ship curShip = new Ship(name, length);
        if(y1 == y2) {
            if(x1 > x2) {int temp = x1; x1=x2; x2=temp;}
            for(int i = x1; i <= x2; i++) {
                if(grid.contains(i,y1).equals("ship")){return false;}
            }
            for(int i = x1; i <= x2; i++) {grid.setShip(i, y1); curShip.setLocation(new Move(y1,i));}
        }
        else if(x1 == x2) {
            if(y1 > y2) {int temp = y1; y1=y2; y2=temp;}
            for (int i = y1; i <= y2; i++) {
                if (grid.contains(x1, i).equals("ship")) {return false;}
            }
            for (int i = y1; i <= y2; i++) {grid.setShip(x1, i); curShip.setLocation(new Move(i,x1));}
        }
        playerShips.add(curShip);
        return true;
    }

    public boolean setShot(int x, int y) {
        return grid.setShot(x, y);
    }
    public void copyShips(ArrayList<Ship> shipsToCopy) {
        playerShips = new ArrayList<>();
        for(Ship x : shipsToCopy) {
            playerShips.add(new Ship(x));
        }
    }
    public ArrayList<Ship> getShips() {return playerShips;}

    public String checkShot(Move target) {
        for(Ship curShip : getShips()) {
            for(Move x: curShip.getLocation()) {
                if(target.equals(x)) {
                    curShip.shot();
                    if (curShip.destroyed()) {
                        return curShip.getType() + " has been sunk!";
                    }
                    return " ship has been hit";
                }
            }
        }
        return " shot missed lol";
    }
    public void randomShipLocation(String type, int hp) {
        Random rand = new Random();
        boolean placed = false;
        while (!placed) {
            int x1 = rand.nextInt(10);
            int y1 = rand.nextInt(10);
            boolean isHorizontal = rand.nextBoolean();
            int x2 = isHorizontal ? x1 + hp - 1 : x1;
            int y2 = isHorizontal ? y1 : y1 + hp - 1;

            String firstSpot = (char)('A' + y1) + Integer.toString(x1 + 1);
            String lastSpot = (char)('A' + y2) + Integer.toString(x2 + 1);
            if (setShipLocations(type, firstSpot + "-" + lastSpot)) {
                placed = true;
            }
        }
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
