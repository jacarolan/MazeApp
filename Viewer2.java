import javax.swing.*;
import java.io.*;
import java.awt.*;
import java.awt.Graphics.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Viewer2 extends JFrame {
    private Components2 content;
    private int width;
    private int height;
    private int x;
    private int y;

    public static final int size = 30;
    public static final int ox = 100;
    public static final int oy = 10;
    public static final double step = 0.001; //Amount of units to traverse before refreshing (at 0 would be a differential+perfect simulator
    public static final int particleSize = 4;
    public Viewer2(String title, Components2 contents, int width, int height, int xpos, int ypos) {
        super(title);
        content = contents;
        this.width = width;
        this.height = height;
        this.x = xpos;
        this.y = ypos;
        this.setContentPane(content);
        this.setSize(this.width, this.height);
        this.setLocation(x,y);
        //this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        //this.setUndecorated(true);
        this.setVisible(true);
    }

    public Components2 getContent() {
        return content;
    }
    
    public static void test() {
        int[][] path = new int[7][];
        for(int i = 0; i < 7; i++) {
            path[i] = new int[2];
        }
        path[0][0] = 2;
        path[0][1] = 2;
        path[1][0] = 3;
        path[1][1] = 2;
        path[2][0] = 3;
        path[2][1] = 3;
        path[3][0] = 3;
        path[3][1] = 4;
        path[4][0] = 4;
        path[4][1] = 4;
        path[5][0] = 5;
        path[5][1] = 4;
        path[6][0] = 5;
        path[6][1] = 5;
        boolean[][] mp = new boolean[10][];
        for(int i = 0; i < 10; i++) {
            mp[i] = new boolean[10];
            for(int ii = 0; ii < 10; ii++) {
                mp[i][ii] = Math.random() < 0.5;
            }
        }
        visualize(path, mp);
    }
    
    public static void visualize(int[][] path, boolean[][] mp) {
        ArrayList<Integer[][]> p = partition(path);
        int[][] map = new int[mp.length*mp[0].length][];
        for(int ii = 0; ii < mp.length; ii++) {
            for(int i=0; i < mp[0].length; i++) {
                map[ii*mp[0].length+i] = new int[2];
                map[ii*mp[0].length+i][0] = i;
                map[ii*mp[0].length+i][1] = ii;
            }
        }
        ArrayList<FieldPartition> pf = new ArrayList<FieldPartition>();
        for(Integer[][] i:p) {
            pf.add(null);
        }
        System.out.println(p.size());
        for(int i = 0; i < p.size(); i += 2) {
            try {
                int[][] test = new int[p.get(i).length][];
                for(int ii = 0; ii < test.length; ii++) {
                    test[ii] = new int[p.get(i)[ii].length];
                    for(int iii = 0; iii < test[ii].length; iii++) {
                        test[ii][iii] = p.get(i)[ii][iii];
                    }
                }
                System.out.println("Constructing fourier from:");
                print(test);
                pf.set(i,new PathField(test,test.length*2+2));
            } catch(IndexOutOfBoundsException e) {System.out.println("asdfs"+i);}
        }
        for(int i = 1; i < p.size()-1; i+= 2) {
            if(p.get(i-1)[0][0] < p.get(i-1)[p.get(i-1).length-1][0] && p.get(i+1)[0][0] < p.get(i+1)[p.get(i+1).length-1][0]) {
                FieldPartition f1 = pf.get(i-1);
                FieldPartition f2 = pf.get(i+1);
                double[] arr1 = new double[3];
                double x1 = f1.xDom()[1];//p.get(i-1)[p.size()-1][0];
                arr1[0] = f1.point(x1);
                arr1[1] = f1.deriv(x1);
                arr1[2] = f1.secondDeriv(x1);
                System.out.println("Derivs: "+arr1[1]+" "+arr1[2]);
                double[] arr2 = new double[3];
                double x2 = f2.xDom()[0];//p.get(i+1)[p.size()-1][0];
                arr2[0] = f2.point(x2);
                arr2[1] = f2.deriv(x2);
                arr2[2] = f2.secondDeriv(x2);
                double xt1 = x1+0.25;
                double[] arrt1 = new double[3];
                arrt1[0] = arr1[0]+0.5;
                arrt1[1] = 2;
                arrt1[2] = 0;
                double xt2 = x2-0.75;
                double[] arrt2 = new double[3];
                arrt2[0] = arr2[0]-0.5*Math.signum(arr2[0]-arr1[0]);
                arrt2[1] = 2;
                arrt2[2] = 0;
                LinkH l1 = new LinkH(x1, arr1, xt1, arrt1);
                LinkV l2 = new LinkV(arrt1[0], flip(arrt1, xt1), arrt2[0], flip(arrt2, xt2));
                LinkH l3 = new LinkH(xt2, arrt2, x2, arr2);
                ArrayList<FieldPartition> elems = new ArrayList<FieldPartition>();
                elems.add(l1);
                elems.add(l2);
                elems.add(l3);
                pf.set(i, new LinkC(elems));
                System.out.println("The first case is randomly being executed");
            } else if(p.get(i-1)[0][0] < p.get(i-1)[p.get(i-1).length-1][0] && p.get(i+1)[0][0] > p.get(i+1)[p.get(i+1).length-1][0]) {
                FieldPartition f1 = pf.get(i-1);
                FieldPartition f2 = pf.get(i+1);
                double[] arr1 = new double[3];
                double x1 = f1.xDom()[1];//p.get(i-1)[p.size()-1][0];
                arr1[0] = f1.point(x1);
                arr1[1] = f1.deriv(x1);
                arr1[2] = f1.secondDeriv(x1);
                System.out.println("Derivs: "+arr1[1]+" "+arr1[2]);
                double[] arr2 = new double[3];
                double x2 = f2.xDom()[1];//p.get(i+1)[p.size()-1][0];
                arr2[0] = f2.point(x2);
                arr2[1] = f2.deriv(x2);
                arr2[2] = f2.secondDeriv(x2);
                double xt1 = x1+0.25;
                double[] arrt1 = new double[3];
                arrt1[0] = arr1[0]+0.5*Math.signum(arr2[0]-arr1[0]);
                arrt1[1] = 2;
                arrt1[2] = 0;
                double xt2 = x2+1.5;
                double[] arrt2 = new double[3];
                arrt2[0] = arr2[0]-0.5*Math.signum(arr2[0]-arr1[0]);
                arrt2[1] = -2;
                arrt2[2] = 0;
                LinkH l1 = new LinkH(x1, arr1, xt1, arrt1);
                LinkV l2 = new LinkV(arrt1[0], flip(arrt1, xt1), arrt2[0], flip(arrt2, xt2));
                LinkH l3 = new LinkH(xt2, arrt2, x2, arr2);
                ArrayList<FieldPartition> elems = new ArrayList<FieldPartition>();
                elems.add(l1);
                elems.add(l2);
                elems.add(l3);
                pf.set(i, new LinkC(elems));
                System.out.println("The case that I am not typing in is being executed");
            } else if(p.get(i-1)[0][0] > p.get(i-1)[p.get(i-1).length-1][0] && p.get(i+1)[0][0] < p.get(i+1)[p.get(i+1).length-1][0]) {
                FieldPartition f1 = pf.get(i-1);
                FieldPartition f2 = pf.get(i+1);
                double[] arr1 = new double[3];
                double x1 = f1.xDom()[0];//p.get(i-1)[p.size()-1][0];
                arr1[0] = f1.point(x1);
                arr1[1] = f1.deriv(x1);
                arr1[2] = f1.secondDeriv(x1);
                System.out.println("Derivs: "+arr1[1]+" "+arr1[2]);
                double[] arr2 = new double[3];
                double x2 = f2.xDom()[0];//p.get(i+1)[p.size()-1][0];
                arr2[0] = f2.point(x2);
                arr2[1] = f2.deriv(x2);
                arr2[2] = f2.secondDeriv(x2);
                double xt1 = x1-0.25;
                double[] arrt1 = new double[3];
                arrt1[0] = arr1[0]+0.5;
                arrt1[1] = -2;
                arrt1[2] = 0;
                double xt2 = x2-0.75;
                double[] arrt2 = new double[3];
                arrt2[0] = arr2[0]-0.5;
                arrt2[1] = 2;
                arrt2[2] = 0;
                LinkH l1 = new LinkH(x1, arr1, xt1, arrt1);
                LinkV l2 = new LinkV(arrt1[0], flip(arrt1, xt1), arrt2[0], flip(arrt2, xt2));
                LinkH l3 = new LinkH(xt2, arrt2, x2, arr2);
                ArrayList<FieldPartition> elems = new ArrayList<FieldPartition>();
                elems.add(l1);
                elems.add(l2);
                elems.add(l3);
                pf.set(i, new LinkC(elems));
                System.out.println("The case that I am typing in is being executed"+i+pf.size());
            } else {
                FieldPartition f1 = pf.get(i-1);
                FieldPartition f2 = pf.get(i+1);
                double[] arr1 = new double[3];
                double x1 = f1.xDom()[0];//p.get(i-1)[p.size()-1][0];
                arr1[0] = f1.point(x1);
                arr1[1] = f1.deriv(x1);
                arr1[2] = f1.secondDeriv(x1);
                System.out.println("Derivs: "+arr1[1]+" "+arr1[2]);
                double[] arr2 = new double[3];
                double x2 = f2.xDom()[1];//p.get(i+1)[p.size()-1][0];
                arr2[0] = f2.point(x2);
                arr2[1] = f2.deriv(x2);
                arr2[2] = f2.secondDeriv(x2);
                double xt1 = x1-0.25;
                double[] arrt1 = new double[3];
                arrt1[0] = arr1[0]+0.5;
                arrt1[1] = -2;
                arrt1[2] = 0;
                double xt2 = x2+1.25;
                double[] arrt2 = new double[3];
                arrt2[0] = arr2[0]-0.5;
                arrt2[1] = -2;
                arrt2[2] = 0;
                LinkH l1 = new LinkH(x1, arr1, xt1, arrt1);
                LinkV l2 = new LinkV(arrt1[0], flip(arrt1, xt1), arrt2[0], flip(arrt2, xt2));
                LinkH l3 = new LinkH(xt2, arrt2, x2, arr2);
                ArrayList<FieldPartition> elems = new ArrayList<FieldPartition>();
                elems.add(l1);
                elems.add(l2);
                elems.add(l3);
                pf.set(i, new LinkC(elems));
                System.out.println("The final case is being executed");
            }
        }
        FieldPartition ff = pf.get(pf.size()-1);
        double xe = path[path.length-1][0]+0.5;
        double[] arr = new double[3];
        arr[0] = path[path.length-1][1]+0.5;
        arr[1] = 0;
        arr[2] = 0;
        double[] arri = new double[3];
        boolean choose = Math.abs(ff.xDom()[0]-xe) < Math.abs(ff.xDom()[1]-xe);
        int cval = 1;
        if(choose) {
            cval = 0;
        }
        double x = ff.xDom()[cval];
        arri[0] = ff.point(x);
        arri[1] = ff.deriv(x);
        arri[2] = ff.secondDeriv(x);
        pf.add(new LinkH(x, arri, xe, arr));

        System.out.println(pf.size());
        //pf.add(new PathField(path, path.length));
        /*double x1 = xoff+9;
        double x2 = xoff+10;
        double[][] li = new double[2][];
        li[0] = new double[3];
        li[0][0] = pf.get(0).point(x1);
        li[0][1] = pf.get(0).deriv(x1);
        li[0][2] = pf.get(0).secondDeriv(x1);
        li[1] = new double[3];
        li[1][0] = pf.get(0).point(x1)+0.862;
        li[1][1] = 0;
        li[1][2] = 0;
        pf.add(new Link(x1, li[0], x2, li[1]));*/

        Components2 comp = new Components2(map, mp, pf);
        Viewer2 view = new Viewer2("A-Mazing Fields", comp, 1400, 800, 300, 100);
        for(;;) {
            comp.step();
            try {
                Thread.sleep((long)(Viewer2.step*1000));
            } catch(Exception e) {}
        }
    }

    public static void main(String[] args) {
        int[][] path = new int[30][];
        for(int i = 0; i < 30; i++) {
            path[i] = new int[2];
        }
        int off = 6;
        int xoff = 5;
        path[0][0] = xoff+10;
        path[0][1] = off+1;
        path[1][0] = xoff+10;
        path[1][1] = off;
        path[2][0] = xoff+9;
        path[2][1] = off;
        path[3][0] = xoff+8;
        path[3][1] = off;
        path[4][0] = xoff+7;
        path[4][1] = off;
        path[5][0] = xoff+6;
        path[5][1] = off;
        path[6][0] = xoff+5;
        path[6][1] = off;
        path[7][0] = xoff+5;
        path[7][1] = off+1;
        path[8][0] = xoff+5;
        path[8][1] = off+2;
        path[9][0] = xoff+4;
        path[9][1] = off+2;
        path[10][0] = xoff+3;
        path[10][1] = off+2;
        path[11][0] = xoff+2;
        path[11][1] = off+2;
        path[12][0] = xoff+2;
        path[12][1] = off+3;
        path[13][0] = xoff+1;
        path[13][1] = off+3;
        path[14][0] = xoff+1;
        path[14][1] = off+3;
        path[9][0] = xoff+6;
        path[9][1] = off+2;
        path[10][0] = xoff+7;
        path[10][1] = off+2;
        path[11][0] = xoff+8;
        path[11][1] = off+2;
        path[12][0] = xoff+8;
        path[12][1] = off+3;
        path[13][0] = xoff+9;
        path[13][1] = off+3;
        path[14][0] = xoff+10;
        path[14][1] = off+3;
        path[9][0] = xoff+5;
        path[9][1] = off+3;
        path[10][0] = xoff+5;
        path[10][1] = off+4;
        path[11][0] = xoff+5;
        path[11][1] = off+5;
        path[12][0] = xoff+4;
        path[12][1] = off+5;
        path[13][0] = xoff+3;
        path[13][1] = off+5;
        path[14][0] = xoff+2;
        path[14][1] = off+5;
        path[15][0] = xoff+1;
        path[15][1] = off+5;
        path[16][0] = xoff+1;
        path[16][1] = off+6;
        path[17][0] = xoff+1;
        path[17][1] = off+7;
        path[18][0] = xoff+2;
        path[18][1] = off+7;
        path[19][0] = xoff+2;
        path[19][1] = off+8;
        path[20][0] = xoff+3;
        path[20][1] = off+8;
        path[21][0] = xoff+4;
        path[21][1] = off+8;
        path[22][0] = xoff+5;
        path[22][1] = off+8;
        path[23][0] = xoff+6;
        path[23][1] = off+8;
        path[24][0] = xoff+6;
        path[24][1] = off+9;
        path[25][0] = xoff+6;
        path[25][1] = off+10;
        path[26][0] = xoff+7;
        path[26][1] = off+10;
        path[27][0] = xoff+8;
        path[27][1] = off+10;
        path[28][0] = xoff+9;
        path[28][1] = off+10;
        path[29][0] = xoff+9;
        path[29][1] = off+9;
        ArrayList<Integer[][]> p = partition(path);
        /*for(Integer[][] i:p) {
        for(Integer[] ii:i) {
        System.out.print(" ("+ii[0]+" | "+ii[1]+") ");
        }
        System.out.println("----Set Division----");
        }*/
        int[][] map = new int[20*30][];
        for(int ii = 0; ii < 30; ii++) {
            for(int i=0; i < 20; i++) {
                map[ii*20+i] = new int[2];
                map[ii*20+i][0] = i;
                map[ii*20+i][1] = ii;
            }
        }
        ArrayList<FieldPartition> pf = new ArrayList<FieldPartition>();
        for(Integer[][] i:p) {
            pf.add(null);
        }
        System.out.println(p.size());
        for(int i = 0; i < p.size(); i += 2) {
            try {
                int[][] test = new int[p.get(i).length][];
                for(int ii = 0; ii < test.length; ii++) {
                    test[ii] = new int[p.get(i)[ii].length];
                    for(int iii = 0; iii < test[ii].length; iii++) {
                        test[ii][iii] = p.get(i)[ii][iii];
                    }
                }
                System.out.println("Constructing fourier from:");
                print(test);
                pf.set(i,new PathField(test,test.length*2+2));
            } catch(IndexOutOfBoundsException e) {System.out.println("asdfs"+i);}
        }
        for(int i = 1; i < p.size()-1; i+= 2) {
            /*if(p.get(i).length <= 1 && Math.signum(p.get(i-1)[0][0]-p.get(i-1)[p.get(i-1).length-1][0]) == Math.signum(p.get(i+1)[0][0]-p.get(i+1)[p.get(i+1).length-1][0])) {
            FieldPartition f1 = pf.get(i-1);
            double[] arr1 = new double[3];
            double x1 = f1.xDom()[1];//p.get(i-1)[p.size()-1][0];
            arr1[0] = f1.point(x1);
            arr1[1] = f1.deriv(x1);
            arr1[2] = f1.secondDeriv(x1);
            FieldPartition f2 = pf.get(i+1);
            double[] arr2 = new double[3];
            double x2 = f2.xDom()[0];//p.get(i+1)[p.size()-1][0];
            arr2[0] = f2.point(x2);
            arr2[1] = f2.deriv(x2);
            arr2[2] = f2.secondDeriv(x2);
            System.out.println("Constructing link from: ");
            System.out.println(x1+" "+arr1[0]+" "+arr1[1]+" "+arr1[2]);
            System.out.println(x2+" "+arr2[0]+" "+arr2[1]+" "+arr2[2]);
            System.out.println(x1+" | "+f1.xDom()[1]);

            System.out.println(x2+" | "+f2.xDom()[0]);
            pf.set(i,new LinkH(x1, arr1, x2, arr2));
            System.out.println("Using LinkH");
            System.out.println("Only a LinkH case");
            } else */
            if(p.get(i-1)[0][0] < p.get(i-1)[p.get(i-1).length-1][0] && p.get(i+1)[0][0] < p.get(i+1)[p.get(i+1).length-1][0]) {
                FieldPartition f1 = pf.get(i-1);
                FieldPartition f2 = pf.get(i+1);
                double[] arr1 = new double[3];
                double x1 = f1.xDom()[1];//p.get(i-1)[p.size()-1][0];
                arr1[0] = f1.point(x1);
                arr1[1] = f1.deriv(x1);
                arr1[2] = f1.secondDeriv(x1);
                System.out.println("Derivs: "+arr1[1]+" "+arr1[2]);
                double[] arr2 = new double[3];
                double x2 = f2.xDom()[0];//p.get(i+1)[p.size()-1][0];
                arr2[0] = f2.point(x2);
                arr2[1] = f2.deriv(x2);
                arr2[2] = f2.secondDeriv(x2);
                double xt1 = x1+0.25;
                double[] arrt1 = new double[3];
                arrt1[0] = arr1[0]+0.5;
                arrt1[1] = 2;
                arrt1[2] = 0;
                double xt2 = x2-0.75;
                double[] arrt2 = new double[3];
                arrt2[0] = arr2[0]-0.5*Math.signum(arr2[0]-arr1[0]);
                arrt2[1] = 2;
                arrt2[2] = 0;
                LinkH l1 = new LinkH(x1, arr1, xt1, arrt1);
                LinkV l2 = new LinkV(arrt1[0], flip(arrt1, xt1), arrt2[0], flip(arrt2, xt2));
                LinkH l3 = new LinkH(xt2, arrt2, x2, arr2);
                ArrayList<FieldPartition> elems = new ArrayList<FieldPartition>();
                elems.add(l1);
                elems.add(l2);
                elems.add(l3);
                pf.set(i, new LinkC(elems));
                System.out.println("The first case is randomly being executed");
            } else if(p.get(i-1)[0][0] < p.get(i-1)[p.get(i-1).length-1][0] && p.get(i+1)[0][0] > p.get(i+1)[p.get(i+1).length-1][0]) {
                FieldPartition f1 = pf.get(i-1);
                FieldPartition f2 = pf.get(i+1);
                double[] arr1 = new double[3];
                double x1 = f1.xDom()[1];//p.get(i-1)[p.size()-1][0];
                arr1[0] = f1.point(x1);
                arr1[1] = f1.deriv(x1);
                arr1[2] = f1.secondDeriv(x1);
                System.out.println("Derivs: "+arr1[1]+" "+arr1[2]);
                double[] arr2 = new double[3];
                double x2 = f2.xDom()[1];//p.get(i+1)[p.size()-1][0];
                arr2[0] = f2.point(x2);
                arr2[1] = f2.deriv(x2);
                arr2[2] = f2.secondDeriv(x2);
                double xt1 = x1+0.25;
                double[] arrt1 = new double[3];
                arrt1[0] = arr1[0]+0.5*Math.signum(arr2[0]-arr1[0]);
                arrt1[1] = 2;
                arrt1[2] = 0;
                double xt2 = x2+1.5;
                double[] arrt2 = new double[3];
                arrt2[0] = arr2[0]-0.5*Math.signum(arr2[0]-arr1[0]);
                arrt2[1] = -2;
                arrt2[2] = 0;
                LinkH l1 = new LinkH(x1, arr1, xt1, arrt1);
                LinkV l2 = new LinkV(arrt1[0], flip(arrt1, xt1), arrt2[0], flip(arrt2, xt2));
                LinkH l3 = new LinkH(xt2, arrt2, x2, arr2);
                ArrayList<FieldPartition> elems = new ArrayList<FieldPartition>();
                elems.add(l1);
                elems.add(l2);
                elems.add(l3);
                pf.set(i, new LinkC(elems));
                System.out.println("The case that I am not typing in is being executed");
            } else if(p.get(i-1)[0][0] > p.get(i-1)[p.get(i-1).length-1][0] && p.get(i+1)[0][0] < p.get(i+1)[p.get(i+1).length-1][0]) {
                FieldPartition f1 = pf.get(i-1);
                FieldPartition f2 = pf.get(i+1);
                double[] arr1 = new double[3];
                double x1 = f1.xDom()[0];//p.get(i-1)[p.size()-1][0];
                arr1[0] = f1.point(x1);
                arr1[1] = f1.deriv(x1);
                arr1[2] = f1.secondDeriv(x1);
                System.out.println("Derivs: "+arr1[1]+" "+arr1[2]);
                double[] arr2 = new double[3];
                double x2 = f2.xDom()[0];//p.get(i+1)[p.size()-1][0];
                arr2[0] = f2.point(x2);
                arr2[1] = f2.deriv(x2);
                arr2[2] = f2.secondDeriv(x2);
                double xt1 = x1-0.25;
                double[] arrt1 = new double[3];
                arrt1[0] = arr1[0]+0.5;
                arrt1[1] = -2;
                arrt1[2] = 0;
                double xt2 = x2-0.75;
                double[] arrt2 = new double[3];
                arrt2[0] = arr2[0]-0.5;
                arrt2[1] = 2;
                arrt2[2] = 0;
                LinkH l1 = new LinkH(x1, arr1, xt1, arrt1);
                LinkV l2 = new LinkV(arrt1[0], flip(arrt1, xt1), arrt2[0], flip(arrt2, xt2));
                LinkH l3 = new LinkH(xt2, arrt2, x2, arr2);
                ArrayList<FieldPartition> elems = new ArrayList<FieldPartition>();
                elems.add(l1);
                elems.add(l2);
                elems.add(l3);
                pf.set(i, new LinkC(elems));
                System.out.println("The case that I am typing in is being executed"+i+pf.size());
            } else {
                FieldPartition f1 = pf.get(i-1);
                FieldPartition f2 = pf.get(i+1);
                double[] arr1 = new double[3];
                double x1 = f1.xDom()[0];//p.get(i-1)[p.size()-1][0];
                arr1[0] = f1.point(x1);
                arr1[1] = f1.deriv(x1);
                arr1[2] = f1.secondDeriv(x1);
                System.out.println("Derivs: "+arr1[1]+" "+arr1[2]);
                double[] arr2 = new double[3];
                double x2 = f2.xDom()[1];//p.get(i+1)[p.size()-1][0];
                arr2[0] = f2.point(x2);
                arr2[1] = f2.deriv(x2);
                arr2[2] = f2.secondDeriv(x2);
                double xt1 = x1-0.25;
                double[] arrt1 = new double[3];
                arrt1[0] = arr1[0]+0.5;
                arrt1[1] = -2;
                arrt1[2] = 0;
                double xt2 = x2+1.25;
                double[] arrt2 = new double[3];
                arrt2[0] = arr2[0]-0.5;
                arrt2[1] = -2;
                arrt2[2] = 0;
                LinkH l1 = new LinkH(x1, arr1, xt1, arrt1);
                LinkV l2 = new LinkV(arrt1[0], flip(arrt1, xt1), arrt2[0], flip(arrt2, xt2));
                LinkH l3 = new LinkH(xt2, arrt2, x2, arr2);
                ArrayList<FieldPartition> elems = new ArrayList<FieldPartition>();
                elems.add(l1);
                elems.add(l2);
                elems.add(l3);
                pf.set(i, new LinkC(elems));
                System.out.println("The final case is being executed");
            }
        }
        FieldPartition ff = pf.get(pf.size()-1);
        double xe = path[path.length-1][0]+0.5;
        double[] arr = new double[3];
        arr[0] = path[path.length-1][1]+0.5;
        arr[1] = 0;
        arr[2] = 0;
        double[] arri = new double[3];
        boolean choose = Math.abs(ff.xDom()[0]-xe) < Math.abs(ff.xDom()[1]-xe);
        int cval = 1;
        if(choose) {
            cval = 0;
        }
        double x = ff.xDom()[cval];
        arri[0] = ff.point(x);
        arri[1] = ff.deriv(x);
        arri[2] = ff.secondDeriv(x);
        pf.add(new LinkH(x, arri, xe, arr));

        System.out.println(pf.size());
        //pf.add(new PathField(path, path.length));
        /*double x1 = xoff+9;
        double x2 = xoff+10;
        double[][] li = new double[2][];
        li[0] = new double[3];
        li[0][0] = pf.get(0).point(x1);
        li[0][1] = pf.get(0).deriv(x1);
        li[0][2] = pf.get(0).secondDeriv(x1);
        li[1] = new double[3];
        li[1][0] = pf.get(0).point(x1)+0.862;
        li[1][1] = 0;
        li[1][2] = 0;
        pf.add(new Link(x1, li[0], x2, li[1]));*/
        boolean[][] mp = new boolean[10][];
        for(int i = 0; i < 10; i++) {
            mp[i] = new boolean[10];
            for(int ii = 0; ii < 10; ii++) {
                mp[i][ii] = true;
            }
        }
        Components2 comp = new Components2(map, mp, pf);
        Viewer2 view = new Viewer2("A-Mazing Fields", comp, 1400, 800, 300, 100);
        for(;;) {
            comp.step();
            try {
                Thread.sleep((long)(Viewer2.step*1000));
            } catch(Exception e) {}
        }
    }

    private static double[] flip(double[] inp, double init) {
        double[] ret = new double[inp.length];
        ret[0] = init;
        for(int i = 1; i < inp.length-1; i++) {
            ret[i] = 1.0/inp[i];
        }
        return ret;
    }

    private static void print(Object[][] arr) {
        for(int i = 0; i < arr.length; i++) {
            for(int ii = 0; ii < arr[i].length; ii++) {
                System.out.print(arr[i][ii]+" ");
            }
            System.out.println("");
        }
    }

    private static void print(int[][] arr) {
        for(int i = 0; i < arr.length; i++) {
            for(int ii = 0; ii < arr[i].length; ii++) {
                System.out.print(arr[i][ii]+" ");
            }
            System.out.println("");
        }
    }

    /*private static void print(Object[] arr) {
    for(int ii = 0; ii < arr.length; ii++) {
    System.out.print(arr[ii]+" ");
    }
    }*/

    private static ArrayList<Integer[][]> partition(int[][] path) {
        ArrayList<Integer[][]> ret = new ArrayList<Integer[][]>();
        ArrayList<Integer[]> p = new ArrayList<Integer[]>();
        int ih = path[0][1];
        int dev = 0;
        boolean linking = false;
        for(int i = 0; i < path.length; i++) {
            if(!linking && path[i][1] != ih && path[i][1] != ih+dev) {
                if(dev == 0) {
                    dev = path[i][1]-ih;
                } else {
                    ret.add(arToL(p));
                    linking = true;
                    ih = path[i][1];
                    p = new ArrayList<Integer[]>();
                }
            }
            if(i == path.length-1 || (linking && path[i][1] == path[i-1][1])) {
                ret.add(arToL(p));
                linking = false;
                ih = path[i][1];
                dev = 0;
                p = new ArrayList<Integer[]>();
            } else {
            }
            Integer[] a = new Integer[2];
            a[0] = path[i][0];
            a[1] = path[i][1];
            p.add(a);
        }
        return ret;
    }

    private static Integer[][] arToL(ArrayList<Integer[]> inp) {
        Integer[][] ret = new Integer[inp.size()][];
        for(int i = 0; i < inp.size(); i++) {
            ret[i] = new Integer[2];
            ret[i][0] = inp.get(i)[0];
            ret[i][1] = inp.get(i)[1];
        }
        return ret;
    }
}