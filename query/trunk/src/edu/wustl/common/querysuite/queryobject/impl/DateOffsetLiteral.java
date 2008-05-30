package edu.wustl.common.querysuite.queryobject.impl;

import edu.wustl.common.querysuite.queryobject.IDateOffsetLiteral;
import edu.wustl.common.querysuite.queryobject.TermType;
import edu.wustl.common.querysuite.queryobject.TimeInterval;

public class DateOffsetLiteral extends Literal implements IDateOffsetLiteral {
    private static final long serialVersionUID = -7510642736372664817L;

    private TimeInterval timeInterval;

    public DateOffsetLiteral() {
        super(TermType.DateOffset);
        this.timeInterval = TimeInterval.Day;
    }

    public DateOffsetLiteral(String literal, TimeInterval timeInterval) {
        super(literal, TermType.DateOffset);
        this.timeInterval = timeInterval;
    }

    public TimeInterval getTimeInterval() {
        return timeInterval;
    }

    public void setTimeInterval(TimeInterval timeInterval) {
        this.timeInterval = timeInterval;
    }

}
