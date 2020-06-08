import javax.swing.ImageIcon;
import java.util.Objects;

public class Wall extends Actor {


    public Wall(int x, int y) {
        super(x, y);

        initWall();
    }

    private void initWall() {
        ImageIcon iicon = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("brick.png")));
        setImage(iicon.getImage());
    }
}