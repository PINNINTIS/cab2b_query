package edu.wustl.common.querysuite.queryobject.impl;

import java.sql.Date;

import edu.wustl.common.querysuite.queryobject.IDateLiteral;
import edu.wustl.common.querysuite.queryobject.IParameter;
import edu.wustl.common.querysuite.queryobject.IParameterizable;
import edu.wustl.common.querysuite.queryobject.TermType;

public class DateLiteral extends ArithmeticOperand implements IDateLiteral, IParameterizable<DateLiteral> {
    private static final long serialVersionUID = 5566821411890106081L;

    private Date date;

    private IParameter<DateLiteral> parameter;

    public DateLiteral() {
        super(TermType.Date);
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public IParameter<DateLiteral> getParameter() {
        return parameter;
    }

    public void setParameter(IParameter<DateLiteral> parameter) {
        this.parameter = parameter;
    }
}
