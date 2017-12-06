import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.lang.StringBuilder;
import java.util.Hashtable;
public class BookGroupsCalculator {
    private ArrayList<ArrayList<Integer>> dataMatrix;
    private Hashtable<Integer,String> names;
    public BookGroupsCalculator(String fileName) throws FileNotFoundException {
        names = new Hashtable<Integer,String>();
        File inpfile = new File(fileName);
        Scanner in = new Scanner(inpfile);
        in.useDelimiter("\n");
        dataMatrix = new ArrayList<ArrayList<Integer>>();
        int c = 0;
        while(in.hasNext()) {
            dataMatrix.add(new ArrayList<Integer>());
            Scanner in2 = new Scanner(in.next());
            in2.useDelimiter(" ");
            names.put(c++,in2.next());
            while(in2.hasNext()) {
                dataMatrix.get(dataMatrix.size()-1).add(in2.nextInt());
            }
        }
    }
    
    public static void main(String args[]) throws FileNotFoundException{
        BookGroupsCalculator calc = new BookGroupsCalculator("data.txt");
        System.out.println(calc);
    }
    
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < dataMatrix.size(); i++) {
            sb.append(names.get(i)+":\t");
            for(int ii = 0; ii < dataMatrix.get(i).size(); ii++) {
                sb.append(dataMatrix.get(i).get(ii)+" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
