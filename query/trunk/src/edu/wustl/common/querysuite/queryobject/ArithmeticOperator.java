package edu.wustl.common.querysuite.queryobject;

public enum ArithmeticOperator implements IBinaryOperator {
    Plus("+"), Minus("-"), MultipliedBy("*"), DividedBy("/"), Unknown("");

    private ArithmeticOperator(String s) {
        this.mathString = s;
    }

    private String mathString;
    
    public String mathString() {
        return mathString;
    }
}
