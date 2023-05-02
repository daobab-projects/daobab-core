package io.daobab.target.buffer.nonheap;

import io.daobab.error.TargetDoesNotSupport;
import io.daobab.model.Entity;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;


class NonHeapEntityList<E extends Entity> implements List<E> {

    private final NonHeapEntities<E> buffer;
    private final List<Integer> pointerList;

    public NonHeapEntityList(NonHeapEntities<E> buffer, List<Integer> pointerList) {
        this.buffer = buffer;
        this.pointerList = pointerList;
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
        throw new TargetDoesNotSupport();
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private final Iterator<Integer> iter = pointerList.iterator();

            @Override
            public boolean hasNext() {
                return iter.hasNext();
            }

            @Override
            public E next() {
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
        throw new TargetDoesNotSupport();
    }

    @Override
    public boolean add(E e) {
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
    public boolean addAll(Collection<? extends E> collection) {
        throw new TargetDoesNotSupport();
    }

    @Override
    public boolean addAll(int i, Collection<? extends E> collection) {
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
    public E get(int i) {
        return buffer.get(i);
    }

    @Override
    public E set(int i, E e) {
        throw new TargetDoesNotSupport();
    }

    @Override
    public void add(int i, E e) {
        throw new TargetDoesNotSupport();
    }

    @Override
    public E remove(int i) {
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
    public ListIterator<E> listIterator() {
        throw new TargetDoesNotSupport();
    }

    @Override
    public ListIterator<E> listIterator(int i) {
        throw new TargetDoesNotSupport();
    }

    @Override
    public List<E> subList(int i, int i1) {
        throw new TargetDoesNotSupport();
    }

}
