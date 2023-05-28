package io.daobab.target.database.query.frozen;


public class DaoParam {

    private final int key;

    private boolean collection;

    private Class<?> accessibleType;

    public int getKey() {
        return key;
    }

    public DaoParam(int position) {
        this.key=position;
    }

    public static DaoParam param(int no) {
        return new DaoParam(no);
    }

    public Class<?> getAccessibleType() {
        return accessibleType;
    }

    public void setAccessibleType(Class<?> accessibleType) {
        this.accessibleType = accessibleType;
    }


    public boolean isCollection() {
        return collection;
    }

    public void setCollection(boolean collection) {
        this.collection = collection;
    }

}
