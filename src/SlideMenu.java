import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SlideMenu extends JPanel {
    private JButton[] buttons = new JButton[4];
    private Sokoban mainFrame;
    private JPanel frameContentPane;

    public SlideMenu(Sokoban frame, JPanel mainPane) {
        this.mainFrame = frame;
        this.frameContentPane = mainPane;
        GridBagConstraints gbc = initBorders();
        initButtons(gbc);
    }

    public GridBagConstraints initBorders() {
        setLayout(new GridBagLayout());
        setBackground(new Color(144, 236, 255));
        setBorder(new EmptyBorder(50, 50, 50, 50));
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        return gbc;
    }

    private void initButtons(GridBagConstraints gbc) {
        buttons[0] = new JButton("BACK TO GAME");
        buttons[1] = new JButton("SELECT LEVEL");
        buttons[2] = new JButton("HIGHSCORES");
        buttons[3] = new JButton("EXIT");

        buttons[0].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.hideMenu();
            }
        });
        buttons[1].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.hideMenu();
                CardLayout cardLayout = (CardLayout) frameContentPane.getLayout();
                cardLayout.show(frameContentPane, "Level menu");
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
            btn.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createBevelBorder(BevelBorder.RAISED),
                    BorderFactory.createEmptyBorder(15, 10, 15, 10)));
            btn.setBackground(new Color(173, 102, 255));
            btn.setFont(new Font("Arial", Font.BOLD, 18));
            add(btn, gbc);
            add(Box.createRigidArea(new Dimension(10, 20)), gbc);
        }
        buttons[3].setBackground(new Color(255, 107, 159));

        gbc.weighty = 1;
    }
}
