package dev.kaldiroglu.dp.behavioral.templateMethod.hw.callcenter;

import java.util.List;

/**
 * A second call center. Its audio for one call is cut short, so the standard verification
 * step rejects it — the call center's code cannot skip that step.
 */
public final class AnkaraCallCenter extends CallImport {

    @Override
    protected List<CallRecord> retrieveMetadata() {
        return List.of(new CallRecord("ANK-1", 300), new CallRecord("ANK-2", 60));
    }

    @Override
    protected Recording retrieveAudio(CallRecord record) {
        int seconds = record.id().equals("ANK-2") ? 30 : record.seconds();
        return new Recording(record.id(), seconds);
    }
}
