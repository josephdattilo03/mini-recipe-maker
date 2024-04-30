import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class GraphPanel extends JPanel {
    private int squareSize = 50;
    private ArrayList<Square> squares = new ArrayList<>();
    private int draggingSquare = -1;

    public GraphPanel() {
        JButton addButton = new JButton("Add Square");
        addButton.addActionListener(e -> addNewSquare());
        setBackground(new Color(173, 216, 230));
        add(addButton);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                for (int i = 0; i < squares.size(); i++) {
                    if (isInsideDeleteButton(squares.get(i), e.getPoint())) {
                        squares.remove(i);
                        repaint();
                        return;
                    }
                    if (isInsideSquare(squares.get(i), e.getPoint())) {
                        startDragging(e.getPoint(), i);
                        break;
                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                stopDragging();
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
                if (draggingSquare != -1) {
                    int halfSize = squareSize / 2;
                    int newX = e.getX() - halfSize;
                    int newY = e.getY() - halfSize;
                    squares.get(draggingSquare).setPosition(new Point(newX, newY));
                    repaint();
                }
            }
        });
    }

    private void addNewSquare() {
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        squares.add(new Square(centerX - squareSize / 2, centerY - squareSize / 2));
        repaint();
    }

    private void startDragging(Point point, int squareIndex) {
        draggingSquare = squareIndex;
    }

    private void stopDragging() {
        draggingSquare = -1;
    }

    private boolean isInsideSquare(Square square, Point point) {
        int halfSize = squareSize / 2;
        return point.x >= square.getPosition().x && point.x <= square.getPosition().x + squareSize &&
                point.y >= square.getPosition().y && point.y <= square.getPosition().y + squareSize;
    }

    private boolean isInsideDeleteButton(Square square, Point point) {
        int deleteButtonSize = 15;
        return point.x >= square.getPosition().x + squareSize - deleteButtonSize &&
                point.x <= square.getPosition().x + squareSize &&
                point.y >= square.getPosition().y &&
                point.y <= square.getPosition().y + deleteButtonSize;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (Square square : squares) {
            g.setColor(new Color(203, 203, 203));
            g.fillRect(square.getPosition().x, square.getPosition().y, squareSize, squareSize);

            // Draw gray button in the upper left
            g.setColor(Color.LIGHT_GRAY);
            g.fillRect(square.getPosition().x, square.getPosition().y, squareSize / 3, squareSize / 3);

            // Draw delete button in the upper right
            g.setColor(Color.RED);
            g.fillOval(square.getPosition().x + squareSize - 15, square.getPosition().y, 15, 15);
        }
    }

    private class Square {
        private Point position;

        public Square(int x, int y) {
            this.position = new Point(x, y);
        }

        public Point getPosition() {
            return position;
        }

        public void setPosition(Point position) {
            this.position = position;
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Graph Panel");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(new GraphPanel());
        frame.setSize(400, 400);
        frame.setVisible(true);
    }
}

