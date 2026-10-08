package dev.kaldiroglu.dp.behavioral.templateMethod.hw.callcenter;

import java.util.ArrayList;
import java.util.List;

/**
 * Homework 1: bring a call center's call records into the company's own system.
 * <p>
 * The five steps are fixed: retrieve the metadata, store it, retrieve the audio, verify the
 * audio against the metadata, store the audio. Only the two "retrieve" steps depend on the
 * call center, so only they are abstract. {@link #run} is the template method and it is
 * {@code final}: no call center can skip the verification.
 */
public abstract class CallImport {

    private final List<String> stored = new ArrayList<>();
    private final List<String> rejected = new ArrayList<>();

    /** The template method. */
    public final void run() {
        List<CallRecord> records = retrieveMetadata();
        for (CallRecord record : records) {
            stored.add("metadata " + record.id());
        }
        for (CallRecord record : records) {
            Recording audio = retrieveAudio(record);
            if (verify(record, audio)) {
                stored.add("audio " + record.id());
            } else {
                rejected.add(record.id());
            }
        }
    }

    /** Depends on the call center. */
    protected abstract List<CallRecord> retrieveMetadata();

    /** Depends on the call center. */
    protected abstract Recording retrieveAudio(CallRecord record);

    /** Standard for every call center: the audio must be the call it claims to be. */
    private boolean verify(CallRecord record, Recording audio) {
        return audio != null && audio.callId().equals(record.id())
                && audio.seconds() == record.seconds();
    }

    public List<String> stored() {
        return List.copyOf(stored);
    }

    public List<String> rejected() {
        return List.copyOf(rejected);
    }
}
