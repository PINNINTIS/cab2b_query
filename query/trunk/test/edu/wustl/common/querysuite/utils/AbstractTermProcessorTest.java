package edu.wustl.common.querysuite.utils;

import static edu.wustl.common.querysuite.utils.DynExtnMockUtil.*;
import junit.framework.TestCase;
import edu.common.dynamicextensions.domain.DomainObjectFactory;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.common.querysuite.factory.QueryObjectFactory;
import edu.wustl.common.querysuite.queryobject.ArithmeticOperator;
import edu.wustl.common.querysuite.queryobject.IArithmeticOperand;
import edu.wustl.common.querysuite.queryobject.IConnector;
import edu.wustl.common.querysuite.queryobject.IExpressionAttribute;
import edu.wustl.common.querysuite.queryobject.IExpressionId;
import edu.wustl.common.querysuite.queryobject.ILiteral;
import edu.wustl.common.querysuite.queryobject.ITerm;
import edu.wustl.common.querysuite.queryobject.TermType;
import edu.wustl.common.querysuite.utils.TermProcessor.TermString;

public abstract class AbstractTermProcessorTest extends TestCase {
    private TermProcessor termProcessor;

    @Override
    protected void setUp() throws Exception {
        setTermProcessor(new TermProcessor());
    }

    protected final void setTermProcessor(TermProcessor termProcessor) {
        this.termProcessor = termProcessor;
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
        check(term, TermString.INVALID);
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

    protected void swapOperands(ITerm term, int i, int j) {
        IArithmeticOperand temp = term.getOperand(i);
        term.setOperand(i, term.getOperand(j));
        term.setOperand(j, temp);
    }

    protected IConnector<ArithmeticOperator> conn(ArithmeticOperator op, int n) {
        return QueryObjectFactory.createArithmeticConnector(op, n);
    }

    protected ITerm newTerm() {
        return QueryObjectFactory.createTerm();
    }

    protected IExpressionAttribute createNumericExpressionAttribute(String attrName, String entityName) {
        return createExpressionAttribute(attrName, entityName, TermType.Numeric);
    }

    protected IExpressionAttribute createDateExpressionAttribute(String attrName, String entityName) {
        return createExpressionAttribute(attrName, entityName, TermType.Date);
    }

    protected IExpressionAttribute createDateOffsetExpressionAttribute(String attrName, String entityName) {
        return createExpressionAttribute(attrName, entityName, TermType.DateOffset);
    }

    private IExpressionAttribute createExpressionAttribute(String attrName, String entityName, TermType termType) {
        return QueryObjectFactory.createExpressionAttribute(exprId(1), createAttribute(attrName,
                createEntity(entityName)), termType);
    }

    private IExpressionId exprId(final int i) {
        return new IExpressionId() {

            public int getInt() {
                return i;
            }

            public boolean isSubExpressionOperand() {
                return false;
            }

            public Long getId() {
                throw new UnsupportedOperationException();
            }

            public void setId(Long id) {
                throw new UnsupportedOperationException();

            }

        };
    }
}