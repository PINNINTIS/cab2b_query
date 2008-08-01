package edu.wustl.common.querysuite.utils;

import edu.wustl.common.querysuite.queryobject.ArithmeticOperator;
import edu.wustl.common.querysuite.queryobject.DSInterval;
import edu.wustl.common.querysuite.queryobject.ITimeIntervalEnum;
import edu.wustl.common.querysuite.queryobject.TimeInterval;
import edu.wustl.common.querysuite.queryobject.YMInterval;
import edu.wustl.common.querysuite.utils.TermProcessor.TermStringOpnd;

class MySQLPrimitiveOperationProcessor extends SQLPrimitiveOperationProcessor {
    MySQLPrimitiveOperationProcessor() {
        super("%Y-%m-%d", "STR_TO_DATE");
    }

    @Override
    String getDateDiffString(String leftStr, String rightStr) {
        return "timestampdiff(SECOND, " + rightStr + ", " + leftStr + ")";
    }

    @Override
    String getDateOffsetString(String s, TimeInterval<?> compoundTimeInterval) {
        ITimeIntervalEnum timeInterval = compoundTimeInterval.primitiveEnum();
        if (timeInterval instanceof DSInterval) {
            return getDSIntervalString(s, (DSInterval) timeInterval);
        } else if (timeInterval instanceof YMInterval) {
            return "interval " + s + " " + timeIntervalStr(timeInterval);
        }
        throw new IllegalArgumentException("Shouldn't occur.");
    }

    private String getDSIntervalString(String s, DSInterval interval) {
        s = "(" + s + ")";
        s = s + "*" + interval.numSeconds();
        return s;
    }

    @Override
    String dateToTimestamp(String s) {
        return "timestamp(" + s + ")";
    }

    @Override
    String getResultString(TermStringOpnd leftTermStrOpnd, ArithmeticOperator operator, TermStringOpnd rightTermStrOpnd) {

        return super.getResultString(leftTermStrOpnd, operator, rightTermStrOpnd);
    }

    @Override
    String getDSTimeOffsetOpString(String timeStr, String offsetStr, ArithmeticOperator operator) {
        if (operator == ArithmeticOperator.Minus) {
            // TODO check
            offsetStr = "-" + offsetStr;
        }
        return "timestampadd(SECOND, " + offsetStr + ", " + timeStr + ")";
    }

    // @Override
    // String getIntervalOp(String leftStr, ArithmeticOperator operator, String
    // rightStr) {
    // if (operator == ArithmeticOperator.Minus) {
    // rightStr = negativeMaketimeString(rightStr);
    // }
    // return "addtime(" + leftStr + ", " + rightStr + ")";
    // }

    private String negativeMaketimeString(String s) {
        return "concat('-',time_format(" + s + ",'%H:%i:%s'))";
    }
}
