package edu.wustl.common.querysuite.utils;

import junit.framework.TestCase;
import edu.wustl.common.querysuite.factory.QueryObjectFactory;
import edu.wustl.common.querysuite.queryobject.ICustomFormula;
import edu.wustl.common.querysuite.queryobject.ITerm;
import edu.wustl.common.querysuite.queryobject.RelationalOperator;
import edu.wustl.common.querysuite.queryobject.TermType;

public class CustomFormulaProcessorTest extends TestCase {
    private CustomFormulaProcessor customFormulaProcessor;

    private ICustomFormula customFormula;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        customFormulaProcessor = new CustomFormulaProcessor();
        customFormula = QueryObjectFactory.createCustomFormula();
        customFormula.setLhs(newTerm(1));
    }

    private ITerm newTerm(int opnd) {
        ITerm term = QueryObjectFactory.createTerm();
        term.addOperand(QueryObjectFactory.createLiteral(String.valueOf(opnd), TermType.Numeric));
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
