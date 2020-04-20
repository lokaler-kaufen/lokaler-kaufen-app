package de.qaware.mercury.business.image.color.analysis.impl;

import de.qaware.mercury.business.image.color.Color;
import de.qaware.mercury.business.image.color.ColorMap;
import de.qaware.mercury.business.image.color.analysis.ColorHistogramCreator;
import de.qaware.mercury.business.image.color.analysis.graphicsmagick.GMImageToTextConverter;
import de.qaware.mercury.business.image.color.analysis.graphicsmagick.GMTextOutputParser;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * This ImageAnalyzer implementation creates a histogram of the image by only looking at
 * the border of the image. The border is most relevant if you wish to embed the image in
 * a matching background color.
 */
@Service
public class BorderImageAnalyzerImpl implements ColorHistogramCreator {

    private final GMImageToTextConverter gmImageToTextConverter;
    private final GMTextOutputParser gmTextOutputParser;

    public BorderImageAnalyzerImpl(GMImageToTextConverter gmImageToTextConverter, GMTextOutputParser gmTextOutputParser) {
        this.gmImageToTextConverter = gmImageToTextConverter;
        this.gmTextOutputParser = gmTextOutputParser;
    }

    @Override
    public Map<Color, Integer> createImageHistogram(InputStream inputStream, int analysisSize) {
        String imageText = gmImageToTextConverter.convertImageToText(inputStream, analysisSize);
        ColorMap colorMap = gmTextOutputParser.parseGMTextOutput(imageText);
        return createImageHistogramFromColorMap(colorMap);
    }

    private Map<Color, Integer> createImageHistogramFromColorMap(ColorMap colorMap) {
        Map<Color, Integer> colorHistogram = new HashMap<>();
        int maxX = colorMap.getWidth() - 1;
        int maxY = colorMap.getHeight() - 1;

        // let's walk around the border:

        // top side left to right
        for (int x = 0; x <= maxX; x++) {
            // increment the color count. if the value does not yet exist, assume it is 0.
            colorHistogram.put(colorMap.getColor(x, 0), colorHistogram.getOrDefault(colorMap.getColor(x, 0), 0) + 1);
        }

        // right side up to down, omitting the first value which was already counted.
        for (int y = 1; y <= maxY; y++) {
            colorHistogram.put(colorMap.getColor(maxX, y), colorHistogram.getOrDefault(colorMap.getColor(maxX, y), 0) + 1);
        }

        // down side left to right (omitting the lower right corner we already counted)
        for (int x = 0; x <= maxX - 1; x++) {
            colorHistogram.put(colorMap.getColor(x, maxY), colorHistogram.getOrDefault(colorMap.getColor(x, maxY), 0) + 1);
        }

        // left side up to down (omitting the upper left and lower left corner we already counted)
        for (int y = 1; y <= maxY - 1; y++) {
            colorHistogram.put(colorMap.getColor(0, y), colorHistogram.getOrDefault(colorMap.getColor(0, y), 0) + 1);
        }
        return colorHistogram;
    }
}
