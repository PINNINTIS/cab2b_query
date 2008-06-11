package edu.wustl.common.querysuite.queryobject;

/**
 * Used to offset a date operand. Such an offset has a time interval associated
 * to it e.g. if the offset is 3 Months, then the time interval is "Month".
 * 
 * @author srinath_k
 * 
 * @param <T> the type of the time interval ({@link DSInterval} or
 *            {@link YMInterval}).
 * @see DSInterval
 * @see YMInterval
 */
public interface IDateOffset<T extends ITimeIntervalEnum> extends IBaseQueryObject {
    T getTimeInterval();
}
