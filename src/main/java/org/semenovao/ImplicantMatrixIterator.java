package org.semenovao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class ImplicantMatrixIterator implements Iterator<ArrayList<Conjunction>> {

    ImplicantMatrix matrix;

    MaskSetIterator iterator;

    ArrayList<Integer> kern;
    ArrayList<Integer> implicants;

    public ImplicantMatrixIterator(ImplicantMatrix matrix){
        this.matrix = matrix;

        kern = this.matrix.findKernel();

        this.implicants = new ArrayList<>(this.matrix.implicants.size());
        for (int i = 0; i < this.matrix.implicants.size();++i){
            if (!kern.contains(i)){
                this.implicants.add(i);
            }
        }
        iterator = new MaskSetIterator(this.implicants.size());

    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public ArrayList<Conjunction> next() {
        while (iterator.hasNext()){
            ArrayList<Boolean> variant = iterator.next();
            List<Integer> lst = new LinkedList<>();
            for (int i = 0; i < variant.size();++i){
                if (variant.get(i)){
                    lst.add(this.implicants.get(i));
                }
            }
            lst.addAll(kern);
            if (this.matrix.canCoverage(lst.stream().mapToInt(x->x).toArray())){
                ArrayList<Conjunction> implicant_res = new ArrayList<>(this.implicants.size());
                for (int i : lst){
                    implicant_res.add(this.matrix.implicants.get(i));
                }
                return  implicant_res;
            }
        }
        return null;
    }
}
