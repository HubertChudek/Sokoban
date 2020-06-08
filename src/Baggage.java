import java.awt.Image;
import java.util.Objects;
import javax.swing.ImageIcon;

public class Baggage extends Actor {

    public Baggage(int x, int y) {
        super(x, y);

        initBaggage();
    }

    private void initBaggage() {
        ImageIcon iicon = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("box.png")));
        setImage(iicon.getImage());
    }

    public void move(int x, int y) {
        int dx = getX() + x;
        int dy = getY() + y;

        setX(dx);
        setY(dy);
    }
}