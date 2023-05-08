package io.daobab.target.database.converter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Consumer;

public interface KeyableCache<F, E> {

    void addKey(F key, Consumer<E> consumer);

    F readFromResultSet(ResultSet rs, int columnIndex) throws SQLException;
}
