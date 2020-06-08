import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class Collision {
    private final int LEFT_COLLISION = 1;           //ustalenie typów kolizji
    private final int RIGHT_COLLISION = 2;
    private final int TOP_COLLISION = 3;
    private final int BOTTOM_COLLISION = 4;

    private ArrayList<Wall> walls;
    private ArrayList<Baggage> baggs;
    private ArrayList<Area> areas;
    private Player soko;

    public boolean isColliding(Actor actor, KeyEvent direction) {
        switch (direction.getKeyCode()) {           //pobrannie kodu klawisza i na jego podstawie wykonanie określonej akcji
            case KeyEvent.VK_LEFT:
                if (checkWallCollision(soko, LEFT_COLLISION)) {
                    return true;
                }
                if (checkBagCollision(LEFT_COLLISION)) {
                    return true;
                }
                return false;
            case KeyEvent.VK_RIGHT:
                if (checkWallCollision(soko, RIGHT_COLLISION)) {
                    return true;
                }
                if (checkBagCollision(RIGHT_COLLISION)) {
                    return true;
                }
                return false;
            case KeyEvent.VK_UP:
                if (checkWallCollision(soko, TOP_COLLISION)) {
                    return true;
                }
                if (checkBagCollision(TOP_COLLISION)) {
                    return true;
                }
                return false;
            case KeyEvent.VK_DOWN:
                if (checkWallCollision(soko, BOTTOM_COLLISION)) {
                    return true;
                }
                if (checkBagCollision(BOTTOM_COLLISION)) {
                    return true;
                }
                return false;
            default:
                return true;
        }
    }

    private boolean checkWallCollision(Actor actor, int type) {         //sprawdza czy aktor podany w argumencie koliduje z jakąkolwiek ścianą

        switch (type) {
            case LEFT_COLLISION:

                for (Wall wall : walls) {
                    if (actor.isLeftCollision(wall)) {
                        return true;
                    }
                }
                return false;
            case RIGHT_COLLISION:
                for (Wall wall : walls) {
                    if (actor.isRightCollision(wall)) {
                        return true;
                    }
                }
                return false;
            case TOP_COLLISION:
                for (Wall wall : walls) {
                    if (actor.isTopCollision(wall)) {
                        return true;
                    }
                }
                return false;
            case BOTTOM_COLLISION:
                for (Wall wall : walls) {
                    if (actor.isBottomCollision(wall)) {
                        return true;
                    }
                }
                return false;
            default:
                break;
        }
        return true;
    }

    private boolean checkBagCollision(int type) {

        switch (type) {

            case LEFT_COLLISION:

                for (int i = 0; i < baggs.size(); i++) {

                    Baggage bag = baggs.get(i);

                    if (soko.isLeftCollision(bag)) {

                        for (Baggage item : baggs) {

                            if (!bag.equals(item)) {

                                if (bag.isLeftCollision(item)) {
                                    return true;
                                }
                            }
                        }
                        if (checkWallCollision(bag, LEFT_COLLISION)) {
                            return true;
                        }

                        bag.move(-Board.SPACE, 0);

                    }
                }
                return false;

            case RIGHT_COLLISION:

                for (int i = 0; i < baggs.size(); i++) {

                    Baggage bag = baggs.get(i);

                    if (soko.isRightCollision(bag)) {

                        for (Baggage item : baggs) {

                            if (!bag.equals(item)) {

                                if (bag.isRightCollision(item)) {
                                    return true;
                                }
                            }

                            if (checkWallCollision(bag, RIGHT_COLLISION)) {
                                return true;
                            }
                        }

                        bag.move(Board.SPACE, 0);

                    }
                }
                return false;

            case TOP_COLLISION:

                for (int i = 0; i < baggs.size(); i++) {

                    Baggage bag = baggs.get(i);

                    if (soko.isTopCollision(bag)) {

                        for (Baggage item : baggs) {

                            if (!bag.equals(item)) {

                                if (bag.isTopCollision(item)) {
                                    return true;
                                }
                            }

                            if (checkWallCollision(bag, TOP_COLLISION)) {
                                return true;
                            }
                        }

                        bag.move(0, -Board.SPACE);

                    }
                }
                return false;

            case BOTTOM_COLLISION:

                for (int i = 0; i < baggs.size(); i++) {

                    Baggage bag = baggs.get(i);

                    if (soko.isBottomCollision(bag)) {

                        for (Baggage item : baggs) {

                            if (!bag.equals(item)) {

                                if (bag.isBottomCollision(item)) {
                                    return true;
                                }
                            }

                            if (checkWallCollision(bag, BOTTOM_COLLISION)) {

                                return true;
                            }
                        }

                        bag.move(0, Board.SPACE);

                    }
                }
                return false;

            default:
                break;
        }
        return false;
    }

    public void loadActors(ArrayList<Wall> walls, ArrayList<Baggage> baggs, ArrayList<Area> areas, Player soko) {
        this.walls = walls;
        this.baggs = baggs;
        this.areas = areas;
        this.soko = soko;
    }
}
