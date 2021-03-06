package edu.wustl.common.querysuite.queryobject.impl;

import edu.wustl.common.querysuite.queryobject.ILiteral;
import edu.wustl.common.querysuite.queryobject.TermType;

public class Literal extends ArithmeticOperand implements ILiteral {

    private static final long serialVersionUID = -6686386680871633180L;

    private String literal;

    protected Literal() {
    // for hibernate
    }

    public Literal(TermType termType) {
        super(termType);
        this.literal = "";
    }

    public String getLiteral() {
        return literal;
    }

    public void setLiteral(String literal) {
        this.literal = literal;
    }

}
