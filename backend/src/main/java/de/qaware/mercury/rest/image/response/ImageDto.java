package de.qaware.mercury.rest.image.response;

import de.qaware.mercury.business.image.Image;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImageDto {
    private String id;

    public static ImageDto of(Image image) {
        return new ImageDto(image.getId().getId().toString());
    }
}
