package org.semenovao;

import java.util.*;
import java.util.stream.IntStream;
import de.vandermeer.asciitable.*;
import de.vandermeer.skb.interfaces.transformers.textformat.TextAlignment;

public class ImplicantMatrix implements Iterable<ArrayList<Conjunction>>{
    List<Conjunction> conjunctions;
    List<Conjunction> implicants;

    ArrayList<ArrayList<Boolean>> absorptions;

    public ImplicantMatrix(List<Conjunction> conjunctions, List<Conjunction> implicants){
        this.conjunctions = conjunctions;
        this.implicants = implicants;
        if (this.implicants.size() == 0){
            this.implicants = this.conjunctions;
        }
        this.absorptions = new ArrayList<>(implicants.size());
        for (int i = 0; i < this.implicants.size();++i){
            this.absorptions.add(new ArrayList<>(conjunctions.size()));
            for (int j = 0; j < this.conjunctions.size();++j){
                this.absorptions.get(i).add(j,false);
            }
        }

        this.initializeAbortions();

//        for(int j = 0; j < this.implicants.size();++j)

    }

    public boolean get(int i, int j){
        return this.absorptions.get(i).get(j);
    }
    public boolean canCoverage(int[] indicesOfImplicants){
        Boolean[] coveredIndices = new Boolean[this.conjunctions.size()];
        Arrays.fill(coveredIndices,false);
        for (int i : indicesOfImplicants)
            for (int j = 0; j < this.conjunctions.size(); ++j) {
                if (coveredIndices[j]) continue;
                if (this.absorptions.get(i).get(j)) {
                    coveredIndices[j] = true;
                }
            }
        return IntStream
                .range(0, coveredIndices.length)
                .allMatch(i -> coveredIndices[i]);
    }
    private void initializeAbortions(){
        for (int i = 0; i < this.absorptions.size();++i){
            Conjunction implicant = this.implicants.get(i);
            for(int j = 0; j < this.absorptions.get(0).size();++j){
                Conjunction conjunct = this.conjunctions.get(j);
                if (implicant.canAbsorb(conjunct)){
                    this.absorptions.get(i).set(j,true);
                }
            }
        }
    }

    ArrayList<Integer> findKernel(){
        Set<Integer> lst = new HashSet<>(this.implicants.size());
        for (int j = 0; j < this.conjunctions.size();++j){
            int countTrue = 0;
            int  idx = -1;
            for (int i = 0; i < this.implicants.size();++i){
                if (this.absorptions.get(i).get(j)){
                    ++countTrue;
                    idx = i;
                }
            }
            if (countTrue == 1){
                lst.add(idx);
            }
        }
        return new ArrayList<>(lst);
    }

    public ArrayList<ArrayList<Conjunction>> findVariants(){
        ArrayList<ArrayList<Conjunction>> res = new ArrayList<>();

        ArrayList<Integer> kern = this.findKernel();
        ArrayList<Integer> impl = new ArrayList<>(this.implicants.size());
        for (int i = 0; i < this.implicants.size();++i){
            if (!kern.contains(i)){
                impl.add(i);
            }
        }

        MaskSetIterator iter = new MaskSetIterator(impl.size());
        while (iter.hasNext()){
            ArrayList<Boolean> variant = iter.next();
            List<Integer> lst = new LinkedList<>();
            for (int i = 0; i < variant.size();++i){
                if (variant.get(i)){
                    lst.add(impl.get(i));
                }
            }
            lst.addAll(kern);
            if (this.canCoverage(lst.stream().mapToInt(x->x).toArray())){
                ArrayList<Conjunction> implicant_res = new ArrayList<>(this.implicants.size());
                for (int i : lst){
                    implicant_res.add(this.implicants.get(i));
                }
                res.add(implicant_res);
            }
        }
        return res;
    }

    public ArrayList<Conjunction> findShortestVariant(){
        ArrayList<ArrayList<Conjunction>> variants = this.findVariants();

        ArrayList<Conjunction> result = null;
//        List<Conjunction> results
        int record = Integer.MAX_VALUE;
        for (ArrayList<Conjunction> variant : variants){
            int size = variant.stream().mapToInt(Conjunction::getSize).sum();
            if (size < record){
                record = size;
                result = variant;
            }
        }

        return result;
    }

    public String toStringBoundArgs(String... vars){
        AsciiTable at = new AsciiTable();
        List<Object> list = new LinkedList<>();
        list.add(" ");
        list.addAll(Arrays.asList(this.conjunctions.stream().map(x->x.boundWithVariables(vars)).toArray()));
        at.addRow(list).setTextAlignment(TextAlignment.CENTER);
        at.addRule();

        for (int i = 0; i < this.absorptions.size();++i){
            List<Object> lst = new LinkedList<>();
            lst.add(this.implicants.get(i).boundWithVariables(vars));
            List<String> result = new LinkedList<>();
            for (Boolean x : this.absorptions.get(i)) {
                String s = x ? "+" : " ";
                result.add(s);
            }
            lst.addAll(result);
            at.addRow(lst).setTextAlignment(TextAlignment.CENTER);
            at.addRule();
        }
        at.getContext().setWidth(this.conjunctions.size()*15);
        return at.render();
    }

    @Override
    public String toString(){
        AsciiTable at = new AsciiTable();
        List<Object> list = new LinkedList<>();
        list.add(" ");
        list.addAll(Arrays.asList(this.conjunctions.stream().map(Conjunction::toString).toArray()));
        at.addRow(list).setTextAlignment(TextAlignment.CENTER);
        at.addRule();

        for (int i = 0; i < this.absorptions.size();++i){
            List<Object> lst = new LinkedList<>();
            lst.add(this.implicants.get(i).toString());
            lst.addAll(this.absorptions.get(i));
            at.addRow(lst).setTextAlignment(TextAlignment.CENTER);
            at.addRule();
        }
        at.getContext().setWidth(this.conjunctions.size()*15);
        return at.render();
    }

    @Override
    public Iterator<ArrayList<Conjunction>> iterator() {
        return new ImplicantMatrixIterator(this);
    }
}
