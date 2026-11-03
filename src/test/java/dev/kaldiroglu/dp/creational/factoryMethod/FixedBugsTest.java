package dev.kaldiroglu.dp.creational.factoryMethod;

import static dev.kaldiroglu.dp.creational.factoryMethod.SourceFacts.sourceOf;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

import dev.kaldiroglu.dp.creational.factoryMethod.gof.figure.LineFigure;
import dev.kaldiroglu.dp.creational.factoryMethod.gof.figure.LineManipulator;
import dev.kaldiroglu.dp.creational.factoryMethod.gof.figure.TextFigure;
import dev.kaldiroglu.dp.creational.factoryMethod.gof.figure.TextManipulator;
import dev.kaldiroglu.dp.creational.factoryMethod.report.DailySalesReportFactory;
import dev.kaldiroglu.dp.creational.factoryMethod.company.solution.fm1.Director;
import dev.kaldiroglu.dp.creational.factoryMethod.company.solution.fm1.EmployeeRandomizer;
import dev.kaldiroglu.dp.creational.factoryMethod.company.solution.fm1.Manager;
import dev.kaldiroglu.dp.creational.factoryMethod.company.solution.fm5.EmployeeEnum;
import dev.kaldiroglu.dp.creational.factoryMethod.company.solution.fm5.EmployeeFactory;

/** One test for each code bug listed in the review of the earlier deck. */
class FixedBugsTest {

    @Test
    void fm6CreateDirectorUsesItsDepartmentAndBonus() {
        var director = (Director) dev.kaldiroglu.dp.creational.factoryMethod.company.solution.fm6
                .EmployeeFactory.createDirector("Ali", "Sales", 7000);
        assertEquals("Sales", director.getDepartment());
        assertEquals(40, director.getTeamSize());
        assertEquals(7000, director.getBonus());
    }

    @Test
    void fm6UsesItsOwnFactoryAndHr() throws Exception {
        assertFalse(sourceOf("company/solution/fm6/HR.java").contains("fm5"));
        assertFalse(sourceOf("company/solution/fm6/Main.java").contains("fm5"));
    }

    @Test
    void fm5RejectsAnUnknownTypeName() {
        assertThrows(IllegalArgumentException.class,
                () -> EmployeeFactory.createFromTypeName("Intern"));
    }

    @Test
    void fm5CreatesTheKindItIsAskedFor() {
        assertInstanceOf(Manager.class, EmployeeFactory.createFromTypeName("Manager"));
        assertInstanceOf(Director.class, EmployeeFactory.createFromEnum(EmployeeEnum.DIRECTOR));
    }

    @Test
    void fm5HasNoNullAndNoNumberedCreateMethods() throws Exception {
        assertFalse(sourceOf("company/solution/fm5/EmployeeFactory.java").contains("null"));
        List<String> names = Arrays.stream(EmployeeFactory.class.getDeclaredMethods())
                .map(m -> m.getName()).toList();
        assertFalse(names.contains("create1"));
        assertFalse(names.contains("create2"));
    }

    @Test
    void theMisspelledManipulatorIsGone() {
        assertThrows(ClassNotFoundException.class, () -> Class.forName(
                "dev.kaldiroglu.dp.creational.factoryMethod.gof.figure.Textanipulator"));
    }

    @Test
    void eachFigureCreatesItsManipulator() {
        assertInstanceOf(LineManipulator.class, new LineFigure().createManipulator());
        assertInstanceOf(TextManipulator.class, new TextFigure().createManipulator());
    }

    @Test
    void everyStageHasTheSameDirectorBonus() {
        var fm2 = new dev.kaldiroglu.dp.creational.factoryMethod.company.solution.fm2.DirectorFactory()
                .create("Ali");

        var fm3 = new dev.kaldiroglu.dp.creational.factoryMethod.company.solution.fm3.HR(
                new dev.kaldiroglu.dp.creational.factoryMethod.company.solution.fm3.EmployeeFactoryImpl(),
                new dev.kaldiroglu.dp.creational.factoryMethod.company.solution.fm3.ManagerFactoryImpl(),
                new dev.kaldiroglu.dp.creational.factoryMethod.company.solution.fm3.DirectorFactoryImpl());
        fm3.addDirector();

        var fm4 = new dev.kaldiroglu.dp.creational.factoryMethod.company.solution.fm4.HR(
                new dev.kaldiroglu.dp.creational.factoryMethod.company.solution.fm4.EmployeeFactoryImpl());
        fm4.addDirector();

        var fm6 = new dev.kaldiroglu.dp.creational.factoryMethod.company.solution.fm6.HR();
        fm6.addDirector();

        var bonuses = Stream.of(
                new dev.kaldiroglu.dp.creational.factoryMethod.company.solution.fm1.DirectorFactory()
                        .create().getBonus(),
                fm2.getBonus(),
                ((dev.kaldiroglu.dp.creational.factoryMethod.company.solution.fm3.Director)
                        fm3.getEmployees().get(0)).getBonus(),
                ((dev.kaldiroglu.dp.creational.factoryMethod.company.solution.fm4.Director)
                        fm4.getEmployees().get(0)).getBonus(),
                ((Director) EmployeeFactory.createFromEnum(EmployeeEnum.DIRECTOR)).getBonus(),
                ((Director) fm6.getEmployees().get(0)).getBonus());
        assertEquals(List.of(5000.0, 5000.0, 5000.0, 5000.0, 5000.0, 5000.0),
                bonuses.toList());
    }

