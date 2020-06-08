import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class LevelsMenu extends JPanel {
    private CardLayout cardLayout = new CardLayout();
    private ImageIcon[] thumbnails;
    private JPanel thumbnailsPanel;
    private Sokoban parent;

    public LevelsMenu(Sokoban parent) {
        this.parent = parent;

        initLevelsMenu();
    }

    private void initLevelsMenu() {
        setBackground(new Color(144, 236, 255));
        setBorder(BorderFactory.createEmptyBorder(40, 50, 50, 50));
        setLayout(new FlowLayout());
        setVisible(true);

        initThumbnailsPanel();
        loadLevelsThumbnails();
        initLevelsPreview();
        initNavigationButtons();
    }

    private void initThumbnailsPanel() {
        JPanel tempPanel = new JPanel();
        tempPanel.setBackground(new Color(144, 236, 255));
        tempPanel.setPreferredSize(new Dimension((int) (parent.getWidth() * 0.9), (int) (parent.getHeight() * 0.7)));
        tempPanel.setLayout(cardLayout);
        thumbnailsPanel = tempPanel;
        add(tempPanel);
    }

    private void initLevelsPreview() {
        for (int i = 0; i < thumbnails.length; i++) {
            JLabel thumb = new JLabel(thumbnails[i]);
            thumb.setText(String.valueOf(i + 1));
            thumb.setHorizontalTextPosition(JLabel.LEFT);
            thumb.setVerticalTextPosition(JLabel.TOP);
            thumb.setFont(new Font("Arial", Font.BOLD, 30));
            thumb.setName(String.valueOf(i + 1));
            thumbnailsPanel.add(thumb);
        }
    }

    private void initNavigationButtons() {
        JButton[] buttons = new JButton[3];

        buttons[0] = new JButton("Prev");
        buttons[1] = new JButton("OK");
        buttons[2] = new JButton("Next");

        for (int i = 0; i < 3; i++) {
            buttons[i].addActionListener(new ButtonAction(thumbnailsPanel, i));
            add(buttons[i]);

            buttons[i].setFocusable(false);
            buttons[i].setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createBevelBorder(BevelBorder.RAISED),
                    BorderFactory.createEmptyBorder(5, 10, 5, 10)));
            buttons[i].setBackground(new Color(173, 102, 255));
            buttons[i].setFont(new Font("Arial", Font.BOLD, 18));
        }
    }

    public void loadLevelsThumbnails() {
        String baseDirectory = "levels/";
        String extension = ".thumbnail.png";
        BufferedImage img = null;
        int filesCount = 0;
        try {
            filesCount = Objects.requireNonNull(new File(baseDirectory).listFiles()).length / 2; //liczba plików w folderze
        } catch (Exception e) {
            System.out.println("No files found!");
            e.printStackTrace();
        }
        thumbnails = new ImageIcon[filesCount];

        for (int i = 0; i < filesCount; i++) {
            try {
                img = ImageIO.read(new File(baseDirectory + "lvl" + (i + 1) + extension));
            } catch (IOException e) {
                e.printStackTrace();
            }
            Image dimg = Objects.requireNonNull(img).getScaledInstance(-(int) (parent.getWidth() * 0.7), (int) (parent.getHeight() * 0.7), Image.SCALE_SMOOTH); //zunifikować!!!

            thumbnails[i] = new ImageIcon(dimg);
        }
    }

    private class ButtonAction implements ActionListener {
        JPanel CLparent;
        int no;

        public ButtonAction(JPanel parent, int no) {
            this.CLparent = parent;
            this.no = no;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            switch (no) {
                case 0:
                    cardLayout.previous(CLparent);
                    break;
                case 1:
                    JLabel card = null;
                    for (Component comp : thumbnailsPanel.getComponents()) {
                        if (comp.isVisible()) {
                            card = (JLabel) comp;
                        }
                    }
                    assert card != null;
                    int lvlNumber = Integer.parseInt(card.getName());
                    parent.loadLevel(lvlNumber);
                    break;
                case 2:
                    cardLayout.next(CLparent);
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
    }
}
