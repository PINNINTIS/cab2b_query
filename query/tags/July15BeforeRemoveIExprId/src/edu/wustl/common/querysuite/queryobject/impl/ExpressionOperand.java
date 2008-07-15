package edu.wustl.common.querysuite.queryobject.impl;

import edu.wustl.common.querysuite.queryobject.IExpressionOperand;

/**
 * 
 * @author chetan_patil
 * @created Aug 10, 2007, 8:04:56 PM
 * 
 * @hibernate.class table="QUERY_EXPRESSION_OPERAND"
 * @hibernate.cache usage="read-write"
 * @hibernate.mapping auto-import="false" 
 */
public abstract class ExpressionOperand extends BaseQueryObject implements IExpressionOperand {

    /**
     * Returns the identifier assigned to BaseQueryObject.
     * @return a unique id assigned to the Condition.
     * 
     * @hibernate.id name="id" column="IDENTIFIER" type="long" length="30" unsaved-value="null" generator-class="native"
     * @hibernate.generator-param name="sequence" value="EXPRESSION_OPERAND_SEQ"
     */
    public Long getId() {
        return id;
    }

}
