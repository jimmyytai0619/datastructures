package adt;

import java.io.Serializable;

/**
 * A simple 1-based dynamic array list implementation for teaching purpose.
 * No JCF is used. Supports auto-resize and positional operations.
 */
public class ArrayList<T> implements ListInterface<T>, Serializable {
    private static final int DEFAULT_CAPACITY = 10;

    private T[] list;               // 0-based backing array
    private int numberOfEntries;    // logical size (n)

    @SuppressWarnings("unchecked")
    public ArrayList() {
        list = (T[]) new Object[DEFAULT_CAPACITY];
        numberOfEntries = 0;
    }

    @Override
    public void clear() {
        for (int i = 0; i < numberOfEntries; i++) list[i] = null;
        numberOfEntries = 0;
    }

    @Override
    public boolean add(T newEntry) {
        ensureCapacity(numberOfEntries + 1);
        list[numberOfEntries++] = newEntry; // append at end
        return true;
    }

    @Override
    public boolean add(int newPosition, T newEntry) {
        if (!validNewPosition(newPosition)) return false;
        ensureCapacity(numberOfEntries + 1);
        int idx = newPosition - 1; // map 1-based -> 0-based
        for (int i = numberOfEntries; i > idx; i--) {
            list[i] = list[i - 1];
        }
        list[idx] = newEntry;
        numberOfEntries++;
        return true;
    }

    @Override
    public T remove(int givenPosition) {
        if (!validPosition(givenPosition)) return null;
        int idx = givenPosition - 1;
        T removed = list[idx];
        for (int i = idx; i < numberOfEntries - 1; i++) {
            list[i] = list[i + 1];
        }
        list[--numberOfEntries] = null;
        return removed;
    }

    @Override
    public boolean replace(int givenPosition, T newEntry) {
        if (!validPosition(givenPosition)) return false;
        list[givenPosition - 1] = newEntry;
        return true;
    }

    @Override
    public T getEntry(int givenPosition) {
        if (!validPosition(givenPosition)) return null;
        return list[givenPosition - 1];
    }

    @Override
    public boolean contains(T anEntry) {
        for (int i = 0; i < numberOfEntries; i++) {
            if (anEntry == null ? list[i] == null : anEntry.equals(list[i])) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int getNumberOfEntries() {
        return numberOfEntries;
    }

    @Override
    public boolean isEmpty() {
        return numberOfEntries == 0;
    }

    // ---------- helpers ----------
    private boolean validPosition(int pos) {
        return pos >= 1 && pos <= numberOfEntries;
    }

    private boolean validNewPosition(int pos) {
        return pos >= 1 && pos <= numberOfEntries + 1;
    }

    @SuppressWarnings("unchecked")
    private void ensureCapacity(int required) {
        if (required <= list.length) return;
        int newCap = Math.max(required, list.length * 2);
        T[] old = list;
        list = (T[]) new Object[newCap];
        for (int i = 0; i < numberOfEntries; i++) list[i] = old[i];
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 1; i <= numberOfEntries; i++) {
            if (i > 1) sb.append(", ");
            sb.append(getEntry(i));
        }
        return sb.append("]").toString();
    }
}
