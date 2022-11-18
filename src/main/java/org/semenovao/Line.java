package org.semenovao;

import java.util.*;
import java.util.stream.Collectors;

public class Line implements Iterable<VariableValue> {
    private final ArrayList<VariableValue> line;
    private final boolean result;

    public Line(Collection<? extends Boolean> lineValues, boolean result) {
        this.line = lineValues.stream().map(VariableValue::fromBool)
                .collect(Collectors.toCollection(ArrayList::new));
        this.result = result;
    }
    public Line(boolean[] values, boolean result) {
        this.result = result;
        this.line = new ArrayList<>(values.length);
        for (boolean i: values) {
            this.line.add(VariableValue.fromBool(i));
        }
    }

    public int difference(Line other) throws IllegalArgumentException {
        if (this.getVariablesCount() != other.getVariablesCount()){
            throw new IllegalArgumentException("lines must be the same length");
        }
        int result = 0;
        for (int i = 0; i < this.line.size(); ++i){
          if(!(other.get(i).equals(this.get(i)))){
              ++result;
          }
        }
        return result;
    }


    public VariableValue get(int index) throws IndexOutOfBoundsException{
        return this.line.get(index);
    }

    public int getVariablesCount(){
        return this.line.size();
    }

    public boolean getResult(){
        return this.result;
    }

    @Override
    public String toString(){
        StringBuilder stringResult = new StringBuilder(this.line.size()*2 + 16);
        stringResult.append('{');

        stringResult.append(String.join(",",this.line.stream().map(VariableValue::toString).collect(Collectors.toList())));
        stringResult.append('}');
        stringResult.append(String.format("(%d)",this.result?1:0));
        return stringResult.toString();
    }

    @Override
    public Iterator<VariableValue> iterator() {
        return this.line.iterator();
    }
}
