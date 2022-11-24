package org.semenovao;
import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.nfunk.jep.ParseException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class TestImplicantMatrix extends TestCase {
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public TestImplicantMatrix(String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( TestIterator.class );
    }

    public void testApp() throws ParseException {
        SDNF dnf = new SDNF(new Boolean[]{true,true,true ,false,false,false,true,false,false,false,false,false,false,false,true,true});
        ImplicantMatrix matrix = new ImplicantMatrix(dnf.getConjunctions(),dnf.getImplicants().getConjunctions());
        Assert.assertTrue(matrix.get(0,0));
        Assert.assertTrue(matrix.get(0,1));
        Assert.assertTrue(matrix.get(1,0));
        Assert.assertTrue(matrix.get(1,2));
        Assert.assertTrue(matrix.get(2,2));
        Assert.assertTrue(matrix.get(2,3));
        Assert.assertTrue(matrix.get(3,3));
        Assert.assertTrue(matrix.get(3,4));
        Assert.assertTrue(matrix.get(4,4));
        Assert.assertTrue(matrix.get(4,5));




        VectorExpressionHelper exprHelper = new VectorExpressionHelper();
        exprHelper.setExpression("(x<=y)<=z");
        Boolean[] v3 = exprHelper.getVectorFromExpression();
        SDNF dnff = new SDNF(v3);
        ImplicantMatrix matrixx = new ImplicantMatrix(dnff.getConjunctions(),dnff.getImplicants().getConjunctions());
        ImplicantMatrix matrixxx = new ImplicantMatrix(dnff.getConjunctions(),dnff.getImplicants().getConjunctions());
        System.out.println(VectorExpressionHelper.getBoundingStringFromConjunctions(matrixx.findShortestVariant(),Arrays.asList("x1","x2","x3","w")));
        ImplicantMatrixIterator iter = new ImplicantMatrixIterator(matrixx);
        ArrayList<ArrayList<Conjunction>> conj = matrixxx.findVariants();

        int c = 0;
        while (iter.hasNext()){
            ArrayList<Conjunction> obj = iter.next();
            Assert.assertEquals(conj.get(c++).toString(),obj.toString());
        }


        VectorExpressionHelper exprHelper2 = new VectorExpressionHelper();
        exprHelper2.setExpression("((x<=y)<=z)<=w");
        Boolean[] v4 = exprHelper2.getVectorFromExpression();
        ArrayList<String> names = exprHelper2.getVariables();
        SDNF dnfff = new SDNF(v4);

        ImplicantMatrix matrixxxx = new ImplicantMatrix(dnfff.getConjunctions(),dnfff.getImplicants().getConjunctions());
        System.out.println(matrixxxx.toStringBoundArgs(names.toArray(new String[0])));
        ArrayList<Conjunction> res = matrixxxx.findShortestVariant();
        System.out.println(VectorExpressionHelper.getBoundingStringFromConjunctions(res, names));



    }

}
