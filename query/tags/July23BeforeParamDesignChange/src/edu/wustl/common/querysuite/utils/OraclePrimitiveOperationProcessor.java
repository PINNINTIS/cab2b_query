package edu.wustl.common.querysuite.utils;

import edu.wustl.common.querysuite.queryobject.DSInterval;
import edu.wustl.common.querysuite.queryobject.ITimeIntervalEnum;
import edu.wustl.common.querysuite.queryobject.TimeInterval;
import edu.wustl.common.querysuite.queryobject.YMInterval;

class OraclePrimitiveOperationProcessor extends SQLPrimitiveOperationProcessor {

    OraclePrimitiveOperationProcessor() {
        super("YYYY-MM-DD", "TO_DATE");
    }

    @Override
    String getDateDiffString(String leftStr, String rightStr) {
        return leftStr + " - " + rightStr;
    }

    @Override
    String getDateOffsetString(String s, TimeInterval<?> compoundTimeInterval) {
        ITimeIntervalEnum timeInterval = compoundTimeInterval.primitiveEnum();
        if (timeInterval == YMInterval.Quarter) {
            s = mult(s, 3);
            timeInterval = YMInterval.Month;
        } else if (timeInterval == DSInterval.Week) {
            s = mult(s, 7);
            timeInterval = DSInterval.Day;
        }
        final String func = getFunc(timeInterval);

        return func + "(" + s + ", '" + timeInterval + "')";
    }

    private String getFunc(ITimeIntervalEnum timeInterval) {
        if (isDayToSec(timeInterval)) {
            return "NUMTODSINTERVAL";
        } else {
            return "NUMTOYMINTERVAL";
        }
    }

    private boolean isDayToSec(ITimeIntervalEnum timeInterval) {
        return timeInterval instanceof DSInterval;
    }

    private String mult(String s, int i) {
        return "(" + s + ") * " + String.valueOf(i);
    }

    @Override
    String dateToTimestamp(String s) {
        // casting to timestamp is needed in view of further interval
        // arithmetic.
        return "cast(" + s + " as timestamp)";
    }
}
