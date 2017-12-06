import java.util.ArrayList;
import java.lang.Math;
import java.util.HashSet;
import java.util.Iterator;
public class PathField implements FieldPartition{
    ArrayList<Double[]> p;
    int prec;
    ArrayList<Double[]> switchp;
    double midp;
    double t;
    double midvp;
    double ioff;
    ArrayList<Double[]> coeffs;
    double[] xdom;
    double[] ydom;
    int[][] ipath;
    public PathField(int[][] pinp, int pr) {
        int[][] pi = pinp;
        if(pinp[0][0] > pinp[pinp.length-1][0]) {
            pi = reverse(pinp);
        }
        ipath = new int[pi.length][];
        for(int i = 0; i > pi.length; i++) {
            ipath[i] = pi[i];
        }
        HashSet<Integer> h = new HashSet<Integer>();
        for(int[] i:pi) {
            h.add((Integer)i[1]);
        }
        Iterator<Integer> it = h.iterator();
        xdom = new double[2];
        xdom[0] = Math.min(pi[0][0],pi[pi.length-1][0])+0.5;
        xdom[1] = Math.max(pi[0][0],pi[pi.length-1][0]);
        double initioff = it.next();
        ioff = initioff-1;
        while(it.hasNext()) {
            ioff = it.next();
        }
        ydom = new double[2];
        ydom[0] = Math.min(initioff, ioff);
        ydom[1] = Math.max(initioff, ioff)+1.0;
        ioff = (ioff+initioff)*0.5;
        int[][] path = new int[pi.length*2+2][];
        path[0] = new int[2];
        path[0][0] = pi[pi.length-1][0]*2+2;
        path[0][1] = (int)(2.0*ioff)-pi[0][1];
        path[path.length-1] = new int[2];
        path[path.length-1][0] = pi[0][0]-1;
        path[path.length-1][1] = pi[0][1];
        for(int i = 0; i < pi.length*2; i++) {
            int[] t = pi[(int)Math.min(i, pi.length*2-i-1)];
            path[i+1] = new int[2];
            path[i+1][1] = (int)(2.0*ioff)-t[1];
            if(i> pi.length*2-i-1) {
                path[i+1][0] = t[0];
            } else {
                path[i+1][0] = pi[pi.length-1][0]*2-t[0]+1;
            }
        }
        prec = pr;
        p = new ArrayList<Double[]>();
        for(int[] i:path) {
            Double[] add = new Double[2];
            add[0] = (double)i[0];
            add[1] = i[1]*2.0-ioff;
            p.add(add);
        } 
        midp = (p.get(0)[0]+p.get(p.size()-1)[0])/2.0;
        t = Math.abs(p.get(0)[0]-p.get(p.size()-1)[0]);
        for(int i = 0; i < p.size(); i++) {
            Double[] s = new Double[2];
            s[0] = p.get(i)[0]-midp;
            s[1] = p.get(i)[1]-ioff;
            p.set(i,s);
        }
        double prevH = p.get(0)[1];
        switchp = new ArrayList<Double[]>();
        for(int i = 1; i < p.size(); i++) {
            if(prevH != p.get(i)[1]) {
                Double[] add = new Double[2];
                add[0] = p.get(i)[0];
                add[1] = prevH;
                switchp.add(add);
                prevH = p.get(i)[1];
            }
        }
        coeffs = new ArrayList<Double[]>();
        for(int i = 0; i < prec; i++) {
            coeffs.add(FourierCoeffs(i));
        }
        System.out.println("Upwards Transform: "+coeffs.get(0)[0]+"\nInitial off: "+ioff+"\nydom1: "+ydom[0]+" ydom2: "+ydom[1]);
    }

    public int[][] reverse(int[][] inp) {
        int[][] ret = new int[inp.length][];
        for(int i = 0; i < ret.length; i++) {
            ret[i] = inp[inp.length-1-i];
        }
        return ret;
    }

    public double[] xDom() {
        return xdom;
    }

    public double[] yDom() {
        return ydom;
    }

    public boolean inDomain(double[] test) {
        return test[0] < xdom[1] && test[0] > xdom[0] && test[1] < ydom[1] && test[1] > ydom[0];
    }

    public int[][] getPath() {
        return ipath;
    }

