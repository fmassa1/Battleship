import java.util.ArrayList;

public class Grid {
    private ArrayList<ArrayList<GridBox>> grid;

    public Grid(){
        for(int row = 0; row < 10;row++) {
            ArrayList<GridBox> newRow = new ArrayList<>();
            for(int col = 0; col < 10;col++) {
                newRow.add(new GridBox(row, col));
            }
            grid.add(newRow);
        }
    }
    public boolean isShip(int x, int y) {return grid.get(x).get(y).hasShip();}

    public void setShip(int x, int y) {grid.get(x).get(y).setShip();}

    public boolean isShot(int x, int y) {return grid.get(x).get(y).hasShot();}
    public void setShot(int x, int y) {grid.get(x).get(y).setShot();}
}
