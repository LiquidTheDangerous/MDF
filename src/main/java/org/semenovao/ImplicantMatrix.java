package org.semenovao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class ImplicantMatrix {
    List<Conjunction> conjunctions;
    List<Conjunction> implicants;

    ArrayList<ArrayList<Boolean>> absorptions;

    public ImplicantMatrix(List<Conjunction> conjunctions, List<Conjunction> implicants){
        this.conjunctions = conjunctions;
        this.implicants = implicants;
        this.absorptions = new ArrayList<>(implicants.size());
        for (int i = 0; i < this.implicants.size();++i){
            this.absorptions.add(new ArrayList<>(conjunctions.size()));
            for (int j = 0; j < this.conjunctions.size();++j){
                this.absorptions.get(i).add(j,false);
            }
        }

        this.initializeAbortions();

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
            var implicant = this.implicants.get(i);
            for(int j = 0; j < this.absorptions.get(0).size();++j){
                var conjunct = this.conjunctions.get(j);
                if (implicant.canAbsorb(conjunct)){
                    this.absorptions.get(i).set(j,true);
                }
            }
        }
    }

}
