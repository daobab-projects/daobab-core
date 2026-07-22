package io.daobab.target.database.converter.type;


import io.daobab.model.Entity;
import io.daobab.model.PrimaryKey;
import io.daobab.target.database.DataBaseTarget;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Base converter that resolves a key value of type {@code F} into a list of referenced entities {@code E}
 * (a one-to-many {@link PrimaryKey} lookup). The key is not read straight from the result set here; it is
 * resolved through the {@link #getDataBaseTarget() target} against the referenced {@link #getTable() table}.
 *
 * @param <F> the key value type
 * @param <E> the referenced entity type, carrying a {@link PrimaryKey} of type {@code F}
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public abstract class TypeConverterPKBasedList<F, E extends Entity & PrimaryKey<?, F, ?>> implements DatabaseTypeConverter<F, List<E>> {

    /**
     * Not used by this converter: the key is resolved into entities rather than read directly, so this
     * always returns {@code null}.
     */
    public F readFromResultSet(ResultSet rs, int columnIndex) throws SQLException {
        return null;
    }

    /**
     * The entity whose key this converter resolves.
     */
    public abstract E getTable();

    /**
     * The target used to look the referenced entities up.
     */
    public abstract DataBaseTarget getDataBaseTarget();


}
