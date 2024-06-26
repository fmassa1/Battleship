// Ricky Massa
// 04-25-2024
// UIC Spring 2024
// GridBox.java
// Used to keep track of a component on a grid

import java.io.Serializable;

public class GridBox implements Serializable {
    private static final long serialVersionUID = 42L;

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
    public boolean setShot() {
        shot = true;
        return ship;
    }

}
