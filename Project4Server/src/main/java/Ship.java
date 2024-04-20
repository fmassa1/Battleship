import java.io.Serializable;
import java.util.ArrayList;

public class Ship implements Serializable {
    private static final long serialVersionUID = 42L;
    String type;
    int health;
    ArrayList<Move> location;

    Ship(String type, int health) {
        this.type = type;
        this.health = health;
        location = new ArrayList<>();
    }

    public void shot() {health--;}
    public boolean destroyed() {return health == 0;}

    public String getType() {return type;}

    public int getHealth() {return health;}

    public ArrayList<Move> getLocation() {return location;}

    public void setLocation(Move location) {this.location.add(location);}
}
