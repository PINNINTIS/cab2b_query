package edu.wustl.common.querysuite.queryobject.impl;

import edu.wustl.common.querysuite.queryobject.INumericLiteral;
import edu.wustl.common.querysuite.queryobject.IParameter;
import edu.wustl.common.querysuite.queryobject.IParameterizable;
import edu.wustl.common.querysuite.queryobject.TermType;

public class NumericLiteral extends ArithmeticOperand implements INumericLiteral, IParameterizable<NumericLiteral> {
    private static final long serialVersionUID = 3542370621181247228L;

    private String number;

    private IParameter<NumericLiteral> parameter;

    public NumericLiteral() {
        super(TermType.Numeric);
    }

    public String getNumber() {
        if (number == null) {
            number = "";
        }
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public IParameter<NumericLiteral> getParameter() {
        return parameter;
    }

    public void setParameter(IParameter<NumericLiteral> parameter) {
        this.parameter = parameter;
    }
}
