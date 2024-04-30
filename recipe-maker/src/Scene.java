import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Scene {
    private JFrame frame;
    private JPanel cardPanel;
    private CardLayout cardLayout;

    public Scene() {
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create the cardPanel to hold multiple pages
        cardPanel = new JPanel();
        cardLayout = new CardLayout();
        cardPanel.setLayout(cardLayout);

        // Add different pages to the cardPanel
        addPage(createPage1(), "Page 1");
        addPage(createPage2(), "Page 2");
        // Add more pages as needed

        frame.add(cardPanel);

        // Set frame size
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double width = screenSize.getWidth();
        double height = screenSize.getHeight();
        frame.setSize((int) width/2, (int) height);
        frame.setVisible(true);
    }

    // Method to add a page to the cardPanel
    private void addPage(Component page, String name) {
        cardPanel.add(page, name);
    }

    private static ImageIcon createImageIcon(String path,
                                             String description) {
        java.net.URL imgURL = Scene.class.getResource(path);

        if (imgURL != null) {
            return new ImageIcon(imgURL, description);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }

    private static ImageIcon resizeIcon(ImageIcon icon, int width, int height) {
        Image img = icon.getImage();
        Image resizedImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(resizedImg);
    }

    // Method to create Page 1
    private JPanel createPage1() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(new Color(173, 216, 230));

        JLabel title = new JLabel("Recipe Maker");
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.PLAIN, 50)); // Set font size to large
        title.setForeground(Color.WHITE);
        title.setBorder(new EmptyBorder(10, 10, 10, 10)); // Add margin
        panel.add(title, BorderLayout.NORTH);

        ImageIcon icon = createImageIcon("dumpling.png", "Dumpling");
        ImageIcon resizedIcon = resizeIcon(icon, 300, 300); // Adjust dimensions as needed
        JLabel commentLabel = new JLabel("", resizedIcon, JLabel.CENTER);
        panel.add(commentLabel);

        JButton nextPageButton = new JButton("Start");
        nextPageButton.addActionListener(e -> cardLayout.show(cardPanel, "Page 2"));
        panel.add(nextPageButton, BorderLayout.SOUTH);

        return panel;
    }


    // Method to create Page 2
    private JPanel createPage2() {
        return new GraphPanel();
    }

    // Add more methods to create additional pages if needed
}

