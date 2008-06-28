package edu.wustl.common.querysuite.queryobject.impl;

import edu.wustl.common.querysuite.queryobject.IDateOffsetLiteral;
import edu.wustl.common.querysuite.queryobject.ITimeIntervalEnum;
import edu.wustl.common.querysuite.queryobject.TermType;

public class DateOffsetLiteral<T extends Enum<?> & ITimeIntervalEnum> extends Literal implements IDateOffsetLiteral<T> {
    private static final long serialVersionUID = -7510642736372664817L;

    private TimeIntervalCompoundEnum<T> compoundTimeInterval;

    protected DateOffsetLiteral() {

    }

    public DateOffsetLiteral(T timeInterval) {
        super(TermType.termType(timeInterval));
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
