package edu.wustl.common.querysuite.utils;

import edu.wustl.common.querysuite.factory.QueryObjectFactory;
import edu.wustl.common.querysuite.queryobject.ArithmeticOperator;
import edu.wustl.common.querysuite.queryobject.DSInterval;
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
        } catch (IllegalArgumentException e) {

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
        setOperator(RelationalOperator.Equals);
        try {
            customFormulaProcessor.asString(customFormula);
            fail();
        } catch (IllegalArgumentException e) {

        }
    }

    public void testIncompatibleRHS() {
        addRhs(dateLiteral("2008-01-01"));
        setOperator(RelationalOperator.Equals);
        try {
            customFormulaProcessor.asString(customFormula);
            fail();
        } catch (IllegalArgumentException e) {

        }
    }

    public void testIncompatibleRHSBetweenIn() {
        addRhs(2);
        addRhs(dateLiteral("2008-01-01"));
        setOperator(RelationalOperator.Between);
        try {
            customFormulaProcessor.asString(customFormula);
            fail();
        } catch (IllegalArgumentException e) {

        }
        customFormula.getAllRhs().add(1, newTerm(3));
        setOperator(RelationalOperator.In);
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
        setOperator(o);
        check("1 " + sql(o));
        o = RelationalOperator.IsNotNull;
        setOperator(o);
        check("1 " + sql(o));
    }

    private String getResult() {
        return customFormulaProcessor.asString(customFormula);
    }

    public void testOneRHS() {
        setOperator(RelationalOperator.Equals);
        checkIllegal();
        addRhs(2);
        check("1 = 2");
        setOperator(RelationalOperator.LessThan);
        check("1 < 2");
    }

    public void testBetween() {
        setOperator(RelationalOperator.Between);
        checkIllegal();
        addRhs(2);
        checkIllegal();
        addRhs(3);
        check("(1 >= 2 and 1 <= 3) or (1 <= 2 and 1 >= 3)");
        addRhs(4);
        checkIllegal();
    }
    
    public void testNotBetween() {
        setOperator(RelationalOperator.NotBetween);
        checkIllegal();
        addRhs(2);
        checkIllegal();
        addRhs(3);
        check("(1 > 2 or 1 < 3) and (1 < 2 or 1 > 3)");
        addRhs(4);
        checkIllegal();
    }

    public void testIn() {
        setOperator(RelationalOperator.In);
        checkIllegal();
        addRhs(2);
        check("1 = 2");
        addRhs(3);
        check("1 = 2 or 1 = 3");
        addRhs(4);
        check("1 = 2 or 1 = 3 or 1 = 4");
    }

    public void testNotIn() {
        setOperator(RelationalOperator.NotIn);
        checkIllegal();
        addRhs(2);
        check("1 != 2");
        addRhs(3);
        check("1 != 2 and 1 != 3");
        addRhs(4);
        check("1 != 2 and 1 != 3 and 1 != 4");
    }

    public void testYMIntervalIllegal() {
        setOperator(RelationalOperator.IsNull);
        customFormula.setLhs(newTerm(dateOffsetLiteral("1", YMInterval.Month)));
        // 1month isnull
        checkIllegal();
        setOperator(RelationalOperator.Equals);

        addRhs(newTerm(dateOffsetLiteral("1", YMInterval.Month)));
        // 1month = 1month
        checkIllegal();

        customFormula.setLhs(newTerm(dateOffsetLiteral("1", DSInterval.Day)));
        // 1day = 1month
        checkIllegal();

        customFormula.getLhs().addOperand(conn(ArithmeticOperator.Minus, 0), dateLiteral("2008-01-01"));
        // 1day - '2008-01-01' = 1month
        checkIllegal();

        customFormula.getLhs().setConnector(0, 1, conn(ArithmeticOperator.Plus, 0));
        // 1day + '2008-01-01' = 1month
        checkIllegal();

        customFormula.getLhs().setOperand(0, dateLiteral("2008-01-02"));
        // '2008-01-02 + '2008-01-01' = 1month
        checkIllegal();

        customFormula.setLhs(newTerm(1));
        // 1 = YMInterval
        checkIllegal();
    }

    public void testYMInterval() {
        setOperator(RelationalOperator.Equals);
        customFormula.setLhs(newTerm(dateLiteral("2008-01-02")));
        customFormula.getLhs().addOperand(conn(ArithmeticOperator.Minus, 0), dateLiteral("2008-01-01"));
        addRhs(newTerm(dateOffsetLiteral("1", YMInterval.Month)));
        check("'2008-01-02' = 1Month + '2008-01-01'");

        setOperator(RelationalOperator.Between);
        addRhs(newTerm(dateOffsetLiteral("2", DSInterval.Day)));
        check("('2008-01-02' >= 1Month + '2008-01-01' and '2008-01-02' <= 2Day + '2008-01-01') or ('2008-01-02' <= 1Month + '2008-01-01' and '2008-01-02' >= 2Day + '2008-01-01')");

        setOperator(RelationalOperator.In);
        check("'2008-01-02' = 1Month + '2008-01-01' or '2008-01-02' = 2Day + '2008-01-01'");
    }

    public void testDSIntervalIllegal() {
        setOperator(RelationalOperator.IsNull);
        customFormula.setLhs(newTerm(dateOffsetLiteral("1", DSInterval.Day)));
        // 1day isnull
        checkIllegal();
        setOperator(RelationalOperator.Equals);

        addRhs(newTerm(dateOffsetLiteral("1", DSInterval.Day)));
        // 1day = 1day
        checkIllegal();

        customFormula.getLhs().addOperand(conn(ArithmeticOperator.Plus, 0), dateOffsetLiteral("2", DSInterval.Day));
        // 1day + 2Day = 1day
        checkIllegal();
    }

    public void testDSInterval() {
        setOperator(RelationalOperator.Equals);
        customFormula.setLhs(newTerm(dateLiteral("2008-01-02")));
        customFormula.getLhs().addOperand(conn(ArithmeticOperator.Minus, 0), dateLiteral("2008-01-01"));
        addRhs(newTerm(dateOffsetLiteral("1", DSInterval.Day)));
        check("'2008-01-02' = 1Day + '2008-01-01'");
    }

    private void addRhs(int i) {
        addRhs(newTerm(i));
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
