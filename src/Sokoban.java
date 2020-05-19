import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

    public Sokoban() {
        initUI();
    }

    private void initUI() {
        board = new Board();
        board.setLayout(null);
        initMenuBttn(board);
        menu = initMenu();

        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, board, menu);
        splitPane.setOneTouchExpandable(false);
        add(splitPane);
        splitPane.setDividerLocation(board.getBoardWidth() + OFFSET);
        splitPane.setDividerSize(0);

        /*TimerTask timerTask = new TimerTask() {
            double ratio = 0.05;
            double delta = ratio / 10;

            @Override
            public void run() {
                ratio += delta;
                if (ratio >= 1.0) {
                    ratio = 1.0;
                    delta = -delta;
                } else if (ratio <= 0.0) {
                    delta = -delta;
                    ratio = 0;
                }
                splitPane.setDividerLocation(ratio);
            }
        };
        Timer silderTimer = new Timer("SilderTimer");
        silderTimer.scheduleAtFixedRate(timerTask, 0, 5);*/

        //Provide minimum sizes for the two components in the split pane
        //Dimension minimumSize = new Dimension(50, 50);
        //board.setMinimumSize(minimumSize);

        setTitle("Sokoban");
        setSize(board.getBoardWidth() + OFFSET, board.getBoardHeight() + 2 * OFFSET);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);     //ustawia akcję na kliknięcie przycisku zamykania(nie jest ustawiona domyślnie)
        setLocationRelativeTo(null);                        //ustawia pozycję okna w odniesieniu do innego komponentu, tu wyśrodkowane
        setResizable(false);
    }

    public void initMenuBttn(final Board board) {
        JButton menuButton = new JButton(new ImageIcon("assets/pause.png"));
        menuButton.setMargin(new Insets(0, 0, 0, 0));
        menuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                board.togglePause();
                splitPane.setDividerLocation(0.7);
            }
        });
        menuButton.setBackground(new Color(193, 191, 255));
        menuButton.setFocusable(false);
        menuButton.setBounds(board.getBoardWidth() - 2 * OFFSET, OFFSET, 40, 40);
        board.add(menuButton);
    }

    public JPanel initMenu() {
        JPanel pane = new JPanel();
        pane.setLayout((new BoxLayout(pane, BoxLayout.PAGE_AXIS)));

        JButton backButton = new JButton("BACK TO GAME");
        backButton.setFocusable(false);
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                board.togglePause();
                splitPane.setDividerLocation(board.getBoardWidth() + OFFSET);
            }
        });
        JButton exitButton = new JButton("EXIT");
        exitButton.setFocusable(false);
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        pane.add(backButton);
        pane.add(exitButton);
        return pane;
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
