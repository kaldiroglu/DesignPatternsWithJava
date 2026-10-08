package dev.kaldiroglu.dp.behavioral.templateMethod.export.domain;

/** The user may not export reports. Nothing was made when this is thrown. */
public final class ExportNotAllowedException extends RuntimeException {

    public ExportNotAllowedException(User user) {
        super(user.name() + " may not export reports");
    }
}
