package io.daobab.converter.json;

import io.daobab.converter.json.type.*;
import io.daobab.creation.ColumnCreator;
import io.daobab.creation.DaobabCache;
import io.daobab.error.DaobabException;
import io.daobab.model.Column;
import io.daobab.test.dao.SakilaTables;
import io.daobab.test.dao.table.Actor;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Converter resolution for a queried column: the wrapper types ({@code Optional}, {@code List}, {@code Set}),
 * an entity-typed column and a plain value column each have to reach their own converter. The assignability
 * tests used to be written the other way round, so only a column declared exactly as {@code Optional} was
 * recognized - a {@code List}/{@code Set} column silently fell through to the plain type lookup and an
 * entity-typed column never reached {@link JsonDaobabEntityConverter} at all.
 */
class TestJsonConverterResolution implements SakilaTables {

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static Column column(String fieldName, Class<?> fieldClass) {
        return ColumnCreator.createColumn(fieldName, fieldName, (Actor) tabActor, (Class) fieldClass);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static Column column(String fieldName, Class<?> fieldClass, Class<?> innerClass) {
        return ColumnCreator.createInnerTypeColumn(fieldName, fieldName, (Actor) tabActor, (Class) fieldClass, innerClass);
    }

    private static Class<?> converterOf(Column<?, ?, ?> column) {
        return JsonConverterManager.INSTANCE.getConverter(column)
                .orElseThrow(() -> new AssertionError("no converter resolved for " + column))
                .getClass();
    }

    @Test
    void listColumnResolvesToTheListConverter() {
        assertEquals(JsonListConverter.class, converterOf(column("ResolutionTags", List.class, String.class)));
    }

    @Test
    void setColumnResolvesToTheSetConverter() {
        assertEquals(JsonSetConverter.class, converterOf(column("ResolutionLabels", Set.class, String.class)));
    }

    @Test
    void optionalColumnResolvesToTheOptionalConverter() {
        assertEquals(JsonOptionalConverter.class, converterOf(column("ResolutionMaybeLength", Optional.class, Integer.class)));
    }

    @Test
    void plainColumnResolvesToItsTypeConverter() {
        assertEquals(JsonStringConverter.class, converterOf(column("ResolutionTitle", String.class)));
    }

    @Test
    void entityColumnResolvesToTheEntityConverter() {
        assertEquals(JsonDaobabEntityConverter.class, converterOf(column("ResolutionActor", Actor.class)));
    }

    @Test
    void aWrapperColumnWithoutAnInnerTypeIsRejected() {
        Column<?, ?, ?> noInnerType = column("ResolutionNoInnerType", List.class);
        DaobabException thrown = assertThrows(DaobabException.class,
                () -> JsonConverterManager.INSTANCE.getConverter(noInnerType));
        assertEquals(true, thrown.getMessage().contains("InnerFieldClass"), thrown.getMessage());
    }

    @Test
    void theColumnCacheRejectsACollectionWithoutAnInnerType() {
        assertThrows(DaobabException.class,
                () -> DaobabCache.getColumn("ResolutionCachedList", "RESOLUTION_CACHED_LIST", (Actor) tabActor, List.class));
    }

    @Test
    void theSameEntityAlwaysSharesOneConversion() {
        //the conversion cache is keyed by the entity class, so a second instance must not add an entry
        assertEquals(JsonConverterManager.INSTANCE.getEntityJsonConverter(new Actor()),
                JsonConverterManager.INSTANCE.getEntityJsonConverter(new Actor()));
    }
}
