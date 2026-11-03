package dev.kaldiroglu.dp.creational.factoryMethod;

import static dev.kaldiroglu.dp.creational.factoryMethod.SourceFacts.classesBeside;
import static dev.kaldiroglu.dp.creational.factoryMethod.SourceFacts.concreteSubtypes;
import static dev.kaldiroglu.dp.creational.factoryMethod.SourceFacts.sourceOf;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.net.URI;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import javax.tools.ToolProvider;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import dev.kaldiroglu.dp.creational.factoryMethod.company.problem.stageTwo.HR;
import dev.kaldiroglu.dp.creational.factoryMethod.company.solution.fm1.Factory;
import dev.kaldiroglu.dp.creational.factoryMethod.company.solution.fm5.EmployeeEnum;

/** The facts the Factory Method deck states, counted from the code. */
class ChapterFiguresTest {

    private static final String FM1 = "company/solution/fm1/";
    private static final String FM5 = "company/solution/fm5/";
    private static final String MODERN = "dev.kaldiroglu.dp.creational.factoryMethod.modern.";

    private static Set<String> simpleNames(List<Class<?>> classes) {
        return classes.stream().map(Class::getSimpleName).collect(Collectors.toSet());
    }

    private static List<Class<?>> fm1Products() throws Exception {
        return concreteSubtypes(dev.kaldiroglu.dp.creational.factoryMethod.company.solution.fm1.Employee.class);
    }

    private static List<Class<?>> fm1Factories() throws Exception {
        return concreteSubtypes(Factory.class);
    }

    private static int count(String regex, String text) {
        Matcher matcher = Pattern.compile(regex).matcher(text);
        int found = 0;
        while (matcher.find()) {
            found++;
        }
        return found;
    }

    @Test
    void hrInStageTwoCallsNewOnEachConcreteClass() throws Exception {
        Set<String> created = Pattern.compile("new (\\w+)\\(")
                .matcher(sourceOf("company/problem/stageTwo/HR.java"))
                .results().map(m -> m.group(1)).collect(Collectors.toSet());
        var products = concreteSubtypes(
                dev.kaldiroglu.dp.creational.factoryMethod.company.problem.stageTwo.Employee.class);
        assertEquals(simpleNames(products), created);
        assertEquals(3, products.size());
        assertEquals(3, Arrays.stream(HR.class.getDeclaredMethods())
                .filter(m -> m.getName().startsWith("add")).count());
    }

    @Test
    void hrInFm1HasOneFieldAndOneAddMethodPerType() throws Exception {
        var hr = dev.kaldiroglu.dp.creational.factoryMethod.company.solution.fm1.HR.class;
        int types = fm1Products().size();
        assertEquals(3, types);
        assertEquals(types, Arrays.stream(hr.getDeclaredFields())
                .filter(f -> f.getType() == Factory.class).count());
        assertEquals(types, Arrays.stream(hr.getDeclaredMethods())
                .filter(m -> m.getName().startsWith("add")).count());
        assertEquals(types, hr.getConstructors()[0].getParameterCount());
    }

    @Test
    void mainInFm1NamesEveryConcreteFactory() throws Exception {
        String main = sourceOf(FM1 + "Main.java");
        for (Class<?> factory : fm1Factories()) {
            assertTrue(main.contains("new " + factory.getSimpleName() + "("), factory.getName());
        }
    }

    @Test
    void factoriesAndProductsInFm1MatchOneToOne() throws Exception {
        Set<Class<?>> returned = new java.util.HashSet<>();
        for (Class<?> factory : fm1Factories()) {
            for (Method method : factory.getDeclaredMethods()) {
                if (method.getName().equals("create") && !method.isBridge()) {
                    returned.add(method.getReturnType());
                }
            }
        }
        assertEquals(fm1Products().size(), fm1Factories().size());
        assertEquals(Set.copyOf(fm1Products()), returned);
    }

    @Test
    void fm4HasExactlyOneImplementationOfItsInterface() throws Exception {
        var implementations = concreteSubtypes(
                dev.kaldiroglu.dp.creational.factoryMethod.company.solution.fm4.EmployeeFactory.class);
        assertEquals(Set.of("EmployeeFactoryImpl"), simpleNames(implementations));
    }

    @Test
    void theFm5SwitchHasOneCasePerType() throws Exception {
        String factory = sourceOf(FM5 + "EmployeeFactory.java");
        int types = EmployeeEnum.values().length;
        assertEquals(3, types);
        assertEquals(types, count("case \"\\w+\" ->", factory));
        assertEquals(types, count("case [A-Z]+ ->", factory));
    }

