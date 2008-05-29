package edu.wustl.common.querysuite.utils;

import edu.wustl.common.querysuite.queryobject.ArithmeticOperator;
import edu.wustl.common.querysuite.queryobject.TermType;
import edu.wustl.common.querysuite.utils.TermProcessor.TermStringOpnd;

class PrimitiveOperationProcessor {
    String getResultString(TermStringOpnd leftTermStrOpnd, ArithmeticOperator operator,
            TermStringOpnd rightTermStrOpnd) {
        return getResultString(leftTermStrOpnd.getString(), operator, rightTermStrOpnd.getString());
    }

    String modifyDateLiteral(String literal) {
        return literal;
    }

    final String getResultString(String leftStr, ArithmeticOperator operator, String rightStr) {
        return leftStr + " " + operator.mathString() + " " + rightStr;
    }
}
