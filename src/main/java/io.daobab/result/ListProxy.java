package io.daobab.result;

import java.util.*;

public class ListProxy<T> implements List<T> {

    ArrayList<T> dd;

    List<T> entities;

    public ListProxy(List<T> entities) {
        this.entities = entities;
    }


    @Override
    public int size() {
        return entities.size();
    }

    @Override
    public boolean isEmpty() {
        return entities.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return entities.contains(o);
    }

    @Override
    public Iterator<T> iterator() {
        return entities.iterator();
    }

    @Override
    public Object[] toArray() {
        return entities.toArray();
    }

    @Override
    public <T> T[] toArray(T[] ts) {
        return entities.toArray(ts);
    }

    @Override
    public boolean add(T t) {
        return entities.add(t);
    }


    @Override
    public boolean remove(Object o) {
        return entities.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        return entities.containsAll(collection);
    }

    @Override
    public boolean addAll(Collection<? extends T> collection) {
        return entities.addAll(collection);
    }


    @Override
    public boolean addAll(int i, Collection<? extends T> collection) {
        return entities.addAll(i, collection);
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        return entities.retainAll(collection);
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        return entities.retainAll(collection);
    }

    @Override
    public void clear() {
        entities.clear();
    }

    @Override
    public T get(int i) {
        return entities.get(i);
    }


    @Override
    public T set(int i, T e) {
        return entities.set(i, e);
    }

    @Override
    public void add(int i, T e) {
        entities.add(i, e);
    }

    @Override
    public T remove(int i) {
        return entities.remove(i);
    }

    @Override
    public int indexOf(Object o) {
        return entities.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return entities.lastIndexOf(o);
    }

    @Override
    public ListIterator<T> listIterator() {
        return entities.listIterator();
    }

    @Override
    public ListIterator<T> listIterator(int i) {
        return entities.listIterator(i);
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        return entities.subList(fromIndex, toIndex);
    }
}
