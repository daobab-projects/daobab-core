package io.daobab.target.util;

import io.daobab.model.Entity;
import io.daobab.target.buffer.BufferQueryTarget;
import io.daobab.target.buffer.query.*;
import io.daobab.target.database.DataBaseTarget;
import io.daobab.target.database.query.*;

@SuppressWarnings("unused")
public class QueryParser {

    public DataBaseQueryPlate toDataBase(DataBaseTarget target, BufferQueryPlate bufferQueryPlate) {
        return new DataBaseQueryPlate(target, bufferQueryPlate.toRemote(false));
    }

    public <E extends Entity, F> DataBaseQueryField<E, F> toDataBase(DataBaseTarget target, BufferQueryField<E, F> bufferQueryPlate) {
        return new DataBaseQueryField<>(target, bufferQueryPlate.toRemote(false));
    }

    public <E extends Entity> DataBaseQueryDelete<E> toDataBase(DataBaseTarget target, BufferQueryDelete<E> bufferQueryPlate) {
        return new DataBaseQueryDelete<>(target, bufferQueryPlate.toRemote(false));
    }

    public <E extends Entity> DataBaseQueryEntity<E> toDataBase(DataBaseTarget target, BufferQueryEntity<E> bufferQueryPlate) {
        return new DataBaseQueryEntity<>(target, bufferQueryPlate.toRemote(false));
    }

    public <E extends Entity> DataBaseQueryUpdate<E> toDataBase(DataBaseTarget target, BufferQueryUpdate<E> bufferQueryPlate) {
        return new DataBaseQueryUpdate<>(target, bufferQueryPlate.toRemote(false));
    }

    public <E extends Entity> DataBaseQueryInsert<E> toDataBase(DataBaseTarget target, BufferQueryInsert<E> bufferQueryPlate) {
        return new DataBaseQueryInsert<>(target, bufferQueryPlate.toRemote(false));
    }


    public BufferQueryPlate toBuffer(BufferQueryTarget target, DataBaseQueryPlate queryPlate) {
        return new BufferQueryPlate(target, queryPlate.toRemote(false));
    }

    public <E extends Entity, F> BufferQueryField<E, F> toBuffer(BufferQueryTarget target, DataBaseQueryField<E, F> queryPlate) {
        return new BufferQueryField<>(target, queryPlate.toRemote(false));
    }

    public <E extends Entity> BufferQueryDelete<E> toBuffer(BufferQueryTarget target, DataBaseQueryDelete<E> queryPlate) {
        return new BufferQueryDelete<>(target, queryPlate.toRemote(false));
    }

    public <E extends Entity> BufferQueryEntity<E> toBuffer(BufferQueryTarget target, DataBaseQueryEntity<E> queryPlate) {
        return new BufferQueryEntity<>(target, queryPlate.toRemote(false));
    }

    public <E extends Entity> BufferQueryUpdate<E> toBuffer(BufferQueryTarget target, DataBaseQueryUpdate<E> queryPlate) {
        return new BufferQueryUpdate<>(target, queryPlate.toRemote(false));
    }

    public <E extends Entity> BufferQueryInsert<E> toBuffer(BufferQueryTarget target, DataBaseQueryInsert<E> queryPlate) {
        return new BufferQueryInsert<>(target, queryPlate.toRemote(false));
    }
}
