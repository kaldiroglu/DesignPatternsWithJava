/**
 * The real design — the same bill of materials, modelled <b>with</b> the
 * Composite pattern.
 *
 * <p>Compare it class by class with {@code ..bom.problem}. Both build the same
 * bicycle from the same figures in {@code ..bom.domain.Catalog} and both get the
 * same totals; everything else is different.</p>
 *
 * <table border="1">
 *   <caption>Pattern roles</caption>
 *   <tr><th>Role</th><th>Class</th></tr>
 *   <tr><td>Component</td><td>{@link dev.kaldiroglu.composite.bom.solution.BomComponent}</td></tr>
 *   <tr><td>Leaf</td><td>{@link dev.kaldiroglu.composite.bom.solution.Part},
 *       {@link dev.kaldiroglu.composite.bom.solution.Service}</td></tr>
 *   <tr><td>Composite</td><td>{@link dev.kaldiroglu.composite.bom.solution.Assembly}</td></tr>
 *   <tr><td>Client</td><td>{@link dev.kaldiroglu.dp.structural.composite.bom.solution.Main}</td></tr>
 * </table>
 *
 * <table border="1">
 *   <caption>What changed, against the naive design</caption>
 *   <tr><th>In {@code problem}</th><th>Here</th></tr>
 *   <tr><td>No supertype; operations are static functions over {@code Object}</td>
 *       <td>One {@code BomComponent} type; operations are methods on it</td></tr>
 *   <tr><td>The recursion is written three times, in the clients</td>
 *       <td>The recursion is written once, in {@code Assembly}</td></tr>
 *   <tr><td>Two child collections</td>
 *       <td>One collection of {@link dev.kaldiroglu.composite.bom.solution.BomLine}</td></tr>
 *   <tr><td>32 spokes are 32 list entries; two wheels are two objects</td>
 *       <td>Quantity lives on the edge: 13 list entries, one wheel object</td></tr>
 *   <tr><td>A new kind of item breaks every client</td>
 *       <td>{@link dev.kaldiroglu.composite.bom.solution.Service} was added and nothing else changed</td></tr>
 * </table>
 *
 * <p>The design decisions — quantity on the edge, cached roll-ups, parent
 * references, the run-time cycle check, and why the child operations are declared
 * on {@code Assembly} rather than on {@code BomComponent} — are argued in
 * {@code Composite - Problem and Solution.md}.</p>
 */
package dev.kaldiroglu.dp.structural.composite.bom.solution;
