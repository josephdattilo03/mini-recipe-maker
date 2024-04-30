import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class GraphPanel extends JPanel {
    private int squareSize = 50;
    private ArrayList<Square> squares = new ArrayList<>();
    private int draggingSquare = 0;

    public GraphPanel() {
        // Add two initial squares
        squares.add(new Square(50, 50));
        squares.add(new Square(200, 200));

        JButton addButton = new JButton("Add Square");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addNewSquare();
            }
        });
        add(addButton);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                for (int i = 0; i < squares.size(); i++) {
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
                if (draggingSquare != 0) {
                    squares.get(draggingSquare - 1).setPosition(e.getPoint());
                    repaint();
                }
            }
        });
    }

    private void addNewSquare() {
        // Add a new square to the right of the last square
        int lastX = squares.get(squares.size() - 1).getPosition().x;
        int newX = lastX + squareSize + 20; // Example: Add new square to the right of the last square
        int newY = 50; // You can modify this according to your requirements
        squares.add(new Square(newX, newY));
        repaint();
    }

    private void startDragging(Point point, int squareIndex) {
        draggingSquare = squareIndex + 1; // Add 1 to squareIndex since ArrayList index starts from 0
    }

    private void stopDragging() {
        draggingSquare = 0;
    }

    private boolean isInsideSquare(Square square, Point point) {
        return point.x >= square.getPosition().x && point.x <= square.getPosition().x + squareSize &&
                point.y >= square.getPosition().y && point.y <= square.getPosition().y + squareSize;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (Square square : squares) {
            g.setColor(Color.BLUE);
            g.fillRect(square.getPosition().x, square.getPosition().y, squareSize, squareSize);
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
