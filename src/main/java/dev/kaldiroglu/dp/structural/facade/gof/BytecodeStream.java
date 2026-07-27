package dev.kaldiroglu.dp.structural.facade.gof;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Subsystem class (GoF, p. 185): the output of compilation. A
 * {@link CodeGenerator} writes instructions here; in the book it is a stream of
 * bytecodes, here a readable list of instruction strings.
 */
public class BytecodeStream {

    private final List<String> instructions = new ArrayList<>();

    public void put(String instruction) {
        instructions.add(instruction);
    }

    public List<String> instructions() {
        return Collections.unmodifiableList(instructions);
    }

    @Override
    public String toString() {
        return String.join("\n", instructions);
    }
}
