package edu.wustl.common.querysuite.queryobject;

/**
 * @author srinath_k
 * 
 * @param
 * <P>
 * the type of the operator
 * @param <V> the type of the operand
 */
public interface IBaseExpression<P extends IBinaryOperator, V extends IOperand> extends IBaseQueryObject {

    /**
     * To get the operand indexed by index in the operand list of Expression.
     * 
     * @param index index of the operand in the operand list of Expression.
     * @return The operand identified by the given index.
     */
    V getOperand(int index);

    /**
     * To set the operand in the Expression at index position
     * 
     * @param index the expected index of the operand in he Expression
     * @param operand The operand to be added in the Expression.
     */
    void setOperand(int index, V operand);

    /**
     * An operator is indexed by {left operand, right operand} i.e. if left
     * index is "i", value of right index must be "i+1" this is just to make the
     * method intuitively clearer...
     * 
     * @param leftOperandIndex The index of the left operand.
     * @param rightOperandIndex The index of the right operand.
     * @return The reference to logical connector between who adjacent operands.
     */
    IConnector<P> getConnector(int leftOperandIndex, int rightOperandIndex);

    /**
     * To set logical connector between two adjescent operands.
     * 
     * @param leftOperandIndex The index of the left operand.
     * @param rightOperandIndex The index of the right operand.
     * @param connector The reference to the logical connector to set.
     * 
     */
    void setConnector(int leftOperandIndex, int rightOperandIndex, IConnector<P> connector);

    /**
     * To add the Parenthesis around the operands specified by left & right
     * operand index. Increments nesting num of all the logical connectors in
     * the expression between the specified operands' indexes, both inclusive.
     * 
     * @param leftOperandIndex The index of the left operand.
     * @param rightOperandIndex The index of the left operand.
     * 
     */
    void addParantheses(int leftOperandIndex, int rightOperandIndex);

    /**
     * Just calls addParantheses(0, size-1)
     */
    void addParantheses();

    /**
     * To remove the Parenthesis around the operands specified by left & right
     * operand index. Decrements nesting num of all the logical connectors in
     * the expression between the specified operands' indexes, both inclusive.
     * 
     * @param leftOperandIndex The index of the left operand.
     * @param rightOperandIndex The index of the left operand.
     */
    void removeParantheses(int leftOperandIndex, int rightOperandIndex);

    /**
     * Just calls removeParantheses(0, size-1)
     */
    void removeParantheses();

    /**
     * Adds an operand to the operands list. A default logical connector AND
     * will be added to the connectors list provided there are atleast two
     * operands in the operands list.
     * 
     * @param operand The reference of the operand added.
     * @return index of the added operand.
     */
    int addOperand(V operand);

    /**
     * To add operand to the Expression with the specified logical connector.
     * This operand will be added as last operand in the operand list.
     * 
     * @param connector the Logical connector by which the operand will be
     *            connected to the operand behind it.
     * @param operand The operand to be added in Expression.
     * @return index of the added operand.
     */
    int addOperand(IConnector<P> connector, V operand);

    /**
     * Inserts an operand with the connector in front of it.
     * 
     * @param index The index at which the operand to be inserted.
     * @param connector the Logical connector by which the operand will be
     *            connected to the operand behind it.
     * @param operand The operand to be added in Expression.
     */
    void addOperand(int index, IConnector<P> connector, V operand);

    /**
     * Inserts an operand with the connector behind it.
     * 
     * @param index The index at which the operand to be inserted.
     * @param operand The operand to be added in Expression.
     * @param connector the Logical connector by which the operand will be
     *            connected operand in front of it.
     * 
     */
    void addOperand(int index, V operand, IConnector<P> connector);

    /**
     * Removes the operand, and the appropriate connector. The adjacent logical
     * connector with the greater nesting number will be removed. If both
     * adjacent connectors are of same nesting number, it is undefined as to
     * which one will be removed.
     * 
     * @param index The index of operand to be removed.
     * @return reference to removed operand.
     */
    V removeOperand(int index);

    /**
     * Removes the operand, and the appropriate connector. The adjacent logical
     * connector with the greater nesting number will be removed. If both
     * adjacent connectors are of same nesting number, it is undefined as to
     * which one will be removed.
     * 
     * @param operand the operand to be removed.
     * @return true if the operand was found; false otherwise.
     */
    boolean removeOperand(V operand);

    /**
     * To get the index of the operand in the operand list of Expression.
     * 
     * @param operand the reference to IExpressionOperand, to be searched in the
     *            Expression.
     * @return The index of the given Expression operand.
     * @see java.util.List#indexOf(java.lang.Object)
     */
    int indexOfOperand(V operand);

    /**
     * To the the number of operand in the operand list of Expression.
     * 
     * @return the no. of operands in the expression.
     */
    int numberOfOperands();

    /**
     * Returns the nesting of the operand. The nesing of an operand is the
     * greater of the nesting of the connectors adjacent to the specified
     * operand.
     * 
     * @param i the index of the operand.
     * @return the nesting of the operand.
     */
    int nestingNumberOfOperand(int i);
}
