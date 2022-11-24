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
        ArrayList keys = new ArrayList(jep.getSymbolTable().keySet());
        for (Object k : keys){
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
            Object eval_res = jep.evaluate(jep.parse(expression));
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

    public static String getBoundingStringFromConjunctions(Collection<? extends Conjunction> conjunctions, Collection<? extends String> variables){
        String[] v = variables.toArray(new String[0]);
        List<String> list = new LinkedList<>();
        for (Conjunction x : conjunctions) {
            String s = x.boundWithVariables(v);
            list.add(s);
        }
        return String.join("||", list);
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
            if (second instanceof Double && first instanceof Boolean){
                stack.push( !(Boolean) first || (Double) second != 0.0);
            }
            if (second instanceof Boolean && first instanceof Double){
                stack.push((Double) first == 0 || (Boolean) second);
            }
            if (second instanceof Double && first instanceof Double){
                stack.push( (Double) first == 0|| (Double) second != 0.0);
            }
//            return false;
        }
    }

}
