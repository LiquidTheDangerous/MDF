package org.semenovao;

import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.nfunk.jep.ParseException;

import java.util.stream.IntStream;

public class TestVectorExpressionHelper extends TestCase{
    public TestVectorExpressionHelper(String testName){
        super(testName);
    }
    public static Test suite() {
        return new TestSuite( TestIterator.class );
    }
    public void testApp() throws ParseException {
        var exprHelper = new VectorExpressionHelper();
        exprHelper.setExpression("x==y");
        var v1 = exprHelper.getVectorFromExpression();
        Assert.assertTrue(v1[0]);
        Assert.assertFalse(v1[1]);
        Assert.assertFalse(v1[2]);
        Assert.assertTrue(v1[3]);

        exprHelper.setExpression("x<=y");
        var v2 = exprHelper.getVectorFromExpression();
        Assert.assertTrue(v2[0]);
        Assert.assertTrue(v2[1]);
        Assert.assertFalse(v2[2]);
        Assert.assertTrue(v2[3]);
//
        exprHelper.setExpression("(x<=y)<=z");
        var v3 = exprHelper.getVectorFromExpression();
        Boolean[] expected = new Boolean[]{false,true,false,true,true,true,false,true};
        IntStream.range(0, v3.length).forEach(i -> Assert.assertEquals(expected[i], v3[i]));
    }
}