    @Test
    void fm3HasThreeInterfacesWithThreeDifferentCreateSignatures() throws Exception {
        var interfaces = classesBeside(
                dev.kaldiroglu.dp.creational.factoryMethod.company.solution.fm3.EmployeeFactory.class)
                .stream().filter(Class::isInterface).toList();
        assertEquals(3, interfaces.size());
        Set<List<Class<?>>> signatures = interfaces.stream()
                .map(i -> Arrays.stream(i.getDeclaredMethods())
                        .filter(m -> m.getName().equals("create"))
                        .map(m -> List.of(m.getParameterTypes())).toList())
                .peek(forOne -> assertEquals(1, forOne.size()))
                .map(forOne -> forOne.get(0))
                .collect(Collectors.toSet());
        assertEquals(3, signatures.size());
    }

    @Test
    void hrInTheConstructorReferenceExampleTakesASupplier() throws Exception {
        var hr = Class.forName(MODERN + "ConstructorReferenceMain$HR");
        assertEquals(List.of(Supplier.class), List.of(hr.getDeclaredConstructors()[0].getParameterTypes()));
    }

    @Test
    void theEmployeeKindIsSealedWithOneRecordPerKind() throws Exception {
        var kind = Class.forName(MODERN + "SealedSwitchMain$EmployeeKind");
        assertTrue(kind.isSealed());
        assertEquals(3, kind.getPermittedSubclasses().length);
        assertTrue(Arrays.stream(kind.getPermittedSubclasses()).allMatch(Class::isRecord));
    }

    @Test
    void theSealedSwitchNeedsNoDefaultNoNullAndNoUnknownBranch() throws IOException {
        String main = sourceOf("modern/SealedSwitchMain.java");
        assertTrue(main.contains("switch (kind)"));
        assertFalse(main.contains("default"));
        assertFalse(main.contains("null"));
        assertFalse(main.contains("IllegalArgumentException"));
    }

    /** The claim on the slide is about the compiler, so the compiler is asked. */
    @Test
    void theCompilerRejectsASwitchThatMissesAKind(@TempDir Path output) {
        String template = """
                class Kinds {
                    sealed interface Kind {}
                    record A() implements Kind {}
                    record B() implements Kind {}
                    static int size(Kind kind) {
                        return switch (kind) {
                            case A a -> 1;
                            %s
                        };
                    }
                }
                """;
        assertEquals("", compile(template.formatted("case B b -> 2;"), output));
        assertTrue(compile(template.formatted(""), output).contains("does not cover all possible input values"));
    }

    private static String compile(String source, Path output) {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        JavaFileObject file = new SimpleJavaFileObject(URI.create("string:///Kinds.java"), JavaFileObject.Kind.SOURCE) {
            @Override
            public CharSequence getCharContent(boolean ignoreEncodingErrors) {
                return source;
            }
        };
        StringWriter messages = new StringWriter();
        compiler.getTask(messages, null, null, List.of("-d", output.toString()), null, List.of(file)).call();
        return messages.toString();
    }

    @Test
    void stageOneDirectorHasNoDepartment() {
        var hr = new dev.kaldiroglu.dp.creational.factoryMethod.company.problem.stageOne.HR();
        hr.addDirector(7, "Ahmet", 19, "Production", 40, 5000);
        assertEquals(null, hr.getEmployees().get(0).getDepartment());
    }

    @Test
    void stageThreeHrCallsNoConstructor() throws Exception {
        assertEquals(0, Pattern.compile("new (Employee|Manager|Director)\\(")
                .matcher(sourceOf("company/problem/stageThree/HR.java")).results().count());
        assertEquals(3, Pattern.compile("case \"\\w+\" ->")
                .matcher(sourceOf("company/problem/stageThree/EmployeeCreator.java")).results().count());
    }

    @Test
    void stageThreeGivesEveryManagerTheSameTeamSize() {
        var hr = new dev.kaldiroglu.dp.creational.factoryMethod.company.problem.stageThree.HR();
        hr.add("Manager", 4, "Mehmet", 14, "Production");
        hr.add("Manager", 5, "Sami", 2, "Marketing");
        for (var employee : hr.getEmployees())
            assertEquals(8, ((dev.kaldiroglu.dp.creational.factoryMethod.company.problem.stageTwo.Manager) employee).getTeamSize());
    }

    @Test
    void stageThreeRejectsAnUnknownTypeOnlyAtRunTime() {
        var hr = new dev.kaldiroglu.dp.creational.factoryMethod.company.problem.stageThree.HR();
        var e = org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class,
                () -> hr.add("Intern", 8, "Deniz", 1, "Sales"));
        assertEquals("Invalid type: Intern", e.getMessage());
    }
}
