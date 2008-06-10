package edu.wustl.common.querysuite.utils;

import edu.wustl.common.querysuite.queryobject.ArithmeticOperator;
import edu.wustl.common.querysuite.queryobject.ITimeIntervalEnum;
import edu.wustl.common.querysuite.queryobject.TermType;
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

        String leftStr = leftTermStrOpnd.getString();
        String rightStr = rightTermStrOpnd.getString();
        if (leftType == TermType.Date) {
            leftStr = dateToTimestamp(leftStr);
            leftType = TermType.Timestamp;
        }
        if (rightType == TermType.Date) {
            rightStr = dateToTimestamp(rightStr);
            rightType = TermType.Timestamp;
        }

        if (leftType == TermType.Timestamp && rightType == TermType.Timestamp) {
            checkMinus(operator);
            return getDateDiffString(leftStr, rightStr);
        }
        if (leftType == TermType.DSInterval && rightType == TermType.DSInterval) {
            checkPlusOrMinus(operator);
            return getIntervalOp(leftStr, operator, rightStr);
        }
        if (leftType == TermType.Timestamp && TermType.isInterval(rightType)) {
            checkPlusOrMinus(operator);
            // rightStr = getDateOffsetString(rightStr,
            // rightTermStrOpnd.getTimeInterval());
            return getTimeOffsetOpString(leftStr, rightStr, operator, rightType);
        }
        if (TermType.isInterval(leftType) && rightType == TermType.Timestamp) {
            checkPlus(operator);
            // leftStr = getDateOffsetString(leftStr,
            // leftTermStrOpnd.getTimeInterval());
            return getTimeOffsetOpString(rightStr, leftStr, operator, leftType);
        }
        return super.getResultString(leftStr, operator, rightStr);
    }

    private String getTimeOffsetOpString(String timeStr, String offsetStr, ArithmeticOperator operator,
            TermType offsetType) {
        return (offsetType == TermType.DSInterval)
                ? getDSTimeOffsetOpString(timeStr, offsetStr, operator)
                : getYMTimeOffsetOpString(timeStr, offsetStr, operator);
    }

    String getDSTimeOffsetOpString(String timeStr, String offsetStr, ArithmeticOperator operator) {
        return super.getResultString(timeStr, operator, offsetStr);
    }

    String getYMTimeOffsetOpString(String timeStr, String offsetStr, ArithmeticOperator operator) {
        return super.getResultString(timeStr, operator, offsetStr);
    }

    String getIntervalOp(String leftStr, ArithmeticOperator operator, String rightStr) {
        return super.getResultString(leftStr, operator, rightStr);
    }

    @Override
    final String modifyDateLiteral(String s) {
        return strToDateFunc + "('" + s + "', '" + dateFormat + "')";
    }

    String timeIntervalStr(ITimeIntervalEnum timeInterval) {
        return timeInterval.toString();
    }

    // for testing
    final String getDateFormat() {
        return dateFormat;
    }

    private void checkPlusOrMinus(ArithmeticOperator operator) {
        if (!(operator == ArithmeticOperator.Plus || operator == ArithmeticOperator.Minus)) {
            throw new IllegalArgumentException();
        }
    }

    private void checkPlus(ArithmeticOperator operator) {
        if (operator != ArithmeticOperator.Plus) {
            throw new IllegalArgumentException();
        }
    }

    private void checkMinus(ArithmeticOperator operator) {
        if (operator != ArithmeticOperator.Minus) {
            throw new IllegalArgumentException();
        }
    }

    abstract String getDateOffsetString(String s, ITimeIntervalEnum timeInterval);

    abstract String getDateDiffString(String leftStr, String rightStr);

    @Override
    final String getIntervalString(String s, ITimeIntervalEnum timeInterval) {
        return getDateOffsetString(s, timeInterval);
    }

    @Override
    abstract String dateToTimestamp(String s);
}
