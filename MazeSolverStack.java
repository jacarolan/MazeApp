import java.util.ArrayList;
import java.lang.StringBuilder;
public class MazeSolverStack extends MazeSolver {
    private Stack<Square> s;
    private ArrayList<Integer> rollback;
    private Maze m;
    private ArrayList<Square> path;
    private boolean done;
    public void makeEmpty() {
        this.s = new Stack<>();
        s.push(m.getStart());
        s.top().work();
        this.rollback = new ArrayList<>();
        path = new ArrayList<Square>();
        done = false;
    }

    public boolean isEmpty() {
        return s.size() == 0;
    }

    public void add(Square sq) {
        s.push(sq);
    }

    public Square next() {
        return null;
    }

    public Square step() {
        Square c;
        try {
            c = s.top();

            Square[] ad = m.getNeighbors(c);
            int num = 0;
            Square ret = new Square(0,0,0);
            for(Square i:ad) {
                if(i.getVal() != 1 && i.getVis() == 0) {
                    ret = i;
                    if(i.getVal() == 3) {
                        ret.work();
                        s.push(ret);
                        return ret;
                    }
                    num++;
                }
            }
            if(num > 1) {
                rollback.add(0);
                incRollback();
                ret.getVis();
                s.push(ret);
                ret.work();
                return ret;
            } else if(num == 1) {
                ret.work();
                s.push(ret);
                incRollback();
                return ret;
            } else {
                this.rollBack();
                return this.step();
            }} catch(Exception e) {
            done = true;
            return null;
        }
    }

    private void incRollback() {
        for(int i = 0; i < rollback.size(); i++) {
            rollback.set(i, rollback.get(i)+1);
        }
    }

    private void rollBack() {
        for(int i = 0; i < rollback.get(rollback.size()-1); i++) {
            s.pop().onPath();
        }
        for(int i = 0; i < rollback.size(); i++) {
            rollback.set(i, rollback.get(i)-rollback.get(rollback.size()-1));
        }
        rollback = new ArrayList<Integer>(rollback.subList(0, rollback.size()-1));
    }

    public MazeSolverStack(Maze maze) {
        this.m = maze;
        this.makeEmpty();
    }

    public boolean isSolved() {
        return s.top().isEqual(m.getEnd()) || s.size() == 0 || done;
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

    public String getPath() {
        if(!this.isSolved()) {
            this.solve();
        }
        StringBuilder sb = new StringBuilder();
        Stack<Square> s2 = (Stack<Square>)s.clone();
        while(s2.size() > 0) {
            Square sq = s2.pop();
            path.add(sq);
        }
        this.cleanup();
        for(Square sq:path) {
            sb.insert(0,"["+sq.getRow()+","+sq.getCol()+"]");
        }
        return sb.toString();
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
}
