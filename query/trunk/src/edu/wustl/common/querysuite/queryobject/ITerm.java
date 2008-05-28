package edu.wustl.common.querysuite.queryobject;

public interface ITerm extends IBaseExpression<ArithmeticOperator, IArithmeticOperand> {
    TermType getTermType();
}
