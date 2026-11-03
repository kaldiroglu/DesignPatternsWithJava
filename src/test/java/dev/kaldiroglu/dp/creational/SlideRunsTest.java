package dev.kaldiroglu.dp.creational;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Every class named by a "· run X" label in the Creational decks, run in this JVM: it must
 * return normally and print the lines the slide quotes.
 */
class SlideRunsTest {

    record Run(String mainClass, List<String> lines) {
        @Override
        public String toString() {
            return mainClass;
        }
    }

    private static Run run(String mainClass, String... lines) {
        return new Run(mainClass, List.of(lines));
    }

    static Stream<Run> runs() {
        return Stream.of(
                // Introduction
                run("introduction.employee.problem.SettersMain",
                        "Alex Kim | Intern | id=null | salary=null",
                        "null null | Full-time | id=null | salary=null"),
                run("introduction.employee.problem.ConstructorsMain",
                        "null null | Full-time | id=null | salary=null",
                        "Sarah Johnson | Full-time | id=null | salary=null",
                        "Michael Chen | Full-time | id=EXE2020045 | salary=180000"),
                run("introduction.employee.problem.SwappedArgumentsMain",
                        "Email     : Engineering",
                        "Department: sarah.johnson@company.com"),
                run("introduction.employee.better.Main",
                        "intern: Alex Kim | Data Science | Data Analyst Intern | Intern | remote=false | tz=PST | salary=25000",
                        "remoteContractor: Elena Rodriguez | Marketing | UI/UX Designer | Contract | remote=true | tz=PST | salary=0"),
                run("introduction.db.problem.Main",
                        "localhost:3306/myapp_dev user=dev_user ssl=true",
                        "localhost:3306/null user=dev_user ssl=true"),
                run("introduction.db.better.Main",
                        "prod-db-cluster.company.com:5432/myapp_production user=prod_user ssl=true",
                        "Rejected: database must not be blank"),
                run("introduction.bank.SettersMain",
                        "Total stored : 1100.0",
                        "Correct total: 1120.0"),
                run("introduction.bank.ConstructorCheckMain",
                        "Accepted: 1120.0",
                        "Rejected: Math doesn't add up!"),
                run("introduction.bank.SecureLoanMain",
                        "Total: 1120.0",
                        "Rejected: Principal must be positive"),
                run("introduction.bank.LoanRecordMain",
                        "LoanRecord[principal=1000.0, rate=0.01, months=12]",
                        "Total: 1120.0",
                        "Rejected: Term must be at least 1 month"),
                run("introduction.bank.ShortTermMain",
                        "Months: 6",
                        "Rejected: A short term is at most 12 months"),
                // Factory Method
                run("factoryMethod.company.problem.stageOne.Main",
                        "Paying 5000.0 to Employee Ahmet in Production",
                        "Paying 1500.0 to Employee Zeynep in Sales",
                        "Paying 3500.0 to Employee Kemal in Production",
                        "Paying 10000.0 to Manager Mehmet in Production",
                        "Paying 4000.0 to Manager Sami in Marketing",
                        "Paying 17500.0 to Director Ahmet in null"),
                run("factoryMethod.company.problem.stageTwo.Main",
                        "Paying 5000.0 to Employee Ahmet in Production",
                        "Paying 1500.0 to Employee Zeynep in Sales",
                        "Paying 3500.0 to Employee Kemal in Production",
                        "Paying 10000.0 to Manager Mehmet in Production",
                        "Paying 4000.0 to Manager Sami in Marketing",
                        "Paying 17500.0 to Director Ahmet in Production"),
                run("factoryMethod.company.problem.stageThree.Main",
                        "Paying 5000.0 to Employee Ahmet in Production",
                        "Paying 1500.0 to Employee Zeynep in Sales",
                        "Paying 3500.0 to Employee Kemal in Production",
                        "Paying 10000.0 to Manager Mehmet in Production",
                        "Paying 4000.0 to Manager Sami in Marketing",
                        "Paying 17500.0 to Director Ahmet in Production"),
                run("factoryMethod.company.problem.stageThree.TeamSizeMain",
                        "Manager is working!",
                        "Manager manages a team of 8"),
                run("factoryMethod.company.solution.fm1.Main",
                        "Paying 40000.0 to Yeliz in Production",
                        "Paying 25000.0 to Selim in Marketing",
                        "Paying 10000.0 to Bekir in Sales",
                        "Paying 80000.0 to Ayse in Sales",
                        "Paying 65000.0 to Sevda in Sales",
                        "Paying 55000.0 to Elif in Management"),
                run("factoryMethod.company.solution.fm2.Main",
                        "Paying 40000.0 to Ismail in Production",
                        "Paying 25000.0 to Can in Marketing",
                        "Paying 10000.0 to Zuhal in Sales",
                        "Paying 80000.0 to Murat in Sales",
                        "Paying 65000.0 to Murat in Engineering",
                        "Paying 55000.0 to Can in Management"),
                run("factoryMethod.company.solution.fm3.Main",
                        "Paying 40000.0 to Ismail in Production",
                        "Paying 25000.0 to Can in Marketing",
                        "Paying 10000.0 to Zuhal in Sales",
                        "Paying 80000.0 to Murat in Sales",
                        "Paying 65000.0 to Murat in Engineering",
                        "Paying 90000.0 to Can in Production"),
                run("factoryMethod.company.solution.fm4.Main",
                        "Paying 40000.0 to Ismail in Production",
                        "Paying 25000.0 to Can in Marketing",
                        "Paying 10000.0 to Zuhal in Sales",
                        "Paying 80000.0 to Murat in Sales",
                        "Paying 65000.0 to Murat in Engineering",
                        "Paying 90000.0 to Can in Production"),
                run("factoryMethod.company.solution.fm5.Main",
                        "Paying 40000.0 to Yeliz in Production",
                        "Paying 25000.0 to Selim in Marketing",
                        "Paying 10000.0 to Bekir in Sales",
                        "Paying 100000.0 to Demet in Marketing",
                        "Paying 35000.0 to Can in Engineering",
                        "Paying 55000.0 to Elif in Management"),
                run("factoryMethod.company.solution.fm6.Main",
                        "Paying 40000.0 to Ismail in Production",
                        "Paying 25000.0 to Can in Marketing",
                        "Paying 10000.0 to Zuhal in Sales",
                        "Paying 80000.0 to Murat in Sales",
                        "Paying 65000.0 to Murat in Engineering",
                        "Paying 90000.0 to Can in Production"),
                run("factoryMethod.report.Main",
                        "Printing Daily Sales Report for 2026-11-01",
                        "Printing Daily Earnings Report for 2026-11-01"),
                run("factoryMethod.gof.app.Main",
                        "MyDocument is open"),
                run("factoryMethod.gof.figure.Main",
                        "LineManipulator stretches the line",
                        "TextManipulator moves the text cursor"),
                run("factoryMethod.modern.ConstructorReferenceMain",
                        "HR hired: Employee",
                        "HR hired: Manager",
                        "HR hired: Director"),
                run("factoryMethod.modern.SealedSwitchMain",
                        "Paying 50000.0 to Ali in Sales",
                        "Paying 90000.0 to Elif in Production",
                        "Paying 120000.0 to Cem in Marketing"));
    }

    @ParameterizedTest
    @MethodSource("runs")
    void printsWhatTheSlideQuotes(Run run) throws Exception {
        String printed = printedBy(run.mainClass());
        for (String line : run.lines()) {
            assertTrue(printed.contains(line),
                    run.mainClass() + " did not print: " + line + "\nIt printed:\n" + printed);
        }
    }

    /** Runs the class's main in this JVM and returns what it printed, with \n line ends. */
    private static String printedBy(String mainClass) throws Exception {
        PrintStream original = System.out;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        System.setOut(new PrintStream(buffer, true, StandardCharsets.UTF_8));
        try {
            Class.forName("dev.kaldiroglu.dp.creational." + mainClass)
                    .getMethod("main", String[].class)
                    .invoke(null, (Object) new String[0]);
        } catch (InvocationTargetException failure) {
            throw new AssertionError(mainClass + " did not exit normally", failure.getCause());
        } finally {
            System.setOut(original);
        }
        return buffer.toString(StandardCharsets.UTF_8).replace("\r\n", "\n");
    }
}
