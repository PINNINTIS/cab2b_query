package edu.wustl.common.querysuite.utils;

import junit.framework.TestCase;
import edu.wustl.common.querysuite.factory.QueryObjectFactory;
import edu.wustl.common.querysuite.queryobject.ArithmeticOperator;
import edu.wustl.common.querysuite.queryobject.IConnector;
import edu.wustl.common.querysuite.queryobject.ILiteral;
import edu.wustl.common.querysuite.queryobject.ITerm;
import edu.wustl.common.querysuite.queryobject.TermType;

public class TermProcessorTest extends TestCase {

    private TermProcessor termProcessor;

    @Override
    public void setUp() {
        termProcessor = new TermProcessor();
    }

    public void test() {
        ITerm term = QueryObjectFactory.createTerm();
        term.addOperand(literal("1"));
        term.addOperand(conn(ArithmeticOperator.Plus, 0), literal("2"));

        assertEquals("(1 + 2)", actual(term, false));

        term.addParantheses();
        term.addOperand(conn(ArithmeticOperator.MultipliedBy, 0), literal("3"));
        assertEquals("((1 + 2) * 3)", actual(term, false));

        term.removeParantheses(0, 1);
        term.addParantheses(1, 2);
        assertEquals("(1 + (2 * 3))", actual(term, false));

        term.addOperand(conn(ArithmeticOperator.Plus, 0), literal("5"));
        assertEquals("(1 + (2 * 3) + 5)", actual(term, false));
    }

    public void test2() {
        ITerm term = QueryObjectFactory.createTerm();
        term.addOperand(literal("1"));
        term.addOperand(conn(ArithmeticOperator.Plus, 0), literal("2"));

        term.addParantheses();
        term.addOperand(conn(ArithmeticOperator.MultipliedBy, 0), literal("3"));
        term.addOperand(conn(ArithmeticOperator.Minus, 1), literal("4"));
        assertEquals("((1 + 2) * (3 - 4))", actual(term, false));

        term.addOperand(conn(ArithmeticOperator.DividedBy, 0), literal("5"));
        assertEquals("((1 + 2) * (3 - 4) / 5)", actual(term, false));

        term.addParantheses(2, 4);
        assertEquals("((1 + 2) * ((3 - 4) / 5))", actual(term, false));

        term.removeParantheses(2, 4);
        term.addParantheses(0, 3);
        assertEquals("(((1 + 2) * (3 - 4)) / 5)", actual(term, false));

        term.addOperand(conn(ArithmeticOperator.Plus, 1), literal("6"));
        assertEquals("(((1 + 2) * (3 - 4)) / (5 + 6))", actual(term, false));
    }

    public void testPostFix() {
        ITerm term = QueryObjectFactory.createTerm();
        term.addOperand(literal("1"));
        term.addOperand(conn(ArithmeticOperator.Plus, 0), literal("2"));

        assertEquals("(+[1, 2])", actual(term, true));

        term.addParantheses();
        term.addOperand(conn(ArithmeticOperator.MultipliedBy, 0), literal("3"));
        assertEquals("(*[(+[1, 2]), 3])", actual(term, true));

        term.removeParantheses(0, 1);
        term.addParantheses(1, 2);
        assertEquals("(+[1, (*[2, 3])])", actual(term, true));

        term.addOperand(conn(ArithmeticOperator.Plus, 0), literal("5"));
        assertEquals("(+[+[1, (*[2, 3])], 5])", actual(term, true));
    }

    public void testPostFix2() {
        ITerm term = QueryObjectFactory.createTerm();
        term.addOperand(literal("1"));
        term.addOperand(conn(ArithmeticOperator.Plus, 0), literal("2"));

        term.addParantheses();
        term.addOperand(conn(ArithmeticOperator.MultipliedBy, 0), literal("3"));
        term.addOperand(conn(ArithmeticOperator.Minus, 1), literal("4"));
        assertEquals("(*[(+[1, 2]), (-[3, 4])])", actual(term, true));

        term.addOperand(conn(ArithmeticOperator.DividedBy, 0), literal("5"));
        assertEquals("(/[*[(+[1, 2]), (-[3, 4])], 5])", actual(term, true));

        term.addParantheses(2, 4);
        assertEquals("(*[(+[1, 2]), (/[(-[3, 4]), 5])])", actual(term, true));

        term.removeParantheses(2, 4);
        term.addParantheses(0, 3);
        assertEquals("(/[(*[(+[1, 2]), (-[3, 4])]), 5])", actual(term, true));

        term.addOperand(conn(ArithmeticOperator.Plus, 1), literal("6"));
        assertEquals("(/[(*[(+[1, 2]), (-[3, 4])]), (+[5, 6])])", actual(term, true));
    }

    private String actual(ITerm term, boolean postFix) {
        return termProcessor.convertTerm(term, postFix).getString();
    }

    private ILiteral literal(String s) {
        return QueryObjectFactory.createLiteral(s, TermType.Numeric);
    }

    private IConnector<ArithmeticOperator> conn(ArithmeticOperator op, int n) {
        return QueryObjectFactory.createArithmeticConnector(op, n);
    }
}