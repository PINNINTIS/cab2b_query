package edu.wustl.common.querysuite.queryobject;

public interface IConnector<T extends IBinaryOperator> {
    /**
     * To get the operator associated with this object.
     * 
     * @return the reference to the operator.
     */
    T getOperator();

    /**
     * To set the operator.
     * 
     * @param operator The operator to set.
     */
    void setOperator(T operator);

    /**
     * denotes no. of parantheses around this operator
     * 
     * @return integer value, that represents no. of parantheses sorrounding
     *         this connector.
     */
    int getNestingNumber();
}
