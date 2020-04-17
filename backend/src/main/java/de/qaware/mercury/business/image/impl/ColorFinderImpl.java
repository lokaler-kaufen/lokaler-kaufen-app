package de.qaware.mercury.business.image.impl;

import antlr.preprocessor.PreprocessorTokenTypes;
import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import de.qaware.mercury.business.image.Color;
import de.qaware.mercury.business.image.ColorDistanceUtil;
import de.qaware.mercury.business.image.ColorFindResult;
import de.qaware.mercury.business.image.ColorFinder;
import de.qaware.mercury.business.image.ColorMap;
import de.qaware.mercury.business.image.Image;
import de.qaware.mercury.business.image.ImageException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
public class ColorFinderImpl implements ColorFinder {
    private static final int EXIT_CODE_SUCCESS = 0;
    private static final int RESIZE_RESOLUTION = 40;
    private static final String PIXEL_REGEX = "^(?<x>[0-9]+),(?<y>[0-9]+): \\([ ]*(?<r>[0-9]+),[ ]*(?<g>[0-9]+),[ ]*(?<b>[0-9]+)\\) (?<hex>#[0-9A-F]*)$";
    private static final Pattern PATTERN = Pattern.compile(PIXEL_REGEX);
    private static final double COLOR_DISTANCE_THRESHOLD = 1.5;

    @Override
    public ColorFindResult getDominantColor(Image.Id imageId, InputStream data) {
        try {
            return scaleImpl(data);
        } catch (IOException e) {
            throw new ImageException(String.format("Failed to scale image '%s'", imageId), e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new ImageException(String.format("Got interrupted while scaling image '%s'", imageId), e);
        }
    }


    private ColorFindResult scaleImpl(InputStream data) throws IOException, InterruptedException {
        String resolution = String.format("%dx%d", RESIZE_RESOLUTION, RESIZE_RESOLUTION);
        String[] args = {
            "gm", "convert", "-background", "white", "-extent", "0x0", "+matte", "-size", resolution, "-", "-resize", resolution, "-depth", "8", "txt:-"
        };

        // Start GraphicsMagick
        String commandLine = String.join(" ", args);
        log.debug("Starting process '{}'", commandLine);
        Process process = new ProcessBuilder(args).start();

        // We pipe the data stream directly into the stdin of GraphicsMagick
        log.debug("Piping image to GraphicsMagick ...");
        // process.getOutputStream() returns the stdin of the process, wtf
        long transferred = data.transferTo(process.getOutputStream());
        process.getOutputStream().close();
        log.debug("Done, piped {} bytes", transferred);

        // Now wait for GraphicsMagick to quit
        log.debug("Waiting for GraphicsMagick to exit ...");
        int exitCode = process.waitFor();
        log.debug("GraphicsMagick exited with code {}", exitCode);
        if (exitCode != EXIT_CODE_SUCCESS) {
            throw new ImageException(String.format("Failed to run process '%s', exit code: %d", commandLine, exitCode));
        }
        String graphicsMagicOutput = CharStreams.toString(new InputStreamReader(process.getInputStream(), Charsets.UTF_8));
        ColorMap colorMap = parseColorMap(graphicsMagicOutput);
        ColorFindResult color = getDominantColor(colorMap);

        // This input stream makes sure that the file gets deleted when the stream is closed
        return ColorFindResult.of("#123456", 1.0);
    }

    private ColorMap parseColorMap(String gmOutput) {
        // Split in lines
        String[] lines = gmOutput.split("\\r?\\n");

        ColorMap colorMap = new ColorMap(lines.length, lines.length);

        for (String line : lines) {
            Matcher matcher = PATTERN.matcher(line);
            if (!matcher.matches()) {
                throw new ImageException(String.format("Could not parse output of GM: '%s'", line));
            }
            try {
                int x = Integer.parseInt(matcher.group("x"));
                int y = Integer.parseInt(matcher.group("y"));
                int r = Integer.parseInt(matcher.group("r"), 10);
                int g = Integer.parseInt(matcher.group("g"), 10);
                int b = Integer.parseInt(matcher.group("b"), 10);
                Color color = Color.of(r, g, b, matcher.group("hex"));
                colorMap.setColor(x, y, color);
            } catch (NumberFormatException e) {
                throw new ImageException(String.format("Cannot parse value from GM output: '%s'", line), e);
            }
        }
        return colorMap;
    }

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

    private ColorFindResult getDominantColor(ColorMap colorMap) {
        Map<Color, Integer> colorHistogram = new HashMap<>();
        int maxX = colorMap.getWidth() - 1;
        int maxY = colorMap.getHeight() - 1;
        // let's walk around the border.

        // top side left to right
        for (int x = 0; x <= maxX; x++) {
            colorHistogram.put(colorMap.getColor(x, 0), colorHistogram.getOrDefault(colorMap.getColor(x, 0), 0) + 1);
        }

        // right side up to down
        for (int y = 1; y <= maxY; y++) {
            colorHistogram.put(colorMap.getColor(maxX, y), colorHistogram.getOrDefault(colorMap.getColor(maxX, y), 0) + 1);
        }

        // down side left to right (omitting the lower right corner we already counted)
        for (int x = 0; x <= maxX; x++) {
            colorHistogram.put(colorMap.getColor(x, maxY), colorHistogram.getOrDefault(colorMap.getColor(x, maxY), 0) + 1);

        }

        // left side up to down (omitting the upper left and lower left corner we already counted)
        for (int y = 1; y <= maxY - 1; y++) {
            colorHistogram.put(colorMap.getColor(0, y), colorHistogram.getOrDefault(colorMap.getColor(0, y), 0) + 1);

        }
        Map.Entry<Color, Integer> topColorEntry = colorHistogram.entrySet().stream().max(Map.Entry.comparingByValue()).orElseThrow();

        double confidence = calculateConfidence(colorHistogram, topColorEntry.getKey());

        ColorFindResult mostConfidentResult = findMostConfidentResult(colorHistogram);
        return ColorFindResult.of(topColorEntry.getKey().getHex(), confidence);
    }


}
