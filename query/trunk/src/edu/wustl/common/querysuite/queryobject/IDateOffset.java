package edu.wustl.common.querysuite.queryobject;

public interface IDateOffset<T extends ITimeIntervalEnum> extends IBaseQueryObject {
    T getTimeInterval();
}
