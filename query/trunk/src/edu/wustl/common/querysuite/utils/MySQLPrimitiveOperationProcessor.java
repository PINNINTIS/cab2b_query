package edu.wustl.common.querysuite.utils;

import edu.wustl.common.querysuite.queryobject.TimeInterval;

class MySQLPrimitiveOperationProcessor extends SQLPrimitiveOperationProcessor {

    public MySQLPrimitiveOperationProcessor(String dateFormat) {
        super(dateFormat);
    }

    @Override
    String getDateDiffString(String leftStr, String rightStr) {
        return "datediff(" + leftStr + ", " + rightStr + ")";
    }

    @Override
    String getDateOffsetString(String s, TimeInterval timeInterval) {
        return "interval " + s + " " + timeIntervalStr(timeInterval);
    }

    @Override
    String strToDateFunc() {
        return "STR_TO_DATE";
    }

}
