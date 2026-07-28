package dev.kaldiroglu.dp.structural.facade.hw.reporting;

import java.util.List;

/** A subsystem class. */
public class PdfRenderer {

    public byte[] render(String title, List<String> rows) {
        return (title + "\n" + String.join("\n", rows)).getBytes();
    }
}
