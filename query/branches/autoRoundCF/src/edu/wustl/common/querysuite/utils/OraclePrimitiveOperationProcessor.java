package edu.wustl.common.querysuite.utils;

import edu.wustl.common.querysuite.queryobject.ArithmeticOperator;

public class OraclePrimitiveOperationProcessor extends SQLPrimitiveOperationProcessor {

	public OraclePrimitiveOperationProcessor() {
        super("YYYY-MM-DD", "TO_DATE");
    }

    @Override
    String getDateDiffString(String leftStr, String rightStr) {
        return numSecs(leftStr + " - " + rightStr);
    }

    private String numSecs(String s) {
        return "extract(day from " + s + ")*86400 + extract(hour from " + s + ")*3600 + extract(minute from " + s
                + ")*60 + extract(second from " + s + ")";
    }

    // @Override
    // String getDateOffsetString(String s, TimeInterval<?>
    // compoundTimeInterval) {
    // ITimeIntervalEnum timeInterval = compoundTimeInterval.primitiveEnum();
    // if (timeInterval instanceof YMInterval) {
    // return getYMIntervalString(s, (YMInterval) timeInterval);
    // }
    // if (timeInterval == DSInterval.Week) {
    // s = mult(s, 7);
    // timeInterval = DSInterval.Day;
    // }
    //
    // return "NUMTODSINTERVAL(" + s + ", '" + timeInterval + "')";
    // }
    //
    // private String getYMIntervalString(String s, YMInterval interval) {
    // switch (interval) {
    // case Month :
    // return s;
    // case Quarter :
    // return mult(s, 3);
    // case Year :
    // return mult(s, 12);
    // default :
    // throw new UnsupportedOperationException("unsupported yminterval");
    // }
    // }
    //
    // @Override
    // String getYMTimeOffsetOpString(String timeStr, String offsetStr,
    // ArithmeticOperator operator) {
    // if (operator == ArithmeticOperator.Minus) {
    // offsetStr = "-(" + offsetStr + ")";
    // }
    // String s = "add_months(" + timeStr + ", " + offsetStr + ")";
    // return dateToTimestamp(s);
    // }
    //
    // private String mult(String s, int i) {
    // return "(" + s + ") * " + String.valueOf(i);
    // }

    @Override
    String dateToTimestamp(String s) {
        // casting to timestamp is needed in view of further interval
        // arithmetic.
        return "cast(" + s + " as timestamp)";
    }

    @Override
    String getTimeOffsetOpString(String timeStr, String offsetStr, ArithmeticOperator operator) {
        return super.getResultString(timeStr, operator, "NUMTODSINTERVAL(" + offsetStr + ", 'second')");
    }
}
