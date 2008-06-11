package edu.wustl.common.querysuite.queryobject;

/**
 * Represents the time intervals that are completely deterministic. The name is
 * borrowed from the corresponding Oracle data type.
 * 
 * @author srinath_k
 * @see YMInterval
 */
public enum DSInterval implements ITimeIntervalEnum {
    Minute, Hour, Day, Week;
}
