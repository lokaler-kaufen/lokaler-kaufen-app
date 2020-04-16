package de.qaware.mercury.business.image;

public class ColorMap {
    private final Color[][] colors;
    private int width = -1;
    private int height = -1;
    private final int maxX;
    private final int maxY;

    public ColorMap(int maxX, int maxY) {
        colors = new Color[maxX][maxY];
        this.maxX = maxX;
        this.maxY = maxY;
    }

    public void setColor(int x, int y, Color color) {
        if (x > maxX || y > maxY || x < 0 || y < 0) {
            throw new IllegalArgumentException("Coordinates must be between 0 and maxX/maxY");
        }
        colors[x][y] = color;
        // width and height should be 1 based
        if (x >= width - 1) {
            width = x + 1;
        }
        if (y >= height - 1) {
            height = y + 1;
        }
    }

    public Color getColor(int x, int y) {
        if (x > width - 1 || y > height - 1 || x < 0 || y < 0) {
            throw new IllegalArgumentException("Coordinates must be at least 0 and less than width/height");
        }
        return colors[x][y];
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
