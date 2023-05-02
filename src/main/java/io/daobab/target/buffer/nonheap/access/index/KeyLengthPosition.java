package io.daobab.target.buffer.nonheap.access.index;

public class KeyLengthPosition<K> {

    private K key;
    private int length;
    private int position;

    public KeyLengthPosition(K key) {
        this.key = key;
    }

    public K getKey() {
        return key;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

}
