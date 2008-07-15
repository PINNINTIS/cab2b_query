package edu.wustl.common.querysuite.queryobject;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;

/**
 * Represents an attribute in a term. Such an attribute must belong to an
 * existing {@link IExpression}'s entity.
 * 
 * @author srinath_k
 * @see IExpression#getQueryEntity()
 */
public interface IExpressionAttribute extends IArithmeticOperand {
    /**
     * @return the expression from which this attribute is sourced.
     */
    IExpressionId getExpressionId();

    void setExpressionId(IExpressionId expressionId);

    /**
     * @return the actual attribute which is used in a term.
     */
    AttributeInterface getAttribute();

    void setAttribute(AttributeInterface attribute);
}
