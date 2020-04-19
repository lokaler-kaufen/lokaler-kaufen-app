package de.qaware.mercury.business.image.color.analysis.graphicsmagick.impl;

import de.qaware.mercury.business.image.ImageException;
import de.qaware.mercury.business.image.color.Color;
import de.qaware.mercury.business.image.color.ColorMap;
import de.qaware.mercury.business.image.color.analysis.graphicsmagick.GMTextOutputParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
public class GMTextOutputParserImpl implements GMTextOutputParser {

    // Example of parsed output: "1,0: ( 78,175, 52) #4EAF34"
    private static final String PIXEL_REGEX = "^(?<x>[0-9]+),(?<y>[0-9]+): \\([ ]*(?<r>[0-9]+),[ ]*(?<g>[0-9]+),[ ]*(?<b>[0-9]+)\\) (?<hex>#[0-9A-F]*)$";
    private static final Pattern PATTERN = Pattern.compile(PIXEL_REGEX);

    @Override
    public ColorMap parseGMTextOutput(String gmTextOutput) {
        log.debug("Parsing GM output.");
        // Split in lines
        String[] lines = gmTextOutput.split("\\r?\\n");

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
        log.debug("Parsed {} lines.", lines.length);
        return colorMap;
    }
}
