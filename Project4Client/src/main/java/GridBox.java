public class GridBox {
    private int width;
    private int height;
    private int x;
    private int y;

    private boolean ship = false;

    public GridBox(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public String getPosition() {return x + ", " + y;}
    public void setShip() {ship = true;}
    public boolean hasShip() {return ship;}

}
