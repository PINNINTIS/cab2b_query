package edu.wustl.common.querysuite.queryobject;

import java.io.ObjectStreamException;
import java.util.ArrayList;
import java.util.List;

import edu.wustl.common.util.CompoundEnum;

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
    static class TimeIntervalCompoundEnum<T extends Enum<?> & ITimeIntervalEnum>
            extends
                CompoundEnum<TimeIntervalCompoundEnum<T>, T> {
        private static final long serialVersionUID = -5638354869641955221L;

        // list of the primitive enums.
        private static final List<ITimeIntervalEnum> primitiveValues = new ArrayList<ITimeIntervalEnum>();

        // list of compound enums; ordering is same as the "values" list
        private static final List<TimeIntervalCompoundEnum<?>> values = new ArrayList<TimeIntervalCompoundEnum<?>>();

        // ordinal tracker
        private static int nextOrdinal = 0;

        // private constructor because this is an enum
        private TimeIntervalCompoundEnum(T primitiveEnum) {
            super(primitiveEnum, nextOrdinal++, primitiveEnum.name());
            primitiveValues.add(primitiveEnum);
            values.add(this);
        }

        // the compound enum values
        public static final TimeIntervalCompoundEnum<DSInterval> CSecond = new TimeIntervalCompoundEnum<DSInterval>(
                DSInterval.Second);

        public static final TimeIntervalCompoundEnum<DSInterval> CMinute = new TimeIntervalCompoundEnum<DSInterval>(
                DSInterval.Minute);

        public static final TimeIntervalCompoundEnum<DSInterval> CHour = new TimeIntervalCompoundEnum<DSInterval>(
                DSInterval.Hour);

        public static final TimeIntervalCompoundEnum<DSInterval> CDay = new TimeIntervalCompoundEnum<DSInterval>(
                DSInterval.Day);

        public static final TimeIntervalCompoundEnum<DSInterval> CWeek = new TimeIntervalCompoundEnum<DSInterval>(
                DSInterval.Week);

        public static final TimeIntervalCompoundEnum<YMInterval> CMonth = new TimeIntervalCompoundEnum<YMInterval>(
                YMInterval.Month);

        public static final TimeIntervalCompoundEnum<YMInterval> CQuarter = new TimeIntervalCompoundEnum<YMInterval>(
                YMInterval.Quarter);

        public static final TimeIntervalCompoundEnum<YMInterval> CYear = new TimeIntervalCompoundEnum<YMInterval>(
                YMInterval.Year);

        // all the compound enum values in this compound enum
        // this method is mandated by the CompoundEnum contract
        public static TimeIntervalCompoundEnum<?>[] values() {
            return values.toArray(new TimeIntervalCompoundEnum<?>[0]);
        }

        public static TimeIntervalCompoundEnum<?> valueOf(String name) {
            if (name == null) {
                throw new NullPointerException("name is null.");
            }
            for (TimeIntervalCompoundEnum<?> timeInterval : values) {
                if (timeInterval.name().equals(name)) {
                    return timeInterval;
                }
            }
            throw new IllegalArgumentException("no compound enum AllDataTypes." + name);
        }

        // returns all the primitive enum values in this compound enum.
        public static ITimeIntervalEnum[] primitiveValues() {
            return primitiveValues.toArray(new ITimeIntervalEnum[0]);
        }

        @SuppressWarnings("unchecked")
        public static <T extends Enum<?> & ITimeIntervalEnum> TimeIntervalCompoundEnum<T> compoundEnum(T primitiveEnum) {
            if (primitiveEnum == null) {
                throw new IllegalArgumentException();
            }
            int index = primitiveValues.indexOf(primitiveEnum);
            return (TimeIntervalCompoundEnum<T>) values.get(index);
        }

        // serialization; ensure unique instance
        private Object readResolve() throws ObjectStreamException {
            return values.get(ordinal());
        }
    };

    T getTimeInterval();
}
