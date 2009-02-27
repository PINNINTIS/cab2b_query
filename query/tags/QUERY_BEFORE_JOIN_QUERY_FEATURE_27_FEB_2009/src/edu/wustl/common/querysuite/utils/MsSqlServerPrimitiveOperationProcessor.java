package edu.wustl.common.querysuite.utils;

import edu.wustl.common.querysuite.queryobject.ArithmeticOperator;
import edu.wustl.common.querysuite.queryobject.IDateLiteral;

public class MsSqlServerPrimitiveOperationProcessor extends SQLPrimitiveOperationProcessor {
	 
	private static final String dateFormat="110";
	private static final String strToDateFunc="convert";
	
	@Override
    String getDateDiffString(String leftStr, String rightStr) {
    	return "convert(numeric(20),DATEDIFF(minute, " + rightStr + ", " + leftStr + "))*60";
    }
    
    @Override
    String modifyDateLiteral(IDateLiteral s) {
        return strToDateFunc + "(datetime,'" + standardDateFormat(s) + "', " + dateFormat + ")";
    }

    @Override
    String dateToTimestamp(String s) {
        return s;
    }

    @Override
    String getTimeOffsetOpString(String timeStr, String offsetStr, ArithmeticOperator operator) {
    	
        if (operator == ArithmeticOperator.Minus) {
            // TODO check
            offsetStr = "-" + offsetStr;
        }
        Integer offSetNumber=new Integer(offsetStr);
        return "DATEADD(second, " + offSetNumber + ", " + timeStr + ")";
    }       
}
