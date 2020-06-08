import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * @author HubiSzubi
 * @version 1.5
 */
public class Sokoban extends JFrame {

    public static final int OFFSET = 30;
    private JPanel mainContentPane = new JPanel();
    private SplitPanel splitPanel;
    private Board board;
    private SlideMenu menu;
    private LevelsMenu levelsMenu;

    public Sokoban() {
        addKeyListener(new TAdapter());     //dodaje nasłuchiwacz klawiatury do komponentu
        initUI();
    }

    private void initUI() {
        board = new Board();
        initPauseBttn(board);

        menu = new SlideMenu(this, mainContentPane);

        levelsMenu = new LevelsMenu(board.getBoardWidth(), board.getBoardHeight());
        levelsMenu.setPreferredSize(new Dimension(board.getBoardWidth(), board.getBoardHeight()));

        splitPanel = new SplitPanel(board, menu);

        initMainPane();

        setTitle("Sokoban");
        setSize(new Dimension(board.getBoardWidth(), board.getBoardHeight()));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);     //ustawia akcję na kliknięcie przycisku zamykania(nie jest ustawiona domyślnie)
        setLocationRelativeTo(null);                        //ustawia pozycję okna w odniesieniu do innego komponentu, tu wyśrodkowane
        setResizable(false);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        pack();
    }

    private void initMainPane() {
        mainContentPane.setLayout(new CardLayout());
        mainContentPane.add(splitPanel, "Split pane");
        mainContentPane.add(levelsMenu, "Level menu");

        this.getContentPane().add(mainContentPane, BorderLayout.CENTER);
    }

    public void initPauseBttn(final Board board) {
        JButton pauseButton = new JButton(new ImageIcon("assets/pause.png"));
        pauseButton.setMargin(new Insets(0, 0, 0, 0));
        pauseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                splitPanel.showMenu();
            }
        });
        pauseButton.setBackground(new Color(144, 236, 255));
        pauseButton.setFocusable(false);
        pauseButton.setBounds(board.getBoardWidth() - 90, 50, 40, 40);
        board.add(pauseButton);
    }

    private class TAdapter extends KeyAdapter {             //klasa obsługująca eventy wciśnięcia klawisza

        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                if (board.getPauseStatus()) {
                    splitPanel.hideMenu();
                } else {
                    splitPanel.showMenu();
                }
            }
            board.keyPressed(e);
        }
    }

    public void hideMenu() {
        splitPanel.hideMenu();
    }

    public void showMenu() {
        splitPanel.showMenu();
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
