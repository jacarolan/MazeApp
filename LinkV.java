public class LinkV implements FieldPartition{
    double[][] pos;
    double[][] der;
    double[][] sder;
    double xoff;
    double fact;
    double[] coeffs;
    double[] xdom;
    double[] ydom;
    public LinkV(double x1, double[] p1, double x2, double[] p2) {
        xdom = new double[2];
        ydom = new double[2];
        xdom[0] = Math.min(x1,x2);
        xdom[1] = Math.max(x1,x2);
        ydom[0] = Math.min(p1[0], p2[0])-0.5;
        ydom[1] = Math.max(p1[0], p2[0])+0.5;
        fact = 1.0/(x2-x1);
        xoff = x1;
        coeffs = new double[6];
        System.out.println("Making LinkV from "+x1+","+p1[0]+" to "+x2+","+p2[0]);
        coeffs[0] = p1[0];
        coeffs[1] = p1[1]/fact;
        coeffs[2] = p1[2]/2.0/fact/fact;
        double[][] mat = new double[3][];
        for(int i = 0; i < mat.length; i++) {
            mat[i] = new double[4];
            if(i == 0) {
                mat[i][0] = 1;
                mat[i][1] = 1;
                mat[i][2] = 1;
                mat[i][3] = p2[0]-coeffs[0]-coeffs[1]-coeffs[2];
            }
            if(i == 1) {
                mat[i][0] = 3;
                mat[i][1] = 4;
                mat[i][2] = 5;
                mat[i][3] = p2[1]/fact-coeffs[1]-2*coeffs[2];
            }
            if(i == 2) {
                mat[i][0] = 6;
                mat[i][1] = 12;
                mat[i][2] = 20;
                mat[i][3] = p2[2]/fact-2*coeffs[2];
            }
        }
        double[] tcoeffs = solveMat(mat);
        for(int i = 0; i < 3; i++) {
            coeffs[i+3] = tcoeffs[i];
        }
        System.out.println("Info Vertical");
        double[] p = new double[2];
        p[0] = 12;
        p[1] = 6;
        double[] op = closestPoint(p);
        for(double i:coeffs) {
            System.out.print(i+" | ");
        }
        System.out.println("\n"+op[0]+" "+op[1]);
    }

    public boolean inDomain(double[] test) {
        return test[0] < ydom[1] && test[0] > ydom[0] && test[1] < xdom[1] && test[1] > xdom[0];
    }

    public double point(double x) {
        double ix = (x-xoff)*fact;
        double ret = 0;
        for(int i = 0; i < 6;i++) {
            ret += coeffs[i]*Math.pow(ix,i);
        }
        return ret;
    }
    
    public double deriv(double x) {
        double ix = (x-xoff)*fact;
        double ret = 0;
        for(int i = 1; i < 6;i++) {
            ret += i*coeffs[i]*Math.pow(ix,i-1);
        }
        ret *= fact;
        return ret;
    }

    public double secondDeriv(double x) {
        double ix = (x-xoff)*fact;
        double ret = 0;
        for(int i = 2; i < 6;i++) {
            ret += i*(i-1)*coeffs[i]*Math.pow(ix,i-2);
        }
        ret *= fact*fact;
        return ret;
    }
    
    private LinkV() {

    }
    
    public double curvature(double x) {
        return -secondDeriv(x)/Math.pow(deriv(x)*deriv(x)+1, 1.5);
    }
    
    public double[] getAccel(double[] ip) {
        double[] inp = new double[2];
        inp[0] = ip[1];
        inp[1] = ip[0];
        double[] p = closestPoint(inp);
        double d = deriv(p[0]);
        double[] ret = new double[2];
        double c = curvature(p[0]);
        ret[0] = -1.0/Math.sqrt(1+d*d)*c;
        ret[1] = d/Math.sqrt(1+d*d)*c;
        return ret;
    }

    private double[] closestPoint(double[] inp) {
        double[] in = inp;
        double x = 1.0/Math.abs(fact)/2.0+xoff;
        while(Math.abs(2*(x-inp[0])+2*(point(x)-inp[1])*deriv(x)) > 0.0000001) {
            x -= (2*(x-inp[0])+2*(point(x)-inp[1])*(deriv(x)))/(2+2*deriv(x)*deriv(x)+2*(point(x)-inp[1])*secondDeriv(x));
        }
        double[] ret = new double[2];
        ret[0] = Math.min(Math.max(xdom[0],x),xdom[1]);
        ret[1] = point(ret[0]);
        return ret;
    }
    
       public double[] xDom() {
        return ydom;
    }
    
        public double[] yDom() {
        return xdom;
    }

    public static void calc() {
        double[] i = {10, 1, 1};
        double[] i2 = {0, -1, 2};
        LinkV l = new LinkV(0, i, 7, i2);
    }

    public double[] solveMat(double[][] inp) {
        double[][] ret = inp;
        for(int i = 0; i < inp.length; i++) {
            ret = iterate(ret, i);
        }
        for(int i = 0; i < ret.length; i++) {
            ret[i][ret[i].length-1] /= ret[i][i];
            ret[i][i] = 1;
        }
        double[] ret2 = new double[ret.length];
        for(int i = 0; i < ret2.length; i++) {
            ret2[i] = ret[i][ret[i].length-1];
        }
        printMat(ret);
        return ret2;
    }

    private double[][] iterate(double[][] inp, int row) {
        double[][] ret = new double[inp.length][];
        for(int i = 0; i < inp.length; i++) {
            ret[i] = new double[inp[i].length];
        }
        for(int i = 0; i < inp.length; i++) {
            if( i != row) {
                double c = inp[i][row]/inp[row][row];
                for(int ii = 0; ii < inp.length+1; ii++) {
                    ret[i][ii] = inp[i][ii]-c*inp[row][ii];
                }
            } else {
                for(int ii = 0; ii < inp.length+1; ii++) {
                    ret[i][ii] = inp[i][ii];
                }
            }
        }
        return ret;
    }

    private void printMat(double[][] mat) {
        for(double[] i:mat) {
            for(double ii:i) {
                System.out.print(ii+"\t|\t");
            }
            System.out.println(" ");
        }
    }
}

