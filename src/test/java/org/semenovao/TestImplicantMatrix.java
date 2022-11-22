package org.semenovao;
import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;


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

    public void testApp(){
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
    }

}
