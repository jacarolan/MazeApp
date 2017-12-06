public class Square {
    private int row;
    private int col;
    private int val;
    private int vis;
    public Square(int row, int col, int val) {
        this.row = row;
        this.col = col;
        this.val = val;
        //Key: Vis = 0: not visited Vis = 1: Currently on worklist Vis = 2: Removed permanently from worklist
        this.vis = 0;
    }

    public int getRow() {
        return this.row;
    }

    public int getCol() {
        return this.col;
    }

    public int getVal() {
        return this.val;
    }
    
    public int getVis() {
        return this.vis;
    }
    
    public boolean isEqual(Square s) {
        return this.row == s.getRow() && this.col == s.getCol();
    }
    
    public void work() {
        this.vis = 1;
    }
    
    public void onPath() {
        this.vis = 2;
    }
    
    public void reset() {
        this.vis = 0;
    }

    public String toString() {
        switch(val) {
            case 0: if(vis == 0) {return "o";} else if(vis == 1) {return "x";} else {return ".";}
            case 1: return "#";
            case 2: return "S";
            case 3: return "E";
        }
        return null;
    }
}