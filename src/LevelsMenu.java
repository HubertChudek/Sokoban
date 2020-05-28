import javax.swing.*;
import java.awt.*;
import java.io.File;

public class LevelsMenu extends JPanel {
    private CardLayout cardLayout = new CardLayout();
    private ImageIcon[] thumbnails;

    public LevelsMenu() {
        initLevelsMenu();
    }

    private void initLevelsMenu() {
        setBackground(new Color(144, 236, 255));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setLayout(cardLayout);
        setVisible(false);

        loadLevelsThumbnails();
    }

    public void loadLevelsThumbnails() {
        String baseDirectory = "levels/";
        String extension = ".thumbnail.png";
        int filesCount = 0;
        try {
            filesCount = new File(baseDirectory).listFiles().length / 2; //liczba plików w folderze
        } catch (Exception e) {
            System.out.println("No files found!");
            e.printStackTrace();
        }
        thumbnails = new ImageIcon[filesCount];

        for (int i = 0; i < filesCount; i++) {
            thumbnails[i] = new ImageIcon(baseDirectory + (i+1) + extension);
        }
    }
}
