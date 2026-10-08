package dev.kaldiroglu.dp.behavioral.templateMethod.export.domain;

/** Someone who asks for an export. Only some users may export reports. */
public record User(String name, boolean mayExport) {
}
