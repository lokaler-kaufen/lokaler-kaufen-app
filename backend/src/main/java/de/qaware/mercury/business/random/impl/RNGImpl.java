package de.qaware.mercury.business.random.impl;

import de.qaware.mercury.business.random.RNG;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.concurrent.ThreadLocalRandom;

@Service
class RNGImpl implements RNG {
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    @Override
    @SuppressWarnings({"java:S2068", "java:S2245"})
    // Shut up SonarQube. If you need crypto-safe random, use nextBytesSecure()
    public byte[] nextBytes(int size) {
        byte[] result = new byte[size];
        ThreadLocalRandom.current().nextBytes(result);
        return result;
    }

    @Override
    public byte[] nextBytesSecure(int size) {
        byte[] result = new byte[size];
        SECURE_RANDOM.nextBytes(result);
        return result;
    }
}
