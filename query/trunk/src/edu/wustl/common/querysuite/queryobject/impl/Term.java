package edu.wustl.common.querysuite.queryobject.impl;

import edu.wustl.common.querysuite.factory.QueryObjectFactory;
import edu.wustl.common.querysuite.queryobject.ArithmeticOperator;
import edu.wustl.common.querysuite.queryobject.IArithmeticOperand;
import edu.wustl.common.querysuite.queryobject.IConnector;
import edu.wustl.common.querysuite.queryobject.ITerm;
import edu.wustl.common.querysuite.utils.TermProcessor;
import edu.wustl.common.querysuite.utils.TermProcessor.TermString;

public class Term extends BaseExpression<ArithmeticOperator, IArithmeticOperand> implements ITerm {

    private static final long serialVersionUID = 6787015118573915634L;

    @Override
    protected IConnector<ArithmeticOperator> getUnknownOperator(int nestingNumber) {
        return QueryObjectFactory.createArithmeticConnector(ArithmeticOperator.Unknown, nestingNumber);
    }

    public TermString getStringRepresentation() {
        TermProcessor processor = new TermProcessor();
        return processor.convertTerm(this);
    }

    @Override
    public String toString() {
        return getStringRepresentation().toString();
    }

}
