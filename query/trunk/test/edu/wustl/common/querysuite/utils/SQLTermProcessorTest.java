package edu.wustl.common.querysuite.utils;

import edu.wustl.common.querysuite.factory.QueryObjectFactory;
import edu.wustl.common.querysuite.queryobject.ArithmeticOperator;
import edu.wustl.common.querysuite.queryobject.ILiteral;
import edu.wustl.common.querysuite.queryobject.ITerm;
import edu.wustl.common.querysuite.queryobject.TermType;
import edu.wustl.common.querysuite.queryobject.TimeInterval;

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
        String d1S = "STR_TO_DATE('d1', " + quotedDateFormat + ")";
        term.addOperand(d1);

        check(term, d1S, TermType.Date);
        term.setOperand(0, createDateExpressionAttribute("a1", "e1"));
        check(term, "e1.a1", TermType.Date);
    }

    public void testOffsetSQL() {
        ITerm term = QueryObjectFactory.createTerm();
        ILiteral d1 = dateLiteral("d1");
        String d1S = "STR_TO_DATE('d1', " + quotedDateFormat + ")";
        term.addOperand(d1);

        term.addOperand(conn(ArithmeticOperator.Plus, 0), dateOffsetLiteral("1"));
        // d1 + 1
        check(term, d1S + " + interval 1 Day", TermType.Date);

        term.getConnector(0, 1).setOperator(ArithmeticOperator.Minus);
        // d1 - 1
        check(term, d1S + " - interval 1 Day", TermType.Date);

        term.getConnector(0, 1).setOperator(ArithmeticOperator.Plus);
        swapOperands(term, 0, 1);
        // 1 + d1
        check(term, "interval 1 Day + " + d1S, TermType.Date);

        swapOperands(term, 0, 1);
        term.setOperand(1, numericLiteral("1"));
        // d1 + 1
        check(term, d1S + " + interval 1 Day", TermType.Date);

        term.getConnector(0, 1).setOperator(ArithmeticOperator.Minus);
        // d1 - 1
        check(term, d1S + " - interval 1 Day", TermType.Date);

        term.getConnector(0, 1).setOperator(ArithmeticOperator.Plus);
        swapOperands(term, 0, 1);
        // 1 + d1
        check(term, "interval 1 Day + " + d1S, TermType.Date);

        term.setOperand(1, createDateExpressionAttribute("a1", "e1"));
        // 1 + a1
        check(term, "interval 1 Day + e1.a1", TermType.Date);

        swapOperands(term, 0, 1);
        // a1 + 1
        check(term, "e1.a1 + interval 1 Day", TermType.Date);

        term.setOperand(1, d1);
        // d1 + a1
        checkInvalid(term);

        swapOperands(term, 0, 1);
        term.setOperand(1, createDateOffsetExpressionAttribute("a1", "e1"));
        check(term, d1S + " + interval e1.a1 Day", TermType.Date);
    }

    public void testDateDiffSQL() {
        ITerm term = QueryObjectFactory.createTerm();
        ILiteral d1 = dateLiteral("d1");
        String d1S = "STR_TO_DATE('d1', " + quotedDateFormat + ")";
        term.addOperand(d1);
        ILiteral d2 = dateLiteral("d2");
        String d2S = "STR_TO_DATE('d2', " + quotedDateFormat + ")";
        term.addOperand(conn(ArithmeticOperator.Minus, 0), d2);
        check(term, "datediff(" + d1S + ", " + d2S + ")", TermType.Numeric);

        term.setOperand(1, createDateExpressionAttribute("a1", "e1"));
        check(term, "datediff(" + d1S + ", e1.a1)", TermType.Numeric);

        swapOperands(term, 0, 1);
        check(term, "datediff(e1.a1, " + d1S + ")", TermType.Numeric);
    }

    public void testDiffOffset() {
        ITerm term = QueryObjectFactory.createTerm();
        ILiteral d1 = dateLiteral("d1");
        String d1S = "STR_TO_DATE('d1', " + quotedDateFormat + ")";
        term.addOperand(d1);
        ILiteral d2 = dateLiteral("d2");
        String d2S = "STR_TO_DATE('d2', " + quotedDateFormat + ")";
        term.addOperand(conn(ArithmeticOperator.Minus, 0), d2);
        term.addOperand(conn(ArithmeticOperator.Plus, 0), numericLiteral("1"));
        check(term, "datediff(" + d1S + ", " + d2S + ") + 1", TermType.Numeric);

        term.setOperand(2, dateOffsetLiteral("1"));
        checkInvalid(term);
    }

    private void switchToOracle() {
        setTermProcessor(new TermProcessor(TermProcessor.defaultAliasProvider, oracleSettings));
    }

    public void testOracleDateFormatSQL() {
        switchToOracle();
        ITerm term = QueryObjectFactory.createTerm();
        ILiteral d1 = dateLiteral("d1");
        String d1S = "TO_DATE('d1', " + quotedDateFormat + ")";
        term.addOperand(d1);

        check(term, d1S, TermType.Date);
    }

    public void testOracleDateDiffSQL() {
        switchToOracle();
        ITerm term = QueryObjectFactory.createTerm();
        ILiteral d1 = dateLiteral("d1");
        String d1S = "TO_DATE('d1', " + quotedDateFormat + ")";
        term.addOperand(d1);
        ILiteral d2 = dateLiteral("d2");
        String d2S = "TO_DATE('d2', " + quotedDateFormat + ")";
        term.addOperand(conn(ArithmeticOperator.Minus, 0), d2);
        check(term, d1S + " - " + d2S, TermType.Numeric);
    }

    public void testOracleOffsetSQL() {
        switchToOracle();
        ITerm term = QueryObjectFactory.createTerm();
        ILiteral d1 = dateLiteral("d1");
        term.addOperand(d1);
        String d1S = "TO_DATE('d1', " + quotedDateFormat + ")";

        // day
        term.addOperand(conn(ArithmeticOperator.Plus, 0), dateOffsetLiteral("off"));
        check(term, d1S + " + NUMTODSINTERVAL(off, 'Day')", TermType.Date);

        // year
        term.setOperand(1, dateOffsetLiteral("off", TimeInterval.Year));
        check(term, d1S + " + NUMTOYMINTERVAL(off, 'Year')", TermType.Date);

        // week
        term.setOperand(1, dateOffsetLiteral("off", TimeInterval.Week));
        check(term, d1S + " + NUMTODSINTERVAL((off) * 7, 'Day')", TermType.Date);

        // quarter
        term.setOperand(1, dateOffsetLiteral("off", TimeInterval.Quarter));
        check(term, d1S + " + NUMTOYMINTERVAL((off) * 3, 'Month')", TermType.Date);

    }
}
