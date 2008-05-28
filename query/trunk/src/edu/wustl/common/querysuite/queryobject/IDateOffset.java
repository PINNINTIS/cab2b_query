package edu.wustl.common.querysuite.queryobject;

public interface IDateOffset extends IBaseQueryObject {
    TimeInterval getTimeInterval();

    void setTimeInterval(TimeInterval timeInterval);
}
