package de.qaware.mercury.business.random;

public interface RNG {
    byte[] nextBytes(int size);

    byte[] nextBytesSecure(int size);
}
