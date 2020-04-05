package de.qaware.mercury.test.uuid;

import de.qaware.mercury.business.uuid.UUIDFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TestUUIDFactory implements UUIDFactory {
    private final List<UUID> generatedUUIDs = new ArrayList<>();

    @Override
    public UUID create() {
        UUID uuid = UUID.randomUUID();
        generatedUUIDs.add(uuid);

        return uuid;
    }

    public List<UUID> getGeneratedUUIDs() {
        return generatedUUIDs;
    }
}
