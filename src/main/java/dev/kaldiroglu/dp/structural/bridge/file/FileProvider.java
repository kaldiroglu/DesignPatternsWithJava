package dev.kaldiroglu.dp.structural.bridge.file;

import java.util.List;

/**
 * The Implementor: the primitives a document store offers.
 * <p>
 * <strong>Not an adapter.</strong> The earlier names here were {@code FileProviderAdaptor},
 * {@code EvernoteAdaptor} and so on, and in a course that also teaches Adapter that naming
 * does real damage: an adapter makes an <em>existing, incompatible</em> interface fit one you
 * already have, after the fact. This interface was designed up front, alongside
 * {@link FileManager}, so that the two hierarchies could vary independently. That is Bridge.
 * <p>
 * Note also what these methods are. Not {@code readFile}/{@code writeFile}/{@code updateFile}
 * mirroring the manager one-for-one — that would be the same interface written twice, and a
 * new manager operation would force every provider to grow. These are storage primitives, and
 * the managers compose them into whatever their department needs.
 */
public interface FileProvider {

    String name();

    /** Opens a document and returns a handle, creating it if it does not exist. */
    String open(String path);

    byte[] read(String handle);

    /** Stores content as a new version and returns that version's number. */
    int write(String handle, byte[] content);

    /** Version numbers held for this document, oldest first. */
    List<Integer> versions(String handle);

    void deleteVersion(String handle, int version);
}
