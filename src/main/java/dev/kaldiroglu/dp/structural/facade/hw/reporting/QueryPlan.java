package dev.kaldiroglu.dp.structural.facade.hw.reporting;

/** A subsystem type. Nothing outside the reporting subsystem should need to name it. */
public record QueryPlan(String table, String filter, int limit) { }
