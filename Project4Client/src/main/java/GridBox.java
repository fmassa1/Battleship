import java.util.ArrayList;

public class GridBox {
    private int width;
    private int height;
    private int x;
    private int y;

    private boolean ship = false;
    private boolean shot = false;
    private boolean empty = true;

    public GridBox(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public int getX(){return x;}
    public int getY(){return y;}
    public void setShip() {ship = true; empty = false;}
    public boolean hasShip() {return ship;}
    public void setShot() {shot = true; empty = false;}
    public boolean hasShot() {return shot;}

    public boolean isEmpty() {return empty;}
}
