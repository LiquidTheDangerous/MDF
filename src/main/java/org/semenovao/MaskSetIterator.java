package org.semenovao;

import java.util.*;

public class MaskSetIterator implements Iterator<ArrayList<Boolean>>
{
    private final long maxNumber;
    private long number;
    private final long len;

    public MaskSetIterator(long len) {
        this.len = len;
        this.number = 0;
        this.maxNumber = 1L <<len;
    }

    @Override
    public boolean hasNext() {
        return number < maxNumber;
    }

    @Override
    public ArrayList<Boolean> next() {
        ArrayList<Boolean> mas = new ArrayList<>((int) this.len);
        for (int i = 0; i < len; i++)
            mas.add(((number & (1L << i)) != 0));
        ++this.number;
        return mas;
    }

}
