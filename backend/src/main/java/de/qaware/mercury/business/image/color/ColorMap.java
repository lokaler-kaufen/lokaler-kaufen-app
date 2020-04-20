package de.qaware.mercury.business.image.color;

/**
 * A x,y based representation of an image, mapping the location to its color.
 * <p>
 * It can be dynamically filled and has the correct information about the images size at all times.
 */
public class ColorMap {

    private final Color[][] colors;
    private int width = -1;
    private int height = -1;
    private final int maxWidth;
    private final int maxHeight;

    /**
     * The constructor of the map.
     *
     * @param maxWidth  the maximum width the contained image can have
     * @param maxHeight the maximum height the contained image can have
     */
    public ColorMap(int maxWidth, int maxHeight) {
        colors = new Color[maxWidth][maxHeight];
        this.maxWidth = maxWidth;
        this.maxHeight = maxHeight;
    }

    /**
     * Set the color for a given image pixel.
     *
     * @param x     the x coordinate
     * @param y     the y coordinate
     * @param color The color of the pixel.
     */
    public void setColor(int x, int y, Color color) {
        if (x >= maxWidth || y >= maxHeight || x < 0 || y < 0) {
            throw new IllegalArgumentException("Coordinates must be at least 0 and less than maxWidth/maxHeight");
        }
        colors[x][y] = color;
        // width and height should be 1 based
        if (x > width - 1) {
            width = x + 1;
        }
        if (y > height - 1) {
            height = y + 1;
        }
    }

    /**
     * Get the color of the pixel at x,y.
     *
     * @param x the x coordinate
     * @param y the y coordinate
     * @return the color at the given coordinate
     */
    public Color getColor(int x, int y) {
        if (x > width - 1 || y > height - 1 || x < 0 || y < 0) {
            throw new IllegalArgumentException("Coordinates must be at least 0 and less than width/height");
        }
        return colors[x][y];
    }

    /**
     * Get the actual width of the contained image.
     *
     * @return the width of the contained image.
     */
    public int getWidth() {
        return width;
    }

    /**
     * Get the actual height of the contained image.
     *
     * @return the height of the contained image.
     */
    public int getHeight() {
        return height;
    }
}
