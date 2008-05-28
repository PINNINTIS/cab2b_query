package edu.wustl.common.querysuite.queryobject.impl;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.wustl.common.querysuite.queryobject.IExpressionAttribute;
import edu.wustl.common.querysuite.queryobject.IExpressionId;
import edu.wustl.common.querysuite.queryobject.TermType;

public class ExpressionAttribute extends ArithmeticOperand implements IExpressionAttribute {
    private static final long serialVersionUID = 2376055279144184693L;

    private IExpressionId expressionId;

    private AttributeInterface attribute;

    public ExpressionAttribute(IExpressionId expressionId, AttributeInterface attribute, TermType termType) {
        this.expressionId = expressionId;
        this.attribute = attribute;
        setTermType(termType);
    }

    @Override
    public void setTermType(TermType termType) {
        // TODO validate termtpe using attribute's data type.
        super.setTermType(termType);
    }

    public AttributeInterface getAttribute() {
        return attribute;
    }

    public void setAttribute(AttributeInterface attribute) {
        this.attribute = attribute;
    }

    public IExpressionId getExpressionId() {
        return expressionId;
    }

    public void setExpressionId(IExpressionId expressionId) {
        this.expressionId = expressionId;
    }

    @Override
    public String toString() {
        return "ExprId: " + expressionId + ", Attribute: " + attribute;
    }
}
