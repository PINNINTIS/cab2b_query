package edu.wustl.common.querysuite.queryobject.impl;

import edu.wustl.common.querysuite.queryobject.IDateOffsetLiteral;
import edu.wustl.common.querysuite.queryobject.ITimeIntervalEnum;
import edu.wustl.common.querysuite.queryobject.TermType;

public class DateOffsetLiteral<T extends ITimeIntervalEnum> extends Literal implements IDateOffsetLiteral<T> {
    private static final long serialVersionUID = -7510642736372664817L;

    private T timeInterval;

    public DateOffsetLiteral(T timeInterval) {
        super(TermType.termType(timeInterval));
        this.timeInterval = timeInterval;
    }

    public T getTimeInterval() {
        return timeInterval;
    }

}
