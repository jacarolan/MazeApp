import java.util.ArrayList;
import java.awt.Graphics;
import java.awt.Color;
public class LinkC implements FieldPartition{
    private ArrayList<FieldPartition> e;
    public LinkC(ArrayList<FieldPartition> elems) {
        e = elems;
    }

    public double[] getAccel(double[] p) {
        for(FieldPartition i:e) {
            if(i.inDomain(p)) {
                return i.getAccel(p);
            }
        }
        return null;
    }

    public ArrayList<FieldPartition> getElems() {
        return e;
    }

    public void draw(Graphics g) {
        for(FieldPartition pt:e) {
            try {
                g.setColor(new Color(0, 125, 255));
                pt = (LinkH)pt;
                for(double x = pt.xDom()[0]; x<pt.xDom()[1]; x+=1.0/Viewer2.size) {
                    g.fillRect((int)(x*Viewer2.size+Viewer2.ox), (int)(pt.point(x)*Viewer2.size+Viewer2.oy), 1, 3);
                }
            } catch(Exception e) {}
            try {
                g.setColor(new Color(255, 0, 125));
                pt = (LinkV)pt;
                for(double x = pt.yDom()[0]; x<pt.yDom()[1]; x+=1.0/Viewer2.size) {
                    g.fillRect((int)(pt.point(x)*Viewer2.size+Viewer2.ox), (int)(x*Viewer2.size+Viewer2.oy), 1, 3);
                }
            } catch(Exception e) {}
        }
    }

    public boolean inDomain(double[] t) {
        boolean ret = false;
        for(FieldPartition i:e) {
            if(i.inDomain(t)) {
                ret = true;
            }
        }
        return ret;
    }

    public double[] xDom() {
        double[] ret = new double[2];
        ret[0] = e.get(0).xDom()[0];
        ret[1] = e.get(0).xDom()[1];
        for(FieldPartition i:e) {
            ret[0] = Math.min(i.xDom()[0], ret[0]);
            ret[1] = Math.max(i.xDom()[1], ret[1]);
        }
        return ret;
    }

    public double[] yDom() {
        double[] ret = new double[2];
        ret[0] = e.get(0).yDom()[0];
        ret[1] = e.get(0).yDom()[1];
        for(FieldPartition i:e) {
            ret[0] = Math.min(i.yDom()[0], ret[0]);
            ret[1] = Math.max(i.yDom()[1], ret[1]);
        }
        return ret;
    }

    public double point(double p) {
        return 0;
    }

    public double deriv(double p) {
        return 0;
    }

    public double secondDeriv(double p) {
        return 0;
    }
}
