package edu.wustl.common.querysuite.queryobject.impl;

import java.util.ArrayList;
import java.util.List;

import edu.wustl.common.querysuite.queryobject.IBaseExpression;
import edu.wustl.common.querysuite.queryobject.IBinaryOperator;
import edu.wustl.common.querysuite.queryobject.IConnector;
import edu.wustl.common.querysuite.queryobject.IOperand;

abstract class BaseExpression<P extends IBinaryOperator, V extends IOperand> extends BaseQueryObject
        implements
            IBaseExpression<P, V> {

    private static final long serialVersionUID = 8426174732093549937L;

    public static final int NO_LOGICAL_CONNECTOR = -1;

    public static final int BOTH_LOGICAL_CONNECTOR = -2;

    protected List<V> expressionOperands = new ArrayList<V>();

    protected List<IConnector<P>> connectors = new ArrayList<IConnector<P>>();

    /**
     * @param operand the operand to be removed.
     * @return true if the operand was found; false otherwise.
     * @see edu.wustl.common.querysuite.queryobject.IExpression#removeOperand(edu.wustl.common.querysuite.queryobject.V)
     */
    public boolean removeOperand(V operand) {
        int index = expressionOperands.indexOf(operand);
        return removeOperand(index) != null;
    }

    /**
     * @param operand The operand to be added in Expression. index of the added
     *            operand.
     * @see edu.wustl.common.querysuite.queryobject.IExpression#addOperand(edu.wustl.common.querysuite.queryobject.V)
     */
    public int addOperand(V operand) {
        expressionOperands.add(operand);
        if (expressionOperands.size() != 1) {
            connectors.add(getUnknownOperator(0));
        }
        return expressionOperands.size() - 1;
    }

    protected abstract IConnector<P> getUnknownOperator(int nestingNumber);

    /**
     * @param logicalConnector the Logical connector by which the operand will
     *            be connected to the operand behind it.
     * @param operand The operand to be added in Expression.
     * @return index of the added operand.
     * @see edu.wustl.common.querysuite.queryobject.IExpression#addOperand(edu.wustl.common.querysuite.queryobject.IConnector
     *      <P>, edu.wustl.common.querysuite.queryobject.V)
     */
    public int addOperand(IConnector<P> logicalConnector, V operand) {
        expressionOperands.add(operand);
        connectors.add(expressionOperands.size() - 2, logicalConnector);
        return expressionOperands.size() - 1;
    }

    /**
     * @param index The index at which the operand to be inserted.
     * @param logicalConnector the Logical connector by which the operand will
     *            be connected to the operand behind it.
     * @param operand The operand to be added in Expression.
     * @throws IndexOutOfBoundsException if the index is out of range (index < 0 ||
     *             index > size()).
     * @see edu.wustl.common.querysuite.queryobject.IExpression#addOperand(int,
     *      edu.wustl.common.querysuite.queryobject.IConnector
     *      <P>, edu.wustl.common.querysuite.queryobject.V)
     */
    public void addOperand(int index, IConnector<P> logicalConnector, V operand) {
        expressionOperands.add(index, operand);
        connectors.add(index - 1, logicalConnector);
        if (index > 0
                && (((Connector) connectors.get(index)).getNestingNumber() > ((Connector) connectors
                        .get(index - 1)).getNestingNumber())) {
            ((Connector) connectors.get(index - 1)).setNestingNumber(((Connector) connectors.get(index))
                    .getNestingNumber());
        }
    }

    /**
     * @param index The index at which the operand to be inserted.
     * @param operand The operand to be added in Expression.
     * @param logicalConnector the Logical connector by which the operand will
     *            be connected operand in front of it.
     * @see edu.wustl.common.querysuite.queryobject.IExpression#addOperand(int,
     *      edu.wustl.common.querysuite.queryobject.V,
     *      edu.wustl.common.querysuite.queryobject.IConnector
     *      <P>)
     */
    public void addOperand(int index, V operand, IConnector<P> logicalConnector) {
        expressionOperands.add(index, operand);
        connectors.add(index, logicalConnector);
        if (index > 0
                && (((Connector) connectors.get(index)).getNestingNumber() < ((Connector) connectors
                        .get(index - 1)).getNestingNumber())) {
            ((Connector) connectors.get(index)).setNestingNumber(((Connector) connectors.get(index - 1))
                    .getNestingNumber());
        }
    }

    /**
     * calls addParantheses(0, size-1)
     * 
     * @see edu.wustl.common.querysuite.queryobject.IExpression#addParantheses()
     */
    public void addParantheses() {
        addParantheses(0, expressionOperands.size() - 1);
    }

    /**
     * @param leftOperandIndex The index of the left operand.
     * @param rightOperandIndex The index of the right operand.
     * @see edu.wustl.common.querysuite.queryobject.IExpression#addParantheses(int,
     *      int)
     */
    public void addParantheses(int leftOperandIndex, int rightOperandIndex) {
        for (int i = leftOperandIndex; i < rightOperandIndex; i++) {
            ((Connector) connectors.get(i)).incrementNestingNumber();
        }
    }

    /**
     * calls removeParantheses(0, size-1)
     * 
     * @see edu.wustl.common.querysuite.queryobject.IExpression#removeParantheses()
     */
    public void removeParantheses() {
        removeParantheses(0, connectors.size() - 1);

    }

    /**
     * Decrements nesting num of all the logical connectors in the expression
     * between the specified operands' indexes, both inclusive.
     * 
     * @param leftOperandIndex The index of the left operand.
     * @param rightOperandIndex The index of the right operand.
     * @see edu.wustl.common.querysuite.queryobject.IExpression#removeParantheses(int,
     *      int)
     */
    public void removeParantheses(int leftOperandIndex, int rightOperandIndex) {
        for (int i = leftOperandIndex; i < rightOperandIndex; i++) {
            ((Connector) connectors.get(i))
                    .setNestingNumber(((Connector) connectors.get(i)).getNestingNumber() - 1);
        }
    }

    /**
     * @param leftOperandIndex The index of the left operand.
     * @param rightOperandIndex The index of the right operand.
     * @return The reference to logical connector between who operands.
     * @see edu.wustl.common.querysuite.queryobject.IExpression#getLogicalConnector(int,
     *      int)
     */
    public IConnector<P> getConnector(int leftOperandIndex, int rightOperandIndex) {
        checkAdjacentOperands(leftOperandIndex, rightOperandIndex);
        if (leftOperandIndex == -1 || leftOperandIndex == connectors.size()) {
            return getUnknownOperator(-1);
        }
        return connectors.get(leftOperandIndex);
    }

    protected void checkAdjacentOperands(int leftOperandIndex, int rightOperandIndex) {
        if (rightOperandIndex != leftOperandIndex + 1) {
            throw new IllegalArgumentException("Incorrect indexes selected; please select adjacent indexes");
        }
    }

    /**
     * @param index the index of operand.
     * @return The operand identified by the given index.
     * @see edu.wustl.common.querysuite.queryobject.IExpression#getOperand(int)
     */
    public V getOperand(int index) {
        return expressionOperands.get(index);
    }

    /**
     * @param leftOperandIndex The index of the left operand.
     * @param rightOperandIndex The index of the right operand.
     * @param logicalConnector the logical connector between let & Right
     *            operand.
     * @see edu.wustl.common.querysuite.queryobject.IExpression#setLogicalConnector(int,
     *      int, edu.wustl.common.querysuite.queryobject.IConnector
     *      <P>)
     */
    public void setConnector(int leftOperandIndex, int rightOperandIndex, IConnector<P> logicalConnector) {
        if (rightOperandIndex == leftOperandIndex + 1) {
            connectors.set(leftOperandIndex, logicalConnector);
        } else {
            throw new IllegalArgumentException("Incorrect indexes selected; please select adjacent indexes");
        }
    }

    /**
     * @param index the expected index of the operand in he Expression
     * @param operand The operand to be added in the Expression.
     * @see edu.wustl.common.querysuite.queryobject.IExpression#setOperand(int,V)
     */
    public void setOperand(int index, V operand) {
        expressionOperands.set(index, operand);
    }

    /**
     * @return the no. of operands in the expression.
     * @see edu.wustl.common.querysuite.queryobject.IExpression#numberOfOperands()
     */
    public int numberOfOperands() {
        return expressionOperands.size();
    }

    /**
     * @param index the index of operand to be removed.
     * @return the removed operand.
     * @see edu.wustl.common.querysuite.queryobject.IExpression#removeOperand(int)
     */
    public V removeOperand(int index) {

        if (index == -1) {
            return null;
        }
        // A and (B or C) remove C => A and B
        // A and (B or C) remove B => A and C
        // A and (B or C) remove A => B or C
        // A and B or C remove B => A and C
        int connectorIndex = indexOfConnectorForOperand(expressionOperands.get(index));
        V operand = expressionOperands.get(index);

        expressionOperands.remove(index);
        if (connectorIndex == Expression.BOTH_LOGICAL_CONNECTOR) // if both
        // adjacent
        // connectors
        // have same
        // nesting
        // no. then
        // remove
        // connector
        // following
        // the
        // operand.
        {
            connectorIndex = index;
        }

        if (connectorIndex != Expression.NO_LOGICAL_CONNECTOR) {
            connectors.remove(connectorIndex);
        }

        return operand;
    }

    /**
     * @param operand the reference to V, to be searched in the Expression.
     * @return The index of the given Expression operand.
     * @see edu.wustl.common.querysuite.queryobject.IExpression#indexOfOperand(edu.wustl.common.querysuite.queryobject.V)
     */
    public int indexOfOperand(V operand) {
        return expressionOperands.indexOf(operand);
    }

    /**
     * To get the adjacent logical connector with the greater nesting number.
     * 
     * @param operand The reference to V of which the Logical connector to
     *            search.
     * @return index of adjacent Logical connector with greater nesting number.
     *         Expression.NO_LOGICAL_CONNECTOR if operand not found or there is
     *         no logical connector present in the Expression.
     *         Expression.BOTH_LOGICAL_CONNECTOR if both adjacent connectors are
     *         of same nesting number
     */
    public int indexOfConnectorForOperand(V operand) {
        int index = expressionOperands.indexOf(operand);

        if (index != -1) {
            if (expressionOperands.size() == 1) // if there is only one
            // Expression then there is no
            // logical connector associated
            // with it.
            {
                index = Expression.NO_LOGICAL_CONNECTOR;
            } else if (index == expressionOperands.size() - 1) // if expression
            // is last
            // operand then
            // index of last
            // connector
            // will be
            // returned.
            {
                index = index - 1;
            } else if (index != 0) // if expression is not 1st & last, then
            // index will depend upon the immediate
            // bracket surrounding that expression.
            {
                int preNesting = ((Connector) connectors.get(index - 1)).getNestingNumber();
                int postNesting = ((Connector) connectors.get(index)).getNestingNumber();
                if (postNesting == preNesting) // if nesting no are same, then
                // there is not direct bracket
                // sorrounding operand.
                {
                    index = Expression.BOTH_LOGICAL_CONNECTOR;
                } else if (postNesting < preNesting) {
                    index--;
                }
            }
        } else {
            index = Expression.NO_LOGICAL_CONNECTOR;
        }

        return index;
    }

    /**
     * @see edu.wustl.common.querysuite.queryobject.IBaseExpression#nestingNumberOfOperand(int)
     */
    public int nestingNumberOfOperand(int i) {
        int leftNesting = getConnector(i - 1, i).getNestingNumber();
        int rightNesting = getConnector(i, i + 1).getNestingNumber();
        if (leftNesting < rightNesting) {
            return rightNesting;
        } else {
            return leftNesting;
        }
    }
}
