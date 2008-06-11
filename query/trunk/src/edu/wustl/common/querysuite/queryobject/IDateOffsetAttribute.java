package edu.wustl.common.querysuite.queryobject;

/**
 * Represents an attribute that is to be used as an offset.
 * 
 * @author srinath_k
 * 
 * @param <T> the type of the time interval ({@link DSInterval} or
 *            {@link YMInterval}).
 * @see IDateOffset
 * @see IExpressionAttribute
 */
public interface IDateOffsetAttribute<T extends ITimeIntervalEnum> extends IDateOffset<T>, IExpressionAttribute {

}
