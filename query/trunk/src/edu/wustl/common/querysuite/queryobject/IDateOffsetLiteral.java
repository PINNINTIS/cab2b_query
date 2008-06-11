package edu.wustl.common.querysuite.queryobject;

/**
 * Represents a literal that is to be used as an offset.
 * 
 * @author srinath_k
 * 
 * @param <T> the type of the time interval ({@link DSInterval} or
 *            {@link YMInterval}).
 * @see IDateOffset
 * @see ILiteral
 */
public interface IDateOffsetLiteral<T extends ITimeIntervalEnum> extends IDateOffset<T>, ILiteral {

}
