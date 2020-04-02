package de.qaware.mercury.cli;

public interface Console {
    void print(String text);

    void printLine(String text);

    String readLine();

    String readPassword();

    boolean readBoolean(boolean theDefault);
}
