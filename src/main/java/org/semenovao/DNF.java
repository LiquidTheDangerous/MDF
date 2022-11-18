package org.semenovao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DNF
{
    ArrayList<Line> lines;
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

            this.lines.add(new Line(List.of(line),func.apply(line)));
        }
    }

    public int size(){
        return this.lines.size();
    }

}
