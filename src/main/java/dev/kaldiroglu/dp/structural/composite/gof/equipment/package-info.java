/**
 * The Composite Sample Code from GoF "Design Patterns", pp. 170–173: computer
 * equipment that can be assembled into ever larger units, where price and power
 * queries are answered identically for a single card and for a whole cabinet.
 *
 * <table border="1">
 *   <caption>Pattern roles</caption>
 *   <tr><th>Role</th><th>Class</th></tr>
 *   <tr><td>Component</td><td>{@link dev.kaldiroglu.composite.gof.equipment.Equipment}</td></tr>
 *   <tr><td>Leaf</td><td>{@link dev.kaldiroglu.composite.gof.equipment.FloppyDisk},
 *       {@link dev.kaldiroglu.composite.gof.equipment.Card}</td></tr>
 *   <tr><td>Composite</td><td>{@link dev.kaldiroglu.composite.gof.equipment.CompositeEquipment}
 *       and its subclasses {@link dev.kaldiroglu.composite.gof.equipment.Chassis},
 *       {@link dev.kaldiroglu.composite.gof.equipment.Cabinet},
 *       {@link dev.kaldiroglu.composite.gof.equipment.Bus}</td></tr>
 *   <tr><td>Client</td><td>{@link dev.kaldiroglu.composite.gof.equipment.Demo}</td></tr>
 * </table>
 */
package dev.kaldiroglu.dp.structural.composite.gof.equipment;
