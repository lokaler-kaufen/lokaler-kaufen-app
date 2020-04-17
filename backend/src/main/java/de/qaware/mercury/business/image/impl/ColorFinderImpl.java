package de.qaware.mercury.business.image.impl;

import de.qaware.mercury.business.image.Color;
import de.qaware.mercury.business.image.ColorDistanceUtil;
import de.qaware.mercury.business.image.ColorFindResult;
import de.qaware.mercury.business.image.ColorFinder;
import de.qaware.mercury.business.image.Image;
import de.qaware.mercury.business.image.analysis.ImageAnalyzer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.Map;

@Service
@Slf4j
public class ColorFinderImpl implements ColorFinder {
    private static final int RESIZE_RESOLUTION = 40;
    private static final double COLOR_DISTANCE_THRESHOLD = 1.5;

    private final ImageAnalyzer imageAnalyzer;

    public ColorFinderImpl(ImageAnalyzer imageAnalyzer) {
        this.imageAnalyzer = imageAnalyzer;
    }


    @Override
    public ColorFindResult getDominantColor(Image.Id imageId, InputStream data) {
        Map<Color, Integer> colorHistogram = imageAnalyzer.createImageHistogram(data, RESIZE_RESOLUTION);

        // todo this has to be its own component
        Map.Entry<Color, Integer> topColorEntry = colorHistogram.entrySet().stream().max(Map.Entry.comparingByValue()).orElseThrow();
        double confidence = calculateConfidence(colorHistogram, topColorEntry.getKey());
        ColorFindResult mostConfidentResult = findMostConfidentResult(colorHistogram);
        return ColorFindResult.of(topColorEntry.getKey().getHex(), confidence);
    }


    // todo this has to be its own component
    private double calculateConfidence(Map<Color, Integer> colorHistogram, Color color){
        double similarColors = colorHistogram.get(color);
        for (Color otherColor: colorHistogram.keySet()){
            if (color.equals(otherColor)){
                continue;
            }
            if (ColorDistanceUtil.getColorDistance(color, otherColor) < COLOR_DISTANCE_THRESHOLD){
                similarColors += colorHistogram.get(otherColor);
            }
        }
        return similarColors / colorHistogram.values().stream().reduce(Integer::sum).orElseThrow();
    }

    // todo this has to be its own component
    private ColorFindResult findMostConfidentResult(Map<Color, Integer> colorHistogram) {
        ColorFindResult bestMatch = ColorFindResult.of(null, -1);
        for (Color color : colorHistogram.keySet()) {
            double confidence = calculateConfidence(colorHistogram, color);
            if (confidence > bestMatch.getConfidence()) {
                bestMatch = ColorFindResult.of(color.getHex(), confidence);
            }
        }
        return bestMatch;
    }

}
