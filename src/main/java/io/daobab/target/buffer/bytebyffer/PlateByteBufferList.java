package io.daobab.target.buffer.bytebyffer;

import io.daobab.model.Plate;
import io.daobab.model.TableColumn;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;


public class PlateByteBufferList implements List<Plate> {

    private final BaseByteBuffer<?> buffer;
    private final List<Integer> pointerList;
    private final Collection<TableColumn> chosenColumns;

    public PlateByteBufferList(BaseByteBuffer<?> buffer, List<Integer> pointerList, Collection<TableColumn> chosenColumns) {
        this.buffer = buffer;
        this.pointerList = pointerList;
        this.chosenColumns = chosenColumns;
    }


    @Override
    public int size() {
        return pointerList.size();
    }

    @Override
    public boolean isEmpty() {
        return pointerList.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return false;
    }


    @Override
    public Iterator<Plate> iterator() {
        return new Iterator<Plate>() {
            private final Iterator<Integer> iter = pointerList.iterator();

            @Override
            public boolean hasNext() {
                return iter.hasNext();
            }

            @Override
            public Plate next() {
                return get(iter.next());
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("no changes allowed");
            }
        };
    }


    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public <T> T[] toArray(T[] ts) {
        return null;
    }

    @Override
    public boolean add(Plate e) {
        return false;
    }

    @Override
    public boolean remove(Object o) {
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends Plate> collection) {
        return false;
    }

    @Override
    public boolean addAll(int i, Collection<? extends Plate> collection) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        return false;
    }

    @Override
    public void clear() {

    }

    @Override
    public Plate get(int i) {
        return buffer.getPlate(i, chosenColumns);
    }

    @Override
    public Plate set(int i, Plate e) {
        return null;
    }

    @Override
    public void add(int i, Plate e) {

    }

    @Override
    public Plate remove(int i) {
        return null;
    }

    @Override
    public int indexOf(Object o) {
        return 0;
    }

    @Override
    public int lastIndexOf(Object o) {
        return 0;
    }

    @Override
    public ListIterator<Plate> listIterator() {
        return null;
    }

    @Override
    public ListIterator<Plate> listIterator(int i) {
        return null;
    }

    @Override
    public List<Plate> subList(int i, int i1) {
        return null;
    }


}
