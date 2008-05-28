package edu.wustl.common.querysuite.queryobject;

public interface ILiteral extends IArithmeticOperand {
    // TODO check date.
    // TODO string not supported.
    String getLiteral();

    void setLiteral(String literal);
}
