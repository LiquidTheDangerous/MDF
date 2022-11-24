package org.semenovao;


import org.nfunk.jep.ParseException;

import java.util.ArrayList;
import java.util.Scanner;

public class App
{

    public static void main( String[] args )  {
        while(true){
            VectorExpressionHelper expressionHelper = new VectorExpressionHelper();
            System.out.println("Expression");
            Scanner scanner = new Scanner(System.in);
            String str = scanner.nextLine();
//            String str;
//            str = "(x||y)<=z";
            if (str.equals("break")){
                break;
            }
            expressionHelper.setExpression(str);
            Boolean[] vector = new Boolean[0];
            try {
                vector = expressionHelper.getVectorFromExpression();
            } catch (ParseException e) {
                System.out.println("Parse error...");
                continue;
            }
            ArrayList<String> variables = expressionHelper.getVariables();
            SDNF sdnf = new SDNF(vector);
            ImplicantMatrix matrix = new ImplicantMatrix(sdnf.getConjunctions(),sdnf.getImplicants().getConjunctions());
            System.out.println(matrix.toStringBoundArgs(variables.toArray(new String[0])));

            ArrayList<Conjunction> res = null;
            int record = Integer.MAX_VALUE;
            for (ArrayList<Conjunction> solution : matrix){
                int size = 0;
                for (Conjunction i : solution){
                    size += i.getSize();
                }
                if (size < record){
                    record = size;
                    res = solution;
                }
                System.out.println(VectorExpressionHelper.getBoundingStringFromConjunctions(solution,variables));

            }

            System.out.printf("Shortest: %s%n",VectorExpressionHelper.getBoundingStringFromConjunctions(res,variables));



//            ArrayList<Conjunction> res = matrix.findShortestVariant();
//            System.out.println(VectorExpressionHelper.getBoundingStringFromConjunctions(res,variables));
        }

        //        System.out.println(matrix.toStringBoundArgs(String.valueOf(variables.stream().map(x->x.toString()))));
    }
}
