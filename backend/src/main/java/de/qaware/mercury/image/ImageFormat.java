package de.qaware.mercury.image;

public enum ImageFormat {
    JPEG(".jpeg"),
    PNG(".png");

    private final String extension;

    ImageFormat(String extension) {
        this.extension = extension;
    }

    public String getExtension() {
        return extension;
    }
}
