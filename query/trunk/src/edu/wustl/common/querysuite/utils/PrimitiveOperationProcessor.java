package edu.wustl.common.querysuite.utils;

import edu.wustl.common.querysuite.queryobject.ArithmeticOperator;
import edu.wustl.common.querysuite.utils.TermProcessor.TermStringOpnd;

class PrimitiveOperationProcessor {
    String getResultString(TermStringOpnd leftTermStrOpnd, ArithmeticOperator operator,
            TermStringOpnd rightTermStrOpnd, boolean postFix) {
        // TODO remove postFix
        String leftOpndString = leftTermStrOpnd.getString();
        String rightOpndString = rightTermStrOpnd.getString();
        String termStr;
        if (postFix) {
            termStr = operator.mathString() + "[" + leftOpndString + ", " + rightOpndString + "]";
        } else {
            termStr = leftOpndString + " " + operator.mathString() + " " + rightOpndString;
        }
        return termStr;
    }
}
