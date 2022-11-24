package org.semenovao;

public enum VariableValue
{
    ONE, ZERO, ANY;
    static VariableValue fromBool(boolean value){
        if(value){
            return VariableValue.ONE;
        }
        return VariableValue.ZERO;
    }

    public String toString(){
        String result = "";
        switch (this){
            case ANY:   result = "*"; break;
            case ZERO:  result = "0"; break;
            case ONE:   result = "1"; break;
        }
        return result;
    }
}
