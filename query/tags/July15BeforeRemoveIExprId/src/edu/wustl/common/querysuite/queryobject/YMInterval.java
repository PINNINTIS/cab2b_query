package edu.wustl.common.querysuite.queryobject;

/**
 * Represents the time intervals whose exact duration depends on the context in
 * which the interval is used. For example, when "1 Month" is to be added to a
 * date field, it may mean 28, 29, 30 or 31 days depending on the value of the
 * date field. The name is borrowed from the corresponding Oracle data type.
 * 
 * @author srinath_k
 * @see DSInterval
 */
public enum YMInterval implements ITimeIntervalEnum {
    Month, Quarter, Year
}
