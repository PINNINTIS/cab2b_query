package edu.wustl.common.querysuite.utils;

import static edu.wustl.common.querysuite.queryobject.RelationalOperator.Equals;
import edu.wustl.common.querysuite.factory.QueryObjectFactory;
import edu.wustl.common.querysuite.queryobject.IArithmeticOperand;
import edu.wustl.common.querysuite.queryobject.ICustomFormula;
import edu.wustl.common.querysuite.queryobject.ITerm;
import edu.wustl.common.querysuite.queryobject.RelationalOperator;

public class CustomFormulaProcessorForJoinQueryTest extends AbstractTermProcessorTest
{
    private CustomFormulaProcessor customFormulaProcessor;

    private ICustomFormula customFormula;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        customFormulaProcessor = new CustomFormulaProcessor();
        customFormula = QueryObjectFactory.createCustomFormula();
        customFormula.setLhs(newTerm(createStringExpressionAttribute("a1", "edu.wustl.e1")));
    }

    public void testNoOperator() {
        try {
            customFormulaProcessor.asString(customFormula);
            fail();
        } catch (IllegalArgumentException e) {

        }
    }

    public void testInvalidLHS() 
    {    
        try 
        {
            customFormulaProcessor.asString(customFormula);
            fail();
        } 
        catch (IllegalArgumentException e)
        {

        }
    }

    public void testIncompatibleRHS() {
        addRhs(dateLiteral("2008-01-01"));
    	customFormula.addRhs(newTerm(createStringExpressionAttribute("a2", "edu.wustl.e2")));
        setOperator(Equals);
        try {
            customFormulaProcessor.asString(customFormula);
            fail();
        } catch (IllegalArgumentException e) {

        }
    }
    
    private ITerm newTerm(IArithmeticOperand opnd) {
        ITerm term = QueryObjectFactory.createTerm();
        term.addOperand(opnd);
        return term;
    }

    private String getResult() {
        return customFormulaProcessor.asString(customFormula);
    }

    public void testOneRHS() {
        setOperator(Equals);
        checkIllegal();
        customFormula.addRhs(newTerm(createStringExpressionAttribute("a2", "edu.wustl.e2")));
        check("e1.a1 = e2.a2");
    }

    private void addRhs(IArithmeticOperand opnd) {
        addRhs(newTerm(opnd));
    }

    private void addRhs(ITerm term) {
        customFormula.addRhs(term);
    }

    private void setOperator(RelationalOperator operator) {
        customFormula.setOperator(operator);
    }

    private void check(String expected) {
        assertEquals(expected, getResult());
        assertTrue(customFormula.isValid());
    }

    private void checkIllegal() {
        assertFalse(customFormula.isValid());
        try {
            getResult();
            fail();
        } catch (IllegalArgumentException e) {

        }
    }
}
