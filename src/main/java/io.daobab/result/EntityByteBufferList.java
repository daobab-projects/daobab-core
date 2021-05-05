package io.daobab.result;

import io.daobab.model.Entity;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;


public class EntityByteBufferList<E extends Entity> implements List<E> {

    private final EntityByteBuffer<E> buffer;
    private final List<Integer> pointerList;

    public EntityByteBufferList(EntityByteBuffer<E> buffer, List<Integer> pointerList) {
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
        return false;
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
        return null;
    }

    @Override
    public boolean add(E e) {
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
    public boolean addAll(Collection<? extends E> collection) {
        return false;
    }

    @Override
    public boolean addAll(int i, Collection<? extends E> collection) {
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
    public E get(int i) {
        return buffer.get(i);
    }

    @Override
    public E set(int i, E e) {
        return null;
    }

    @Override
    public void add(int i, E e) {

    }

    @Override
    public E remove(int i) {
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
    public ListIterator<E> listIterator() {
        return null;
    }

    @Override
    public ListIterator<E> listIterator(int i) {
        return null;
    }

    @Override
    public List<E> subList(int i, int i1) {
        return null;
    }


}
