package edu.wustl.common.querysuite.queryobject;

public interface IArithmeticOperand extends IOperand {
    TermType getTermType();

    void setTermType(TermType termType);
}
