package io.daobab.target.database.converter.type;


import io.daobab.model.Entity;
import io.daobab.model.PrimaryKey;
import io.daobab.target.database.DataBaseTarget;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Base converter that resolves a foreign key value of type {@code F} into the referenced entity {@code E}
 * (a {@link PrimaryKey} lookup). The key is not read straight from the result set here; it is resolved through
 * the {@link #getDataBaseTarget() target} against the referenced {@link #getTable() table}.
 *
 * @param <F> the foreign key (primary key value) type
 * @param <E> the referenced entity type, carrying a {@link PrimaryKey} of type {@code F}
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public abstract class TypeConverterPKBased<F, E extends Entity & PrimaryKey<?, F, ?>> implements DatabaseTypeConverter<F, E> {

    /**
     * Not used by this converter: the key is resolved into an entity rather than read directly, so this
     * always returns {@code null}.
     */
    public F readFromResultSet(ResultSet rs, int columnIndex) throws SQLException {
        return null;
    }

    /**
     * The entity whose primary key this converter resolves.
     */
    public abstract E getTable();

    /**
     * The target used to look the referenced entity up.
     */
    public abstract DataBaseTarget getDataBaseTarget();


}
