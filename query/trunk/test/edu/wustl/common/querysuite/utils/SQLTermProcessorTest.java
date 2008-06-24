package edu.wustl.common.querysuite.utils;

import edu.wustl.common.querysuite.factory.QueryObjectFactory;
import edu.wustl.common.querysuite.queryobject.ArithmeticOperator;
import edu.wustl.common.querysuite.queryobject.DSInterval;
import edu.wustl.common.querysuite.queryobject.IDateOffsetAttribute;
import edu.wustl.common.querysuite.queryobject.ILiteral;
import edu.wustl.common.querysuite.queryobject.ITerm;
import edu.wustl.common.querysuite.queryobject.TermType;
import edu.wustl.common.querysuite.queryobject.YMInterval;

public class SQLTermProcessorTest extends AbstractTermProcessorTest {
    private static final String dateFormat = "yyyy-mm-dd";

    private static final DatabaseSQLSettings mySQLSettings = new DatabaseSQLSettings(DatabaseType.MySQL,
            dateFormat);

    private static final DatabaseSQLSettings oracleSettings = new DatabaseSQLSettings(DatabaseType.Oracle,
            dateFormat);

    private static final String quotedDateFormat = "'" + dateFormat + "'";

    @Override
    protected void setUp() throws Exception {
        setTermProcessor(new TermProcessor(TermProcessor.defaultAliasProvider, mySQLSettings));
    }

    public void testDateFormatSQL() {
        ITerm term = QueryObjectFactory.createTerm();
        ILiteral d1 = dateLiteral("d1");
        String d1S = "timestamp(STR_TO_DATE('d1', " + quotedDateFormat + "))";
        term.addOperand(d1);

        check(term, d1S, TermType.Timestamp);
        term.setOperand(0, createDateExpressionAttribute("a1", "e1"));
        check(term, "timestamp(e1.a1)", TermType.Timestamp);
    }

    public void testOffsetSQL() {
        ITerm term = QueryObjectFactory.createTerm();
        term.addOperand(dateOffsetLiteral("1"));
        check(term, "maketime(1*24, 0, 0)", TermType.DSInterval);
        term.setOperand(0, dateOffsetLiteral("1", YMInterval.Month));
        checkInvalid(term);

        ILiteral d1 = dateLiteral("d1");
        String d1S = "timestamp(STR_TO_DATE('d1', " + quotedDateFormat + "))";
        term.setOperand(0, d1);

        term.addOperand(conn(ArithmeticOperator.Plus, 0), dateOffsetLiteral("1"));
        // d1 + 1
        check(term, "timestamp(" + d1S + ", maketime(1*24, 0, 0))", TermType.Timestamp);

        term.getConnector(0, 1).setOperator(ArithmeticOperator.Minus);
        // d1 - 1
        check(term, "timestamp(" + d1S + ", -maketime(1*24, 0, 0))", TermType.Timestamp);

        term.getConnector(0, 1).setOperator(ArithmeticOperator.Plus);
        swapOperands(term, 0, 1);
        // 1 + d1
        check(term, "timestamp(" + d1S + ", maketime(1*24, 0, 0))", TermType.Timestamp);

        swapOperands(term, 0, 1);
        term.setOperand(1, numericLiteral("1"));
        // d1 + 1
        check(term, "timestamp(" + d1S + ", maketime(1*24, 0, 0))", TermType.Timestamp);

        term.getConnector(0, 1).setOperator(ArithmeticOperator.Minus);
        // d1 - 1
        check(term, "timestamp(" + d1S + ", -maketime(1*24, 0, 0))", TermType.Timestamp);

        term.getConnector(0, 1).setOperator(ArithmeticOperator.Plus);
        swapOperands(term, 0, 1);
        // 1 + d1
        check(term, "timestamp(" + d1S + ", maketime(1*24, 0, 0))", TermType.Timestamp);

        term.setOperand(1, createDateExpressionAttribute("a1", "e1"));
        // 1 + a1
        check(term, "timestamp(timestamp(e1.a1), maketime(1*24, 0, 0))", TermType.Timestamp);

        swapOperands(term, 0, 1);
        // a1 + 1
        check(term, "timestamp(timestamp(e1.a1), maketime(1*24, 0, 0))", TermType.Timestamp);
        term.setOperand(0, createTimestampExpressionAttribute("a1", "e1"));
        // a1 + 1
        check(term, "timestamp(timestamp(e1.a1), maketime(1*24, 0, 0))", TermType.Timestamp);

        term.setOperand(1, d1);
        // a1 + d1
        checkInvalid(term);

        term.setOperand(0, d1);
        IDateOffsetAttribute offsetAttr = createDateOffsetExpressionAttribute("a1", "e1", YMInterval.Year);
        term.setOperand(1, offsetAttr);
        // d1 + a1Off
        check(term, d1S + " + interval e1.a1 Year", TermType.Timestamp);

        offsetAttr = createDateOffsetExpressionAttribute("a1", "e1", DSInterval.Day);
        term.setOperand(1, offsetAttr);
        // d1 + a1Off
        check(term, "timestamp(" + d1S + ", maketime(e1.a1*24, 0, 0))", TermType.Timestamp);
    }

