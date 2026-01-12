//Name: Phonlapat Urairong
//ID: 6588114
//Section: 3
public class MyArrayList<E> {
    private static final int INITIAL_CAPACITY = 10;
    private Object[] data;
    private int size;

    public MyArrayList() {
        data = new Object[INITIAL_CAPACITY];
        size = 0;
    }

    public void add(E element) {
        if (size == data.length) {
            ensureCapacity();
        }
        data[size++] = element;
    }

    @SuppressWarnings("unchecked")
    public E get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index out of bounds");
        }
        return (E) data[index];
    }

    public void set(int index, E element) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index out of bounds");
        }
        data[index] = element;
    }

    public void remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index out of bounds");
        }
        // Shift elements
        for (int i = index; i < size - 1; i++) {
            data[i] = data[i + 1];
        }
        data[--size] = null;// clear the latest element
    }

    public int size() {
        return size;
    }

    private void ensureCapacity() {
        int newCapacity = data.length * 2;
        Object[] newData = new Object[newCapacity];
        System.arraycopy(data, 0, newData, 0, size);
        data = newData;
    }
}

