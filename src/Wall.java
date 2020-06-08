import javax.swing.ImageIcon;

public class Wall extends Actor {


    public Wall(int x, int y) {
        super(x, y);

        initWall();
    }

    private void initWall() {

        ImageIcon iicon = new ImageIcon("assets/brick.png");
        setImage(iicon.getImage());
    }
}