import javax.swing.*;
import java.io.*;
import java.awt.*;
import java.awt.Graphics.*;
import javax.imageio.ImageIO;
import java.util.ArrayList;
import java.awt.image.BufferedImage;
public class Particle {
    private double[] pos;
    private double[] vel;
    private double[] a;
    private ArrayList<FieldPartition> f;
    public Particle(double x, double y, double[] vinit, ArrayList<FieldPartition> f) {
        this.pos = new double[2];
        this.pos[0] = x;
        this.pos[1] = y;
        this.vel = vinit;
        this.f = f;
        this.a = new double[2];
    }

    public void step() {
        this.pos[0] += vel[0]*Viewer2.step;
        this.pos[1] += vel[1]*Viewer2.step;
        a = new double[2];
        for(int i = 0; i < f.size(); i++) {
            if(f.get(i).inDomain(this.pos)) {
                a = f.get(i).getAccel(pos);
            }
        }
        if(a != null) {
            vel[0] += a[0]*Viewer2.step;
            vel[1] += a[1]*Viewer2.step;
        }
    }

    public double[] getPos() {
        return pos;
    }

    public void draw(Graphics g) {
        g.setColor(new Color(200, 0, 0));
        g.fillOval((int)(pos[0]*Viewer2.size+Viewer2.ox-Viewer2.particleSize/2), (int)(pos[1]*Viewer2.size+Viewer2.oy-Viewer2.particleSize/2), Viewer2.particleSize, Viewer2.particleSize);
        g.setColor(new Color(0, 200, 0));
        g.drawLine((int)(pos[0]*Viewer2.size+Viewer2.ox),(int)(pos[1]*Viewer2.size+Viewer2.oy), (int)(pos[0]*Viewer2.size+Viewer2.ox+50*a[0]), (int)(pos[1]*Viewer2.size+Viewer2.oy+50*a[1]));
    }

    public double getX() {
        return this.pos[0];
    }

    public double getY() {
        return this.pos[1];
    }
}