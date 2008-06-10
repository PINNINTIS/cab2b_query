package edu.wustl.common.querysuite.queryobject.impl;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.wustl.common.querysuite.queryobject.IDateOffsetAttribute;
import edu.wustl.common.querysuite.queryobject.IExpressionId;
import edu.wustl.common.querysuite.queryobject.ITimeIntervalEnum;
import edu.wustl.common.querysuite.queryobject.TermType;

public class DateOffsetAttribute<T extends ITimeIntervalEnum> extends ExpressionAttribute
        implements
            IDateOffsetAttribute<T> {
    private static final long serialVersionUID = 3883684246378982941L;

    private T timeInterval;

    public DateOffsetAttribute(IExpressionId expressionId, AttributeInterface attribute, T timeInterval) {
        super(expressionId, attribute, TermType.termType(timeInterval));
        this.timeInterval = timeInterval;
    }

    public T getTimeInterval() {
        return timeInterval;
    }
}
