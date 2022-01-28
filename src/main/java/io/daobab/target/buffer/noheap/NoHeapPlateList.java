package io.daobab.target.buffer.noheap;

import io.daobab.error.TargetDoesNotSupport;
import io.daobab.model.Plate;
import io.daobab.model.TableColumn;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;


class NoHeapPlateList implements List<Plate> {

    private final NoHeapBuffer<?> buffer;
    private final List<Integer> pointerList;
    private final Collection<TableColumn> chosenColumns;

    public NoHeapPlateList(NoHeapBuffer<?> buffer, List<Integer> pointerList, Collection<TableColumn> chosenColumns) {
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
        throw new TargetDoesNotSupport();
    }

    @Override
    public <T> T[] toArray(T[] ts) {
        throw new TargetDoesNotSupport();
    }

    @Override
    public boolean add(Plate e) {
        throw new TargetDoesNotSupport();
    }

    @Override
    public boolean remove(Object o) {
        throw new TargetDoesNotSupport();
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        throw new TargetDoesNotSupport();
    }

    @Override
    public boolean addAll(Collection<? extends Plate> collection) {
        throw new TargetDoesNotSupport();
    }

    @Override
    public boolean addAll(int i, Collection<? extends Plate> collection) {
        throw new TargetDoesNotSupport();
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        throw new TargetDoesNotSupport();
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        throw new TargetDoesNotSupport();
    }

    @Override
    public void clear() {
        throw new TargetDoesNotSupport();
    }

    @Override
    public Plate get(int i) {
        return buffer.getPlate(i, chosenColumns);
    }

    @Override
    public Plate set(int i, Plate e) {
        throw new TargetDoesNotSupport();
    }

    @Override
    public void add(int i, Plate e) {
        throw new TargetDoesNotSupport();
    }

    @Override
    public Plate remove(int i) {
        throw new TargetDoesNotSupport();
    }

    @Override
    public int indexOf(Object o) {
        throw new TargetDoesNotSupport();
    }

    @Override
    public int lastIndexOf(Object o) {
        throw new TargetDoesNotSupport();
    }

    @Override
    public ListIterator<Plate> listIterator() {
        throw new TargetDoesNotSupport();
    }

    @Override
    public ListIterator<Plate> listIterator(int i) {
        throw new TargetDoesNotSupport();
    }

    @Override
    public List<Plate> subList(int i, int i1) {
        throw new TargetDoesNotSupport();
    }


}
