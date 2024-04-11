import javafx.scene.layout.GridPane;

public class BattleshipGame {
    private Grid playerBoard;
    private Grid enemyBoard;

    private BattleshipLogic logic;

    public BattleshipGame() {
        playerBoard = new Grid();
        enemyBoard = new Grid();
        logic = new BattleshipLogic();
    }


    public Grid getEnemyBoard() {return enemyBoard;}

    public void setEnemyBoard(Grid enemyBoard) {this.enemyBoard = enemyBoard;}

    public Grid getPlayerBoard() {return playerBoard;}

    public void setPlayerBoard(Grid playerBoard) {this.playerBoard = playerBoard;}
}
