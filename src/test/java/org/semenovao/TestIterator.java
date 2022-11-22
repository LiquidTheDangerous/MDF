package org.semenovao;

import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class TestIterator
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public TestIterator(String testName )
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

    public void testApp()
    {
        int len = 5;
        var iterator = new MaskSetIterator(len);
        int matchSum = 0;
        while(iterator.hasNext()){
            var i = iterator.next();
            int sum = 0;
            for(int j = 0; j < len; ++j){
                if (i.get(j)){
                    sum += 1<<j;
                }
            }
            Assert.assertEquals(sum, matchSum);
            ++matchSum;
        }
    }
}
