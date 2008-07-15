package edu.wustl.common.querysuite.utils;

import edu.wustl.common.querysuite.queryobject.ArithmeticOperator;
import edu.wustl.common.querysuite.queryobject.DSInterval;
import edu.wustl.common.querysuite.queryobject.ITimeIntervalEnum;
import edu.wustl.common.querysuite.queryobject.YMInterval;
import edu.wustl.common.querysuite.utils.TermProcessor.TermStringOpnd;

class MySQLPrimitiveOperationProcessor extends SQLPrimitiveOperationProcessor {

    MySQLPrimitiveOperationProcessor(String dateFormat) {
        super(dateFormat, "STR_TO_DATE");
    }

    @Override
    String getDateDiffString(String leftStr, String rightStr) {
        return "timediff(" + leftStr + ", " + rightStr + ")";
    }

    @Override
    String getDateOffsetString(String s, ITimeIntervalEnum timeInterval) {
        if (timeInterval instanceof DSInterval) {
            return getDSIntervalString(s, (DSInterval) timeInterval);
        } else if (timeInterval instanceof YMInterval) {
            return "interval " + s + " " + timeIntervalStr(timeInterval);
        }
        throw new IllegalArgumentException("Shouldn't occur.");
    }

    private String getDSIntervalString(String s, DSInterval interval) {
        String hour = "0";
        String minute = "0";
        String sec = "0";
        s = "(" + s + ")";
        switch (interval) {
            case Second :
                sec = s + "%60";
                minute = "(" + s + "%3600)/60";
                hour = s + "/3600";
                break;
            case Minute :
                minute = s + "%60";
                hour = s + "/60";
                break;
            case Hour :
                hour = s;
                break;
            case Day :
                hour = s + "*24";
                break;
            case Week :
                hour = s + "*24*7";
                break;
            default :
                throw new RuntimeException("can't occur.");
        }
        return "maketime(" + hour + ", " + minute + ", " + sec + ")";
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
            offsetStr = negativeMaketimeString(offsetStr);
        }
        return "timestamp(" + timeStr + ", " + offsetStr + ")";
    }

    @Override
    String getIntervalOp(String leftStr, ArithmeticOperator operator, String rightStr) {
        if (operator == ArithmeticOperator.Minus) {
            rightStr = negativeMaketimeString(rightStr);
        }
        return "addtime(" + leftStr + ", " + rightStr + ")";
    }

    private String negativeMaketimeString(String s) {
        return "concat('-',time_format(" + s + ",'%H:%i:%s'))";
    }
}
