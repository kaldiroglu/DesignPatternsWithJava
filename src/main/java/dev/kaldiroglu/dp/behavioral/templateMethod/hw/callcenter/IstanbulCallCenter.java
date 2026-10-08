package dev.kaldiroglu.dp.behavioral.templateMethod.hw.callcenter;

import java.util.List;

/** One call center. In a real system the two methods would call its web service. */
public final class IstanbulCallCenter extends CallImport {

    @Override
    protected List<CallRecord> retrieveMetadata() {
        return List.of(new CallRecord("IST-1", 120), new CallRecord("IST-2", 45));
    }

    @Override
    protected Recording retrieveAudio(CallRecord record) {
        return new Recording(record.id(), record.seconds());
    }
}
