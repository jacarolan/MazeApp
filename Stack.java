import java.util.NoSuchElementException;
import java.util.ArrayList;
public class Stack<T> implements StackADT<T> {
    private ArrayList<T> data;
    public Stack() {
        data = new ArrayList<T>();
    }

    public void push(T item) {
        data.add(item);
    }

    public T pop() throws NoSuchElementException {
        T ret = data.get(data.size()-1);
        data.remove(data.size()-1);
        return ret;
    }

    public T top() throws NoSuchElementException {
        return data.get(data.size()-1);
    }

    public Stack<T> clone() {
        Stack<T> ret = new Stack<T>();
        for(T i:data) {
            ret.push(i);
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
}
