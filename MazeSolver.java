import java.util.ArrayList;
public abstract class MazeSolver {
    public abstract void makeEmpty();

    public abstract boolean isEmpty();

    public abstract void add(Square sq);

    public abstract Square next();

    public abstract Square step();

    public abstract boolean isSolved();

    public abstract void solve();
    
    public abstract String getPath();
    
    public abstract ArrayList<Integer[]> getPointPath();
}
