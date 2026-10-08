package dev.kaldiroglu.dp.behavioral.templateMethod.hw.callcenter;

import java.util.List;

/** The third call center. It has no calls today. */
public final class IzmirCallCenter extends CallImport {

    @Override
    protected List<CallRecord> retrieveMetadata() {
        return List.of();
    }

    @Override
    protected Recording retrieveAudio(CallRecord record) {
        return new Recording(record.id(), record.seconds());
    }
}
