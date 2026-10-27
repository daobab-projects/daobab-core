package io.daobab.statement.condition;

import io.daobab.error.AtLeastOneColumnToUpdateIsRequired;
import io.daobab.target.buffer.query.BufferQueryUpdate;
import io.daobab.target.database.query.DataBaseQueryUpdate;
import io.daobab.test.dao.table.Film;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Assignments in {@link SetFields} occupy the pointers {@code 1 .. getCounter() - 1}; pointer 0 is never used.
 * Code that iterated from 0, or took {@code getCounter() == 0} for "empty", read past that layout.
 */
class SetFieldsTest {

    private static Film film() {
        return new Film().setTitle("ACADEMY DINOSAUR").setDescription("An epic drama");
    }

    @Test
    void valueForColumnFindsTheOnlyAssignment() {
        Film film = film();
        SetFields fields = SetFields.setValuesArray(film, film.colTitle());

        assertEquals("ACADEMY DINOSAUR", fields.getValueForColumn(film.colTitle()));
    }

    @Test
    void valueForColumnFindsEveryAssignment() {
        Film film = film();
        SetFields fields = SetFields.setValuesArray(film, film.colTitle(), film.colDescription());

        assertEquals("ACADEMY DINOSAUR", fields.getValueForColumn(film.colTitle()));
        assertEquals("An epic drama", fields.getValueForColumn(film.colDescription()));
    }

    @Test
    void valueForColumnIsNullForColumnNotAssigned() {
        Film film = film();
        SetFields fields = SetFields.setValuesArray(film, film.colTitle());

        assertNull(fields.getValueForColumn(film.colDescription()));
    }

    @Test
    void emptySetHasSizeZero() {
        assertEquals(0, new SetFields().size());
    }

    @Test
    void updateWithoutColumnsIsRejectedWithDedicatedError() {
        SetFields empty = new SetFields();

        assertThrows(AtLeastOneColumnToUpdateIsRequired.class, () -> new DataBaseQueryUpdate<Film>(null, empty));
        assertThrows(AtLeastOneColumnToUpdateIsRequired.class, () -> new BufferQueryUpdate<Film>(null, empty));
    }
}
