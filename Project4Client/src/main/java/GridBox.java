import java.util.ArrayList;

public class GridBox {
    private int x;
    private int y;

    private boolean ship = false;
    private boolean shot = false;

    public GridBox(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public int getX(){return x;}
    public int getY(){return y;}
    public void setShip() {ship = true;}
    public String contains() {
        if(ship && shot) {return "shot ship";}
        if(ship) {return "ship";}
        if(shot) {return "shot";}
        return "none";
    }
    public void setShot() {shot = true;}

}
