package edu.wustl.common.querysuite.queryobject;

import edu.wustl.common.querysuite.utils.TermProcessor.TermString;

public interface ITerm extends IBaseExpression<ArithmeticOperator, IArithmeticOperand> {
    TermString getStringRepresentation();
}
