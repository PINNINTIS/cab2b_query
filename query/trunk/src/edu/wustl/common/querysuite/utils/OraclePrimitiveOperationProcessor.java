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
        // TODO Auto-generated method stub
        return null;
    }
}
