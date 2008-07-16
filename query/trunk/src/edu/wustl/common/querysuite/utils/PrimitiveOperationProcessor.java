package edu.wustl.common.querysuite.utils;

import edu.wustl.common.querysuite.queryobject.ArithmeticOperator;
import edu.wustl.common.querysuite.queryobject.IDateLiteral;
import edu.wustl.common.querysuite.queryobject.ITimeIntervalEnum;
import edu.wustl.common.querysuite.utils.TermProcessor.TermStringOpnd;

class PrimitiveOperationProcessor {
    String getResultString(TermStringOpnd leftTermStrOpnd, ArithmeticOperator operator, TermStringOpnd rightTermStrOpnd) {
        return getResultString(leftTermStrOpnd.getString(), operator, rightTermStrOpnd.getString());
    }

    String modifyDateLiteral(IDateLiteral literal) {
        return "'" + literal.getDate().toString() + "'";
    }

    String getIntervalString(String s, ITimeIntervalEnum timeInterval) {
        return s;
    }

    String dateToTimestamp(String s) {
        return s;
    }

    final String getResultString(String leftStr, ArithmeticOperator operator, String rightStr) {
        return leftStr + " " + operator.mathString() + " " + rightStr;
    }
}
