package io.daobab.target.buffer.function.command.type;

/**
 * How a {@link BufferFunction} is applied over a buffer: {@link #AGGREGATED} collapses the whole buffer to a
 * single result row, {@link #NORMAL} transforms each row independently.
 *
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public enum FunctionType {
    /**
     * Collapses the whole buffer to a single result (e.g. {@code SUM}, {@code COUNT}).
     */
    AGGREGATED,
    /**
     * Transforms each row independently (e.g. {@code UPPER}, {@code LENGTH}).
     */
    NORMAL
}
