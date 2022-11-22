package org.semenovao;

import org.nfunk.jep.JEP;
import org.nfunk.jep.Node;
import org.nfunk.jep.ParseException;
import org.nfunk.jep.function.PostfixMathCommand;

import java.util.*;

public class VectorExpressionHelper
{
    JEP jep;
    String expression;

    ArrayList<String> variables;
    public VectorExpressionHelper(){
        this.jep = new JEP();
        this.jep.getOperatorSet().getLE().setPFMC(new LessOrEqual());
        this.jep.setAllowUndeclared(true);
    }
    public Boolean[] getVectorFromExpression() throws ParseException {
        var keys = new ArrayList(jep.getSymbolTable().keySet());
        for (var k : keys){
            this.jep.removeVariable(k.toString());
        }

        Node node = jep.parse(expression);
        Set set = jep.getSymbolTable().keySet();

        ArrayList variables = new ArrayList<>(set);
        int varCount = variables.size();
        variables.sort(Comparator.comparing(Object::toString));

        this.variables = variables;

        Boolean[] result = new Boolean[1<<variables.size()];

        MaskSetIterator iter = new MaskSetIterator(varCount);
        int c = 0;
        while (iter.hasNext()){
            ArrayList<Boolean> bitMask = iter.next();
            for (int i = 0; i < bitMask.size();++i){
                jep.setVarValue(variables.get(i).toString(),bitMask.get(bitMask.size() - i - 1));
            }
            var eval_res = jep.evaluate(jep.parse(expression));
            if (eval_res instanceof Boolean){
                result[c++] = (Boolean) eval_res;
            }
            else if(eval_res instanceof Double){
                result[c++] = (Double)eval_res != 0;
            }
        }

        return result;
    }

    public void setExpression(String expression){
        this.expression = expression;
    }

    public ArrayList<String> getVariables(){
        return this.variables;
    }

    static String getBoundingStringFromConjunctions(Collection<? extends Conjunction> conjunctions, Collection<? extends String> variables){
        String[] v = variables.toArray(new String[0]);
        return String.join("||",conjunctions.stream().map(x->x.boundWithVariables(v)).toList());
    }

    static class LessOrEqual extends PostfixMathCommand {

        LessOrEqual(){
            this.numberOfParameters = 2;
        }

        @Override
        public void run(Stack stack) throws ParseException {
            this.checkStack(stack);
            Object second = stack.pop();
            Object first = stack.pop();
            if (second instanceof Boolean && first instanceof Boolean){
                stack.push( !(Boolean) first || (Boolean) second);
            }
//            return false;
        }
    }

}
