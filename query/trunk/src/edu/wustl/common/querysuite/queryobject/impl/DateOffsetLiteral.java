package edu.wustl.common.querysuite.queryobject.impl;

import edu.wustl.common.querysuite.queryobject.IDateOffsetLiteral;
import edu.wustl.common.querysuite.queryobject.ITimeIntervalEnum;
import edu.wustl.common.querysuite.queryobject.TermType;

public class DateOffsetLiteral<T extends Enum<?> & ITimeIntervalEnum> extends ArithmeticOperand
        implements
            IDateOffsetLiteral<T> {
    private static final long serialVersionUID = -7510642736372664817L;

    private TimeIntervalCompoundEnum<T> compoundTimeInterval;

    private String offset;

    private DateOffsetLiteral() {
        super(TermType.DSInterval);
    }

    public DateOffsetLiteral(T timeInterval) {
        super(TermType.termType(timeInterval));
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

    public String getOffset() {
        if (offset == null) {
            offset = "";
        }
        return offset;
    }

    public void setOffset(String offset) {
        this.offset = offset;
    }

}
