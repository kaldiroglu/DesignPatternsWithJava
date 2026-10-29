package dev.kaldiroglu.dp.introduction.problems.complexIfs;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * The figures the Introduction deck quotes for {@code processUser}: its parameters, how deep
 * its {@code if} statements are nested, and how many different answers it can give.
 * <p>
 * The source is read with its comments removed, so a comment that names a result or holds
 * a brace cannot change a count.
 */
class UserProcessorTest {

    private static final Path SOURCE = Path.of(
            "src/main/java/dev/kaldiroglu/dp/introduction/problems/complexIfs/UserProcessor.java");

    private static String code() throws IOException {
        String source = Files.readString(SOURCE);
        return source.replaceAll("(?s)/\\*.*?\\*/", "").replaceAll("//[^\\n]*", "");
    }

    @Test
    @DisplayName("processUser takes 7 parameters")
    void sevenParameters() {
        Method method = Arrays.stream(UserProcessor.class.getDeclaredMethods())
                .filter(m -> m.getName().equals("processUser"))
                .findFirst().orElseThrow();
        assertEquals(7, method.getParameterCount());
    }

    @Test
    @DisplayName("its if statements are nested 15 levels deep")
    void fifteenLevels() throws IOException {
        int depth = 0;
        int deepest = 0;
        for (char c : code().toCharArray()) {
            if (c == '{') deepest = Math.max(deepest, ++depth);
            if (c == '}') depth--;
        }
        // One level for the class and one for the method; the rest are if and else blocks.
        assertEquals(15, deepest - 2);
    }

    @Test
    @DisplayName("it can give 43 different results")
    void fortyThreeResults() throws IOException {
        Matcher m = Pattern.compile("return \"(\\w+)\"").matcher(code());
        Set<String> results = new HashSet<>();
        int returns = 0;
        while (m.find()) {
            results.add(m.group(1));
            returns++;
        }
        assertEquals(43, results.size());
        assertEquals(43, returns, "every return gives a different result");
    }

    @Test
    @DisplayName("the three users in Main get the answers Main prints")
    void mainsAnswers() {
        UserProcessor processor = new UserProcessor();
        User trialUser = new User() {
            @Override
            public int getAge() {
                return 30;
            }
        };
        assertEquals("NULL_USER_ACCESS_DENIED",
                processor.processUser(null, false, "TRIAL", 1, false, "IT", true));
        assertEquals("ACCESS_DENIED_TOO_YOUNG",
                processor.processUser(new User(), false, "TRIAL", 1, false, "IT", true));
        assertEquals("TRIAL_EXPIRED_UPGRADE_REQUIRED",
                processor.processUser(trialUser, false, "TRIAL", 1, false, "IT", true));
    }
}
