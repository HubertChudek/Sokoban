import javax.swing.*;

public class Area extends Actor{


    public Area(int x, int y) {
        super(x, y);

        initArea();
    }

    private void initArea() {

        ImageIcon image = new ImageIcon("assets/area.png");
        setImage(image.getImage());
    }
}
