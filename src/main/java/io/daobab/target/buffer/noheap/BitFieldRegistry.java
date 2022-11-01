package io.daobab.target.buffer.noheap;

import io.daobab.model.*;
import io.daobab.target.buffer.noheap.access.array.*;
import io.daobab.target.buffer.noheap.access.field.*;
import io.daobab.target.buffer.noheap.access.index.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.*;
import java.util.function.Function;

@SuppressWarnings("unchecked")
public class BitFieldRegistry {

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

        register(Timestamp.class, BitFieldTimestamp::new, BitFieldTimestampNotNull::new);
        registerArray(Timestamp.class, BitArrayTimestamp::new);
        registerArrayNotNull(Timestamp.class, BitArrayTimestampNotNull::new);
        registerIndex(Timestamp.class, BitIndexTimestamp::new);

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

        register(BigDecimal.class, BitFieldBigDecimal::new, BitFieldBigDecimalNotNull::new);
        registerArray(BigDecimal.class, BitArrayBigDecimal::new);
        registerArrayNotNull(BigDecimal.class, BitArrayBigDecimalNotNull::new);
        registerIndex(BigDecimal.class, BitIndexBigDecimal::new);


        register(java.sql.Date.class, BitFieldSqlDate::new, BitFieldSqlDateNotNull::new);
//        register(Character.class,new BitFieldCharacterNotNull::new);
        register(BigInteger.class, BitFieldBigInteger::new, BitFieldBigIntegerNotNull::new);
    }


    public <F, B extends BitField<F>> void registerIndex(final Class<F> clazz, final TriFunction<TableColumn, SortedMap<F, Collection<Integer>>, List<Integer>, BitIndex<F, B, ?>> bitIndex) {
        bitBufferIndexBaseMap.put(clazz, bitIndex);
    }

    public <F, B extends BitField<F>> void register(final Class<F> clazz, final Function<TableColumn, B> bitField, final Function<TableColumn, B> bitFieldNotNull) {
        classSet.add(clazz);
        bitFieldMap.put(clazz, bitField);
        bitFieldNotNullMap.put(clazz, bitFieldNotNull);
    }

    public <F, B extends BitField<F>> void registerArray(final Class<F> clazz, final Function<TableColumn, BitArrayBase<F, B>> bitArray) {
        bitArrayBaseMap.put(clazz, bitArray);
    }

    public <F, B extends BitField<F>> void registerArrayNotNull(final Class<F> clazz, Function<TableColumn, BitArrayBaseNotNull<F, B>> bitArrayBaseNotNull) {
        bitArrayBaseNotNullMap.put(clazz, bitArrayBaseNotNull);
    }

    public <F> Optional<BitField<F>> getBitFieldFor(Class<F> clazz, TableColumn tableColumn) {
        return getOrNull(tableColumn, (Function<TableColumn, BitField<F>>) bitFieldMap.get(clazz));
    }

    public <F> Optional<BitField<F>> getBitFieldFor(TableColumn tableColumn) {
        return getBitFieldFor(tableColumn.getColumn().getFieldClass(), tableColumn);
    }

    private <X> Optional<X> getOrNull(TableColumn tableColumn, Function<TableColumn, X> function) {
        if (function == null) {
            return Optional.empty();
        }
        return Optional.of(function.apply(tableColumn));
    }

    public <F, E extends ColumnsProvider, B extends BitField<F>, I extends BitIndex<F, B, ?>> Optional<I> createIndex(TableColumn tableColumn, List<E> entities) {
        Class<F> fieldClass = tableColumn.getColumn().getFieldClass();
        Optional<BitField<F>> bitField = getBitFieldFor(tableColumn);
        if (!bitField.isPresent()) {
            return Optional.empty();
        }

        List<Integer> nullValues = new ArrayList<>();
        SortedMap<F, List<Integer>> map = new TreeMap<>(bitField.get().comparator());
        for (int i = 0; i < entities.size(); i++) {
            E entity = entities.get(i);
            F colVal;
            if (entity instanceof Entity) {
                colVal = (F) tableColumn.getColumn().getValue((EntityRelation<?>) entity);
            } else {
                colVal = (F) ((Plate) entity).getValue(tableColumn.getColumn());
            }

            if (colVal == null) {
                nullValues.add(i);
                continue;
            }
            List<Integer> list = map.computeIfAbsent(colVal, k -> new ArrayList<>());
            list.add(i);
        }

//        System.out.println("index created for " + tableColumn.getColumn().getColumnName() + " keys size: " + map.size() + " total size " + entities.size());
        if (map.size() == entities.size()) {
//            System.out.println("index is useless. dropped.");
//            return Optional.empty();
        }
        return getBitIndexFor(fieldClass, tableColumn, map, nullValues);
    }

//
//    public <F, E extends Entity, B extends BitField<F>, I extends BitIndex<F, B, ?>> Optional<I> create8Index(TableColumn tableColumn, List<Plate> entities) {
//        //Class<F> fieldClass = tableColumn.getColumn().getFieldClass();
//        Optional<BitField<F>> bitField = getBitFieldFor(tableColumn);
//        if (!bitField.isPresent()) {
//            return Optional.empty();
//        }
//
//        List<Integer> nullValues = new ArrayList<>();
//        SortedMap<F, List<Integer>> map = new TreeMap<>(bitField.get().comparator());
//        for (int i = 0; i < entities.size(); i++) {
//            Plate entity = entities.get(i);
//            F colVal = (F) entity.getValue(tableColumn.getColumn());
//            if (colVal == null) {
//                nullValues.add(i);
//                continue;
//            }
//            List<Integer> list = map.computeIfAbsent(colVal, k -> new ArrayList<>());
//            list.add(i);
//        }
//
//        System.out.println("index created for "+tableColumn.getColumn().getColumnName()+" keys size: "+map.size()+" total size "+entities.size());
//        if (map.size()==entities.size()){
//            System.out.println("index is useless. dropped.");
////            return Optional.empty();
//        }
//        return getBitIndexFor(fieldClass, tableColumn, map, nullValues);
//    }

    public <F, B extends BitField<F>, I extends BitIndex<F, B, ?>> Optional<I> getBitIndexFor(Class<F> clazz, TableColumn tableColumn, SortedMap<F, List<Integer>> valueIndex, List<Integer> nullValues) {
        TriFunction<TableColumn, SortedMap<F, List<Integer>>, List<Integer>, I> function = (TriFunction<TableColumn, SortedMap<F, List<Integer>>, List<Integer>, I>) bitBufferIndexBaseMap.get(clazz);
        if (function == null) {
            return Optional.empty();
        }
        return Optional.of(function.apply(tableColumn, valueIndex, nullValues));
    }

    public <F> Optional<BitArrayBase<F, ?>> getBitArrayBaseFor(Class<F> clazz, TableColumn tableColumn) {
        return getOrNull(tableColumn, (Function<TableColumn, BitArrayBase<F, ?>>) bitArrayBaseMap.get(clazz));
    }

    public <F> Optional<BitArrayBaseNotNull<F, ?>> getArrayBaseNotNullFor(Class<F> clazz, TableColumn tableColumn) {
        return getOrNull(tableColumn, (Function<TableColumn, BitArrayBaseNotNull<F, ?>>) bitArrayBaseNotNullMap.get(clazz));
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
