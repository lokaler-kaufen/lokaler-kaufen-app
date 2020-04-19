package de.qaware.mercury.business.image.color.analysis.graphicsmagick;

import de.qaware.mercury.business.image.color.ColorMap;

/**
 * GraphicsMagick can convert images to a textual view, which is essentially a list of every pixel in the picture.
 * <p>
 * An entry looks like this: 1,0: ( 78,175, 52) #4EAF34
 * The semantic of the entry is: x,y (r,g,b) hex-value
 * <p>
 * A class implementing this interface converts the raw GraphicsMagick output to a ColorMap.
 */
public interface GMTextOutputParser {

    /**
     * Converts a given raw output of GraphicsMagick's text view to a ColorMap.
     *
     * @param gmTextOutput The raw output of GraphicsMagick, in the form of "1,0: ( 78,175, 52) #4EAF34"
     *                     Entries in the output are to be separated by a newline.
     * @return The color map representation of the given gmTextOutput.
     */
    ColorMap parseGMTextOutput(String gmTextOutput);
}
