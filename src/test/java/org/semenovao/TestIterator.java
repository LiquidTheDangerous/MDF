package org.semenovao;

import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.util.ArrayList;
import java.util.Arrays;

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
        MaskSetIterator iterator = new MaskSetIterator(len);
        int matchSum = 0;
        while(iterator.hasNext()){
            ArrayList<Boolean> i = iterator.next();
            int sum = 0;
            for(int j = 0; j < len; ++j){
                if (i.get(j)){
                    sum += 1<<j;
                }
            }
            Assert.assertEquals(sum, matchSum);
            ++matchSum;
        }

        SubSetIterator iter = new SubSetIterator(Arrays.asList(1,2,3,4,5,6,7));
        while (iter.hasNext()){
            System.out.println(iter.next());
        }
    }
}
