package edu.wustl.common.querysuite.queryobject.impl;

import org.apache.commons.lang.builder.HashCodeBuilder;

import edu.wustl.common.querysuite.queryobject.IExpressionId;

/**
 * @author Mandar Shidhore
 * @author chetan_patil
 * @version 1.0
 * @created 12-Oct-2006 11.12.04 AM
 * 
 * @hibernate.joined-subclass table="QUERY_EXPRESSIONID"
 * @hibernate.joined-subclass-key column="IDENTIFIER"
 */
public class ExpressionId extends ExpressionOperand implements IExpressionId {
    private static final long serialVersionUID = 2012640054952775498L;

    private int expressionIdentifier;

    /**
     * Default Constructor
     */
    public ExpressionId() {

    }

    /**
     * Parameterized Constructor
     * 
     * @param id The id to be assigned.
     */
    public ExpressionId(int expressionId) {
        this.expressionIdentifier = expressionId;
    }

    public int getInt() {
        return expressionIdentifier;
    }

    /**
     * To get the HashCode for the object. It will be calculated based on
     * expression Id.
     * 
     * @return The hash code value for the object.
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(expressionIdentifier).toHashCode();
    }

    /**
     * To check whether two objects are equal.
     * 
     * @param obj reference to the object to be checked for equality.
     * @return true if expression id of both objects are equal.
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj != null && this.getClass() == obj.getClass()) {
            ExpressionId expressionId = (ExpressionId) obj;
            if (this.expressionIdentifier == expressionId.expressionIdentifier) {
                return true;
            }
        }
        return false;
    }

    /**
     * @return String representation of Expression Id object in the form:
     *         [ExpressionId: id]
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "ExpressionId:" + expressionIdentifier;
    }

    // for hibernate
    /**
     * This method returns the expression id.
     * 
     * @return The integer value sassigned to this Expression id.
     * @see edu.wustl.common.querysuite.queryobject.IExpressionId#getExpressionIdentifier()
     * 
     * @hibernate.property name="expressionId" column="SUB_EXPRESSION_ID"
     *                     type="integer" length="30" not-null="true"
     */
    @SuppressWarnings("unused")
    private int getExpressionIdentifier() {
        return expressionIdentifier;
    }

    /**
     * This method sets the expression id.
     * 
     * @param expressionId
     */
    @SuppressWarnings("unused")
    private void setExpressionIdentifier(int expressionIdentifier) {
        this.expressionIdentifier = expressionIdentifier;
    }

}
