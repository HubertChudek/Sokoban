import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.TimerTask;
import java.util.Timer;
import java.util.concurrent.Semaphore;
import javax.swing.*;

public class Board extends JPanel {

    final static int SPACE = 50;             //wielkość sprite'u bloku ściany i jednocześnie jednego pola na planszy

    private ArrayList<Wall> walls;          //listy przechowujące wszystkie części mapy
    private ArrayList<Baggage> baggs;
    private ArrayList<Area> areas;

    private Player soko;
    private int w = 0;
    private int h = 0;
    private int moves = 0;
    private int counter = 0;               //timer do zliczania czasu gry
    private Timer timer;
    private Collision collision;

    private boolean isCompleted = false;
    private boolean isPaused = false;
    private Semaphore sem = new Semaphore(1);

    //poziomy są przechowywane w postaci ciągu znaków w plikach txt,
    //w celu czytelności i łatwości edycji
    //# - ściana
    //$ - pudło
    //. - miejsce gdzie należy przesunąć pudła
    // @ - sokoban/gracz
    // \n - nowy rząd
    private String level = "@";

    public Board() {
        timer = new Timer();
        collision = new Collision();
        initBoard();
        collision.loadActors(walls, baggs, soko);
    }

    private void initBoard() {
        setFocusable(true);
        setLayout(null);
        loadLevel(1);
        initWorld();
        initScores();
    }

    private void initWorld() {          //inicjuje mapę

        walls = new ArrayList<>();      //inincjacja list
        baggs = new ArrayList<>();
        areas = new ArrayList<>();
        this.w = 0;
        this.h = 0;

        //odległość planszy od brzegów okna
        int OFFSET = SPACE;
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
        this.w += 2 * OFFSET;
        this.h = y + 2 * OFFSET;                   //dosotsowuje wysokość okna do liczby rzędów

        collision.loadActors(walls, baggs, soko);
    }

    @Override
    public void addNotify() {
        super.addNotify();

        Thread animator = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        revalidate();
                        repaint();
                        Thread.sleep(30);
                    } catch (InterruptedException ignored) {
                    }
                }
            }
        });
        animator.start();
    }

    private void initScores() {
        counter = 0;
        moves = 0;
        initTimer();
    }

    private void initTimer() {
        timer.cancel();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                try {
                    sem.acquire();
                    counter++;
                    sem.release();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        timer = new Timer("Timer");
        timer.scheduleAtFixedRate(timerTask, 1000, 1000);
    }

    private void drawWorld(Graphics g) {            //rysuje obiekty świata na planszy
        g.setColor(new Color(154, 156, 150));
        g.fillRect(0, 0, this.getWidth(), this.getHeight());

        ArrayList<Actor> world = new ArrayList<>();
        world.addAll(walls);
        world.addAll(areas);
        world.addAll(baggs);
        world.add(soko);

        for (Actor item : world) {
            g.drawImage(item.getImage(), item.getX(), item.getY(), this);
        }
        if (isCompleted) {
            drawCompleted(g);
        }
    }

    private void drawCompleted(Graphics g) {
        String msg1 = "Completed";
        String msg2 = "Press 'r' to restart";
        Font big = new Font("Calibri", Font.BOLD, 100);
        Font small = new Font("Dialog.plain", Font.BOLD, 15);
        FontMetrics fm1 = getFontMetrics(big);
        FontMetrics stdfm = getFontMetrics(small);


        g.setColor(new Color(105, 255, 56));
        g.setFont(big);
        g.drawString(msg1, (getBoardWidth() - fm1.stringWidth(msg1)) / 2,
                getBoardHeight() / 2);

        g.setFont(small);
        g.setColor(Color.BLACK);
        g.drawString(msg2, (getBoardWidth() - stdfm.stringWidth(msg2)) / 2, (getBoardHeight() / 2) + 30);
    }

    private void drawScores(Graphics g) {
        String time = "Time passed: 0";
        try {
            sem.acquire();
            time = "Time passed: 0" + counter / 60 + ":";
            if (counter % 60 < 10) {
                time += "0" + counter % 60;
            } else {
                time += counter % 60;
            }
            sem.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Font small = new Font("Dialog.plain", Font.BOLD, 15);
        g.setFont(small);
        g.setColor(new Color(0, 0, 0));
        g.drawString(time, 25, 20);

        String moves = "Moves: " + this.moves;

        g.setColor(new Color(0, 0, 0));
        g.drawString(moves, 25, 35);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        drawWorld(g);
        drawScores(g);
        Toolkit.getDefaultToolkit().sync();
    }

    public void keyPressed(KeyEvent e) {

        if (isCompleted || isPaused) {
            if (e.getKeyCode() == KeyEvent.VK_R) {
                restartLevel();
            }
            return;
        }

        switch (e.getKeyCode()) {           //pobrannie kodu klawisza i na jego podstawie wykonanie określonej akcji
            case KeyEvent.VK_LEFT:
                if (collision.isColliding(e)) {
                    break;
                }
                isCompleted();
                soko.move(-SPACE, 0);
                moves++;
                break;
            case KeyEvent.VK_RIGHT:
                if (collision.isColliding(e)) {
                    break;
                }
                isCompleted();
                soko.move(SPACE, 0);
                moves++;
                break;
            case KeyEvent.VK_UP:
                if (collision.isColliding(e)) {
                    break;
                }
                isCompleted();
                soko.move(0, -SPACE);
                moves++;
                break;
            case KeyEvent.VK_DOWN:
                if (collision.isColliding(e)) {
                    break;
                }
                isCompleted();
                soko.move(0, SPACE);
                moves++;
                break;
            case KeyEvent.VK_R:
                restartLevel();
                break;
            default:
                break;
        }
        isCompleted();
    }


    private void isCompleted() {              //sprawdza czy poziom został ukonczony, czyli czy wszystkie skzynki są na polach
        int numberOfBags = baggs.size();
        int finishedBags = 0;

        for (Baggage bag : baggs) {
            for (int j = 0; j < numberOfBags; j++) {

                Area area = areas.get(j);

                if (bag.getX() == area.getX() && bag.getY() == area.getY()) {
                    finishedBags += 1;
                }
            }
        }

        if (finishedBags == numberOfBags) { //(finishedBags == numberOfBags)
            isCompleted = true;
            timer.cancel();
        }
    }

    public void loadLevel(int number) {
        String baseDirectory = "levels/lvl";
        String extension = ".txt";
        File file = new File(baseDirectory + number + extension);
        BufferedReader reader;
        String line;
        level = "";
        try {
            reader = new BufferedReader(new FileReader(file));
            while ((line = reader.readLine()) != null) {
                level += line + '\n';
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.exit(-2);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reloadBoard() {
        initWorld();
        initScores();
    }

    public void restartLevel() {
        areas.clear();
        baggs.clear();
        walls.clear();

        initWorld();
        isCompleted = false;
        if (isPaused) {
            togglePause();
        }
        initScores();
    }

    public void togglePause() {
        if (isPaused) {
            initTimer();
            isPaused = false;
        } else {
            timer.cancel();
            isPaused = true;
        }
    }

    public boolean getPauseStatus() {
        return this.isPaused;
    }

    public int getBoardWidth() {
        return this.w;
    }

    public int getBoardHeight() {
        return this.h;
    }
}
