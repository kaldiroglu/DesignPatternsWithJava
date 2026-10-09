package dev.kaldiroglu.dp.behavioral.iterator.orgchart.problem;

import dev.kaldiroglu.dp.behavioral.iterator.orgchart.domain.Employee;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * The three attempts of Part 1: a department that gives out its lists, one that copies,
 * and one that calls back.
 */
class ProblemTest {

    private static final Employee AYSE = new Employee("Ayse", "CEO");
    private static final Employee DENIZ = new Employee("Deniz", "head of sales");
    private static final Employee ALI = new Employee("Ali", "sales");
    private static final Employee CAN = new Employee("Can", "export");
    private static final Employee MERT = new Employee("Mert", "head of operations");
    private static final Employee ELIF = new Employee("Elif", "support");

    /** The same company as orgchart.solution.Main, built as stage three. */
    private static CallbackDepartment callbackCompany(Employee salesHead) {
        CallbackDepartment export = new CallbackDepartment("Export").add(CAN);
        CallbackDepartment sales = new CallbackDepartment("Sales").add(salesHead).add(ALI).add(export);
        CallbackDepartment support = new CallbackDepartment("Support").add(ELIF);
        CallbackDepartment operations = new CallbackDepartment("Operations").add(MERT).add(support);
        return new CallbackDepartment("Head office").add(AYSE).add(sales).add(operations);
    }

    // ------------------------------------------------------------------ stage one

    @Test
    @DisplayName("stage one works: payroll gets one payslip for every person")
    void payrollPaysEveryoneOnce() {
        OpenDepartment sales = new OpenDepartment("Sales").add(DENIZ).add(ALI)
                .add(new OpenDepartment("Export").add(CAN));
        OpenDepartment company = new OpenDepartment("Head office").add(AYSE).add(sales);

        assertEquals(List.of("payslip for Ayse", "payslip for Deniz", "payslip for Ali", "payslip for Can"),
                new PayrollRun().payslips(company));
    }

    @Test
    @DisplayName("stage one: callers get the real lists, so any caller can add or remove people")
    void callersCanChangeTheRealLists() {
        OpenDepartment sales = new OpenDepartment("Sales").add(DENIZ).add(ALI);

        sales.members().remove(ALI);

        assertEquals(List.of("payslip for Deniz"), new PayrollRun().payslips(sales),
                "a caller removed Ali from the department itself");
    }

    // ------------------------------------------------------------------ stage two

    @Test
    @DisplayName("stage two: callers get a copy they cannot change, in one order")
    void stageTwoGivesAnUnchangeableCopy() {
        CopyingDepartment sales = new CopyingDepartment("Sales").add(DENIZ).add(ALI)
                .add(new CopyingDepartment("Export").add(CAN));
        CopyingDepartment company = new CopyingDepartment("Head office").add(AYSE).add(sales)
                .add(new CopyingDepartment("Operations").add(MERT));

        List<Employee> everyone = company.everyone();

        assertEquals(List.of(AYSE, DENIZ, ALI, CAN, MERT), everyone);
        assertThrows(UnsupportedOperationException.class, () -> everyone.add(ELIF));
        assertNotSame(everyone, company.everyone(), "every call makes a new copy");
    }

    // ------------------------------------------------------------------ stage three

    @Test
    @DisplayName("stage three: no copy, and two orders of walking")
    void stageThreeWalksInTwoOrders() {
        CallbackDepartment company = callbackCompany(DENIZ);
        List<Employee> byDepartment = new ArrayList<>();
        List<Employee> byLevel = new ArrayList<>();

        company.forEachMember(byDepartment::add);
        company.forEachMemberByLevel(byLevel::add);

        assertEquals(List.of(AYSE, DENIZ, ALI, CAN, MERT, ELIF), byDepartment);
        assertEquals(List.of(AYSE, DENIZ, ALI, MERT, CAN, ELIF), byLevel);
    }

    @Test
    @DisplayName("stage three: the change report finds the new head of sales, but only after copying both charts")
    void theProblemReportFindsTheChange() {
        Employee zeynep = new Employee("Zeynep", "head of sales");

        Optional<String> change = new ChangeReport().firstDifference(callbackCompany(DENIZ),
                callbackCompany(zeynep));

        assertEquals(Optional.of("Deniz (head of sales) -> Zeynep (head of sales)"), change);
    }

    @Test
    @DisplayName("stage three: the change report answers none for equal charts and sees a size change")
    void theProblemReportHandlesEqualAndShorterCharts() {
        CallbackDepartment shorter = new CallbackDepartment("Head office").add(AYSE);

        assertEquals(Optional.empty(),
                new ChangeReport().firstDifference(callbackCompany(DENIZ), callbackCompany(DENIZ)));
        assertEquals(Optional.of("the charts have different sizes"),
                new ChangeReport().firstDifference(callbackCompany(DENIZ), shorter));
    }
}
