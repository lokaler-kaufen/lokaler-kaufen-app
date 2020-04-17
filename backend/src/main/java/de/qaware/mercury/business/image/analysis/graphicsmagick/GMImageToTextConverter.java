package de.qaware.mercury.business.image.analysis.graphicsmagick;

import java.io.InputStream;

public interface GMImageToTextConverter {
    String convertImageToText(InputStream image, int targetSize);
}
