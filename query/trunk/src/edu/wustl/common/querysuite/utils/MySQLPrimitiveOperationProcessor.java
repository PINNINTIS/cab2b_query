package edu.wustl.common.querysuite.utils;

import edu.wustl.common.querysuite.queryobject.ArithmeticOperator;
import edu.wustl.common.querysuite.queryobject.TermType;
import edu.wustl.common.querysuite.queryobject.TimeInterval;
import edu.wustl.common.querysuite.utils.TermProcessor.TermStringOpnd;

class MySQLPrimitiveOperationProcessor extends PrimitiveOperationProcessor {

    @Override
    String getResultString(TermStringOpnd leftTermStrOpnd, ArithmeticOperator operator,
            TermStringOpnd rightTermStrOpnd, boolean postFix) {
        TermType leftType = leftTermStrOpnd.getTermType();
        TermType rightType = rightTermStrOpnd.getTermType();

        String leftStr = getDateCheckedString(leftTermStrOpnd);
        String rightStr = getDateCheckedString(rightTermStrOpnd);

        if (leftType == TermType.Date && rightType == TermType.Date) {
            if (operator != ArithmeticOperator.Minus) {
                throw new IllegalArgumentException();
            }
            return "datediff(" + leftStr + ", " + rightStr + ")";
        }
        if (leftType == TermType.Date && rightType == TermType.DateOffset) {
            if (!(operator == ArithmeticOperator.Plus || operator == ArithmeticOperator.Minus)) {
                throw new IllegalArgumentException();
            }
            rightStr = getDateOffsetString(rightStr, rightTermStrOpnd.getTimeInterval());
        }
        if (leftType == TermType.DateOffset && rightType == TermType.Date) {
            if (operator != ArithmeticOperator.Plus) {
                throw new IllegalArgumentException();
            }
            leftStr = getDateOffsetString(leftStr, leftTermStrOpnd.getTimeInterval());
        }
        leftTermStrOpnd = new TermStringOpnd(leftStr, null, false);
        rightTermStrOpnd = new TermStringOpnd(leftStr, null, false);
        return super.getResultString(leftTermStrOpnd, operator, rightTermStrOpnd, postFix);
    }

    private String getDateCheckedString(TermStringOpnd opnd) {
        String s = opnd.getString();
        if (opnd.getTermType() == TermType.Date && opnd.isLiteral()) {
            s = transformLiteralDate(s);
        }
        return s;
    }

    private String getDateOffsetString(String s, TimeInterval timeInterval) {
        return "interval " + s + " " + timeInterval;
    }

    private String transformLiteralDate(String s) {
        // TODO Auto-generated method stub
        return null;
    }

}
