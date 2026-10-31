package dev.kaldiroglu.dp.structural;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Every class named by a "· run X" label in the Structural decks, run in this JVM: it must
 * return normally and print the lines the slide quotes. A slide that shows code and no
 * figure gets the line that shows the slide's claim. Timestamps are left out, so the
 * facade.notification rows assert the lines around them.
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
                // Adapter
                run("adapter.electricity.problem1.Main",
                        "TurkishHomeAppliance Shaver is starting!",
                        "USPowerProvider started to provide electricity."),
                run("adapter.electricity.powerAdapter1.Main",
                        "USTurkishPowerAdapter: Converting from USPowerSource to TurkishPowerSource",
                        "USPowerProvider started to provide electricity."),
                run("adapter.electricity.classAdapter.Main",
                        "US source: switch pushed -> LIVE (110V/60Hz)",
                        "after pushSwitch(); isLive=true"),
                run("adapter.electricity.pluggable.electricity.parameterized.PluggableDemo",
                        "US source: switch pushed (110V / 60Hz)",
                        "UK source: toggle flipped (230V / 50Hz)",
                        "Kenyan source: switch is pushed"),

                // Bridge
                run("bridge.notifications.problem.StageOneMain",
                        "3 kinds x 3 channels = 9 branches, in one method"),
                run("bridge.notifications.problem.StageTwoMain",
                        "3 kinds x 3 channels = 9 classes"),
                run("bridge.notifications.problem.StageThreeMain",
                        "Bora's stored preference : SMS",
                        "the notification used    : email"),
                run("bridge.violation.Main",
                        "AType -> My variable: 42",
                        "aStringVariable : null"),
                run("bridge.file.problem.StageOneMain",
                        "FileNet    insurance!claims/2026/case-4021    kept 8, allowed 2"),
                run("bridge.file.solution.CombinationsMain",
                        "Evernote     finance keeps 5, live versions [4, 5, 6, 7, 8]",
                        "FileNet      insurance keeps 2, live versions [7, 8]",
                        "2 departments + 3 stores = 5 classes, 6 combinations."),
                run("bridge.file.solution.SwapMain",
                        "written on FileNet"),
                run("bridge.notifications.solution.classic.SwapMain",
                        "on the wire        : 1 email, 1 sms"),
                run("bridge.notifications.solution.classic.CombinationsMain",
                        "9 messages, and every combination worked"),
                run("bridge.notifications.solution.factory.PreferenceMain",
                        "Akin   prefers EMAIL -> sent over email",
                        "Bora   prefers SMS   -> sent over sms",
                        "Ceyda  prefers PUSH  -> sent over push"),
                run("bridge.notifications.solution.shared.Main",
                        "abstractions sharing the channel : 3",
                        "messages through the one channel : 3",
                        "transport connections opened     : 1"),

                // Composite
                run("composite.bom.problem.RightAnswersMain",
                        "Bicycle cost:   $396.00",
                        "Bicycle weight: 5950 g",
                        "Bicycle parts:  80"),
                run("composite.bom.problem.WheelDriftMain",
                        "Are the two wheels the same object? false",
                        "Wheel 1 now costs $86.60, wheel 2 still costs $85.00"),
                run("composite.gof.equipment.Demo",
                        "The net price of the chassis is $440.00",
                        "The net price of the cabinet is $530.00"),
                run("composite.graphic.ShapeCountMain",
                        "shapes on the display: 5",
                        "shapes in the logo   : 2",
                        "a single circle      : 1"),
                run("composite.graphic.SafetyMain",
                        "Drawing canvas: Logo\n",
                        "\n5\n"),
                run("composite.fileSystem.SixQuestionsMain",
                        "size of the whole tree : 170980 bytes",
                        "biggest leaf           : ImportantReport.docx"),
                run("composite.fileSystem.DiskReportMain",
                        "whole tree : 167,816 bytes",
                        "one file   : 45,000 bytes"),
                run("composite.fileSystem.CacheMain",
                        "three calls, nothing changed : 0",
                        "after one file is added      : 3 — exactly Reports, Dev and akin"),
                run("composite.bom.solution.RollUpsMain",
                        "City Bicycle           (assembly)         cost   $396.00     5950 g    80 part(s)",
                        "14g Spoke              (purchased part)   cost     $0.40        5 g     1 part(s)"),
                run("composite.bom.solution.SharingMain",
                        "Line 2 of the bicycle requires 2 x 700c Wheel",
                        "Is it the same object as the catalog's wheel? true"),
                run("composite.bom.solution.NewItemMain",
                        "Bicycle cost $399.20 -> $413.20, weight unchanged at 5990 g, part count unchanged at 88.",
                        "Shipping estimate is still $29.40."),

                // Decorator
                run("decorator.middleware.problem.CopyPasteMain",
                        "priceForOrder logged   3 lines (retry included)",
                        "priceForReorder logged 2 lines"),
                run("decorator.middleware.problem.FlagsMain",
                        "5 booleans = 32 configurations"),
                run("decorator.middleware.problem.RateLimitMain",
                        "fourth request  : refused",
                        "limiter counted : 3",
                        "supplier calls  : 9"),
                run("decorator.middleware.problem.SubclassChainMain",
                        "would take 325 classes"),
                run("decorator.gof.visual.solution.Main",
                        "|hello^|", "|therev|", "|there|#", "||hello||"),
                run("decorator.gof.stream.solution.SampleCodeMain",
                        "file now holds   : 12 3a cafe"),
                run("decorator.middleware.solution.classic.ClassicChainMain",
                        "supplier calls : 2",
                        "log lines      : 4"),
                run("decorator.middleware.solution.classic.VendorFeedMain",
                        "vendor calls: 1 for 2 requests",
                        "log lines   : 2"),
                run("decorator.middleware.solution.functional.FunctionalMain",
                        "supplier calls : 2"),
                run("decorator.middleware.solution.fluent.FluentMain",
                        "supplier calls : 2"),
                run("decorator.toast.problem.MenuMain",
                        "Cheese toast = 8",
                        "Cheese sausage toast = 11"),
                run("decorator.toast.solution.StepByStepMain",
                        "bare bread          5",
                        "+ cheddar cheese    8",
                        "+ sucuk sausage     11",
                        "+ tomato            13",
                        "+ ketchup           14",
                        "+ Russian salad     16"),
                run("decorator.toast.solution.DoubleCheeseMain",
                        "Double cheese: 11"),
                run("decorator.toast.solution.PromotionPositionMain",
                        "outermost, so it discounts everything   12",
                        "innermost, so it discounts only bread   14"),
                run("decorator.io.DataInputOutputStreamDemo",
                        "total: $646.82  file: 274 bytes",
                        "total: $646.82  file: 186 bytes",
                        "Compression saved 88 bytes"),

                // Facade
                run("facade.notification.problem.Client",
                        "[EMAIL] To: alice@example.com | Subject: Order #ORD-9876 Confirmed",
                        "[PUSH]  Token: dev_toke*** | Title: Order #ORD-9876 Confirmed"),
                run("facade.gof.Client",
                        "z = x + y - 1", "PUSH 3", "STORE z", "RETURN"),
                run("facade.notification.solution1.Client",
                        "Order ORD-9876 confirmed.",
                        "NotificationResult{slack=true, sms=true, email=true, push=true}"),
                run("facade.notification.solution2.Client",
                        "NotificationResult{sms=true, email=true}",
                        "NotificationResult{slack=true, push=true}",
                        "NotificationResult{email=true}"),
                run("facade.slf4j.Main",
                        "Loaded 42 rows for user john",
                        "Disk usage at 91%",
                        "isDebugEnabled() = false"),

                // Flyweight
                run("flyweight.forest.problem.Main",
                        "Textures loaded      : 10000",
                        "Texture bytes held   : 40,960,000"),
                run("flyweight.gof.FontRangesMain",
                        "flyweight is a nice solution",
                        "'n' @ (27,0) : Times",
                        "'l' @ (0,1) : Courier"),
                run("flyweight.forest.solution.Main",
                        "Textures loaded      : 2",
                        "Texture bytes held   : 8,192"),
                run("flyweight.book.wrong.Main",
                        "this book"),
                run("flyweight.book.correct.Main",
                        "Character occurrences in the book : 236",
                        "Distinct flyweights allocated     : 29",
                        "Occurrences that cost no object   : 207"),
                run("flyweight.quote.problem.Main",
                        "Instruments in the universe         : 4",
                        "Distinct symbol objects retained    : 100,000"),

                // Proxy
                run("proxy.pm.pm1.Main",
                        "problems heard by the PM herself : 2",
                        "problems actually resolved       : 1"),
                run("proxy.pm.pm2.Main",
                        "calls screened by the proxy : 2",
                        "calls passed to the PM      : 1"),
                run("proxy.pm.pm3.ScreeningMain",
                        "calls screened : 2", "passed on      : 1", "refused        : 1"),
                run("proxy.pm.pm3.SecretaryMain",
                        "Prime Minister created yet? false\nPrime Minister created yet? true"),
                run("proxy.pm.pm4.Main",
                        "RealPM: the Prime Minister takes office.",
                        "calls screened : 2", "passed on      : 1", "refused        : 1"),
                run("proxy.network.Main",
                        "requests logged by the proxy : 3",
                        "requests that reached the gateway : 1"),
                run("proxy.gof.Demo",
                        "Total page extent: Extent[width=1024, height=1368]",
                        "[Image] Loading raster data from 'diagram.png' (expensive)..."),
                run("proxy.gof.TwentyFiguresMain",
                        "figures in the document : 20",
                        "total page height       : 5940",
                        "images read from disk   : 0"));
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

    /** Flyweight p22 (The Font Is Stored Once per Range): Times covers 28 positions. */
    @Test
    void timesCoversTwentyEightPositions() throws Exception {
        long times = printedBy("flyweight.gof.FontRangesMain").lines()
                .filter(line -> line.endsWith(": Times")).count();
        assertEquals(28, times);
    }

    /** Runs the class's main in this JVM and returns what it printed, with \n line ends. */
    private static String printedBy(String mainClass) throws Exception {
        PrintStream original = System.out;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        System.setOut(new PrintStream(buffer, true, StandardCharsets.UTF_8));
        try {
            Class.forName("dev.kaldiroglu.dp.structural." + mainClass)
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
