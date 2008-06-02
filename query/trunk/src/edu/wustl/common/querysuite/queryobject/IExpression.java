package edu.wustl.common.querysuite.queryobject;

/**
 * A list of operands, and the logical connectors (AND, OR), that together form
 * a logical expression<br>
 * The connectors are identified by the position of the operands on either side,
 * e.g.,
 * 
 * <pre>
 *  given:  operand connector operand connector operand index:    0        0,
 * 1      1       1,2        2
 * </pre>
 * 
 * An IExpression belongs to a constraint entity; and constraints on another
 * associated entity will be present as a subexpression on the associated entity .
 * <br>
 * Conversely, if an expression has a subexpression, there must an association
 * in the join graph from the parent expression to the subexpression. <br>
 * Note: "subexpression" refers to an operand that is the IExpressionId of the
 * child expression. The entity of the subexpression will generally be different
 * from the entity of this expression (exception is when a class is associated
 * to itself, e.g. Specimen). <br>
 * The expression for an expressionId is found from
 * {@link edu.wustl.common.querysuite.queryobject.IConstraints}.
 * 
 * @see edu.wustl.common.querysuite.queryobject.IJoinGraph
 * @see edu.wustl.common.querysuite.queryobject.
 *      IConstraints#addExpression(IFunctionalClass)
 * @version 1.0
 * @updated 22-Dec-2006 2:50:17 PM
 */
public interface IExpression extends IBaseExpression<LogicalOperator, IExpressionOperand> {

    /**
     * The constraintEntity of IRule is constructor param to impl class.
     * 
     * @return The reference to the constraintEntity associated with this
     *         expression.
     */
    IQueryEntity getQueryEntity();

    /**
     * To get the Expression id assigned to this Expression.
     * 
     * @return the id of this expression.
     */
    IExpressionId getExpressionId();

    /**
     * This expression is in view or not
     * 
     * @return true if it is in view
     */
    boolean isInView();

    /**
     * This expression is visible or not on DAG
     * 
     * @return
     */
    boolean isVisible();

    void setVisible(boolean isVisible);

    /**
     * To set the expression in view.
     * 
     * @param isInView true if this expression should be added in view.
     */
    void setInView(boolean isInView);

    /**
     * To check whether there are any rule present in the Expression.
     * 
     * @return true if there is atleast one rule present in the operand list of
     *         expression.
     */
    boolean containsRule();

    /**
     * To check whether there are any custom formulas present in the Expression.
     * 
     * @return true if there is atleast one custom formula present in the
     *         operand list of expression.
     */
    boolean containsCustomFormula();
}