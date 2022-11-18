package org.semenovao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

public class SDNF
{
    ArrayList<Line> lines;
    private SDNF(){}
    public SDNF(Collection<? extends Line> lines){
        this.lines = new ArrayList<>(lines);
    }
    public SDNF(Boolean[] vector) {
        int size = vector.length;
        var capacity = Stream
                .of(vector)
                .filter(
                        x -> x
                )
                .count();
        this.lines = new ArrayList<>((int) capacity);
        if ((size & (size - 1)) != 0){
            throw new IllegalArgumentException("len must be power of two");
        }
        int paramsCount = 31 - Integer.numberOfLeadingZeros(size);
        for (int i = 0;i < size;++i)
        {
            if (!vector[i]){
                continue;
            }
            Boolean[] line = new Boolean[paramsCount];
            int radx = 1;
            int c = 0;
            while (radx < size){
                line[c++] = (radx & i) != 0;
                radx = radx << 1;
            }
            this.lines.add(new Line(List.of(line),true));
        }

    }

    public SDNF(IDiscreteFunction func, int paramsCount) throws  IllegalArgumentException {

        if (paramsCount > 32)
        {
            throw new IllegalArgumentException("too many args");
        }
        int linesCount =  1<<paramsCount;
        this.lines = new ArrayList<>(linesCount);
        for (int i = 0;i < linesCount;++i)
        {
            Boolean[] line = new Boolean[paramsCount];
            int radx = 1;
            int c = 0;
            while (radx < linesCount){
                line[c++] = (radx & i) != 0;
                radx = radx << 1;
            }
            if (func.apply(line)){
                this.lines.add(new Line(List.of(line),func.apply(line)));
            }
        }
    }

    void extend(SDNF dnf){
        this.lines.addAll(dnf.lines);
    }

    public SDNF getImplicants() {
        SDNF result = new SDNF();
        result.lines = new ArrayList<>(this.lines.size()*4);
        SDNF next = this.getNextImplicants();
        do {
            result.extend(next);
            next = next.getNextImplicants();
        } while (next.lines.size() != 0);
        return result;
    }
    SDNF getNextImplicants(){
        SDNF dnf = new SDNF();
        dnf.lines = new ArrayList<>(this.lines.size());

        for (int i = 0; i < this.lines.size() - 1;++i){
            for (int j = i + 1; j < this.lines.size(); ++j){
                var lineFirst = this.lines.get(i);

                var lineSecond = this.lines.get(j);

                var diff = lineFirst.difference(lineSecond);
                if (diff == 1){
                    int index = lineFirst.findFirstDifference(lineSecond);
                    Line l = lineFirst.clone();
                    l.set(index,VariableValue.ANY);
                    if (dnf.lines.stream().noneMatch(x-> x.difference(l) == 0)){
                        dnf.lines.add(l);
                    }
                }
            }
        }
        return dnf;
    }

    public int size(){
        return this.lines.size();
    }

}
