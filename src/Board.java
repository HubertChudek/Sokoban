import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import javax.swing.*;

public class Board extends JPanel {

    private final int OFFSET = Sokoban.OFFSET;      //odległość planszy od brzegów okna
    public final static int SPACE = 50;                   //wielkość sprite'u bloku ściany i jednocześnie jednego pola na planszy
    private final int LEFT_COLLISION = 1;           //ustalenie typów kolizji
    private final int RIGHT_COLLISION = 2;
    private final int TOP_COLLISION = 3;
    private final int BOTTOM_COLLISION = 4;

    private ArrayList<Wall> walls;      //listy przechowujące wszystkie części mapy
    private ArrayList<Baggage> baggs;
    private ArrayList<Area> areas;

    private Player soko;
    private int w = 0;
    private int h = 0;

    private boolean isCompleted = false;

    private String level                    //poziom jest przechowywany w postaci ciągu znaków,
            = "    ######\n"                //w celu czytelności i łatwości edycji
            + "    ##   #\n"                //# - ściana
            + "    ##$  #\n"                //$ - pudło
            + "  ####  $##\n"               //. - miejsce gdzie należy przesunąć pudła
            + "  ##  $ $ #\n"               //@ - sokoban/gracz
            + "#### # ## #   ######\n"      //\n - nowy rząd
            + "##   # ## #####  ..#\n"
            + "## $  $          ..#\n"
            + "###### ### #@##  ..#\n"
            + "    ##     #########\n"
            + "    ########\n";

    public Board() {

        initBoard();
    }

    private void initBoard() {

        addKeyListener(new TAdapter());     //dodaje nasłuchiwacz klawiatury do komponentu
        setFocusable(true);
        initWorld();
    }

    private void initWorld() {          //inicjuje mapę

        walls = new ArrayList<>();      //inincjacja list
        baggs = new ArrayList<>();
        areas = new ArrayList<>();

        int x = OFFSET;                 //ustalanie początku rysowania elementów
        int y = OFFSET;

        Wall w;
        Baggage b;
        Area a;

        for (int i = 0; i < level.length(); i++) {  //przechodzi znak po znaku po stringu poziomu

            switch (level.charAt(i)) {              //i tworzy odpowiednie obiekty i dodaje je do list
                case '#':                           //jednocześnie sukcesywnie ustawiając ich pozycję na planszy
                    w = new Wall(x, y);
                    walls.add(w);
                    x += SPACE;
                    break;
                case '$':
                    b = new Baggage(x, y);
                    baggs.add(b);
                    x += SPACE;
                    break;
                case '.':
                    a = new Area(x, y);
                    areas.add(a);
                    x += SPACE;
                    break;
                case '@':
                    soko = new Player(x, y);
                    x += SPACE;
                    break;
                case '\n':
                    y += SPACE;
                    if (this.w < x) {       //dostosowuje szerokość okna do najdłuższego rzędu mapy
                        this.w = x;
                    }
                    x = OFFSET;
                    break;
                case ' ':
                    x += SPACE;
                    break;
            }
        }
        this.h = y;                   //dosotsowuje wysokość okna do liczby rzędów
    }

