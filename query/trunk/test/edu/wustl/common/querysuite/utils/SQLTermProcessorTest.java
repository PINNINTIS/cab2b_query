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
    private static final DatabaseSQLSettings mySQLSettings = new DatabaseSQLSettings(DatabaseType.MySQL);

    private static final DatabaseSQLSettings oracleSettings = new DatabaseSQLSettings(DatabaseType.Oracle);

    private static final String mySqlQuotedDateFormat = "'%Y-%m-%d'";

    private static final String oracleQuotedDateFormat = "'YYYY-MM-DD'";

    @Override
    protected void setUp() throws Exception {
        setTermProcessor(new TermProcessor(TermProcessor.defaultAliasProvider, mySQLSettings));
    }

    public void testDateFormatSQL() {
        ITerm term = QueryObjectFactory.createTerm();
        ILiteral d1 = dateLiteral("2008-01-01");
        String d1S = "timestamp(STR_TO_DATE('2008-01-01', " + mySqlQuotedDateFormat + "))";
        term.addOperand(d1);

        check(term, d1S, TermType.Timestamp);
        term.setOperand(0, createDateExpressionAttribute("a1", "e1"));
        check(term, "timestamp(e1.a1)", TermType.Timestamp);
    }

    public void testOffsetSQL() {
        ITerm term = QueryObjectFactory.createTerm();
        term.addOperand(dateOffsetLiteral("1"));
        check(term, "(1)*86400", TermType.DSInterval);

        ILiteral d1 = dateLiteral("2008-01-01");
        String d1S = "timestamp(STR_TO_DATE('2008-01-01', " + mySqlQuotedDateFormat + "))";
        term.setOperand(0, d1);

        term.addOperand(conn(ArithmeticOperator.Plus, 0), dateOffsetLiteral("1"));
        // d1 + 1
        check(term, "timestampadd(SECOND, (1)*86400, " + d1S + ")", TermType.Timestamp);

        term.getConnector(0, 1).setOperator(ArithmeticOperator.Minus);
        // d1 - 1
        check(term, "timestampadd(SECOND, -(1)*86400, " + d1S + ")", TermType.Timestamp);

        term.getConnector(0, 1).setOperator(ArithmeticOperator.Plus);
        swapOperands(term, 0, 1);
        // 1 + d1
        check(term, "timestampadd(SECOND, (1)*86400, " + d1S + ")", TermType.Timestamp);

        swapOperands(term, 0, 1);
        term.setOperand(1, numericLiteral("1"));
        // d1 + 1
        check(term, "timestampadd(SECOND, (1)*86400, " + d1S + ")", TermType.Timestamp);

        term.getConnector(0, 1).setOperator(ArithmeticOperator.Minus);
        // d1 - 1
        check(term, "timestampadd(SECOND, -(1)*86400, " + d1S + ")", TermType.Timestamp);

        term.getConnector(0, 1).setOperator(ArithmeticOperator.Plus);
        swapOperands(term, 0, 1);
        // 1 + d1
        check(term, "timestampadd(SECOND, (1)*86400, " + d1S + ")", TermType.Timestamp);

        term.setOperand(1, createDateExpressionAttribute("a1", "e1"));
        // 1 + a1
        check(term, "timestampadd(SECOND, (1)*86400, timestamp(e1.a1))", TermType.Timestamp);

        swapOperands(term, 0, 1);
        // a1 + 1
        check(term, "timestampadd(SECOND, (1)*86400, timestamp(e1.a1))", TermType.Timestamp);
        term.setOperand(0, createTimestampExpressionAttribute("a1", "e1"));
        // a1 + 1
        check(term, "timestampadd(SECOND, (1)*86400, timestamp(e1.a1))", TermType.Timestamp);

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
        check(term, "timestampadd(SECOND, (e1.a1)*86400, " + d1S + ")", TermType.Timestamp);
    }

    public void testDateDiffSQL() {
        ITerm term = QueryObjectFactory.createTerm();
        ILiteral d1 = dateLiteral("2008-01-01");
        String d1S = "timestamp(STR_TO_DATE('2008-01-01', " + mySqlQuotedDateFormat + "))";
        term.addOperand(d1);
        ILiteral d2 = dateLiteral("2008-01-02");
        String d2S = "timestamp(STR_TO_DATE('2008-01-02', " + mySqlQuotedDateFormat + "))";
        term.addOperand(conn(ArithmeticOperator.Minus, 0), d2);
        check(term, "timestampdiff(SECOND, " + d2S + ", " + d1S + ")", TermType.DSInterval);

        term.setOperand(1, createTimestampExpressionAttribute("a1", "e1"));
        check(term, "timestampdiff(SECOND, timestamp(e1.a1), " + d1S + ")", TermType.DSInterval);

        swapOperands(term, 0, 1);
        check(term, "timestampdiff(SECOND, " + d1S + ", timestamp(e1.a1))", TermType.DSInterval);
    }

    public void testDiffOffset() {
        ITerm term = QueryObjectFactory.createTerm();
        ILiteral d1 = dateLiteral("2008-01-01");
        String d1S = "timestamp(STR_TO_DATE('2008-01-01', " + mySqlQuotedDateFormat + "))";
        term.addOperand(d1);
        ILiteral d2 = dateLiteral("2008-01-02");
        String d2S = "timestamp(STR_TO_DATE('2008-01-02', " + mySqlQuotedDateFormat + "))";
        term.addOperand(conn(ArithmeticOperator.Minus, 0), d2);
        term.addOperand(conn(ArithmeticOperator.Plus, 0), numericLiteral("1"));

        // d1 - d2 + 1
        check(term, "timestampdiff(SECOND, " + d2S + ", " + d1S + ") + (1)*86400", TermType.DSInterval);

        term.setOperand(2, dateOffsetLiteral("1"));
        check(term, "timestampdiff(SECOND, " + d2S + ", " + d1S + ") + (1)*86400", TermType.DSInterval);

        term.addParantheses(1, 2);
        // d1 - (d2 + 1)
        check(term, "timestampdiff(SECOND, (timestampadd(SECOND, (1)*86400, " + d2S + ")), " + d1S + ")",
                TermType.DSInterval);

        term.addOperand(conn(ArithmeticOperator.Minus, 0), createDateOffsetExpressionAttribute("a1", "e1",
                DSInterval.Minute));
        // d1 - (d2 + 1) - a1
        String expectedRes = "timestampadd(SECOND, (1)*86400, " + d2S + ")";
        expectedRes = "(" + expectedRes + ")";
        expectedRes = "timestampdiff(SECOND, " + expectedRes + ", " + d1S + ")";
        expectedRes = expectedRes + " - (e1.a1)*60";
        check(term, expectedRes, TermType.DSInterval);

        term.setOperand(3, createDateOffsetExpressionAttribute("a1", "e1", YMInterval.Month));
        checkInvalid(term);
    }

    private void switchToOracle() {
        setTermProcessor(new TermProcessor(TermProcessor.defaultAliasProvider, oracleSettings));
    }

    public void testOracleDateFormatSQL() {
        switchToOracle();
        ITerm term = QueryObjectFactory.createTerm();
        ILiteral d1 = dateLiteral("2008-01-01");
        String d1S = "cast(TO_DATE('2008-01-01', " + oracleQuotedDateFormat + ") as timestamp)";
        term.addOperand(d1);

        check(term, d1S, TermType.Timestamp);
    }

    public void testOracleDateDiffSQL() {
        switchToOracle();
        ITerm term = QueryObjectFactory.createTerm();
        ILiteral d1 = dateLiteral("2008-01-01");
        String d1S = "cast(TO_DATE('2008-01-01', " + oracleQuotedDateFormat + ") as timestamp)";
        term.addOperand(d1);
        ILiteral d2 = dateLiteral("2008-01-02");
        String d2S = "cast(TO_DATE('2008-01-02', " + oracleQuotedDateFormat + ") as timestamp)";
        term.addOperand(conn(ArithmeticOperator.Minus, 0), d2);
        check(term, d1S + " - " + d2S, TermType.DSInterval);
    }

    public void testOracleOffsetSQL() {
        switchToOracle();
        ITerm term = QueryObjectFactory.createTerm();
        ILiteral d1 = dateLiteral("2008-01-01");
        term.addOperand(d1);
        String d1S = "cast(TO_DATE('2008-01-01', " + oracleQuotedDateFormat + ") as timestamp)";

        // day
        term.addOperand(conn(ArithmeticOperator.Plus, 0), dateOffsetLiteral("off"));
        check(term, d1S + " + NUMTODSINTERVAL(off, 'Day')", TermType.Timestamp);

        // month
        term.setOperand(1, dateOffsetLiteral("off", YMInterval.Month));
        check(term, "cast(add_months(" + d1S + ", off) as timestamp)", TermType.Timestamp);

        // year
        term.setOperand(1, dateOffsetLiteral("off", YMInterval.Year));
        check(term, "cast(add_months(" + d1S + ", (off) * 12) as timestamp)", TermType.Timestamp);

        // week
        term.setOperand(1, dateOffsetLiteral("off", DSInterval.Week));
        check(term, d1S + " + NUMTODSINTERVAL((off) * 7, 'Day')", TermType.Timestamp);

        // quarter
        term.setOperand(1, dateOffsetLiteral("off", YMInterval.Quarter));
        check(term, "cast(add_months(" + d1S + ", (off) * 3) as timestamp)", TermType.Timestamp);

        //-ve month
        term.getConnector(0, 1).setOperator(ArithmeticOperator.Minus);
        term.setOperand(1, dateOffsetLiteral("off", YMInterval.Month));
        check(term, "cast(add_months(" + d1S + ", -(off)) as timestamp)", TermType.Timestamp);
        
        // -ve year
        term.setOperand(1, dateOffsetLiteral("off", YMInterval.Year));
        check(term, "cast(add_months(" + d1S + ", -((off) * 12)) as timestamp)", TermType.Timestamp);
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
