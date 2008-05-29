package edu.wustl.common.querysuite.utils;

import edu.wustl.common.querysuite.queryobject.ArithmeticOperator;
import edu.wustl.common.querysuite.queryobject.TermType;
import edu.wustl.common.querysuite.queryobject.TimeInterval;
import edu.wustl.common.querysuite.utils.TermProcessor.TermStringOpnd;

abstract class SQLPrimitiveOperationProcessor extends PrimitiveOperationProcessor {
    private final String dateFormat;

    private final String strToDateFunc;

    SQLPrimitiveOperationProcessor(String dateFormat, String strToDateFunc) {
        if (dateFormat == null) {
            throw new NullPointerException("date format is null.");
        }
        if (strToDateFunc == null) {
            throw new NullPointerException("str-to-date function is null.");
        }
        this.dateFormat = dateFormat;
        this.strToDateFunc = strToDateFunc;
    }

    @Override
    String getResultString(TermStringOpnd leftTermStrOpnd, ArithmeticOperator operator,
            TermStringOpnd rightTermStrOpnd) {
        TermType leftType = leftTermStrOpnd.getTermType();
        TermType rightType = rightTermStrOpnd.getTermType();

        String leftStr = getDateCheckedString(leftTermStrOpnd);
        String rightStr = getDateCheckedString(rightTermStrOpnd);

        if (leftType == TermType.Date && rightType == TermType.Date) {
            if (operator != ArithmeticOperator.Minus) {
                throw new IllegalArgumentException();
            }
            return getDateDiffString(leftStr, rightStr);
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
        return super.getResultString(leftStr, operator, rightStr);
    }

    @Override
    String modifyDateLiteral(String literal) {
        return transformLiteralDate(literal);
    }

    private String getDateCheckedString(TermStringOpnd opnd) {
        String s = opnd.getString();
        if (opnd.getTermType() == TermType.Date && opnd.isLiteral()) {
            s = transformLiteralDate(s);
        }
        return s;
    }

    private String transformLiteralDate(String s) {
        return strToDateFunc + "('" + s + "','" + dateFormat + "')";
    }

    String timeIntervalStr(TimeInterval timeInterval) {
        return timeInterval.toString();
    }

    abstract String getDateOffsetString(String s, TimeInterval timeInterval);

    abstract String getDateDiffString(String leftStr, String rightStr);
}
