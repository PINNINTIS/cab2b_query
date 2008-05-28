package edu.wustl.common.querysuite.queryobject;

/**
 * A marker interface for an operand. An operand is either a subexpression (in
 * which case, the corresponding expression id is added), or a rule.
 * 
 * @version 1.0
 * @updated 11-Oct-2006 02:56:16 PM
 */
public interface IExpressionOperand extends IBaseQueryObject, IOperand {

    /**
     * Used to determine whether the operand is an expression or a rule.
     * 
     * @return true- if subexpression, false - if this is a rule.
     * @deprecated this interface should just be a marker.
     */
    boolean isSubExpressionOperand();

}
