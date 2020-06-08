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
    private JButton pauseButton;
    private SlideMenu menu;
    private LevelsMenu levelsMenu;

    public Sokoban() {
        addKeyListener(new TAdapter());     //dodaje nasłuchiwacz klawiatury do komponentu
        initUI();
    }

    private void initUI() {
        board = new Board();
        initPauseBttn(board);

        initFrame();
        menu = new SlideMenu(this, mainContentPane);
        levelsMenu = new LevelsMenu(this);
        levelsMenu.setPreferredSize(new Dimension(board.getBoardWidth(), board.getBoardHeight()));
        splitPanel = new SplitPanel(board, menu);
        splitPanel.showMenu();
        initMainPane();

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
        this.pauseButton = pauseButton;
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

    public void loadLevel(int number) {
        board.loadLevel(number);
        board.reloadBoard();
        splitPanel.showMenu();
        splitPanel.setPreferredSize(new Dimension(board.getBoardWidth(), board.getBoardHeight()));
        splitPanel.setDividerLocation(board.getBoardWidth());
        setSize(new Dimension(board.getBoardWidth(), board.getBoardHeight()));
        pauseButton.setBounds(board.getBoardWidth() - 90, 50, 40, 40);
        refreshLevelsMenu();
    }

    private void refreshLevelsMenu() {
        CardLayout cardLayout = (CardLayout) mainContentPane.getLayout();
        cardLayout.removeLayoutComponent(levelsMenu);
        levelsMenu = new LevelsMenu(this);
        levelsMenu.setPreferredSize(new Dimension(board.getBoardWidth(), board.getBoardHeight()));
        mainContentPane.add(levelsMenu, "Level menu");
    }

    private void initFrame() {
        setTitle("Sokoban");
        setSize(new Dimension(board.getBoardWidth(), board.getBoardHeight()));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);     //ustawia akcję na kliknięcie przycisku zamykania(nie jest ustawiona domyślnie)
        setLocationRelativeTo(null);                        //ustawia pozycję okna w odniesieniu do innego komponentu, tu wyśrodkowane
        setResizable(false);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
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
