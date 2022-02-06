package io.daobab.target.buffer.noheap;

import io.daobab.error.TargetNotSupports;
import io.daobab.model.Entity;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;


class NoHeapEntityList<E extends Entity> implements List<E> {

    private final NoHeapEntities<E> buffer;
    private final List<Integer> pointerList;

    public NoHeapEntityList(NoHeapEntities<E> buffer, List<Integer> pointerList) {
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
        throw new TargetNotSupports();
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
        throw new TargetNotSupports();
    }

    @Override
    public boolean add(E e) {
        throw new TargetNotSupports();
    }

    @Override
    public boolean remove(Object o) {
        throw new TargetNotSupports();
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        throw new TargetNotSupports();
    }

    @Override
    public boolean addAll(Collection<? extends E> collection) {
        throw new TargetNotSupports();
    }

    @Override
    public boolean addAll(int i, Collection<? extends E> collection) {
        throw new TargetNotSupports();
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        throw new TargetNotSupports();
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        throw new TargetNotSupports();
    }

    @Override
    public void clear() {
        throw new TargetNotSupports();
    }

    @Override
    public E get(int i) {
        return buffer.get(i);
    }

    @Override
    public E set(int i, E e) {
        throw new TargetNotSupports();
    }

    @Override
    public void add(int i, E e) {
        throw new TargetNotSupports();
    }

    @Override
    public E remove(int i) {
        throw new TargetNotSupports();
    }

    @Override
    public int indexOf(Object o) {
        throw new TargetNotSupports();
    }

    @Override
    public int lastIndexOf(Object o) {
        throw new TargetNotSupports();
    }

    @Override
    public ListIterator<E> listIterator() {
        throw new TargetNotSupports();
    }

    @Override
    public ListIterator<E> listIterator(int i) {
        throw new TargetNotSupports();
    }

    @Override
    public List<E> subList(int i, int i1) {
        throw new TargetNotSupports();
    }

}
