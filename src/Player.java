import javax.swing.*;

public class Player extends Actor {

    public Player(int x, int y) {
        super(x, y);

        initPlayer();
    }

    private void initPlayer() {

        ImageIcon iicon = new ImageIcon("assets/soko.png");
        setImage(iicon.getImage());
    }

    public void move(int x, int y) {

        setX(getX() + x);
        setY(getY() + y);
    }
}
