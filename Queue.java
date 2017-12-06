import java.util.NoSuchElementException;
import java.util.ArrayList;
import java.util.NoSuchElementException;
public class Queue<T> implements QueueADT<T> {
    private ArrayList<T> data;
    public Queue() {
        data = new ArrayList<T>();
    }

    public void enqueue(T item) {
        data.add(item);
    }

    public T dequeue() throws NoSuchElementException {
        T ret = data.get(0);
        data.remove(0);
        return ret;
    }

    public T front() throws NoSuchElementException {
        return data.get(0);
    }

    public Queue<T> clone() {
        Queue<T> ret = new Queue<T>();
        for(T i:data) {
            ret.enqueue(i);
        }
        return ret;
    }

    public int size() {
        return data.size();
    }

    public boolean isEmpty() {
        if(data.size() > 0) {
            return false;
        }
        return true;
    }

    public void clear() {
        data = new ArrayList<T>();
    }
    
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(T i:data) {
            sb.append(i.toString());
        }
        return sb.toString();
    }
}
