import javax.swing.*;
import java.awt.*;

public class Sokoban extends JFrame {

    public static final int OFFSET = 30;

    public Sokoban() {

        initUI();
    }

    private void initUI() {

        Board board = new Board();
        add(board);
        board.setLayout(null);

        var menuButton = new JButton("M");
        menuButton.addActionListener((event) -> board.togglePause());
        menuButton.setBackground(Color.BLUE);
        menuButton.setFocusable(false);
        menuButton.setBounds(board.getBoardWidth() - 2 * OFFSET, OFFSET, 40, 40);
        board.add(menuButton);

        setTitle("Sokoban");
        setSize(board.getBoardWidth() + OFFSET, board.getBoardHeight() + 2 * OFFSET);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);     //ustawia akcję na kliknięcie przycisku zamykania(nie jest ustawiona domyślnie)
        setLocationRelativeTo(null);                        //ustawia pozycję okna w odniesieniu do innego komponentu, tu wyśrodkowane
        setResizable(false);
    }

    public static void main(String[] args) {

        EventQueue.invokeLater(() -> {
            Sokoban game = new Sokoban();
            game.setVisible(true);
        });
    }

}
