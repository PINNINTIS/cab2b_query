package edu.wustl.common.querysuite.utils;

import edu.wustl.common.querysuite.queryobject.TimeInterval;

public class OraclePrimitiveOperationProcessor extends SQLPrimitiveOperationProcessor {

    public OraclePrimitiveOperationProcessor(String dateFormat) {
        super(dateFormat);
    }

    @Override
    String getDateDiffString(String leftStr, String rightStr) {
        return leftStr + " - " + rightStr;
    }

    @Override
    String getDateOffsetString(String s, TimeInterval timeInterval) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    String strToDateFunc() {
        return "TO_DATE";
    }

}
