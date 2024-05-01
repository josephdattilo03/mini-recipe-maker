import java.awt.*;
import java.util.ArrayList;

public class Square {
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

    public String getTitle() {
        return this.title;
    }

    public String getDescription() {
        return this.description;
    }
}