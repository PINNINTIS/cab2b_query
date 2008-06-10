package edu.wustl.common.querysuite.queryobject.impl;

import edu.wustl.common.querysuite.queryobject.IDateOffset;
import edu.wustl.common.querysuite.queryobject.ITimeIntervalEnum;

abstract class DateOffset<T extends ITimeIntervalEnum> extends BaseQueryObject implements IDateOffset<T> {

    private static final long serialVersionUID = 3876691936011600104L;

    private T timeInterval;

    public T getTimeInterval() {
        return timeInterval;
    }

    public void setTimeInterval(T timeInterval) {
        this.timeInterval = timeInterval;
    }

}
