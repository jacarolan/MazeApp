import java.util.Scanner;
import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
public class Maze {
    private Square[][] maze;
    public boolean loadMaze(String fname) {
        try {
            ArrayList<ArrayList<Square>> temp = new ArrayList();
            Scanner in = new Scanner(new File(fname));
            in.useDelimiter("\n");
            in.next();
            for(int i = 0; in.hasNext(); i++) {
                Scanner inin = new Scanner(in.next());
                inin.useDelimiter(" ");
                temp.add(new ArrayList<Square>());
                for(int ii = 0; inin.hasNext(); ii++) {
                    String inp = inin.next();
                    //System.out.println(inp);
                    //System.out.println(Integer.parseInt(inp)+2);
                    temp.get(i).add(new Square(i,ii,Integer.parseInt(inp)));
                }
            }
            maze = new Square[temp.size()][];
            for(int i = 0; i < temp.size(); i++) {
                maze[i] = new Square[temp.get(i).size()];
                for(int ii = 0; ii < temp.get(i).size(); ii++) {
                    maze[i][ii] = temp.get(i).get(ii);
                }
            }
            return true;
        } catch(Exception e) {
            return false;
        }
    }

    public Square[] getNeighbors(Square q) {
        Square[] ret = new Square[4];
        /*for(int i = 0; i < 4; i++) {
        try {
        ret[i] = maze[q.getRow()+(i%2)*(i-1)][q.getCol()+((i+1)%2)*(i-2)];
        } catch(Exception e) {
        Square[] tempret = new Square[ret.length];
        for(int ii = 0; ii < ret.length; ii++) {
        tempret[ii] = ret[ii];
        }
        ret = new Square[ret.length-1];
        for(int ii = 0; ii < i; ii++) {
        ret[ii] = tempret[ii];
        }
        }
        }*/
        int vals = 0;
        if(q.getRow() != 0) {
            ret[vals] = maze[q.getRow()-1][q.getCol()];
            vals++;
        }
        if(q.getCol() != 0) {
            ret[vals] = maze[q.getRow()][q.getCol()-1];
            vals++;
        }
        if(q.getRow() != maze.length-1) {
            ret[vals] = maze[q.getRow()+1][q.getCol()];
            vals++;
        }
        if(q.getCol() != maze[0].length-1) {
            ret[vals] = maze[q.getRow()][q.getCol()+1];
            vals++;
        }
        Square[] ret2 = new Square[vals];
        for(int ii = 0; ii < vals; ii++) {
            ret2[ii] = ret[ii];
        }
        return ret2;
    }

    public void reset() {
        for(Square[] i: maze) {
            for(Square ii:i) {
                ii.reset();
            }
        }
    }

    public Square getStart() {
        for(int i = 0; i < maze.length*maze[0].length; i++) {
            Square s = maze[i/maze[0].length][i%maze[0].length];
            if(maze[i/maze[0].length][i%maze[0].length].getVal() == 2) {
                return maze[i/maze[0].length][i%maze[0].length];
            }
        }
        return null;
    }

    public Square getEnd() {
        for(int i = 0; i < maze.length*maze[0].length; i++) {
            if(maze[i/maze[0].length][i%maze[0].length].getVal() == 3) {
                return maze[i/maze[0].length][i%maze[0].length];
            }
        }
        return null;
    }

    public ArrayList<Integer[]> getWalls() {
        ArrayList<Integer[]> ret = new ArrayList<Integer[]>();
        for(int y = 0; y < maze.length; y++) {
            for(int x = 0; x < maze[0].length; x++) {
                if(maze[x][y].getVal() == 1) {
                    Integer[] add = new Integer[2];
                    add[0] = x;
                    add[1] = y;
                    ret.add(add);
                }
            }
        }
		return ret;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(Square[] i:maze) {
            for(Square s:i) {
                sb.append(s.toString()+" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}