    @Test
    void hrInFm4TakesTheInterface() {
        var hr = dev.kaldiroglu.dp.creational.factoryMethod.company.solution.fm4.HR.class;
        assertEquals(List.of(dev.kaldiroglu.dp.creational.factoryMethod.company.solution.fm4.EmployeeFactory.class),
                List.of(hr.getConstructors()[0].getParameterTypes()));
    }

    @Test
    void theSameSeedGivesTheSameValues() {
        EmployeeRandomizer.setGenerator(new Random(EmployeeRandomizer.SEED));
        String first = EmployeeRandomizer.createName() + EmployeeRandomizer.createId();
        EmployeeRandomizer.setGenerator(new Random(EmployeeRandomizer.SEED));
        assertEquals(first, EmployeeRandomizer.createName() + EmployeeRandomizer.createId());
    }

    @Test
    void aReportNameUsesTheDateItsFactoryWasGiven() {
        var report = new DailySalesReportFactory(LocalDate.of(2026, 11, 1)).generateReport();
        assertEquals("Daily Sales Report for 2026-11-01", report.getName());
    }

    @Test
    void stageTwoKeepsTheSalaryItPays() {
        var manager = new dev.kaldiroglu.dp.creational.factoryMethod.company.problem.stageTwo.Manager(1, "Ali", 12, "Sales", 8);
        var director = new dev.kaldiroglu.dp.creational.factoryMethod.company.problem.stageTwo.Director(2, "Cem", 6, "Sales", 40, 5000);
        assertEquals(manager.calculateSalary(), manager.getSalary());
        assertEquals(director.calculateSalary(), director.getSalary());
    }

    @Test
    void fm1KeepsTheSalaryItPays() {
        var manager = new dev.kaldiroglu.dp.creational.factoryMethod.company.solution.fm1.Manager(1, "Ali", 12, "Sales", 8);
        var director = new dev.kaldiroglu.dp.creational.factoryMethod.company.solution.fm1.Director(2, "Cem", 6, "Sales", 40, 5000);
        assertEquals(manager.calculateSalary(), manager.getSalary());
        assertEquals(director.calculateSalary(), director.getSalary());
    }

    @Test
    void fm2KeepsTheSalaryItPays() {
        var manager = new dev.kaldiroglu.dp.creational.factoryMethod.company.solution.fm2.Manager(1, "Ali", 12, "Sales", 8);
        var director = new dev.kaldiroglu.dp.creational.factoryMethod.company.solution.fm2.Director(2, "Cem", 6, "Sales", 40, 5000);
        assertEquals(manager.calculateSalary(), manager.getSalary());
        assertEquals(director.calculateSalary(), director.getSalary());
    }

    @Test
    void fm3KeepsTheSalaryItPays() {
        var manager = new dev.kaldiroglu.dp.creational.factoryMethod.company.solution.fm3.Manager(1, "Ali", 12, "Sales", 8);
        var director = new dev.kaldiroglu.dp.creational.factoryMethod.company.solution.fm3.Director(2, "Cem", 6, "Sales", 40, 5000);
        assertEquals(manager.calculateSalary(), manager.getSalary());
        assertEquals(director.calculateSalary(), director.getSalary());
    }

    @Test
    void fm4KeepsTheSalaryItPays() {
        var manager = new dev.kaldiroglu.dp.creational.factoryMethod.company.solution.fm4.Manager(1, "Ali", 12, "Sales", 8);
        var director = new dev.kaldiroglu.dp.creational.factoryMethod.company.solution.fm4.Director(2, "Cem", 6, "Sales", 40, 5000);
        assertEquals(manager.calculateSalary(), manager.getSalary());
        assertEquals(director.calculateSalary(), director.getSalary());
    }

    @Test
    void variety1KeepsTheSalaryItPays() {
        var manager = new dev.kaldiroglu.dp.creational.factoryMethod.company.solution.variety1.Manager(1, "Ali", 12, "Sales", 8);
        var director = new dev.kaldiroglu.dp.creational.factoryMethod.company.solution.variety1.Director(2, "Cem", 6, "Sales", 40, 5000);
        assertEquals(manager.calculateSalary(), manager.getSalary());
        assertEquals(director.calculateSalary(), director.getSalary());
    }

    @Test
    void variety2KeepsTheSalaryItPays() {
        var manager = new dev.kaldiroglu.dp.creational.factoryMethod.company.solution.variety2.Manager(1, "Ali", 12, "Sales", 8);
        var director = new dev.kaldiroglu.dp.creational.factoryMethod.company.solution.variety2.Director(2, "Cem", 6, "Sales", 40, 5000);
        assertEquals(manager.calculateSalary(), manager.getSalary());
        assertEquals(director.calculateSalary(), director.getSalary());
    }
}
