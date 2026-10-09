package dev.kaldiroglu.dp.behavioral.templateMethod.hw.callcenter;

import java.util.List;

/**
 * Imports the calls of three call centers. Ankara's second recording is too short, and
 * the verification step, which no call center can skip, rejects it.
 */
public final class Main {

    public static void main(String[] args) {
        List<CallImport> imports = List.of(
                new IstanbulCallCenter(), new AnkaraCallCenter(), new IzmirCallCenter());
        for (CallImport callImport : imports) {
            callImport.run();
            System.out.println(callImport.getClass().getSimpleName()
                    + ": stored " + callImport.stored() + ", rejected " + callImport.rejected());
        }
    }
}
