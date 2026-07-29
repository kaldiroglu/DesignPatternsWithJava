/**
 * The book example as it was first written — a factory, a character, lines and pages, and
 * the word "flyweight" nowhere justified by the code.
 *
 * <p>The classes here are kept <b>exactly as written</b>, because what is wrong with them is
 * the lesson. Every one of these is the mistake the pattern exists to prevent, and every one
 * of them is asserted by {@code BookFlyweightTest} so that it cannot be quietly fixed here
 * instead of in {@code book.correct}.</p>
 *
 * <ol>
 *   <li><b>The factory shares nothing.</b> {@link BookFactory#createCharacter} calls
 *       {@code new Character(...)} on every request, so a page of a thousand letters holds a
 *       thousand objects. Its {@code as} field — presumably meant to be the pool — is never
 *       read or written. A FlyweightFactory that always allocates is just a constructor with
 *       extra steps.</li>
 *
 *   <li><b>The flyweight stores extrinsic state.</b> {@link Character} carries {@code line}
 *       and {@code position} fields, and {@link Line#add} writes them. The comments in the
 *       class label them "Extrinsic properties" while storing them intrinsically — which is
 *       precisely what makes sharing impossible. Add pooling to defect 1 without fixing this
 *       one and the code breaks: the second 'o' in "book" would overwrite the position of the
 *       first, and both would claim to live at the same place.</li>
 *
 *   <li><b>Intrinsic state that is never used.</b> {@code createCharacter('t', true)} records
 *       {@code upperCase} and {@link Character#getValue()} ignores it, so the demo asks for a
 *       capital T and prints "this book" in lower case.</li>
 *
 *   <li><b>An off-by-one in the capacity check.</b> {@link Line#add} declares the line full at
 *       {@code numberOfCharacters + 1}, so a line built to hold ten characters accepts
 *       eleven.</li>
 *
 *   <li><b>A path around the check.</b> {@link Line#addEndOfLine()} appends straight to the
 *       list without consulting {@code full}, without setting the line, and without setting a
 *       position — so it can overflow a line that has just refused an ordinary character.</li>
 * </ol>
 *
 * @see dev.kaldiroglu.dp.structural.flyweight.book.correct
 */
package dev.kaldiroglu.dp.structural.flyweight.book.wrong;
