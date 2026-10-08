package dev.kaldiroglu.dp.behavioral.templateMethod.hw.recordfile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

/**
 * Homework 3: read a file of records, one record per line.
 * <p>
 * Opening, reading line by line and closing are the same for every kind of record; only
 * {@link #parse} differs. The template method also gives a guarantee: the reader is closed
 * even when {@code parse} fails on a bad line. A subclass cannot forget to close it,
 * because it never sees the reader.
 */
public abstract class RecordFileReader<T> {

    /** The template method. */
    public final List<T> readAll(Reader source) throws IOException {
        List<T> records = new ArrayList<>();
        try (BufferedReader lines = new BufferedReader(source)) {
            String line;
            while ((line = lines.readLine()) != null) {
                if (!line.isBlank()) {
                    records.add(parse(line));
                }
            }
        }
        return records;
    }

    /** A primitive operation: turn one line into one record. */
    protected abstract T parse(String line);
}
