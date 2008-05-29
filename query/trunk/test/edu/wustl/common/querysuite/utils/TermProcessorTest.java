package edu.wustl.common.querysuite.utils;

import junit.framework.TestCase;
import edu.wustl.common.querysuite.factory.QueryObjectFactory;
import edu.wustl.common.querysuite.queryobject.ArithmeticOperator;
import edu.wustl.common.querysuite.queryobject.IArithmeticOperand;
import edu.wustl.common.querysuite.queryobject.IConnector;
import edu.wustl.common.querysuite.queryobject.ILiteral;
import edu.wustl.common.querysuite.queryobject.ITerm;
import edu.wustl.common.querysuite.queryobject.TermType;
import edu.wustl.common.querysuite.utils.TermProcessor.TermString;

public class TermProcessorTest extends TestCase {

    private TermProcessor termProcessor;

    private static final TermString INVALID_RES = new TermString("", TermType.Invalid);

    @Override
    public void setUp() {
        termProcessor = new TermProcessor();
        termProcessor.setPrimitiveOperationProcessor(new PrimitiveOperationProcessorMock(false));
    }

    public void test() {
        ITerm term = QueryObjectFactory.createTerm();
        term.addOperand(numericLiteral("1"));
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
        ITerm term = QueryObjectFactory.createTerm();
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
        ITerm term = QueryObjectFactory.createTerm();
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
        ITerm term = QueryObjectFactory.createTerm();
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
        ILiteral date1 = dateLiteral("d1");
        ILiteral date2 = dateLiteral("d2");
        ITerm term = QueryObjectFactory.createTerm();
        term.addOperand(date1);
        term.addOperand(conn(ArithmeticOperator.Plus, 0), date2);
        checkInvalid(term);

        ILiteral dateOffset1 = dateOffsetLiteral("off1");
        term.setOperand(1, dateOffset1);
        // d1 + off1
        check(term, concat(date1, ArithmeticOperator.Plus, dateOffset1), TermType.Date);
        swapOperands(term, 0, 1);
        // off1 + d1
        check(term, concat(dateOffset1, ArithmeticOperator.Plus, date1), TermType.Date);
        swapOperands(term, 0, 1);

        ILiteral numLiteral1 = numericLiteral("1");
        term.setOperand(1, numLiteral1);
        // d1 + 1
        check(term, concat(date1, ArithmeticOperator.Plus, numLiteral1), TermType.Date);
        swapOperands(term, 0, 1);
        // 1 + d1
        check(term, concat(numLiteral1, ArithmeticOperator.Plus, date1), TermType.Date);
        swapOperands(term, 0, 1);

        term.getConnector(0, 1).setOperator(ArithmeticOperator.Minus);

        term.setOperand(1, dateOffset1);
        // d1 - off1
        check(term, concat(date1, ArithmeticOperator.Minus, dateOffset1), TermType.Date);
        swapOperands(term, 0, 1);
        // off1 - d1
        checkInvalid(term);
        swapOperands(term, 0, 1);

        term.setOperand(1, numLiteral1);
        // d1 - 1
        check(term, concat(date1, ArithmeticOperator.Minus, numLiteral1), TermType.Date);
        swapOperands(term, 0, 1);
        // 1 - d1
        checkInvalid(term);
        swapOperands(term, 0, 1);

        term.setOperand(1, date2);
        // d1 - d2
        check(term, concat(date1, ArithmeticOperator.Minus, date2), TermType.Numeric);

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
        ITerm term = QueryObjectFactory.createTerm();
        ILiteral f1 = dateOffsetLiteral("f1");
        ILiteral n1 = numericLiteral("n1");

        term.addOperand(dateLiteral("d1"));
        term.addOperand(conn(ArithmeticOperator.Minus, 0), f1);
        term.addOperand(conn(ArithmeticOperator.Plus, 0), dateLiteral("d2"));
        checkInvalid(term);

        term.addParantheses(1, 2);
        check(term, "d1 - (f1 + d2)", TermType.Numeric);

        term.removeParantheses(1, 2);
        term.getConnector(1, 2).setOperator(ArithmeticOperator.Minus);
        check(term, "d1 - f1 - d2", TermType.Numeric);
        swapOperands(term, 0, 1);
        checkInvalid(term);
        swapOperands(term, 0, 1);
        term.setOperand(1, n1);
        check(term, "d1 - n1 - d2", TermType.Numeric);
        swapOperands(term, 1, 2);
        check(term, "d1 - d2 - n1", TermType.Numeric);

        term.addParantheses(1, 2);
        term.addOperand(1, conn(ArithmeticOperator.Plus, 1), f1);
        check(term, "(d1 + f1) - (d2 - n1)", TermType.Numeric);
    }

    protected void swapOperands(ITerm term, int i, int j) {
        IArithmeticOperand temp = term.getOperand(i);
        term.setOperand(i, term.getOperand(j));
        term.setOperand(j, temp);
    }

    private String concat(ILiteral s1, ArithmeticOperator oper, ILiteral s2) {
        return s1.getLiteral() + " " + oper.mathString() + " " + s2.getLiteral();
    }

    protected void checkNumeric(ITerm term, String expected) {
        checkNumeric(term, expected, false);
    }

    protected void checkNumeric(ITerm term, String expectedStr, boolean preFix) {
        termProcessor.setPrimitiveOperationProcessor(new PrimitiveOperationProcessorMock(preFix));
        TermString actual = termProcessor.convertTerm(term);
        TermString expected = new TermString(expectedStr, TermType.Numeric);
        assertEquals(expected, actual);
    }

    protected void checkInvalid(ITerm term) {
        check(term, INVALID_RES);
    }

    protected void check(ITerm term, String s, TermType termType) {
        check(term, new TermString(s, termType));
    }

    protected void check(ITerm term, TermString expected) {
        TermString actual = termProcessor.convertTerm(term);
        assertEquals(expected, actual);
    }

    protected ILiteral numericLiteral(String s) {
        return literal(s, TermType.Numeric);
    }

    protected ILiteral dateLiteral(String s) {
        return literal(s, TermType.Date);
    }

    protected ILiteral dateOffsetLiteral(String s) {
        return literal(s, TermType.DateOffset);
    }

    protected ILiteral literal(String s, TermType termType) {
        return QueryObjectFactory.createLiteral(s, termType);
    }

    protected IConnector<ArithmeticOperator> conn(ArithmeticOperator op, int n) {
        return QueryObjectFactory.createArithmeticConnector(op, n);
    }
}