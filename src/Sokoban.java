import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Sokoban extends JFrame {

    public static final int OFFSET = 30;
    private JSplitPane splitPane;
    private Board board;

    public Sokoban() {

        initUI();
    }

    private void initUI() {

        board = new Board();
        add(board);
        board.setLayout(null);
        initMenuBttn(board);

        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, board, new JPanel());
        splitPane.setOneTouchExpandable(false);
        splitPane.setDividerLocation(board.getBoardWidth() + 100);
        splitPane.setDividerSize(0);
        add(splitPane);

//Provide minimum sizes for the two components in the split pane
        Dimension minimumSize = new Dimension(200, 200);
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
                splitPane.setDividerLocation(board.getBoardWidth() - 300);
            }
        });
        menuButton.setBackground(new Color(193, 191, 255));
        menuButton.setFocusable(false);
        menuButton.setBounds(board.getBoardWidth() - 2 * OFFSET, OFFSET, 40, 40);
        board.add(menuButton);
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
