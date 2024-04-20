import java.io.Serializable;

public class Move implements Serializable {
    private static final long serialVersionUID = 42L;

    private int x;
    private int y;
    private boolean ai = true;

    public Move(int x, int y){
        this.x = x;
        this.y = y;
    }
    public Move(Move t){
        this.x = t.getX();
        this.y = t.getY();
    }
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Move move = (Move) obj;
        return x == move.x && y == move.y;
    }
    public void printMove() {
        System.out.println("("+x+","+y+")");
    }

    public int getX() {return x;}

    public int getY() {return y;}

    public void setOnline() {ai=false;}
    public boolean isAi() {return ai;}
}
