package edu.wustl.common.querysuite.queryobject.impl;

import edu.wustl.common.querysuite.queryobject.IDateOffset;
import edu.wustl.common.querysuite.queryobject.TimeInterval;

abstract class DateOffset extends BaseQueryObject implements IDateOffset {

    private static final long serialVersionUID = 3876691936011600104L;

    private TimeInterval timeInterval;

    public TimeInterval getTimeInterval() {
        return timeInterval;
    }

    public void setTimeInterval(TimeInterval timeInterval) {
        this.timeInterval = timeInterval;
    }

}
