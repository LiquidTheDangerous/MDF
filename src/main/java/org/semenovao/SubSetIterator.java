package org.semenovao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class SubSetIterator implements Iterator<Collection<?>> {

    MaskSetIterator MaskIterator;
    Collection<?> collection;

    public SubSetIterator(Collection<?> collection){
        MaskIterator = new MaskSetIterator(collection.size());
        this.collection = collection;
    }

    @Override
    public boolean hasNext() {
        return MaskIterator.hasNext();
    }

    @Override
    public Collection<Object> next() {
        ArrayList<Boolean> indices = MaskIterator.next();

        ArrayList<Object> result = new ArrayList<>((int) indices.stream().filter(x->x).count());
        Iterator<?> iter = collection.iterator();
        //        result
        for (Boolean index : indices) {
            Object obj = iter.next();
            if (index) {
                result.add(obj);
            }
        }
//        IntStream.range(0,indices.size()).filter(indices::get).forEach(i->result.add(collection.g));

        return result;
    }
}
