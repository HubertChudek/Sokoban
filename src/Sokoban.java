import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author HubiSzubi
 * @version 1.5
 */
public class Sokoban extends JFrame {

    public static final int OFFSET = 30;
    private JSplitPane splitPane;
    private Board board;
    private JPanel menu;

    Timer sliderTimer;
    TimerTask menuTask;
    int divider;

    public Sokoban() {
        addKeyListener(new TAdapter());     //dodaje nasłuchiwacz klawiatury do komponentu
        initUI();
    }

    private void initUI() {
        board = new Board();
        board.setLayout(null);
        initMenuBttn(board);
        menu = initMenu();

        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                true,
                board,
                menu);
        initSliderTimer();
        splitPane.setPreferredSize(new Dimension(board.getBoardWidth(), board.getBoardHeight()));
        splitPane.setOneTouchExpandable(false);
        add(splitPane);
        splitPane.setDividerLocation(board.getBoardWidth());
        splitPane.setDividerSize(0);

        setTitle("Sokoban");
        setMinimumSize(new Dimension(board.getBoardWidth(), board.getBoardHeight()));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);     //ustawia akcję na kliknięcie przycisku zamykania(nie jest ustawiona domyślnie)
        setLocationRelativeTo(null);                        //ustawia pozycję okna w odniesieniu do innego komponentu, tu wyśrodkowane
        setResizable(false);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        pack();
    }

    public void hideMenu() {
        board.togglePause();
        menuTask.cancel();
        menuTask = createTask("hide");
        sliderTimer.scheduleAtFixedRate(menuTask, 0, 5);
    }


    public void showMenu() {
        board.togglePause();
        menuTask.cancel();
        menuTask = createTask("show");
        sliderTimer.scheduleAtFixedRate(menuTask, 0, 5);
    }

    private void initSliderTimer() {
        sliderTimer = new Timer("SilderTimer");
        menuTask = createTask("show");
    }

    public TimerTask createTask(String name) throws IllegalArgumentException {
        TimerTask timerTask;
        divider = splitPane.getDividerLocation();

        if (name.equals("show")) {
            timerTask = new TimerTask() {

                @Override
                public void run() {
                    divider -= 8;
                    splitPane.setDividerLocation(divider);
                    if (divider <= (int) (board.getBoardWidth() * 0.75)) {
                        menuTask.cancel();
                    }
                }
            };
        } else if (name.equals("hide")) {
            timerTask = new TimerTask() {

                @Override
                public void run() {
                    divider += 8;
                    splitPane.setDividerLocation(divider);
                    if (divider >= board.getBoardWidth()) {
                        menuTask.cancel();
                    }
                }
            };
        } else {
            throw new IllegalArgumentException();
        }

        return timerTask;
    }

    public void initMenuBttn(final Board board) {
        JButton menuButton = new JButton(new ImageIcon("assets/pause.png"));
        menuButton.setMargin(new Insets(0, 0, 0, 0));
        menuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                showMenu();
            }
        });
        menuButton.setBackground(new Color(193, 191, 255));
        menuButton.setFocusable(false);
        menuButton.setBounds(board.getBoardWidth() - 90, 50, 40, 40);
        board.add(menuButton);
    }

    public JPanel initMenu() {
        JPanel pane = new JPanel(new GridBagLayout());
        pane.setBackground(new Color(193, 191, 255));

        pane.setBorder(new EmptyBorder(50, 50, 50, 50));
        pane.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JButton[] buttons = new JButton[4];
        buttons[0] = new JButton("BACK TO GAME");
        buttons[1] = new JButton("SELECT LEVEL");
        buttons[2] = new JButton("HIGHSCORES");
        buttons[3] = new JButton("EXIT");

        buttons[0].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hideMenu();
            }
        });
        buttons[3].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        for (JButton btn : buttons) {
            btn.setFocusable(false);
            btn.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
            pane.add(btn, gbc);
            pane.add(Box.createRigidArea(new Dimension(10, 20)), gbc);
        }
        gbc.weighty = 1;

        return pane;
    }

    private class TAdapter extends KeyAdapter {             //klasa obsługująca eventy wciśnięcia klawisza

        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                if (board.getPauseStatus()) {
                    hideMenu();
                } else {
                    showMenu();
                }
            }
            board.keyPressed(e);
        }
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                Sokoban game = new Sokoban();
                game.setVisible(true);
            }
        });
    }
}
