import javax.swing.*;
import java.awt.*;

public class Scene {
    private JFrame frame;
    private JPanel cardPanel;
    private CardLayout cardLayout;

    public Scene() {
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        cardPanel = new JPanel();
        cardLayout = new CardLayout();
        cardPanel.setLayout(cardLayout);

        addPage(createPage1(), "Page 1");
        addPage(createPage2(), "Page 2");


        frame.add(cardPanel);


        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double width = screenSize.getWidth();
        double height = screenSize.getHeight();
        frame.setSize((int) width / 2, (int) height / 2);
        frame.setVisible(true);
    }


    private void addPage(Component page, String name) {
        cardPanel.add(page, name);
    }


    private JPanel createPage1() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // Load the image
        ImageIcon imageIcon = new ImageIcon("dumpling.png");

        // Create a JLabel to hold the image
        JLabel imageLabel = new JLabel(imageIcon);
        panel.add(imageLabel, BorderLayout.CENTER);

        JButton nextPageButton = new JButton("Next Page");
        nextPageButton.addActionListener(e -> cardLayout.show(cardPanel, "Page 2"));
        panel.add(nextPageButton, BorderLayout.SOUTH);

        return panel;
    }



    private JPanel createPage2() {
        JPanel panel = new JPanel();
        JButton previousPageButton = new JButton("Previous Page");
        previousPageButton.addActionListener(e -> cardLayout.show(cardPanel, "Page 1"));
        panel.add(previousPageButton);
        return panel;
    }

}

