package edu.wustl.common.querysuite.utils;

import edu.wustl.common.querysuite.queryobject.TimeInterval;

class OraclePrimitiveOperationProcessor extends SQLPrimitiveOperationProcessor {

    OraclePrimitiveOperationProcessor(String dateFormat) {
        super(dateFormat, "TO_DATE");
    }

    @Override
    String getDateDiffString(String leftStr, String rightStr) {
        return leftStr + " - " + rightStr;
    }

    @Override
    String getDateOffsetString(String s, TimeInterval timeInterval) {
        if (timeInterval == TimeInterval.Quarter) {
            s = mult(s, 3);
            timeInterval = TimeInterval.Month;
        } else if (timeInterval == TimeInterval.Week) {
            s = mult(s, 7);
            timeInterval = TimeInterval.Day;
        }
        final String func = getFunc(timeInterval);

        return func + "(" + s + ", '" + timeInterval + "')";
    }

    private String getFunc(TimeInterval timeInterval) {
        if (isDayToSec(timeInterval)) {
            return "NUMTODSINTERVAL";
        } else {
            return "NUMTOYMINTERVAL";
        }
    }

    private boolean isDayToSec(TimeInterval timeInterval) {
        return timeInterval.compareTo(TimeInterval.Week) <= 0;
    }

    private String mult(String s, int i) {
        return "(" + s + ") * " + String.valueOf(i);
    }
}
