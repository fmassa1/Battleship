// Ricky Massa
// 04-25-2024
// UIC Spring 2024
// Ship.java
// Holds the information of a ship

import java.io.Serializable;
import java.util.ArrayList;

public class Ship implements Serializable {
    private static final long serialVersionUID = 42L;
    private String type;
    private int health;
    private ArrayList<Move> location;

    Ship(String type, int health) {
        this.type = type;
        this.health = health;
        location = new ArrayList<>();
    }
    public Ship(Ship otherShip) {
        this.type = otherShip.type;
        this.health = otherShip.health;
        this.location = new ArrayList<>();
        for(Move x: otherShip.getLocation()) {
            this.location.add(new Move(x));
        }
    }

    public void shot() {health--;}
    public boolean destroyed() {return health == 0;}

    public String getType() {return type;}

    public int getHealth() {return health;}

    public ArrayList<Move> getLocation() {return location;}

    public void setLocation(Move location) {this.location.add(location);}
}
