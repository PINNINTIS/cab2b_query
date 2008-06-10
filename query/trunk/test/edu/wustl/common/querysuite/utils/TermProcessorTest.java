package edu.wustl.common.querysuite.utils;

import edu.wustl.common.querysuite.queryobject.ArithmeticOperator;
import edu.wustl.common.querysuite.queryobject.IExpressionAttribute;
import edu.wustl.common.querysuite.queryobject.ILiteral;
import edu.wustl.common.querysuite.queryobject.ITerm;
import edu.wustl.common.querysuite.queryobject.TermType;
import edu.wustl.common.querysuite.queryobject.YMInterval;
import edu.wustl.common.querysuite.utils.TermProcessor.IAttributeAliasProvider;

public class TermProcessorTest extends AbstractTermProcessorTest {

    public void test() {
        ITerm term = newTerm();
        term.addOperand(dateOffsetLiteral("1", YMInterval.Month));
        checkInvalid(term);

        term.setOperand(0, dateOffsetLiteral("1"));
        check(term, "1", TermType.DSInterval);

        term.setOperand(0, numericLiteral("1"));
        checkNumeric(term, "1");
        term.addOperand(conn(ArithmeticOperator.Plus, 0), numericLiteral("2"));

        checkNumeric(term, "1 + 2");

        term.addOperand(conn(ArithmeticOperator.MultipliedBy, 0), numericLiteral("3"));
        checkNumeric(term, "1 + 2 * 3");
        term.addParantheses(0, 1);
        checkNumeric(term, "(1 + 2) * 3");

        term.removeParantheses(0, 1);
        term.addParantheses(1, 2);
        checkNumeric(term, "1 + (2 * 3)");

        term.addOperand(conn(ArithmeticOperator.Plus, 0), numericLiteral("5"));
        checkNumeric(term, "1 + (2 * 3) + 5");
    }

    public void test2() {
        ITerm term = newTerm();
        term.addOperand(numericLiteral("1"));
        term.addOperand(conn(ArithmeticOperator.Plus, 0), numericLiteral("2"));

        term.addParantheses();
        term.addOperand(conn(ArithmeticOperator.MultipliedBy, 0), numericLiteral("3"));
        term.addOperand(conn(ArithmeticOperator.Minus, 1), numericLiteral("4"));
        checkNumeric(term, "(1 + 2) * (3 - 4)");

        term.addOperand(conn(ArithmeticOperator.DividedBy, 0), numericLiteral("5"));
        checkNumeric(term, "(1 + 2) * (3 - 4) / 5");

        term.addParantheses(2, 4);
        checkNumeric(term, "(1 + 2) * ((3 - 4) / 5)");

        term.removeParantheses(2, 4);
        term.addParantheses(0, 3);
        checkNumeric(term, "((1 + 2) * (3 - 4)) / 5");

        term.addOperand(conn(ArithmeticOperator.Plus, 1), numericLiteral("6"));
        checkNumeric(term, "((1 + 2) * (3 - 4)) / (5 + 6)");
    }

    public void testPreFix() {
        ITerm term = newTerm();
        term.addOperand(numericLiteral("1"));
        term.addOperand(conn(ArithmeticOperator.Plus, 0), numericLiteral("2"));

        checkNumeric(term, "+[1, 2]", true);

        term.addOperand(conn(ArithmeticOperator.MultipliedBy, 0), numericLiteral("3"));
        checkNumeric(term, "*[+[1, 2], 3]", true);

        term.addParantheses(0, 1);
        checkNumeric(term, "*[(+[1, 2]), 3]", true);

        term.removeParantheses(0, 1);
        term.addParantheses(1, 2);
        checkNumeric(term, "+[1, (*[2, 3])]", true);

        term.addOperand(conn(ArithmeticOperator.Plus, 0), numericLiteral("5"));
        checkNumeric(term, "+[+[1, (*[2, 3])], 5]", true);
    }

    public void testPreFix2() {
        ITerm term = newTerm();
        term.addOperand(numericLiteral("1"));
        term.addOperand(conn(ArithmeticOperator.Plus, 0), numericLiteral("2"));

        term.addParantheses();
        term.addOperand(conn(ArithmeticOperator.MultipliedBy, 0), numericLiteral("3"));
        term.addOperand(conn(ArithmeticOperator.Minus, 1), numericLiteral("4"));
        checkNumeric(term, "*[(+[1, 2]), (-[3, 4])]", true);

        term.addOperand(conn(ArithmeticOperator.DividedBy, 0), numericLiteral("5"));
        checkNumeric(term, "/[*[(+[1, 2]), (-[3, 4])], 5]", true);

        term.addParantheses(2, 4);
        checkNumeric(term, "*[(+[1, 2]), (/[(-[3, 4]), 5])]", true);

        term.removeParantheses(2, 4);
        term.addParantheses(0, 3);
        checkNumeric(term, "/[(*[(+[1, 2]), (-[3, 4])]), 5]", true);

        term.addOperand(conn(ArithmeticOperator.Plus, 1), numericLiteral("6"));
        checkNumeric(term, "/[(*[(+[1, 2]), (-[3, 4])]), (+[5, 6])]", true);
    }

