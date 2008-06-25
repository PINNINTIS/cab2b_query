package edu.wustl.common.querysuite.queryobject.impl;

import org.apache.commons.lang.builder.HashCodeBuilder;

import edu.wustl.common.querysuite.queryobject.BinaryOperatorCompoundEnum;
import edu.wustl.common.querysuite.queryobject.IBinaryOperator;
import edu.wustl.common.querysuite.queryobject.IConnector;

/**
 * @author Mandar Shidhore
 * @author chetan_patil
 * @version 1.0
 * @created 12-Oct-2006 15.04.04 AM
 * 
 * @hibernate.class table="QUERY_LOGICAL_CONNECTOR"
 * @hibernate.cache usage="read-write"
 */
// TODO check hbm etc...
public class Connector<P extends Enum<?> & IBinaryOperator> extends BaseQueryObject implements IConnector<P> {
    private static final long serialVersionUID = 3065606993455242889L;

    private BinaryOperatorCompoundEnum<P> compoundOperator;

    private String operatorString;

    private int nestingNumber = 0;

    /**
     * Default Constructor
     */
    public Connector() {

    }

    /**
     * Parameterized Constructor
     * 
     * @param logicalOperator The reference to the Logical operator.
     */
    public Connector(P operator) {
        setOperator(operator);
    }

    /**
     * The constructor to instantiate the Logical connector object with the
     * given logical operator & nesting number.
     * 
     * @param operator The reference to the Logical operator.
     * @param nestingNumber The integer value which will decide no. of
     *            parenthesis surrounded by this operator.
     */
    public Connector(P operator, int nestingNumber) {
        setOperator(operator);
        this.nestingNumber = nestingNumber;
    }

    /**
     * Returns the identifier assigned to BaseQueryObject.
     * 
     * @return a unique id assigned to the Condition.
     * 
     * @hibernate.id name="id" column="IDENTIFIER" type="long" length="30"
     *               unsaved-value="null" generator-class="native"
     * @hibernate.generator-param name="sequence" value="LOGICAL_CONNECTOR_SEQ"
     */
    public Long getId() {
        return id;
    }

    /**
     * @see edu.wustl.common.querysuite.queryobject.IConnector#getOperator()
     */
    public P getOperator() {
        if (compoundOperator == null) {
            return null;
        }
        return compoundOperator.primitiveEnum();
    }

    /**
     * @see edu.wustl.common.querysuite.queryobject.IConnector#setOperator(edu.wustl.common.querysuite.queryobject.IBinaryOperator)
     */
    public void setOperator(P operator) {
        this.compoundOperator = BinaryOperatorCompoundEnum.compoundEnum(operator);
    }

    /**
     * @return the operatorString
     * 
     * @hibernate.property name="operatorString" column="LOGICAL_OPERATOR"
     *                     type="string" update="true" Insert="true"
     */
    public String getOperatorString() {
        return operatorString;
    }

    /**
     * @param operatorString the operatorString to set
     */
    public void setOperatorString(String operatorString) {
        this.operatorString = operatorString;
    }

    /**
     * @return integer value, that represents no. of parantheses sorrounding
     *         this connector.
     * @see edu.wustl.common.querysuite.queryobject.ILogicalConnector#getNestingNumber()
     * 
     * @hibernate.property name="nestingNumber" column="NESTING_NUMBER"
     *                     type="integer" update="true" insert="true"
     */
    public int getNestingNumber() {
        return nestingNumber;
    }

    /**
     * To set the nesting numbet for this Logical connector.
     * 
     * @param nestingNumber the nesting number.
     */
    public void setNestingNumber(int nestingNumber) {
        this.nestingNumber = nestingNumber;

    }

    /**
     * to increment nesting numeber by 1.
     * 
     */
    public void incrementNestingNumber() {
        nestingNumber++;
    }

    /**
     * 
     * @return hash code value for this object. It uses logicalOperator &
     *         nestingNumber for the hash code calculation.
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(compoundOperator).append(nestingNumber).toHashCode();
    }

    /**
     * @param obj The reference of object to be compared.
     * @return true if object specified is of same class, and locial operator &
     *         nesting number of two classes are equal.
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        boolean isEqual = false;

        if (this == obj) {
            isEqual = true;
        }

        if (!isEqual && (obj != null && this.getClass() == obj.getClass())) {
            Connector<?> logicalConnector = (Connector<?>) obj;
            if (this.compoundOperator != null && this.compoundOperator.equals(logicalConnector.compoundOperator)
                    && this.nestingNumber == logicalConnector.nestingNumber) {
                isEqual = true;
            }
        }

        return isEqual;
    }

    /**
     * @return String representation for this object as Logicaloperator:nesting
     *         number.
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "[" + compoundOperator + ":" + nestingNumber + "]";
    }

    // for hibernate
    private BinaryOperatorCompoundEnum<P> getCompoundOperator() {
        return compoundOperator;
    }

    private void setCompoundOperator(BinaryOperatorCompoundEnum<P> compoundOperator) {
        this.compoundOperator = compoundOperator;
    }

}