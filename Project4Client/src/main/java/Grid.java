import java.util.ArrayList;

public class Grid {
    private ArrayList<ArrayList<GridBox>> grid;

    public Grid(){
        grid = new ArrayList<>();
        for(int row = 0; row < 10;row++) {
            ArrayList<GridBox> newRow = new ArrayList<>();
            for(int col = 0; col < 10;col++) {
                newRow.add(new GridBox(row, col));
            }
            grid.add(newRow);
        }
    }
    public String contains(int x, int y) {return grid.get(y).get(x).contains();}

    public void setShip(int x, int y) {grid.get(y).get(x).setShip();}

    public void setShot(int x, int y) {grid.get(x).get(y).setShot();}
}
