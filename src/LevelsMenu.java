import javax.imageio.ImageIO;
import javax.swing.*;
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
    private int w = 0;
    private int h = 0;


    public LevelsMenu(int width, int height) {
        this.w = width;
        this.h = height;

        initLevelsMenu();
    }

    private void initLevelsMenu() {
        setBackground(new Color(144, 236, 255));
        setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        setLayout(new FlowLayout());
        setVisible(true);

        thumbnailsPanel = initThumbnailsPanel();
        add(thumbnailsPanel);
        loadLevelsThumbnails();
        initLevelsLayout();
        initNavigationButtons();
    }

    private JPanel initThumbnailsPanel() {
        JPanel tempPanel =  new JPanel();
        tempPanel.setBackground(new Color(144, 236, 255));
        tempPanel.setLayout(cardLayout);

        return tempPanel;
    }

    private void initLevelsLayout() {
        for (ImageIcon ii : thumbnails) {
            JPanel workPanel = new JPanel();
            workPanel.setLayout(new FlowLayout());
            JLabel thumb = new JLabel(ii);
            workPanel.add(thumb);
            thumbnailsPanel.add(thumb);
        }
    }

    private void initNavigationButtons() {
        JButton buttons[] = new JButton[3];


        buttons[0] = new JButton("Prev");
        buttons[1] = new JButton("OK");
        buttons[2] = new JButton("Next");

        buttons[0].addActionListener(new ButtonAction(thumbnailsPanel, 0));
        buttons[2].addActionListener(new ButtonAction(thumbnailsPanel, 2));

        add(buttons[0]);
        add(buttons[2]);

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
            Image dimg = Objects.requireNonNull(img).getScaledInstance(-(w - 100), h - 100, Image.SCALE_SMOOTH); //zunifikować!!!

            thumbnails[i] = new ImageIcon(dimg);
        }
    }

    private class ButtonAction implements ActionListener {
        JPanel parent;
        int no;

        public ButtonAction(JPanel parent, int no) {
            this.parent = parent;
            this.no = no;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            switch (no) {
                case 0:
                    cardLayout.previous(parent);
                    break;
                case 1:
                    break;
                case 2:
                    cardLayout.next(parent);
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
    }
}
