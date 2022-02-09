package io.daobab.target.buffer.noheap;

import io.daobab.model.Column;
import io.daobab.model.TableColumn;
import io.daobab.target.buffer.noheap.access.array.*;
import io.daobab.target.buffer.noheap.access.field.*;
import io.daobab.target.buffer.noheap.access.index.*;
import io.daobab.target.buffer.noheap.index.BitBufferIndexBase;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.function.Function;

@SuppressWarnings("unchecked")
public class BitFieldRegistry {

    //    public static final BitFieldRegistry instance=new BitFieldRegistry();
    private final Set<Class<?>> classSet = new HashSet<>();
    private final Map<Class<?>, Function<TableColumn, ?>> bitFieldMap = new HashMap<>();
    private final Map<Class<?>, Function<TableColumn, ?>> bitFieldNotNullMap = new HashMap<>();
    private final Map<Class<?>, TriFunction<?, ?, ?, ?>> bitBufferIndexBaseMap = new HashMap<>();
    private final Map<Class<?>, Function<TableColumn, ?>> bitArrayBaseMap = new HashMap<>();
    private final Map<Class<?>, Function<TableColumn, ?>> bitArrayBaseNotNullMap = new HashMap<>();

    public BitFieldRegistry() {
        register(String.class, BitFieldString::new, BitFieldStringNotNull::new);
        registerArray(String.class, BitArrayString::new);
        registerArrayNotNull(String.class, BitArrayStringNotNull::new);
        registerIndex(String.class, BitIndexString::new);

        register(Integer.class, BitFieldInteger::new, BitFieldIntegerNotNull::new);
        registerArray(Integer.class, BitArrayInteger::new);
        registerArrayNotNull(Integer.class, BitArrayIntegerNotNull::new);
        registerIndex(Integer.class, BitIndexInteger::new);

        register(Long.class, BitFieldLong::new, BitFieldLongNotNull::new);
        registerArray(Long.class, BitArrayLong::new);
        registerArrayNotNull(Long.class, BitArrayLongNotNull::new);
        registerIndex(Long.class, BitIndexLong::new);

        register(Long.class, BitFieldLong::new, BitFieldLongNotNull::new);
        registerArray(Long.class, BitArrayLong::new);
        registerArrayNotNull(Long.class, BitArrayLongNotNull::new);
        registerIndex(Long.class, BitIndexLong::new);

        register(Double.class, BitFieldDouble::new, BitFieldDoubleNotNull::new);
        registerArray(Double.class, BitArrayDouble::new);
        registerArrayNotNull(Double.class, BitArrayDoubleNotNull::new);
        registerIndex(Double.class, BitIndexDouble::new);

        register(Float.class, BitFieldFloat::new, BitFieldFloatNotNull::new);
        registerArray(Float.class, BitArrayFloat::new);
        registerArrayNotNull(Float.class, BitArrayFloatNotNull::new);
        registerIndex(Float.class, BitIndexFloat::new);

        register(Short.class, BitFieldShort::new, BitFieldShortNotNull::new);
        registerArray(Short.class, BitArrayShort::new);
        registerArrayNotNull(Short.class, BitArrayShortNotNull::new);
        registerIndex(Short.class, BitIndexShort::new);

        register(Date.class, BitFieldDate::new, BitFieldDateNotNull::new);
        registerArray(Date.class, BitArrayDate::new);
        registerArrayNotNull(Date.class, BitArrayDateNotNull::new);
        registerIndex(Date.class, BitIndexDate::new);


        register(java.sql.Date.class, BitFieldSqlDate::new, BitFieldSqlDateNotNull::new);
        register(java.sql.Timestamp.class, BitFieldTimestamp::new, BitFieldTimestampNotNull::new);
//        register(Character.class,new BitFieldCharacterNotNull::new);
        register(BigDecimal.class, BitFieldBigDecimal::new, BitFieldBigDecimalNotNull::new);
        register(BigInteger.class, BitFieldBigInteger::new, BitFieldBigIntegerNotNull::new);
    }


    public <F, B extends BitField<F>> void registerIndex(final Class<F> clazz, final TriFunction<TableColumn, SortedMap<F, Collection<Integer>>, List<Integer>, AbstractBitIndex<F, B, ?>> bitIndex) {
        bitBufferIndexBaseMap.put(clazz, bitIndex);
    }

    public <F, B extends BitField<F>> void register(final Class<F> clazz, final Function<TableColumn, B> bitField, final Function<TableColumn, B> bitFieldNotNull) {
        bitFieldMap.put(clazz, bitField);
        bitFieldNotNullMap.put(clazz, bitFieldNotNull);
    }

    public <F, B extends BitField<F>> void registerArray(final Class<F> clazz, final Function<TableColumn, BitArrayBase<F, B>> bitArray) {
        bitArrayBaseMap.put(clazz, bitArray);
    }

    public <F, B extends BitField<F>> void registerArrayNotNull(final Class<F> clazz, Function<TableColumn, BitArrayBaseNotNull<F, B>> bitArrayBaseNotNull) {
        bitArrayBaseNotNullMap.put(clazz, bitArrayBaseNotNull);
    }

    public <F> BitField<F> getBitFieldFor(Class<F> clazz, TableColumn tableColumn) {
        return (BitField<F>) bitFieldMap.get(clazz).apply(tableColumn);
    }

    public <F> BitField<F> getBitFieldFor(TableColumn tableColumn) {
        return (BitField<F>) bitFieldMap.get(tableColumn.getColumn().getFieldClass()).apply(tableColumn);
    }

    public <F> BitBufferIndexBase<F> getBitIndexFor(Class<F> clazz) {
        return (BitBufferIndexBase<F>) bitBufferIndexBaseMap.get(clazz);
    }

    public <F> BitArrayBase<F, ?> getBitArrayBaseFor(Class<F> clazz) {
        return (BitArrayBase<F, ?>) bitArrayBaseMap.get(clazz);
    }

    public <F> BitArrayBaseNotNull<F, ?> getArrayBaseNotNullFor(Class<F> clazz) {
        return (BitArrayBaseNotNull<F, ?>) bitArrayBaseNotNullMap.get(clazz);
    }

    protected boolean mayBeBitBuffered(Column col) {
        for (Class<?> cl : classSet) {
            if (cl.isAssignableFrom(col.getFieldClass())) {
                return true;
            }
        }
        return false;
    }

}
