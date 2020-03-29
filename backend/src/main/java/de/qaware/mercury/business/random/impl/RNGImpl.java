package de.qaware.mercury.business.random.impl;

import de.qaware.mercury.business.random.RNG;
import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;

@Service
class RNGImpl implements RNG {
    @Override
    public byte[] nextBytes(int size) {
        byte[] result = new byte[size];
        ThreadLocalRandom.current().nextBytes(result);
        return result;
    }
}
