package edu.wustl.common.querysuite.utils;

import edu.wustl.common.querysuite.queryobject.TimeInterval;

class MySQLPrimitiveOperationProcessor extends SQLPrimitiveOperationProcessor {

    MySQLPrimitiveOperationProcessor(String dateFormat) {
        super(dateFormat, "STR_TO_DATE");
    }

    @Override
    String getDateDiffString(String leftStr, String rightStr) {
        return "datediff(" + leftStr + ", " + rightStr + ")";
    }

    @Override
    String getDateOffsetString(String s, TimeInterval timeInterval) {
        return "interval " + s + " " + timeIntervalStr(timeInterval);
    }
}
