package dev.kaldiroglu.dp.creational.introduction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.net.http.HttpClient;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import dev.kaldiroglu.dp.creational.introduction.bank.BankLoan;
import dev.kaldiroglu.dp.creational.introduction.bank.LoanRecord;
import dev.kaldiroglu.dp.creational.introduction.bank.LoanWithSetters;
import dev.kaldiroglu.dp.creational.introduction.bank.SecureLoan;
import dev.kaldiroglu.dp.creational.introduction.bank.ShortTerm;

/** The figures and behaviors the Introduction to Creational Patterns deck states. */
class ChapterFiguresTest {

    private static long instanceFields(Class<?> type) {
        return Arrays.stream(type.getDeclaredFields())
                .filter(f -> !Modifier.isStatic(f.getModifiers())).count();
    }

    private static long setters(Class<?> type) {
        return Arrays.stream(type.getDeclaredMethods()).map(Method::getName)
                .filter(n -> n.startsWith("set")).count();
    }

    @Test
    void theProblemEmployeeHasTwentyOneFieldsAndFourConstructors() {
        var employee = dev.kaldiroglu.dp.creational.introduction.employee.problem.Employee.class;
        assertEquals(21, instanceFields(employee));
        assertEquals(4, employee.getConstructors().length);
        assertEquals(21, setters(employee));
    }

    @Test
    void theBetterEmployeeHasFiveParts() {
        var employee = dev.kaldiroglu.dp.creational.introduction.employee.better.Employee.class;
        assertEquals(5, instanceFields(employee));
        assertEquals(5, employee.getConstructors()[0].getParameterCount());
    }

    @Test
    void theFivePartsHoldTheTwentyOneFields() {
        assertEquals(6, instanceFields(dev.kaldiroglu.dp.creational.introduction.employee.better.PersonalInfo.class));
        assertEquals(4, instanceFields(dev.kaldiroglu.dp.creational.introduction.employee.better.ContactInfo.class));
        assertEquals(7, instanceFields(dev.kaldiroglu.dp.creational.introduction.employee.better.JobInfo.class));
        assertEquals(3, instanceFields(dev.kaldiroglu.dp.creational.introduction.employee.better.WorkInfo.class));
        assertEquals(1, instanceFields(dev.kaldiroglu.dp.creational.introduction.employee.better.Compensation.class));
    }

    @Test
    void theBetterEmployeeAlwaysHasACompensation() {
        var employee = new dev.kaldiroglu.dp.creational.introduction.employee.better.Employee(
                null, null, null, null, null);
        assertEquals(BigDecimal.ZERO, employee.getCompensation().getSalary());
    }

    @Test
    void bothConnectionsHaveFifteenFields() {
        assertEquals(15, instanceFields(
                dev.kaldiroglu.dp.creational.introduction.db.problem.DatabaseConnection.class));
        assertEquals(15, instanceFields(
                dev.kaldiroglu.dp.creational.introduction.db.better.DatabaseConnection.class));
    }

    @Test
    void theBetterConnectionHasNoSettersAndOnlyFinalFields() {
        var connection = dev.kaldiroglu.dp.creational.introduction.db.better.DatabaseConnection.class;
        assertEquals(0, setters(connection));
        for (Field field : connection.getDeclaredFields()) {
            assertTrue(Modifier.isFinal(field.getModifiers()), field.getName());
        }
        assertEquals(0, connection.getConstructors().length);
    }

    @Test
    void theFailingFigureFromTheReviewNowPasses() {
        // 1000 + 1000 * 0.01 * 36 is 1359.9999999999998 in double arithmetic.
        assertEquals(1360.0, new BankLoan(1000, 0.01, 36, 1360).getTotalRepayment());
    }

    @Test
    void bankLoanRejectsAWrongTotal() {
        assertThrows(IllegalArgumentException.class, () -> new BankLoan(1000, 0.01, 12, 1100));
    }

    @Test
    void settersAcceptAWrongTotal() {
        LoanWithSetters loan = new LoanWithSetters();
        loan.setPrincipal(1000);
        loan.setRate(0.01);
        loan.setMonths(12);
        loan.setTotalRepayment(1100);
        assertEquals(1100, loan.getTotalRepayment());
        assertEquals(1120, loan.correctTotal());
    }

    @Test
    void secureLoanHasNoPublicConstructorAndNoSetters() {
        assertEquals(0, SecureLoan.class.getConstructors().length);
        assertEquals(0, setters(SecureLoan.class));
        assertEquals(1120, SecureLoan.create(1000, 0.01, 12).getTotalRepayment());
    }

    @Test
    void theRecordCalculatesItsTotalAndChecksItsArguments() {
        assertTrue(LoanRecord.class.isRecord());
        assertEquals(1120, new LoanRecord(1000, 0.01, 12).totalRepayment());
        assertThrows(IllegalArgumentException.class, () -> new LoanRecord(1000, 0.01, 0));
    }

    @Test
    void theCheckRunsBeforeTheSuperclassConstructor() {
        assertEquals(6, new ShortTerm(6).getMonths());
        assertThrows(IllegalArgumentException.class, () -> new ShortTerm(36));
    }

    /** The objects on "Infrastructure Objects Are Easy to Create". */
    @Test
    void theEasyCreationCallsCompileAndReturnObjects() {
        assertNotNull(LocalDateTime.now());
        assertNotNull(Runtime.getRuntime());
        assertNotNull(List.of("a", "b"));
        assertNotNull(Path.of("report.txt"));
        assertNotNull(HttpClient.newBuilder().build());
        assertFalse(Runtime.getRuntime() != Runtime.getRuntime());
    }
}
