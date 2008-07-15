package edu.wustl.common.querysuite.queryobject.impl;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.wustl.cab2b.common.cache.AbstractEntityCache;
import edu.wustl.common.querysuite.queryobject.IExpressionAttribute;
import edu.wustl.common.querysuite.queryobject.IExpressionId;
import edu.wustl.common.querysuite.queryobject.TermType;

public class ExpressionAttribute extends ArithmeticOperand implements IExpressionAttribute {
    private static final long serialVersionUID = 2376055279144184693L;

    private IExpressionId expressionId;

    private AttributeInterface attribute;

    protected ExpressionAttribute() {
    // for hibernate
    }

    public ExpressionAttribute(IExpressionId expressionId, AttributeInterface attribute, TermType termType) {
        setExpressionId(expressionId);
        setAttribute(attribute);
        setTermType(termType);
    }

    @Override
    public void setTermType(TermType termType) {
        super.setTermType(termType);
    }

    public AttributeInterface getAttribute() {
        return attribute;
    }

    public void setAttribute(AttributeInterface attribute) {
        if (attribute == null) {
            throw new NullPointerException();
        }
        this.attribute = attribute;
    }

    public IExpressionId getExpressionId() {
        return expressionId;
    }

    public void setExpressionId(IExpressionId expressionId) {
        if (expressionId == null) {
            throw new NullPointerException();
        }
        this.expressionId = expressionId;
    }

    @Override
    public String toString() {
        return "ExprId: " + expressionId + ", Attribute: " + attribute;
    }

    // for hibernate

    @SuppressWarnings("unused")
    private Long getAttributeId() {
        return attribute.getId();
    }

    @SuppressWarnings("unused")
    private void setAttributeId(Long attributeId) {
        setAttribute(AbstractEntityCache.getCache().getAttributeById(attributeId));
    }
}
