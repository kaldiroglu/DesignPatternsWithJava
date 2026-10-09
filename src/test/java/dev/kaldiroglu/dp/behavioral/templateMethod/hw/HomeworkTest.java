package dev.kaldiroglu.dp.behavioral.templateMethod.hw;

import dev.kaldiroglu.dp.behavioral.templateMethod.hw.callcenter.AnkaraCallCenter;
import dev.kaldiroglu.dp.behavioral.templateMethod.hw.callcenter.CallImport;
import dev.kaldiroglu.dp.behavioral.templateMethod.hw.callcenter.IstanbulCallCenter;
import dev.kaldiroglu.dp.behavioral.templateMethod.hw.callcenter.IzmirCallCenter;
import dev.kaldiroglu.dp.behavioral.templateMethod.hw.onboarding.ContractorOnboarding;
import dev.kaldiroglu.dp.behavioral.templateMethod.hw.onboarding.EmployeeOnboarding;
import dev.kaldiroglu.dp.behavioral.templateMethod.hw.recordfile.Customer;
import dev.kaldiroglu.dp.behavioral.templateMethod.hw.recordfile.CustomerFileReader;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/** The worked solutions of the Template Method homework, and what the speaker notes say about them. */
class HomeworkTest {

    // ------------------------------------------------------------ 1 · three call centers

    @Test
    @DisplayName("a call center whose audio matches stores every metadata and audio record")
    void istanbulStoresEverything() {
        CallImport istanbul = new IstanbulCallCenter();
        istanbul.run();

        assertEquals(List.of("metadata IST-1", "metadata IST-2", "audio IST-1", "audio IST-2"),
                istanbul.stored());
        assertEquals(List.of(), istanbul.rejected());
    }

    @Test
    @DisplayName("a recording that is too short is rejected by the verification step")
    void ankaraShortRecordingIsRejected() {
        CallImport ankara = new AnkaraCallCenter();
        ankara.run();

        assertEquals(List.of("ANK-2"), ankara.rejected());
        assertEquals(List.of("metadata ANK-1", "metadata ANK-2", "audio ANK-1"), ankara.stored());
    }

    @Test
    @DisplayName("a call center with no calls stores nothing")
    void izmirHasNoCalls() {
        CallImport izmir = new IzmirCallCenter();
        izmir.run();

        assertEquals(List.of(), izmir.stored());
    }

    @Test
    @DisplayName("verification is private and the template method is final, so no call center can skip it")
    void verificationCannotBeSkipped() throws Exception {
        Method run = CallImport.class.getDeclaredMethod("run");
        Method verify = java.util.Arrays.stream(CallImport.class.getDeclaredMethods())
                .filter(m -> m.getName().equals("verify")).findFirst().orElseThrow();

        assertTrue(Modifier.isFinal(run.getModifiers()));
        assertTrue(Modifier.isPrivate(verify.getModifiers()));
    }

    // ------------------------------------------------------------ 2 · a first day

    @Test
    @DisplayName("the equipment hook gives an employee a laptop, and a contractor overrides it to give nothing")
    void theEquipmentHook() {
        assertEquals(List.of("e-mail for Elif", "payroll for Elif", "laptop for Elif",
                        "mentor for Elif", "welcome message to Elif"),
                new EmployeeOnboarding().start("Elif"));
        assertEquals(List.of("e-mail for Mert", "mentor for Mert", "welcome message to Mert"),
                new ContractorOnboarding().start("Mert"));
    }

    // ------------------------------------------------------------ 3 · a file of records

    @Test
    @DisplayName("the record reader turns each line into a record and skips blank lines")
    void readsEveryLine() throws IOException {
        List<Customer> customers = new CustomerFileReader()
                .readAll(new StringReader("Ayse;Istanbul\n\nDeniz ; Izmir\n"));

        assertEquals(List.of(new Customer("Ayse", "Istanbul"), new Customer("Deniz", "Izmir")), customers);
    }

    @Test
    @DisplayName("the reader is closed even when a line fails to parse")
    void theReaderIsClosedWhenParsingFails() {
        class TrackingReader extends StringReader {
            boolean closed;

            TrackingReader(String text) {
                super(text);
            }

            @Override
            public void close() {
                closed = true;
                super.close();
            }
        }
        TrackingReader source = new TrackingReader("Ayse;Istanbul\nnot a customer\n");

        assertThrows(IllegalArgumentException.class, () -> new CustomerFileReader().readAll(source));
        assertTrue(source.closed);
    }
}
