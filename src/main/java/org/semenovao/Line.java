package org.semenovao;

import java.util.*;
import java.util.stream.Collectors;

public class Line implements Iterable<VariableValue>,Cloneable,Comparable<Line> {
    private ArrayList<VariableValue> line;
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

    public int findFirstDifference(Line other){
        for (int i = 0; i < this.line.size(); ++i){
          if(!(other.get(i).equals(this.get(i)))){
             return i;
          }
        }
        return -1;
    }


    public VariableValue get(int index) throws IndexOutOfBoundsException{
        return this.line.get(index);
    }

    public void set(int index, VariableValue value){
        this.line.set(index,value);
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
        for (int i = 0; i < this.line.size(); ++i){
            stringResult.append(this.line.get(this.line.size() - i - 1));
            if (i != this.line.size() - 1){
                stringResult.append(", ");
            }
        }
//        stringResult.append(String.join(",",this.line.stream().map(VariableValue::toString).collect(Collectors.toList())));
        stringResult.append('}');
        stringResult.append(String.format("(%d)",this.result?1:0));
        return stringResult.toString();
    }

    @Override
    public Iterator<VariableValue> iterator() {
        return this.line.iterator();
    }

    @Override
    public Line clone() {
        try {
            Line clone = (Line) super.clone();
            ArrayList<VariableValue> newValues = new ArrayList<>(this.line.size());
            newValues.addAll(this.line);
            clone.line = newValues;
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    @Override
    public int compareTo(Line o) {
        if (o.line.size() != this.line.size()){
            return -1;
        }
        int result = 0;
        for (int i = 0;i < this.line.size(); ++i){
            result += this.line.get(i).ordinal() - o.line.get(i).ordinal();
        }
        return result;
    }
}
