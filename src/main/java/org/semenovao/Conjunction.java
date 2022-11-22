package org.semenovao;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Conjunction implements Iterable<VariableValue>,Cloneable,Comparable<Conjunction> {
    private ArrayList<VariableValue> line;
    private final boolean result;

    public Conjunction(Collection<? extends Boolean> lineValues, boolean result) {
        this.line = lineValues.stream().map(VariableValue::fromBool)
                .collect(Collectors.toCollection(ArrayList::new));
        this.result = result;
    }
    public Conjunction(boolean[] values, boolean result) {
        this.result = result;
        this.line = new ArrayList<>(values.length);
        for (boolean i : values) {
            this.line.add(VariableValue.fromBool(i));
        }
    }

    public int difference(Conjunction other) throws IllegalArgumentException {
        if (this.getVariablesCount() != other.getVariablesCount()){
            throw new IllegalArgumentException("lines must be the same length");
        }
        return (int) IntStream
                .range(0, this.line.size())
                .filter(i -> !(other.get(i).equals(this.get(i))))
                .count();
    }

    public int getSize(){
        return (int) this.line.stream()
                .filter(variableValue -> variableValue != VariableValue.ANY)
                .count();
    }

    public int findFirstDifference(Conjunction other){
        return IntStream
                .range(0, this.line.size())
                .filter(i -> !(other.get(i).equals(this.get(i))))
                .findFirst()
                .orElse(-1);
    }

    String boundWithVariables(String... vars){
        StringBuilder string = new StringBuilder(this.line.size()*2);
        for (int i = 0; i < this.line.size(); ++i){
            switch (this.line.get(this.line.size() - i - 1)){
                case ONE -> string.append(vars[i]);
                case ZERO -> string.append(String.format("!%s",vars[i]));
            }
        }
        return string.toString();
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

    public boolean canAbsorb(Conjunction other){
        if (other.line.size() != this.line.size()){
            return false;
        }

        return IntStream
                .range(0, this.line.size())
                .filter(i -> this.line.get(i) != VariableValue.ANY)
                .noneMatch(i -> this.line.get(i) != other.line.get(i));
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
        stringResult.append('}');
        return stringResult.toString();
    }

    @Override
    public Iterator<VariableValue> iterator() {
        return this.line.iterator();
    }

    @Override
    public Conjunction clone() {
        try {
            Conjunction clone = (Conjunction) super.clone();
            ArrayList<VariableValue> newValues = new ArrayList<>(this.line.size());
            newValues.addAll(this.line);
            clone.line = newValues;
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    @Override
    public int compareTo(Conjunction o) {
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
