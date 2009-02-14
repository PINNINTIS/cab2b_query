package edu.wustl.common.querysuite.utils;

import edu.wustl.common.querysuite.queryobject.ArithmeticOperator;
import edu.wustl.common.querysuite.queryobject.IDateLiteral;
import edu.wustl.common.querysuite.queryobject.TimeInterval;

public class DB2XQueryPrimitiveOperationProcessor extends SQLPrimitiveOperationProcessor {

    public DB2XQueryPrimitiveOperationProcessor() 
    {
        super("YYYY-MM-DD" , "xs:dateTime");
    }

    @Override
    String getDateDiffString(String leftStr, String rightStr) {
        return numSecs(leftStr + " - " + rightStr);
    }

    private String numSecs(String s) {
        return "days-from-duration(" + s + ") * 86400 + " + "hours-from-duration(" + s + ") * 3600 + " 
        	+ " minutes-from-duration(" + s + ") * 60 + " + " seconds-from-duration(" + s + ")";
    }

    @Override
    String dateToTimestamp(String s) 
    {
// If it is an attribute 
    	if(s.contains("$"))
    	{
    		s = "xs:dateTime(" + s + ")";
    	}
    	else
    	{
    		s = "xs:dateTime(\"" + s + "T00:00:00\")";
    	}
        return s;
    }

    @Override
    String getTimeOffsetOpString(String timeStr, String offsetStr, ArithmeticOperator operator) {
        return super.getResultString(timeStr, operator, "xs:dateTime(PT" + offsetStr + "S)");
    }
    
    protected String modifyDateLiteral(IDateLiteral s) {
        return standardDateFormat(s);
    }
}
