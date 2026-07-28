package dev.kaldiroglu.dp.structural.proxy.hw.vault;

/** The Subject: a document that can be read, edited and closed. */
public interface Document {

    String read();

    void write(String text);

    void close();
}