    private Double[] FourierCoeffs(int n) {
        if(n > 0) {
            double[] ret = new double[2];
            //ret1 = sin(nx) coeff, ret2 = cos(nx) coeff
            ret[0] = 1.0/n*Math.cos(2*Math.PI/t*switchp.get(0)[0]*n)*p.get(0)[1]-1.0/n*Math.cos(2*Math.PI/t*p.get(0)[0]*n)*p.get(0)[1];
            for(int i = 0; i < switchp.size()-1; i++) {
                ret[0] -= 1.0/n*Math.cos(2*Math.PI/t*switchp.get(i+1)[0]*n)*switchp.get(i)[1]-1.0/n*Math.cos(2*Math.PI/t*switchp.get(i)[0]*n)*switchp.get(i)[1];
            }
            int i = switchp.size()-1;
            ret[0] -= 1.0/n*Math.cos(2*Math.PI/t*p.get(p.size()-1)[0]*n)*switchp.get(i)[1]-1.0/n*Math.cos(2*Math.PI/t*switchp.get(i)[0]*n)*switchp.get(i)[1];
            ret[0] *= -1.0/Math.PI;
            ret[1] = 1.0/n*Math.sin(2*Math.PI/t*switchp.get(0)[0]*n)*p.get(0)[1];
            for(i = 0; i < switchp.size()-1; i++) {
                ret[1] -= 1.0/n*Math.sin(2*Math.PI/t*switchp.get(i+1)[0]*n)*switchp.get(i)[1]-1.0/n*Math.sin(2*Math.PI/t*switchp.get(i)[0]*n)*switchp.get(i)[1];
            }
            i = switchp.size()-1;
            ret[1] -= 1.0/n*Math.sin(2*Math.PI/t*p.get(p.size()-1)[0]*n)*switchp.get(i)[1]-1.0/n*Math.sin(2*Math.PI/t*switchp.get(i)[0]*n)*switchp.get(i)[1];
            ret[1] *= 1.0/Math.PI;
            Double[] ret2 = new Double[2];
            ret2[0] = (Double)ret[0];
            ret2[1] = ret[1];
            return ret2;
        } else {
            double[] ret = new double[1];
            ret[0] = switchp.get(0)[1]*(switchp.get(0)[0]);
            for(int i = 0; i < switchp.size()-1; i++) {
                ret[0] += (switchp.get(i)[1])*(p.get(i+1)[0]-switchp.get(i)[0]);
            }
            int i = switchp.size()-1;
            ret[0] += (switchp.get(i)[1])*(p.get(p.size()-1)[0]-switchp.get(i)[0]);
            ret[0] *= 1.0/(t);
            Double[] ret2 = new Double[1];
            ret2[0] = (Double)ret[0];
            return ret2;
        }
    }

    public double point(double x) {
        x -= midp+0.5;
        double ret = (double)coeffs.get(0)[0];
        for(int i = 1; i < coeffs.size(); i++) {
            ret += coeffs.get(i)[0]*Math.sin(i*2*Math.PI/t*x)+coeffs.get(i)[1]*Math.cos(i*2*Math.PI/t*x);
        }
        ret = ret/2.0;
        return ret+ioff+0.5;
    }

    public double deriv(double x) {
        x -= midp+0.5;
        double ret = 0;
        for(int i = 1; i < coeffs.size(); i++) {
            ret += i*2*Math.PI/t*coeffs.get(i)[0]*Math.cos(i*2*Math.PI/t*x)-i*2*Math.PI/t*coeffs.get(i)[1]*Math.sin(i*2*Math.PI/t*x);
        }
        ret = ret/2.0;
        return ret;
    }

    public double secondDeriv(double x) {
        x -= midp+0.5;
        double ret = 0;
        for(int i = 1; i < coeffs.size(); i++) {
            ret += -i*2*Math.PI/t*i*2*Math.PI/t*coeffs.get(i)[0]*Math.sin(i*2*Math.PI/t*x)-i*2*Math.PI/t*i*2*Math.PI/t*coeffs.get(i)[1]*Math.cos(i*2*Math.PI/t*x);
        }
        ret = ret/2.0;
        return ret;
    }

    public double curvature(double x) {
        return -secondDeriv(x)/Math.pow(deriv(x)*deriv(x)+1, 1.5);
    }

    public double[] getAccel(double[] p) {
        double d = deriv(p[0]);
        double[] ret = new double[2];
        double c = curvature(p[0]);
        ret[0] = d/Math.sqrt(1+d*d)*c;
        ret[1] = -1/Math.sqrt(1+d*d)*c;
        return ret;
    }

    /*public static void main(String[] args) {
    int[][] p = new int[6][];
    for(int i = 0; i < 6; i++) {
    p[i] = new int[2];
    if(i == 0) {
    p[i][0] = 1;
    p[i][1] = 2;
    }
    if(i == 1) {
    p[i][0] = 2;
    p[i][1] = 2;
    }
    if(i == 2) {
    p[i][0] = 3;
    p[i][1] = 2;
    }
    if(i == 3) {
    p[i][0] = 4;
    p[i][1] = 2;
    }
    if(i == 4) {
    p[i][0] = 4;
    p[i][1] = 1;
    }
    if(i == 5) {
    p[i][0] = 5;
    p[i][1] = 1;
    }
    /*if(i == 6) {
    p[i][0] = 5;
    p[i][1] = 1;
    }
    if(i == 7) {
    p[i][0] = 6;
    p[i][1] = 1;
    }
    if(i == 8) {
    p[i][0] = 7;
    p[i][1] = 1;
    }*//*
    }
    PathField pf = new PathField(p,2);
    for(double i = 1; i <= 5; i += 0.5) {
    System.out.println(i+" "+1.0/pf.curvature(i));   
    }
    }*/
}