package de.qaware.mercury.test.fixtures;

import de.qaware.mercury.business.image.Image;
import de.qaware.mercury.business.uuid.UUIDFactory;
import de.qaware.mercury.test.uuid.TestUUIDFactory;

public final class ImageIdFixture {
    private ImageIdFixture() {
    }

    public static Image.Id create() {
        return create(new TestUUIDFactory());
    }

    public static Image.Id create(UUIDFactory uuidFactory) {
        return Image.Id.random(uuidFactory);
    }
}
