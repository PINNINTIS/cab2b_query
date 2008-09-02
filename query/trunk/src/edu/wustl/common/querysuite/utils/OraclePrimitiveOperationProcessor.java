package edu.wustl.common.querysuite.utils;

import edu.wustl.common.querysuite.queryobject.ArithmeticOperator;
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
        if (timeInterval instanceof YMInterval) {
            return getYMIntervalString(s, (YMInterval) timeInterval);
        }
        if (timeInterval == DSInterval.Week) {
            s = mult(s, 7);
            timeInterval = DSInterval.Day;
        }

        return "NUMTODSINTERVAL(" + s + ", '" + timeInterval + "')";
    }

    private String getYMIntervalString(String s, YMInterval interval) {
        switch (interval) {
            case Month :
                return s;
            case Quarter :
                return mult(s, 3);
            case Year :
                return mult(s, 12);
            default :
                throw new UnsupportedOperationException("unsupported yminterval");
        }
    }

    @Override
    String getYMTimeOffsetOpString(String timeStr, String offsetStr, ArithmeticOperator operator) {
        if (operator == ArithmeticOperator.Minus) {
            offsetStr = "-(" + offsetStr + ")";
        }
        String s = "add_months(" + timeStr + ", " + offsetStr + ")";
        return dateToTimestamp(s);
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
