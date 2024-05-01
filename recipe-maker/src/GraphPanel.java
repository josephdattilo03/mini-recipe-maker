import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;

import static javax.swing.JOptionPane.showMessageDialog;

public class GraphPanel extends JPanel {
    private ArrayList<Square> squares = new ArrayList<>();
    private int draggingSquare = -1;
    private Point arrowStart;
    private Point arrowEnd;
    private Square arrowStartSquare;
    private Square arrowEndSquare;
    private boolean drawingArrow = false;
    private final JTextField titleField;
    private final JTextArea descriptionArea;
    private final JTextArea recipeTitleArea;
    private final JTextArea recipeDescriptionArea;

    public GraphPanel() {
        JButton addButton = new JButton("Add Step");
        addButton.addActionListener(e -> addNewSquare());

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
        inputPanel.add(addButton);

        titleField = new JTextField("Enter Title");
        inputPanel.add(titleField);

        descriptionArea = new JTextArea("Enter Description");
        descriptionArea.setLineWrap(true);
        descriptionArea.setRows(4);
        inputPanel.add(descriptionArea);

        setBackground(new Color(173, 216, 230));
        add(inputPanel, BorderLayout.NORTH);

        JPanel outputPanel = new JPanel();
        JButton submitButton = new JButton("Write to File");
        submitButton.addActionListener(e -> convertToFile());
        this.recipeTitleArea = new JTextArea("Recipe Title");
        outputPanel.add(recipeTitleArea);
        this.recipeDescriptionArea = new JTextArea("Recipe Description");
        recipeDescriptionArea.setRows(4);
        outputPanel.add(recipeDescriptionArea);
        outputPanel.add(submitButton);
        outputPanel.setLayout(new BoxLayout(outputPanel, BoxLayout.Y_AXIS));
        add(outputPanel, BorderLayout.SOUTH);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                for (int i = 0; i < squares.size(); i++) {
                    if (isInsideDeleteButton(squares.get(i), e.getPoint())) {
                        Square deletedSq = squares.get(i);
                        squares.remove(i);
                        for (Square sq : squares)  {
                            if (sq.getArrowsTo().contains(deletedSq)) {
                                sq.getArrowsTo().remove(deletedSq);
                            }
                        }
                        repaint();
                        return;
                    }
                    if (isInsideSquare(squares.get(i), e.getPoint())) {
                        startDragging(i);
                        break;
                    }
                }
                if (isInsideGrayCorner(e.getPoint()) != null) {
                    drawingArrow = true;
                    arrowStart = e.getPoint();
                    arrowStartSquare = isInsideGrayCorner(e.getPoint());
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                stopDragging();
                if (drawingArrow) {
                    for (Square square : squares) {
                        if (isInsideGrayCorner(e.getPoint()) != null) {
                            arrowEndSquare = isInsideGrayCorner(e.getPoint());
                            if (!arrowEndSquare.equals(arrowStartSquare)) {
                                arrowStartSquare.addArrowTo(arrowEndSquare);
                            }
                            break;
                        }
                    }
                    drawingArrow = false;
                    repaint();
                }
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
                if (draggingSquare != -1) {
                    squares.get(draggingSquare).setPosition(
                            new Point(e.getX() - 100, e.getY()));
                    repaint();
                }
                if (drawingArrow) {
                    arrowEnd = e.getPoint();
                    repaint();
                }
            }
        });
    }

    private void convertToFile() {
        if (squares.size() < 1) {
            return;
        }
        HashMap<Square, ArrayList<Square>> graph = new HashMap<>();
        for (Square square : squares) {
            graph.put(square, square.getArrowsTo());
        }
        try {
            Graph outputToFile = new Graph(graph, squares.get(0));
            outputToFile.txtFile(recipeTitleArea.getText(), recipeDescriptionArea.getText());
            if (!outputToFile.graphHasImage()) {
                showMessageDialog(null, "No image found to associate with this recipe, outputting text file anyways. ");
            }
            showMessageDialog(null, "Recipe created");
        } catch (IllegalArgumentException e) {
            showMessageDialog(null, "Current graph contains a cycle!");
        }
    }

    private void addNewSquare() {
        int x = 50;
        int y = 300;
        String title = titleField.getText();
        String description = descriptionArea.getText();
        Square square = new Square(x, y, title, description);
        square.calculateSize(getGraphics());
        squares.add(square);
        repaint();
    }

    private void startDragging(int squareIndex) {
        draggingSquare = squareIndex;
    }

    private void stopDragging() {
        draggingSquare = -1;
    }

    private boolean isInsideSquare(Square square, Point point) {
        return square.contains(point);
    }

    private boolean isInsideDeleteButton(Square square, Point point) {
        int deleteButtonSize = 15;
        return point.x >= square.getPosition().x + square.getWidth() - deleteButtonSize &&
                point.x <= square.getPosition().x + square.getWidth() &&
                point.y >= square.getPosition().y &&
                point.y <= square.getPosition().y + deleteButtonSize;
    }

    private Square isInsideGrayCorner(Point point) {
        for (Square square : squares) {
            if (point.x >= square.getPosition().x - 10 && point.x <= square.getPosition().x + 10 &&
                    point.y >= square.getPosition().y - 10 && point.y <= square.getPosition().y + 10) {
                return square;
            }
        }
        return null;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (Square square : squares) {
            square.draw(g);
            for (Square sq : square.getArrowsTo()) {
                drawArrow(g, square.getPosition(), sq.getPosition());
            }
        }
        if (drawingArrow) {
            drawArrow(g, arrowStart, arrowEnd);
        }
    }

    private void drawArrow(Graphics g, Point start, Point end) {

        int midX = (start.x + end.x) / 2;
        int midY = (start.y + end.y) / 2;


        double angle = Math.atan2(end.y - start.y, end.x - start.x);


        int arrowSize = 12;
        int x1 = (int) (midX - arrowSize * Math.cos(angle - Math.PI / 6));
        int y1 = (int) (midY - arrowSize * Math.sin(angle - Math.PI / 6));
        int x2 = (int) (midX - arrowSize * Math.cos(angle + Math.PI / 6));
        int y2 = (int) (midY - arrowSize * Math.sin(angle + Math.PI / 6));


        g.setColor(Color.BLACK);
        g.drawLine(start.x, start.y, end.x, end.y);


        g.fillPolygon(new int[]{midX, x1, x2}, new int[]{midY, y1, y2}, 3);
    }


}
