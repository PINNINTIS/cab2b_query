package edu.wustl.common.querysuite.utils;

import edu.wustl.common.querysuite.factory.QueryObjectFactory;
import edu.wustl.common.querysuite.queryobject.ArithmeticOperator;
import edu.wustl.common.querysuite.queryobject.IArithmeticOperand;
import edu.wustl.common.querysuite.queryobject.ICustomFormula;
import edu.wustl.common.querysuite.queryobject.ITerm;
import edu.wustl.common.querysuite.queryobject.RelationalOperator;
import edu.wustl.common.querysuite.queryobject.YMInterval;

public class CustomFormulaProcessorTest extends AbstractTermProcessorTest {
    private CustomFormulaProcessor customFormulaProcessor;

    private ICustomFormula customFormula;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        customFormulaProcessor = new CustomFormulaProcessor();
        customFormula = QueryObjectFactory.createCustomFormula();
        customFormula.setLhs(newTerm(1));
    }

    public void testNoOperator() {
        try {
            customFormulaProcessor.asString(customFormula);
            fail();
        } catch (NullPointerException e) {

        }
    }

    public void testInvalidLHS() {
        customFormula.getLhs().addOperand(conn(ArithmeticOperator.Minus, 0), dateLiteral("2008-01-01"));
        try {
            customFormulaProcessor.asString(customFormula);
            fail();
        } catch (IllegalArgumentException e) {

        }
    }

    public void testInvalidRHS() {
        addRhs(dateOffsetLiteral("off", YMInterval.Month));
        customFormula.setOperator(RelationalOperator.Equals);
        try {
            customFormulaProcessor.asString(customFormula);
            fail();
        } catch (IllegalArgumentException e) {

        }
    }

    public void testIncompatibleRHS() {
        addRhs(dateLiteral("2008-01-01"));
        customFormula.setOperator(RelationalOperator.Equals);
        try {
            customFormulaProcessor.asString(customFormula);
            fail();
        } catch (IllegalArgumentException e) {

        }
    }

    public void testIncompatibleRHSBetweenIn() {
        addRhs(2);
        addRhs(dateLiteral("2008-01-01"));
        customFormula.setOperator(RelationalOperator.Between);
        try {
            customFormulaProcessor.asString(customFormula);
            fail();
        } catch (IllegalArgumentException e) {

        }
        customFormula.getAllRhs().add(1, newTerm(3));
        customFormula.setOperator(RelationalOperator.In);
        try {
            customFormulaProcessor.asString(customFormula);
            fail();
        } catch (IllegalArgumentException e) {

        }
    }

    private ITerm newTerm(int opnd) {
        return newTerm(QueryObjectFactory.createNumericLiteral(String.valueOf(opnd)));
    }

    private ITerm newTerm(IArithmeticOperand opnd) {
        ITerm term = QueryObjectFactory.createTerm();
        term.addOperand(opnd);
        return term;
    }

    public void testNoRHS() {
        RelationalOperator o = RelationalOperator.IsNull;
        customFormula.setOperator(o);
        check("1 " + sql(o));
        o = RelationalOperator.IsNotNull;
        customFormula.setOperator(o);
        check("1 " + sql(o));
    }

    private String getResult() {
        return customFormulaProcessor.asString(customFormula);
    }

    public void testOneRHS() {
        customFormula.setOperator(RelationalOperator.Equals);
        checkIllegal();
        addRhs(2);
        check("1 = 2");
        customFormula.setOperator(RelationalOperator.LessThan);
        check("1 < 2");
    }

    public void testBetween() {
        customFormula.setOperator(RelationalOperator.Between);
        checkIllegal();
        addRhs(2);
        checkIllegal();
        addRhs(3);
        check("(1 >= 2 and 1 <= 3) or (1 <= 2 and 1 >= 3)");
        addRhs(4);
        checkIllegal();
    }

    public void testIn() {
        customFormula.setOperator(RelationalOperator.In);
        checkIllegal();
        addRhs(2);
        check("1 = 2");
        addRhs(3);
        check("1 = 2 or 1 = 3");
        addRhs(4);
        check("1 = 2 or 1 = 3 or 1 = 4");
    }

    public void testNotIn() {
        customFormula.setOperator(RelationalOperator.NotIn);
        checkIllegal();
        addRhs(2);
        check("1 != 2");
        addRhs(3);
        check("1 != 2 and 1 != 3");
        addRhs(4);
        check("1 != 2 and 1 != 3 and 1 != 4");
    }

    private void addRhs(int i) {
        customFormula.addRhs(newTerm(i));
    }

    private void addRhs(IArithmeticOperand opnd) {
        customFormula.addRhs(newTerm(opnd));
    }

    private String sql(RelationalOperator o) {
        return RelationalOperator.getSQL(o);
    }

    private void check(String expected) {
        assertEquals(expected, getResult());
    }

    private void checkIllegal() {
        try {
            getResult();
            fail();
        } catch (IllegalArgumentException e) {

        }
    }
}