    public void testDateDiffSQL() {
        ITerm term = QueryObjectFactory.createTerm();
        ILiteral d1 = dateLiteral("d1");
        String d1S = "timestamp(STR_TO_DATE('d1', " + quotedDateFormat + "))";
        term.addOperand(d1);
        ILiteral d2 = dateLiteral("d2");
        String d2S = "timestamp(STR_TO_DATE('d2', " + quotedDateFormat + "))";
        term.addOperand(conn(ArithmeticOperator.Minus, 0), d2);
        check(term, "timediff(" + d1S + ", " + d2S + ")", TermType.DSInterval);

        term.setOperand(1, createTimestampExpressionAttribute("a1", "e1"));
        check(term, "timediff(" + d1S + ", timestamp(e1.a1))", TermType.DSInterval);

        swapOperands(term, 0, 1);
        check(term, "timediff(timestamp(e1.a1), " + d1S + ")", TermType.DSInterval);
    }

    public void testDiffOffset() {
        ITerm term = QueryObjectFactory.createTerm();
        ILiteral d1 = dateLiteral("d1");
        String d1S = "timestamp(STR_TO_DATE('d1', " + quotedDateFormat + "))";
        term.addOperand(d1);
        ILiteral d2 = dateLiteral("d2");
        String d2S = "timestamp(STR_TO_DATE('d2', " + quotedDateFormat + "))";
        term.addOperand(conn(ArithmeticOperator.Minus, 0), d2);
        term.addOperand(conn(ArithmeticOperator.Plus, 0), numericLiteral("1"));

        // d1 - d2 + 1
        check(term, "addtime(timediff(" + d1S + ", " + d2S + "), maketime(1*24, 0, 0))", TermType.DSInterval);

        term.setOperand(2, dateOffsetLiteral("1"));
        check(term, "addtime(timediff(" + d1S + ", " + d2S + "), maketime(1*24, 0, 0))", TermType.DSInterval);

        term.addParantheses(1, 2);
        // d1 - (d2 + 1)
        check(term, "timediff(" + d1S + ", (timestamp(" + d2S + ", maketime(1*24, 0, 0))))", TermType.DSInterval);

        term.addOperand(conn(ArithmeticOperator.Minus, 0), createDateOffsetExpressionAttribute("a1", "e1",
                DSInterval.Minute));
        // d1 - (d2 + 1) - a1
        check(term, "addtime(timediff(" + d1S + ", (timestamp(" + d2S
                + ", maketime(1*24, 0, 0)))), -maketime(0, e1.a1, 0))", TermType.DSInterval);

        term.setOperand(3, createDateOffsetExpressionAttribute("a1", "e1", YMInterval.Month));
        checkInvalid(term);
    }

    private void switchToOracle() {
        setTermProcessor(new TermProcessor(TermProcessor.defaultAliasProvider, oracleSettings));
    }

    public void testOracleDateFormatSQL() {
        switchToOracle();
        ITerm term = QueryObjectFactory.createTerm();
        ILiteral d1 = dateLiteral("d1");
        String d1S = "cast(TO_DATE('d1', " + quotedDateFormat + ") as timestamp)";
        term.addOperand(d1);

        check(term, d1S, TermType.Timestamp);
    }

    public void testOracleDateDiffSQL() {
        switchToOracle();
        ITerm term = QueryObjectFactory.createTerm();
        ILiteral d1 = dateLiteral("d1");
        String d1S = "cast(TO_DATE('d1', " + quotedDateFormat + ") as timestamp)";
        term.addOperand(d1);
        ILiteral d2 = dateLiteral("d2");
        String d2S = "cast(TO_DATE('d2', " + quotedDateFormat + ") as timestamp)";
        term.addOperand(conn(ArithmeticOperator.Minus, 0), d2);
        check(term, d1S + " - " + d2S, TermType.DSInterval);
    }

    public void testOracleOffsetSQL() {
        switchToOracle();
        ITerm term = QueryObjectFactory.createTerm();
        ILiteral d1 = dateLiteral("d1");
        term.addOperand(d1);
        String d1S = "cast(TO_DATE('d1', " + quotedDateFormat + ") as timestamp)";

        // day
        term.addOperand(conn(ArithmeticOperator.Plus, 0), dateOffsetLiteral("off"));
        check(term, d1S + " + NUMTODSINTERVAL(off, 'Day')", TermType.Timestamp);

        // year
        term.setOperand(1, dateOffsetLiteral("off", YMInterval.Year));
        check(term, d1S + " + NUMTOYMINTERVAL(off, 'Year')", TermType.Timestamp);

        // week
        term.setOperand(1, dateOffsetLiteral("off", DSInterval.Week));
        check(term, d1S + " + NUMTODSINTERVAL((off) * 7, 'Day')", TermType.Timestamp);

        // quarter
        term.setOperand(1, dateOffsetLiteral("off", YMInterval.Quarter));
        check(term, d1S + " + NUMTOYMINTERVAL((off) * 3, 'Month')", TermType.Timestamp);
    }

    public void testOracleOffsetMath() {
        switchToOracle();
        ITerm term = QueryObjectFactory.createTerm();
        term.addOperand(dateOffsetLiteral("o1", DSInterval.Day));
        check(term, "NUMTODSINTERVAL(o1, 'Day')", TermType.DSInterval);

        term.addOperand(conn(ArithmeticOperator.Plus, 0), dateOffsetLiteral("o2", DSInterval.Day));
        check(term, "NUMTODSINTERVAL(o1, 'Day') + NUMTODSINTERVAL(o2, 'Day')", TermType.DSInterval);
        term.setOperand(1, dateOffsetLiteral("o2", YMInterval.Month));
        checkInvalid(term);
    }
}
