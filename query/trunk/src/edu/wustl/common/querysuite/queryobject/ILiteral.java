package edu.wustl.common.querysuite.queryobject;

/**
 * Represents a literal operand in a term.
 * 
 * @author srinath_k
 */
public interface ILiteral extends IArithmeticOperand {
    // TODO string not supported.
    String getLiteral();

    void setLiteral(String literal);
}
