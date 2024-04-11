import java.util.ArrayList;

public class Ship {
    String type;
    int health;

    Ship(String type, int health) {
        this.type = type;
        this.health = health;
    }

    public void shot() {health--;}
    public boolean destroyed() {return health == 0;}

    public String getType() {return type;}

    public int getHealth() {return health;}

}
