package dev.kaldiroglu.dp.behavioral.templateMethod.export.domain;

/** The result of one export: a file name and its contents. */
public record Export(String fileName, String content) {
}
