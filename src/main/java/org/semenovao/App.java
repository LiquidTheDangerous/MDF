package org.semenovao;


import org.nfunk.jep.ParseException;

import java.util.ArrayList;
import java.util.Scanner;

public class App
{

    public static void main( String[] args ) throws ParseException {
        VectorExpressionHelper expressionHelper = new VectorExpressionHelper();
        System.out.println("Expression");
        Scanner scanner = new Scanner(System.in);
        String str = scanner.nextLine();
        expressionHelper.setExpression(str);
        Boolean[] vector = expressionHelper.getVectorFromExpression();
        ArrayList<String> variables = expressionHelper.getVariables();
        SDNF sdnf = new SDNF(vector);
        ImplicantMatrix matrix = new ImplicantMatrix(sdnf.getConjunctions(),sdnf.getImplicants().getConjunctions());

        System.out.println(matrix.toStringBoundArgs(variables.toArray(new String[0])));

        ArrayList<Conjunction> res = matrix.findShortestVariant();
        System.out.println(VectorExpressionHelper.getBoundingStringFromConjunctions(res,variables));

        //        System.out.println(matrix.toStringBoundArgs(String.valueOf(variables.stream().map(x->x.toString()))));
    }
}
