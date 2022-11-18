package org.semenovao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DNF
{
    ArrayList<Line> lines;
    private DNF(){};
    public DNF(Collection<? extends Line> lines){
        this.lines = new ArrayList<>(lines);
    }

    public DNF(IDiscreteFunction func,int paramsCount) throws NoSuchMethodException, IllegalArgumentException {

        if (paramsCount > 32)
        {
            throw new IllegalArgumentException("too many args");
        }
        int linesCount = (int) Math.pow(2, paramsCount);
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

    DNF getImplicants(){
        DNF dnf = new DNF();
        dnf.lines = new ArrayList<>(this.lines.size());

        for (int i = 0; i < this.lines.size() - 1;++i){
            ArrayList<Line> lines = new ArrayList<>(this.size());
            for (int j = i + 1; j < this.lines.size(); ++j){
                var lineFirst = this.lines.get(i);

                var lineSecond = this.lines.get(j);

                var diff = lineFirst.difference(lineSecond);
                if (diff == 1){
                    int index = lineFirst.findFirstDifference(lineSecond);
                    Line l = lineFirst.clone();
                    l.set(index,VariableValue.ANY);
                    dnf.lines.add(l);
                }
            }
        }
        return dnf;
    }

    public int size(){
        return this.lines.size();
    }

}