    public void testTwoOperandDateArithmetic() {
        ITerm term = newTerm();
        ILiteral date1 = dateLiteral("d1");
        ILiteral date2 = dateLiteral("d2");
        term.addOperand(date1);
        term.addOperand(conn(ArithmeticOperator.Plus, 0), date2);
        checkInvalid(term);

        ILiteral dateOffset1 = dateOffsetLiteral("off1");
        term.setOperand(1, dateOffset1);
        // d1 + off1
        check(term, concat(date1, ArithmeticOperator.Plus, dateOffset1), TermType.Timestamp);
        swapOperands(term, 0, 1);
        // off1 + d1
        check(term, concat(dateOffset1, ArithmeticOperator.Plus, date1), TermType.Timestamp);
        swapOperands(term, 0, 1);

        ILiteral numLiteral1 = numericLiteral("1");
        term.setOperand(1, numLiteral1);
        // d1 + 1
        check(term, concat(date1, ArithmeticOperator.Plus, numLiteral1), TermType.Timestamp);
        swapOperands(term, 0, 1);
        // 1 + d1
        check(term, concat(numLiteral1, ArithmeticOperator.Plus, date1), TermType.Timestamp);
        swapOperands(term, 0, 1);

        term.getConnector(0, 1).setOperator(ArithmeticOperator.Minus);

        term.setOperand(1, dateOffset1);
        // d1 - off1
        check(term, concat(date1, ArithmeticOperator.Minus, dateOffset1), TermType.Timestamp);
        swapOperands(term, 0, 1);
        // off1 - d1
        checkInvalid(term);
        swapOperands(term, 0, 1);

        term.setOperand(1, numLiteral1);
        // d1 - 1
        check(term, concat(date1, ArithmeticOperator.Minus, numLiteral1), TermType.Timestamp);
        swapOperands(term, 0, 1);
        // 1 - d1
        checkInvalid(term);
        swapOperands(term, 0, 1);

        term.setOperand(1, date2);
        // d1 - d2
        check(term, concat(date1, ArithmeticOperator.Minus, date2), TermType.DSInterval);

        // mult and divide
        term.getConnector(0, 1).setOperator(ArithmeticOperator.MultipliedBy);
        checkInvalid(term);
        term.setOperand(1, numLiteral1);
        checkInvalid(term);
        term.setOperand(1, dateOffset1);
        checkInvalid(term);

        term.getConnector(0, 1).setOperator(ArithmeticOperator.DividedBy);
        checkInvalid(term);
        term.setOperand(1, numLiteral1);
        checkInvalid(term);
        term.setOperand(1, dateOffset1);
        checkInvalid(term);
    }

    public void testMultiOperandDateArithmetic() {
        ITerm term = newTerm();
        ILiteral f1 = dateOffsetLiteral("f1");
        ILiteral n1 = numericLiteral("n1");

        term.addOperand(dateLiteral("d1"));
        term.addOperand(conn(ArithmeticOperator.Minus, 0), f1);
        term.addOperand(conn(ArithmeticOperator.Plus, 0), dateLiteral("d2"));
        checkInvalid(term);

        term.addParantheses(1, 2);
        check(term, "d1 - (f1 + d2)", TermType.DSInterval);

        term.removeParantheses(1, 2);
        term.getConnector(1, 2).setOperator(ArithmeticOperator.Minus);
        check(term, "d1 - f1 - d2", TermType.DSInterval);
        swapOperands(term, 0, 1);
        checkInvalid(term);
        swapOperands(term, 0, 1);
        term.setOperand(1, n1);
        check(term, "d1 - n1 - d2", TermType.DSInterval);
        swapOperands(term, 1, 2);
        check(term, "d1 - d2 - n1", TermType.DSInterval);

        term.addParantheses(1, 2);
        term.addOperand(1, conn(ArithmeticOperator.Plus, 1), f1);
        check(term, "(d1 + f1) - (d2 - n1)", TermType.DSInterval);
    }

    public void testExprAttr() {
        ITerm term = newTerm();
        IExpressionAttribute a1 = createNumericExpressionAttribute("a1", "edu.wustl.e1");
        String alias = "e1.a1";
        term.addOperand(a1);
        check(term, alias, TermType.Numeric);

        term.addOperand(conn(ArithmeticOperator.Plus, 0), numericLiteral("1"));
        check(term, alias + " + 1", TermType.Numeric);
    }

    public void testConstructor() {
        TermProcessor termProcessor = new TermProcessor();
        assertSame(termProcessor.getAliasProvider(), TermProcessor.defaultAliasProvider);

        checkPrimitiveOperationProcessor(DatabaseType.MySQL, MySQLPrimitiveOperationProcessor.class);
        checkPrimitiveOperationProcessor(DatabaseType.Oracle, OraclePrimitiveOperationProcessor.class);
    }

    private void checkPrimitiveOperationProcessor(DatabaseType mySQL,
            Class<? extends PrimitiveOperationProcessor> name) {
        IAttributeAliasProvider aliasProvider = new IAttributeAliasProvider() {

            public String getAliasFor(IExpressionAttribute exprAttr) {
                throw new UnsupportedOperationException();
            }

        };
        String dateFormat = "fooDateFormat";
        DatabaseSQLSettings sqlSettings = new DatabaseSQLSettings(DatabaseType.MySQL, dateFormat);
        TermProcessor termProcessor = new TermProcessor(aliasProvider, sqlSettings);

        assertSame(termProcessor.getAliasProvider(), aliasProvider);
        PrimitiveOperationProcessor actualPrimProc = termProcessor.getPrimitiveOperationProcessor();
        assertEquals(actualPrimProc.getClass(), MySQLPrimitiveOperationProcessor.class);
        MySQLPrimitiveOperationProcessor mysqlProc = (MySQLPrimitiveOperationProcessor) actualPrimProc;
        assertSame(mysqlProc.getDateFormat(), dateFormat);
    }

    private String concat(ILiteral s1, ArithmeticOperator oper, ILiteral s2) {
        return s1.getLiteral() + " " + oper.mathString() + " " + s2.getLiteral();
    }

}