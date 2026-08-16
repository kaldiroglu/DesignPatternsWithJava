package dev.kaldiroglu.dp.structural.bridge.file.problem;

/**
 * The other axis: which vendor actually holds the bytes.
 * <p>
 * Chosen by procurement, per site, and changed when a contract ends. Nothing about this list
 * is derived from {@link Department}, and nothing about {@code Department} is derived from
 * this one — which is the whole reason the two multiply.
 */
public enum Store {
    EVERNOTE, SHAREPOINT, FILENET
}