/*public class LinkV implements FieldPartition{
    double[][] pos;
    double[][] der;
    double[][] sder;
    double xoff;
    double fact;
    double[] coeffs;
    double[] xdom;
    double[] ydom;
    public LinkV(double x1, double[] p1, double x2, double[] p2) {
        xdom = new double[2];
        ydom = new double[2];

        System.out.println("Making LinkV from "+p1[0]+","+x1+" to "+p2[0]+","+x2);
        System.out.println("Derivative Information: "+p1[1]+", "+p1[2]+" | "+p2[1]+", "+p2[2]);
        xdom[0] = Math.min(x1,x2);
        xdom[1] = Math.max(x1,x2);
        ydom[0] = Math.min(p1[0], p2[0])-1.0;
        ydom[1] = Math.max(p1[0], p2[0])+1.0;
        fact = 1.0/(x2-x1);
        xoff = x1;
        coeffs = new double[6];
        coeffs[0] = p1[0];
        coeffs[1] = p1[1]/fact;
        coeffs[2] = p1[2]/2.0/fact/fact;
        double[][] mat = new double[3][];
        for(int i = 0; i < mat.length; i++) {
            mat[i] = new double[4];
            if(i == 0) {
                mat[i][0] = 1;
                mat[i][1] = 1;
                mat[i][2] = 1;
                mat[i][3] = p2[0]-coeffs[0]-coeffs[1]-coeffs[2];
            }
            if(i == 1) {
                mat[i][0] = 3;
                mat[i][1] = 4;
                mat[i][2] = 5;
                mat[i][3] = p2[1]-coeffs[1]-2*coeffs[2];
            }
            if(i == 2) {
                mat[i][0] = 6;
                mat[i][1] = 12;
                mat[i][2] = 20;
                mat[i][3] = p2[2]-2*coeffs[2];
            }
        }
        double[] tcoeffs = solveMat(mat);
        for(int i = 0; i < 3; i++) {
            coeffs[i+3] = tcoeffs[i];
        }
        System.out.println("Info Vertical");
        double[] p = new double[2];
        p[0] = 12;
        p[1] = 6;
        double[] op = closestPoint(p);
        for(double i:coeffs) {
            System.out.print(i+" | ");
        }
        System.out.println("\n"+op[0]+" "+op[1]);
    }

    public boolean inDomain(double[] test) {
        return test[0] < ydom[1] && test[0] > ydom[0] && test[1] < xdom[1] && test[1] > xdom[0];
    }

    public double point(double x) {
        double ix = (x-xoff)*fact;
        double ret = 0;
        for(int i = 0; i < 6;i++) {
            ret += coeffs[i]*Math.pow(ix,i);
        }
        return ret;
    }

    public double deriv(double x) {
        double ix = (x-xoff)*fact;
        double ret = 0;
        for(int i = 1; i < 6;i++) {
            ret += i*coeffs[i]*Math.pow(ix,i-1);
        }
        ret *= fact;
        System.out.println("Derivative: "+ret);
        return ret;
    }

    public double secondDeriv(double x) {
        double ix = (x-xoff)*fact;
        double ret = 0;
        for(int i = 2; i < 6;i++) {
            ret += i*(i-1)*coeffs[i]*Math.pow(ix,i-2);
        }
        ret *= fact*fact;
        System.out.println("Second Derivative: "+ret);
        return ret;
    }

    private LinkV() {

    }

    public double curvature(double x) {
        //System.out.println("("+point(x)+", "+x+") Curvature: "+(-secondDeriv(x)/Math.pow(deriv(x)*deriv(x)+1, 1.5)));
        return -secondDeriv(x)/Math.pow(deriv(x)*deriv(x)+1, 1.5);
    }

    public double[] getAccel(double[] ip) {
        double[] inp = new double[2];
        inp[0] = ip[1];
        inp[1] = ip[0];
        double[] p = closestPoint(inp);
        double d = deriv(p[1]);
        double[] ret = new double[2];
        double c = curvature(p[1]);
        System.out.println("Distance: "+Math.sqrt(Math.pow((inp[0]-p[0]),2.0)+Math.pow((inp[1]-p[1]),2.0)));
        ret[0] = d/Math.sqrt(1+d*d)*c;//+Math.pow((inp[0]-p[0]),1.0)*100;
        ret[1] = -1.0/Math.sqrt(1+d*d)*c;//+Math.pow((inp[1]-p[1]),1.0)*100;
        System.out.println("Got accel from LinkV: "+ret[0]+" , "+ret[1]);
        return ret;
    }

    private double[] closestPoint(double[] inp) {
        double[] in = inp;
        double x = 1.0/Math.abs(fact)/2.0+xoff;
        while(Math.abs(2*(x-inp[0])+2*(point(x)-inp[1])*deriv(x)) > 0.0000001) {
            x -= (2*(x-inp[0])+2*(point(x)-inp[1])*(deriv(x)))/(2+2*deriv(x)*deriv(x)+2*(point(x)-inp[1])*secondDeriv(x));
        }
        double[] ret = new double[2];
        ret[0] = Math.min(Math.max(xdom[0],x),xdom[1]);
        ret[1] = point(ret[0]);
        return ret;
    }

    public double[] xDom() {
        return ydom;
    }

    public double[] yDom() {
        return xdom;
    }

    public static void calc() {
        double[] i = {10, 1, 1};
        double[] i2 = {0, -1, 2};
        LinkV l = new LinkV(0, i, 7, i2);
    }

    public double[] solveMat(double[][] inp) {
        double[][] ret = inp;
        for(int i = 0; i < inp.length; i++) {
            ret = iterate(ret, i);
        }
        for(int i = 0; i < ret.length; i++) {
            ret[i][ret[i].length-1] /= ret[i][i];
            ret[i][i] = 1;
        }
        double[] ret2 = new double[ret.length];
        for(int i = 0; i < ret2.length; i++) {
            ret2[i] = ret[i][ret[i].length-1];
        }
        printMat(ret);
        return ret2;
    }

    private double[][] iterate(double[][] inp, int row) {
        double[][] ret = new double[inp.length][];
        for(int i = 0; i < inp.length; i++) {
            ret[i] = new double[inp[i].length];
        }
        for(int i = 0; i < inp.length; i++) {
            if( i != row) {
                double c = inp[i][row]/inp[row][row];
                for(int ii = 0; ii < inp.length+1; ii++) {
                    ret[i][ii] = inp[i][ii]-c*inp[row][ii];
                }
            } else {
                for(int ii = 0; ii < inp.length+1; ii++) {
                    ret[i][ii] = inp[i][ii];
                }
            }
        }
        return ret;
    }

    private void printMat(double[][] mat) {
        for(double[] i:mat) {
            for(double ii:i) {
                System.out.print(ii+"\t|\t");
            }
            System.out.println(" ");
        }
    }
}*/


