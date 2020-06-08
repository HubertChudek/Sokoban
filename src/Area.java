import javax.swing.*;
import java.util.Objects;

public class Area extends Actor{


    public Area(int x, int y) {
        super(x, y);

        initArea();
    }

    private void initArea() {
        ImageIcon iicon = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("area.png")));
        setImage(iicon.getImage());
    }
}
