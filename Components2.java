import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
public class Components2 extends JPanel {
    private int[][] m;
    private ArrayList<SquareGUI> s;
    private ArrayList<FieldPartition> pf;
    private Particle part;
    public Components2(int[][] map, boolean[][] mp, ArrayList<FieldPartition> pf) {
        this.m = map;
        this.s = new ArrayList<SquareGUI>();
        for(int[] i:m) {
            s.add(new SquareGUI(i[0], i[1], mp[i[0]][i[1]]));
        }
        this.pf = pf;
        double[] v = new double[2];
        double d = pf.get(0).deriv(pf.get(0).xDom()[1]);
        v[0] = -1.0/Math.sqrt(1.0+d*d);
        v[1] = -d/Math.sqrt(1.0+d*d);
        if(Math.signum(v[0]) != 1) {
            d = pf.get(0).deriv(pf.get(0).xDom()[0]);
            v[0] = 1.0/Math.sqrt(1.0+d*d);
            v[1] = d/Math.sqrt(1.0+d*d);
        }
        part = new Particle(pf.get(0).xDom()[1], pf.get(0).point(pf.get(0).xDom()[1]), v, pf);
    }

    public void step() {
        part.step();
        this.repaint();
    }

    private void paintFieldLines(Graphics g) {
        g.setColor(new Color(200, 50, 50));
        for(int[] i:m) {
            for(FieldPartition fp:pf) {
                double[] point = new double[2];
                point[0] = i[0]+0.5;
                point[1] = i[1]+0.5;
                if(fp.inDomain(point)) {
                    //g.drawLine((int)(point[0]*Viewer2.size+Viewer2.ox),(int)(point[1]*Viewer2.size+Viewer2.oy), (int)(point[0]*Viewer2.size+Viewer2.ox+10*fp.getAccel(point)[0]), (int)(point[1]*Viewer2.size+Viewer2.oy+10*fp.getAccel(point)[1]));
                }
            }
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        for(SquareGUI i:s) {
            i.draw(g);
        }
        for(FieldPartition pt:pf) {

            try {
                //pt = (Link)pt;
                g.setColor(new Color(0, 125, 255));
                /*for(double x = 0.5; x < p[p.length-1][0]+0.5; x += 1.0/Viewer2.size) {
                g.fillRect((int)(x*Viewer2.size+Viewer2.ox), (int)(pt.point(x)*Viewer2.size+Viewer2.oy), 1, 3);
                }*/
                //pt = (PathField)pt;
                for(double x = pt.xDom()[0]; x<pt.xDom()[1]; x+=1.0/Viewer2.size) {
                    g.fillRect((int)(x*Viewer2.size+Viewer2.ox), (int)(pt.point(x)*Viewer2.size+Viewer2.oy), 1, 3);
                }
            } catch(Exception e) {

                /*g.setColor(new Color(255, 125, 255));
                /*for(double x = 0.5; x < p[p.length-1][0]+0.5; x += 1.0/Viewer2.size) {
                g.fillRect((int)(x*Viewer2.size+Viewer2.ox), (int)(pt.point(x)*Viewer2.size+Viewer2.oy), 1, 3);
                }*//*
                for(double x = pt.xDom()[0]; x<pt.xDom()[1]; x+=1.0/Viewer2.size) {
                g.fillRect((int)(x*Viewer2.size+Viewer2.ox), (int)(pt.point(x)*Viewer2.size+Viewer2.oy), 1, 3);
                }*/
            }
            try {
                pt = (LinkC)pt;
                ((LinkC)pt).draw(g);
            } catch(Exception e) {}
        }
        //         this.paintFieldLines(g);
        part.draw(g);
        //        g.drawLine((int)(part.getX()*Viewer2.size+Viewer2.ox),(int)(part.getY()*Viewer2.size+Viewer2.oy), (int)(part.getX()*Viewer2.size+Viewer2.ox+50*pf.get(1).getAccel(part.getPos())[0]), (int)(part.getY()*Viewer2.size+Viewer2.oy+50*pf.get(1).getAccel(part.getPos())[1]));
    }
}