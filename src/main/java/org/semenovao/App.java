package org.semenovao;
//import org.nfunk.jep.*;


public class App 
{

    public static void main( String[] args ) throws NoSuchMethodException {
        IDiscreteFunction f = p ->  (!p[0]&&!p[1])||(p[0]&&p[2]||!p[0]&&!p[2]);
        DNF dnf = new DNF(f,3);
        var d = dnf.getImplicants();

        //        System.out.println(3 & 1<<0);
    }
}
