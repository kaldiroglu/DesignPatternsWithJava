/**
 * A circle animation written as an illustration of Flyweight, which it is not.
 *
 * <p>Kept exactly as written, because it is the honest starting point: it looks like the
 * pattern — there is a {@code Factory}, a {@code create()}, a comment naming intrinsic and
 * extrinsic state — and none of the machinery does what those names promise. Everything
 * listed below is asserted by {@code CirclesFlyweightTest}.</p>
 *
 * <ol>
 *   <li><b>Nothing is shared.</b> {@link CircleFactory#create()} calls {@code new Circle(...)}
 *       on every request. A thousand circles are a thousand objects, which is the situation
 *       the pattern exists to change.</li>
 *
 *   <li><b>The labels are the wrong way round.</b> {@link Circle} marks {@code canvas} as
 *       intrinsic and {@code center}, {@code color} and {@code radius} as extrinsic — and
 *       then stores all four as fields. Extrinsic state that lives in a field is not
 *       extrinsic; it is just state, and it is what makes the object unshareable.</li>
 *
 *   <li><b>The state is mutable, and mutated from another thread.</b>
 *       {@link CircleThread#run()} calls {@code setCenter}, {@code setColor} and
 *       {@code setRadius} while the event dispatch thread is painting. Sharing an object like
 *       this between two drawings would not save memory, it would produce tearing.</li>
 *
 *   <li><b>Painting asks to be painted again.</b> {@link CirclesCanvas#paintComponent} calls
 *       {@code showUp()} on every circle, and {@code showUp()} calls {@code canvas.repaint()}.
 *       Each paint schedules the next one, forever, whether or not anything changed.</li>
 *
 *   <li><b>The appearance counter races.</b> {@code numberOfAppearance} is a plain
 *       {@code static int} incremented from the animation threads and from the paint thread
 *       at once, so the number the Stop button shows is not reliably any number at all.</li>
 *
 *   <li><b>Stop is permanent.</b> {@link CircleThread} keeps {@code runnable} in a static
 *       field, so stopping one circle stops every circle — and every circle started
 *       afterwards exits immediately.</li>
 *
 *   <li><b>Smaller things.</b> {@code updateCircle(Circle c)} ignores its parameter and works
 *       on the field. {@link Circle} extends {@code JComponent} but is never added to a
 *       container. {@code CirclesFrame} keeps {@code width}/{@code height} fields of 600×800
 *       that its constructor never updates, so circles are placed inside a rectangle that is
 *       not the one on screen. {@link ButtonListener} is dead code with two empty branches.</li>
 * </ol>
 *
 * @see dev.kaldiroglu.dp.structural.flyweight.circles.solution
 */
package dev.kaldiroglu.dp.structural.flyweight.circles.problem;
