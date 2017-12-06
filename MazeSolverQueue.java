import java.util.ArrayList;
import java.lang.StringBuilder;
import java.util.HashMap;
public class MazeSolverQueue extends MazeSolver {
    private Queue<Square> s;
    private Maze m;
    private boolean done;
    private ArrayList<Square> path;
    private HashMap<Square, Square> prevSq;
    //private ArrayList<Square> fp;
    public void makeEmpty() {
        this.s = new Queue<>();
        s.enqueue(m.getStart());
        s.front().work();
        done = false;
        path = new ArrayList<Square>();
        prevSq = new HashMap<Square, Square>();
    }

    public boolean isEmpty() {
        return s.size() == 0;
    }

    public void add(Square sq) {
        s.enqueue(sq);
    }

    public Square next() {
        return null;
    }

    public Square step() {
        Square c;
        try {
            c = s.dequeue();
            Square[] ad = m.getNeighbors(c);
            c.work();
            int num = 0;
            Square ret = null;
            for(Square i:ad) {
                if(i.getVal() == 3) {
                    this.done = true;
                    this.add(i);
                    prevSq.put(i,c);
                    Square temp = i;
                    while(temp != null) {
                        path.add(temp);
                        temp = prevSq.get(temp);
                    }
                    return null;
                }
                if(i.getVis() == 0 && i.getVal() == 0) {
                    i.work();
                    this.add(i);
                    ret = i;
                    prevSq.put(i,c);
                }
            }
            if(ret != null) {
                return ret;
            }
            return this.step();
        } catch(Exception e) {
            this.done = true;
            return null;
        }
    }

    public MazeSolverQueue(Maze maze) {
        this.m = maze;
        this.makeEmpty();
    }

    public boolean isSolved() {
        return done || s.size() == 0;
    }

    public void solve() {
        while(!this.isSolved()) {
            this.next();
        }
    }

    private boolean adjacent(Square s1, Square s2) {
        return (Math.abs(s1.getRow()-s2.getRow()) == 1 && s1.getCol()-s2.getCol() == 0) || (Math.abs(s1.getCol()-s2.getCol()) == 1 && s1.getRow()-s2.getRow() == 0);
    }

    private void cleanup() {
        for(int i = 0; i < path.size(); i++) {
            for(int ii = i+3; ii < path.size(); ii++) {
                if(adjacent(path.get(i),path.get(ii))) {
                    for(int iii = i; iii < ii-1; iii++) {
                        path.remove(i+1);
                    }
                }
            }
        }
    }

    public ArrayList<Integer[]> getPointPath() {
        ArrayList<Integer[]> ret = new ArrayList<Integer[]>();
        for(int i = path.size()-1; i > -1; i--) {
			Integer[] add = new Integer[2];
			add[0] = path.get(i).getCol();
			add[1] = path.get(i).getRow();
			ret.add(add);
        }
		return ret;
    }

    public String getPath() {
        this.cleanup();
        StringBuilder sb = new StringBuilder();
        for(int ii = path.size()-1; ii > -1; ii--) {
            Square i = path.get(ii);
            i.onPath();
            sb.append("["+i.getRow()+","+i.getCol()+"]");
        }
        return sb.toString();
    }

}