    private void buildWorld(Graphics g) {            //rysuje obiekty świata na planszy

        g.setColor(new Color(255, 143, 175));
        g.fillRect(0, 0, this.getWidth(), this.getHeight());

        ArrayList<Actor> world = new ArrayList<>();

        world.addAll(walls);
        world.addAll(areas);
        world.addAll(baggs);
        world.add(soko);

        for (int i = 0; i < world.size(); i++) {

            Actor item = world.get(i);

            g.drawImage(item.getImage(), item.getX(), item.getY(), this);
        }

        if (isCompleted) {

            g.setColor(new Color(0, 0, 0));
            g.drawString("Completed", 25, 20);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        buildWorld(g);
        Toolkit.getDefaultToolkit().sync();
    }

    private class TAdapter extends KeyAdapter {             //klasa obsługująca eventy wciśnięcia klawisza

        @Override
        public void keyPressed(KeyEvent e) {

            switch (e.getKeyCode()) {           //pobrannie kodu klawisza i na jego podstawie wykonanie określonej akcji
                case KeyEvent.VK_LEFT:
                    if (checkWallCollision(soko, LEFT_COLLISION)) {
                        break;
                    }
                    if (checkBagCollision(LEFT_COLLISION)) {
                        break;
                    }
                    soko.move(-SPACE, 0);
                    break;
                case KeyEvent.VK_RIGHT:
                    if (checkWallCollision(soko, RIGHT_COLLISION)) {
                        break;
                    }
                    if (checkBagCollision(RIGHT_COLLISION)) {
                        break;
                    }
                    soko.move(SPACE, 0);
                    break;
                case KeyEvent.VK_UP:
                    if (checkWallCollision(soko, TOP_COLLISION)) {
                        break;
                    }
                    if (checkBagCollision(TOP_COLLISION)) {
                        break;
                    }
                    soko.move(0, -SPACE);
                    break;
                case KeyEvent.VK_DOWN:
                    if (checkWallCollision(soko, BOTTOM_COLLISION)) {
                        break;
                    }
                    if (checkBagCollision(BOTTOM_COLLISION)) {
                        break;
                    }
                    soko.move(0, SPACE);
                    break;
                case KeyEvent.VK_R:
                    restartLevel();
                    break;
                default:
                    break;
            }
            isCompleted();
            repaint();
            if (isCompleted) {
                return;
            }
        }
    }

    private boolean checkWallCollision(Actor actor, int type) {         //sprawdza czy aktor podany w argumencie koliduje z jakąkolwiek ścianą

        switch (type) {
            case LEFT_COLLISION:

                for (int i = 0; i < walls.size(); i++) {
                    Wall wall = walls.get(i);
                    if (actor.isLeftCollision(wall)) {
                        return true;
                    }
                }
                return false;
            case RIGHT_COLLISION:
                for (int i = 0; i < walls.size(); i++) {
                    Wall wall = walls.get(i);
                    if (actor.isRightCollision(wall)) {
                        return true;
                    }
                }
                return false;
            case TOP_COLLISION:
                for (int i = 0; i < walls.size(); i++) {
                    Wall wall = walls.get(i);
                    if (actor.isTopCollision(wall)) {
                        return true;
                    }
                }
                return false;
            case BOTTOM_COLLISION:
                for (int i = 0; i < walls.size(); i++) {
                    Wall wall = walls.get(i);
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

                        for (int j = 0; j < baggs.size(); j++) {

                            Baggage item = baggs.get(j);

                            if (!bag.equals(item)) {

                                if (bag.isLeftCollision(item)) {
                                    return true;
                                }
                            }
                        }
                        if (checkWallCollision(bag, LEFT_COLLISION)) {
                            return true;
                        }

                        bag.move(-SPACE, 0);
                        isCompleted();
                    }
                }
                return false;

            case RIGHT_COLLISION:

                for (int i = 0; i < baggs.size(); i++) {

                    Baggage bag = baggs.get(i);

                    if (soko.isRightCollision(bag)) {

                        for (int j = 0; j < baggs.size(); j++) {

                            Baggage item = baggs.get(j);

                            if (!bag.equals(item)) {

                                if (bag.isRightCollision(item)) {
                                    return true;
                                }
                            }

                            if (checkWallCollision(bag, RIGHT_COLLISION)) {
                                return true;
                            }
                        }

                        bag.move(SPACE, 0);
                        isCompleted();
                    }
                }
                return false;

            case TOP_COLLISION:

                for (int i = 0; i < baggs.size(); i++) {

                    Baggage bag = baggs.get(i);

                    if (soko.isTopCollision(bag)) {

                        for (int j = 0; j < baggs.size(); j++) {

                            Baggage item = baggs.get(j);

                            if (!bag.equals(item)) {

                                if (bag.isTopCollision(item)) {
                                    return true;
                                }
                            }

                            if (checkWallCollision(bag, TOP_COLLISION)) {
                                return true;
                            }
                        }

                        bag.move(0, -SPACE);
                        isCompleted();
                    }
                }
                return false;

            case BOTTOM_COLLISION:

                for (int i = 0; i < baggs.size(); i++) {

                    Baggage bag = baggs.get(i);

                    if (soko.isBottomCollision(bag)) {

                        for (int j = 0; j < baggs.size(); j++) {

                            Baggage item = baggs.get(j);

                            if (!bag.equals(item)) {

                                if (bag.isBottomCollision(item)) {
                                    return true;
                                }
                            }

                            if (checkWallCollision(bag, BOTTOM_COLLISION)) {

                                return true;
                            }
                        }

                        bag.move(0, SPACE);
                        isCompleted();
                    }
                }
                return false;

            default:
                break;
        }
        return false;
    }

    public void isCompleted() {              //sprawdza czy poziom został ukonczony, czyli czy wszystkie skzynki są na polach

        int numberOfBags = baggs.size();
        int finishedBags = 0;

        for (int i = 0; i < numberOfBags; i++) {
            Baggage bag = baggs.get(i);

            for (int j = 0; j < numberOfBags; j++) {

                Area area = areas.get(j);

                if (bag.getX() == area.getX() && bag.getY() == area.getY()) {
                    finishedBags += 1;                                      //!!!
                }
            }
        }

        if (numberOfBags == finishedBags) {

            isCompleted = true;
            repaint();
        }

    }

    public void restartLevel() {

        areas.clear();
        baggs.clear();
        walls.clear();

        initWorld();
        isCompleted = false;
    }


    public int getBoardWidth() {
        return this.w;
    }

    public int getBoardHeight() {
        return this.h;
    }
}
