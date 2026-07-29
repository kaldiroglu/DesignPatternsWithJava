/**
 * The book example with its defects fixed — the version to read second.
 *
 * <p>{@link Character} keeps only what a letter <em>is</em>: its value and its case, both
 * final, with a package-private constructor so that only {@link CharacterFactory} can mint
 * one. {@link Line} holds position as the index in its list, which is where it turned out
 * to need no home at all.</p>
 *
 * <p>Five things changed from {@code book.wrong}, and every one of them is asserted by
 * {@code BookFlyweightTest} against both packages:</p>
 *
 * <ol>
 *   <li><b>The factory pools.</b> Two requests for the same letter return the same object,
 *       and {@link CharacterFactory#savedCount()} makes the saving a number.</li>
 *   <li><b>The flyweight holds no extrinsic state.</b> The {@code line} and {@code position}
 *       fields are gone, which is what makes sharing possible at all.</li>
 *   <li><b>Intrinsic state is used.</b> {@code upperCase} now affects
 *       {@link Character#getValue()}, so a capital renders as one.</li>
 *   <li><b>The capacity check is right.</b> A line built for ten holds ten.</li>
 *   <li><b>Nothing walks around the check.</b> There is no second path into the list.</li>
 * </ol>
 *
 * <p>236 characters of text cost <b>29</b> objects.</p>
 *
 * @see dev.kaldiroglu.dp.structural.flyweight.book.wrong
 */
package dev.kaldiroglu.dp.structural.flyweight.book.correct;
