package org.semenovao;
//import org.nfunk.jep.*;


public class App 
{

    public static void main( String[] args ) {
//        System.out.println(31 - Integer.numberOfLeadingZeros(5));
//        IDiscreteFunction f = p ->  (!p[0]&&!p[1])||(p[0]&&p[2]||!p[0]&&!p[2]);
        SDNF dnf = new SDNF(new Boolean[]{true,true,false,true,true,true,false,false});
        var d = dnf.getImplicants();
        System.out.println(16 & (16 - 1));
        //        System.out.println(3 & 1<<0);
    }
}
