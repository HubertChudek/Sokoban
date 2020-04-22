import java.awt.*;

public class Actor {

    private final int SPACE = Board.SPACE;

    private int x;
    private int y;
    private Image image;

    public Actor(int x, int y) {

        this.x = x;
        this.y = y;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image img) {
        image = img;
    }

    public int getX() {

        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public void setX(int x) {

        this.x = x;
    }

    public void setY(int y) {

        this.y = y;
    }

    public boolean isLeftCollision(Actor actor) {

        return getX() - SPACE == actor.getX() && getY() == actor.getY();
    }

    public boolean isRightCollision(Actor actor) {

        return getX() + SPACE == actor.getX() && getY() == actor.getY();
    }

    public boolean isTopCollision(Actor actor) {

        return getY() - SPACE == actor.getY() && getX() == actor.getX();
    }

    public boolean isBottomCollision(Actor actor) {

        return getY() + SPACE == actor.getY() && getX() == actor.getX();
    }
}