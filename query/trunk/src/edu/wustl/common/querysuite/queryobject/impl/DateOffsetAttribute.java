package edu.wustl.common.querysuite.queryobject.impl;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.wustl.common.querysuite.queryobject.IDateOffsetAttribute;
import edu.wustl.common.querysuite.queryobject.IExpressionId;
import edu.wustl.common.querysuite.queryobject.TermType;
import edu.wustl.common.querysuite.queryobject.TimeInterval;

public class DateOffsetAttribute extends ExpressionAttribute implements IDateOffsetAttribute {
    private static final long serialVersionUID = 3883684246378982941L;

    public DateOffsetAttribute(IExpressionId expressionId, AttributeInterface attribute) {
        super(expressionId, attribute, TermType.DateOffset);
    }

    private TimeInterval timeInterval = TimeInterval.Day;

    public TimeInterval getTimeInterval() {
        return timeInterval;
    }

    public void setTimeInterval(TimeInterval timeInterval) {
        this.timeInterval = timeInterval;
    }

}
