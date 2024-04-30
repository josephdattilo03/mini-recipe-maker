import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class GraphPanel extends JPanel {
    private ArrayList<Square> squares = new ArrayList<>();
    private int draggingSquare = -1;
    private Point arrowStart;
    private Point arrowEnd;

    private Square arrowStartSquare;
    private Square arrowEndSquare;
    private boolean drawingArrow = false; // Flag to indicate if arrow is being drawn

    private JTextField titleField;
    private JTextArea descriptionArea;

    public GraphPanel() {
        JButton addButton = new JButton("Add Square");
        addButton.addActionListener(e -> addNewSquare());

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
        inputPanel.add(addButton);

        titleField = new JTextField("Title");
        inputPanel.add(titleField);

        descriptionArea = new JTextArea("Description");
        descriptionArea.setLineWrap(true);
        descriptionArea.setRows(4);
        inputPanel.add(descriptionArea);

        setBackground(new Color(173, 216, 230));
        add(inputPanel);

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
                // Check if clicked in the gray corner of a square to start drawing arrow
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
                // If arrow was being drawn, find square mouse released over and set arrow end
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

    private void addNewSquare() {
        int x = 50;
        int y = 50;
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
                drawArrow(g, square.position, sq.position);
            }
        }
        if (drawingArrow) {
            drawArrow(g, arrowStart, arrowEnd);
        }

    }

    private void drawArrow(Graphics g, Point start, Point end) {

        // stack exchange for arrow graphic
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawLine(start.x, start.y, end.x, end.y);


        int arrowSize = 8;
        double angle = Math.atan2(end.y - start.y, end.x - start.x);
        int x1 = (int) (end.x - arrowSize * Math.cos(angle - Math.PI / 6));
        int y1 = (int) (end.y - arrowSize * Math.sin(angle - Math.PI / 6));
        int x2 = (int) (end.x - arrowSize * Math.cos(angle + Math.PI / 6));
        int y2 = (int) (end.y - arrowSize * Math.sin(angle + Math.PI / 6));

        g2d.fillPolygon(new int[]{end.x, x1, x2}, new int[]{end.y, y1, y2}, 3);
        g2d.dispose();
    }

    private class Square {
        private Point position;
        private String title;
        private String description;
        private ArrayList<Square> arrowsTo;
        private ArrayList<Square> arrowsFrom;
        private int width;
        private int height;

        public Square(int x, int y, String title, String description) {
            this.position = new Point(x, y);
            this.title = title;
            this.description = description;
            this.arrowsTo = new ArrayList<>();
            this.arrowsFrom = new ArrayList<>();
        }

        public void addArrowTo(Square sq) {
            this.arrowsTo.add(sq);
        }


        public ArrayList<Square> getArrowsTo() {
            return this.arrowsTo;
        }

        public Point getPosition() {
            return position;
        }

        public void setPosition(Point position) {
            this.position = position;
        }

        public void calculateSize(Graphics g) {
            FontMetrics titleMetrics = g.getFontMetrics();
            width = 200;
            int descriptionWidth = width - 10;
            int numLines = (int) Math.ceil((double) description.length() * g.getFontMetrics().getHeight() / descriptionWidth);
            height = (titleMetrics.getHeight() / 2 * numLines) + 50;
        }

        public boolean contains(Point point) {
            return point.x >= position.x && point.x <= position.x + width &&
                    point.y >= position.y && point.y <= position.y + height;
        }

        public void draw(Graphics g) {
            g.setColor(new Color(203, 203, 203));
            g.fillRect(position.x, position.y, width, height);

            // Draw gray button in the upper left
            g.setColor(Color.LIGHT_GRAY);
            g.fillRect(position.x - 10, position.y - 10, 20, 20);

            // Draw delete button in the upper right
            g.setColor(Color.RED);
            g.fillOval(position.x + width - 15, position.y, 15, 15);

            // Draw title and description
            g.setColor(Color.BLACK);
            g.drawString(title, position.x + 5, position.y + g.getFontMetrics().getHeight());
            drawWrappedText(g, description, position.x + 5, position.y + g.getFontMetrics().getHeight() * 2, width - 10);
        }

        private void drawWrappedText(Graphics g, String text, int x, int y, int maxWidth) {
            int lineHeight = g.getFontMetrics().getHeight();
            int start = 0;
            for (int i = 0; i < text.length(); i++) {
                if (g.getFontMetrics().stringWidth(text.substring(start, i)) > maxWidth) {
                    g.drawString(text.substring(start, i), x, y);
                    y += lineHeight;
                    start = i;
                }
            }
            if (start < text.length()) {
                g.drawString(text.substring(start), x, y);
            }
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }
    }
}
