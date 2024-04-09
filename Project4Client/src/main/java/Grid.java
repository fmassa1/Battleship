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
}
