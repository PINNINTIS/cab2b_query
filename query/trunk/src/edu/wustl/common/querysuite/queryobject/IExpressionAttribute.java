package edu.wustl.common.querysuite.queryobject;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;

public interface IExpressionAttribute extends IArithmeticOperand {
    IExpressionId getExpressionId();

    void setExpressionId(IExpressionId expressionId);

    AttributeInterface getAttribute();

    void setAttribute(AttributeInterface attribute);
}
