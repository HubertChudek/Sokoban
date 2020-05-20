import javax.swing.*;
import javax.swing.border.EmptyBorder;
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
        splitPane.setDividerSize(10);

        /*TimerTask timerTask = new TimerTask() {
            double ratio = 0.02;
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
        Dimension minimumSize = new Dimension(100, 100);
        menu.setMinimumSize(minimumSize);

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
                board.togglePause();
                splitPane.setDividerLocation(board.getBoardWidth() + OFFSET);
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
