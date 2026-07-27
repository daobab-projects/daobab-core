package io.daobab.target.database;

import io.daobab.error.DaobabException;
import io.daobab.model.Entity;
import io.daobab.model.Plate;
import io.daobab.target.buffer.single.Entities;
import io.daobab.target.buffer.single.EntityList;
import io.daobab.target.buffer.single.PlateBuffer;
import io.daobab.target.database.query.DataBaseQueryEntity;
import io.daobab.target.database.query.DataBaseQueryPlate;
import io.daobab.test.dao.table.Actor;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Verifies {@code readRecord} / {@code readRecordList} on a database target for both the plate and the entity
 * queries: the selected columns are mapped onto the record components in order, and a component-count mismatch
 * fails. The low-level reads are stubbed so the mapping is exercised without a real database.
 */
class TestReadRecord {

    private static final Timestamp TS = Timestamp.valueOf("2026-01-01 10:00:00");
    private static final Actor TAB = new Actor();

    private static Actor actor(int id, String first, String last) {
        return new Actor().setActorId(id).setFirstName(first).setLastName(last).setLastUpdate(TS);
    }

    @Test
    void readRecordFromPlateMapsColumnsInOrder() {
        StubTarget target = new StubTarget();
        target.onePlate = new Plate(actor(1, "NICK", "WAHLBERG"));

        DataBaseQueryPlate query = target.select(TAB.colActorId(), TAB.colFirstName());
        ActorNames rec = target.readRecord(query, ActorNames.class);

        assertEquals(new ActorNames(1, "NICK"), rec);
    }

    @Test
    void readRecordListFromPlateMapsEveryRow() {
        StubTarget target = new StubTarget();
        target.manyPlates.add(new Plate(actor(1, "NICK", "WAHLBERG")));
        target.manyPlates.add(new Plate(actor(2, "ED", "CHASE")));

        DataBaseQueryPlate query = target.select(TAB.colActorId(), TAB.colFirstName());
        List<ActorNames> recs = target.readRecordList(query, ActorNames.class);

        assertEquals(List.of(new ActorNames(1, "NICK"), new ActorNames(2, "ED")), recs);
    }

    // ---------------------------------------------------------------- plate query

    @Test
    void readRecordFromPlateReturnsNullWhenNoRow() {
        StubTarget target = new StubTarget();
        target.onePlate = null;

        DataBaseQueryPlate query = target.select(TAB.colActorId(), TAB.colFirstName());
        assertNull(target.readRecord(query, ActorNames.class));
    }

    @Test
    void readRecordFromPlateRejectsComponentCountMismatch() {
        StubTarget target = new StubTarget();
        DataBaseQueryPlate query = target.select(TAB.colActorId(), TAB.colFirstName());

        // a 4-component record against a 2-column query
        DaobabException ex = assertThrows(DaobabException.class,
                () -> target.readRecord(query, ActorFull.class));
        assertTrue(ex.getMessage().contains("match the selected columns"), ex.getMessage());
    }

    @Test
    void readRecordFromEntityMapsColumnsInOrder() {
        StubTarget target = new StubTarget();
        target.oneActor = actor(5, "PENELOPE", "GUINESS");

        DataBaseQueryEntity<Actor> query = target.select(TAB);
        ActorFull rec = target.readRecord(query, ActorFull.class);

        assertEquals(new ActorFull(5, "PENELOPE", "GUINESS", TS), rec);
    }

    @Test
    void readRecordListFromEntityMapsEveryRow() {
        StubTarget target = new StubTarget();
        target.manyActors.add(actor(5, "PENELOPE", "GUINESS"));
        target.manyActors.add(actor(6, "NICK", "WAHLBERG"));

        DataBaseQueryEntity<Actor> query = target.select(TAB);
        List<ActorFull> recs = target.readRecordList(query, ActorFull.class);

        assertEquals(List.of(
                new ActorFull(5, "PENELOPE", "GUINESS", TS),
                new ActorFull(6, "NICK", "WAHLBERG", TS)), recs);
    }

    // ---------------------------------------------------------------- entity query

    @Test
    void readRecordFromEntityReturnsNullWhenNoRow() {
        StubTarget target = new StubTarget();
        target.oneActor = null;

        DataBaseQueryEntity<Actor> query = target.select(TAB);
        assertNull(target.readRecord(query, ActorFull.class));
    }

    @Test
    void readRecordFromEntityRejectsComponentCountMismatch() {
        StubTarget target = new StubTarget();
        DataBaseQueryEntity<Actor> query = target.select(TAB);

        // a 2-component record against the 4-column entity
        DaobabException ex = assertThrows(DaobabException.class,
                () -> target.readRecordList(query, ActorNames.class));
        assertTrue(ex.getMessage().contains("match the selected columns"), ex.getMessage());
    }

    record ActorNames(Integer actorId, String firstName) {
    }

    record ActorFull(Integer actorId, String firstName, String lastName, Timestamp lastUpdate) {
    }

    /**
     * A target that stubs the row reads so {@code readRecord}/{@code readRecordList} can be exercised without a
     * database connection - they still run their real mapping over whatever these return.
     */
    private static final class StubTarget extends MockDataBase {

        final List<Plate> manyPlates = new ArrayList<>();
        final List<Actor> manyActors = new ArrayList<>();
        Plate onePlate;
        Actor oneActor;

        @Override
        public Plate readPlate(DataBaseQueryPlate query) {
            return onePlate;
        }

        @Override
        public PlateBuffer readPlateList(DataBaseQueryPlate query) {
            return new PlateBuffer(new ArrayList<>(manyPlates));
        }

        @SuppressWarnings("unchecked")
        @Override
        public <E extends Entity> E readEntity(DataBaseQueryEntity<E> query) {
            return (E) oneActor;
        }

        @SuppressWarnings("unchecked")
        @Override
        public <E extends Entity> Entities<E> readEntityList(DataBaseQueryEntity<E> query) {
            return (Entities<E>) new EntityList<>(new ArrayList<>(manyActors), Actor.class);
        }
    }
}
