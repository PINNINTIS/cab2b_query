package edu.wustl.common.querysuite.queryobject.impl;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.wustl.common.querysuite.queryobject.IDateOffsetAttribute;
import edu.wustl.common.querysuite.queryobject.IExpressionId;
import edu.wustl.common.querysuite.queryobject.ITimeIntervalEnum;
import edu.wustl.common.querysuite.queryobject.TermType;

public class DateOffsetAttribute<T extends Enum<?> & ITimeIntervalEnum> extends ExpressionAttribute
        implements
            IDateOffsetAttribute<T> {
    private static final long serialVersionUID = 3883684246378982941L;

    private TimeIntervalCompoundEnum<T> compoundTimeInterval;

    protected DateOffsetAttribute() {

    }

    public DateOffsetAttribute(IExpressionId expressionId, AttributeInterface attribute, T timeInterval) {
        super(expressionId, attribute, TermType.termType(timeInterval));
        if (timeInterval == null) {
            throw new NullPointerException();
        }
        this.compoundTimeInterval = TimeIntervalCompoundEnum.compoundEnum(timeInterval);
    }

    public T getTimeInterval() {
        return compoundTimeInterval.primitiveEnum();
    }

    // for hibernate
    @SuppressWarnings("unused")
    private TimeIntervalCompoundEnum<T> getCompoundTimeInterval() {
        return compoundTimeInterval;
    }

    @SuppressWarnings("unused")
    private void setCompoundTimeInterval(TimeIntervalCompoundEnum<T> timeIntervalCompoundEnum) {
        this.compoundTimeInterval = timeIntervalCompoundEnum;
    }

}
