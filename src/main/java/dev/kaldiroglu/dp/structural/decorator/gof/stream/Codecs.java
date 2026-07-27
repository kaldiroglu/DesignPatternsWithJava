package dev.kaldiroglu.dp.structural.decorator.gof.stream;

import java.util.Map;

/**
 * The two transformations GoF's stream example applies: compression and conversion to
 * 7-bit ASCII. Kept in one place so that both designs transform data identically and the
 * comparison is about structure only.
 */
public final class Codecs {

    private static final Map<Character, String> FOLDINGS = Map.ofEntries(
            Map.entry('á', "a"), Map.entry('à', "a"), Map.entry('â', "a"), Map.entry('ä', "a"),
            Map.entry('é', "e"), Map.entry('è', "e"), Map.entry('ê', "e"), Map.entry('ë', "e"),
            Map.entry('í', "i"), Map.entry('ï', "i"), Map.entry('î', "i"),
            Map.entry('ó', "o"), Map.entry('ô', "o"), Map.entry('ö', "o"),
            Map.entry('ú', "u"), Map.entry('ü', "u"), Map.entry('û', "u"),
            Map.entry('ç', "c"), Map.entry('ñ', "n"), Map.entry('ß', "ss"));

    private Codecs() {
    }

    /**
     * Run-length encoding: a run of two or more identical characters becomes the run
     * length followed by the character. {@code "aaabbc"} becomes {@code "3a2bc"}.
     */
    public static String compress(String input) {
        StringBuilder out = new StringBuilder();
        int i = 0;
        while (i < input.length()) {
            char c = input.charAt(i);
            int run = 1;
            while (i + run < input.length() && input.charAt(i + run) == c) {
                run++;
            }
            if (run >= 2) {
                out.append(run);
            }
            out.append(c);
            i += run;
        }
        return out.toString();
    }

    /** Folds accented letters onto their 7-bit equivalents; anything else non-ASCII becomes '?'. */
    public static String toAscii7(String input) {
        StringBuilder out = new StringBuilder();
        for (char c : input.toCharArray()) {
            if (c < 128) {
                out.append(c);
            } else {
                String folded = FOLDINGS.get(Character.toLowerCase(c));
                if (folded == null) {
                    out.append('?');
                } else if (Character.isUpperCase(c)) {
                    out.append(folded.toUpperCase());
                } else {
                    out.append(folded);
                }
            }
        }
        return out.toString();
    }
}
