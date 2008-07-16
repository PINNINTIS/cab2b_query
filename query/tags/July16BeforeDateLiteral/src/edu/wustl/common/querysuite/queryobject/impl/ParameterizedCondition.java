/**
 * 
 */
package edu.wustl.common.querysuite.queryobject.impl;

import java.util.List;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.wustl.common.querysuite.queryobject.ICondition;
import edu.wustl.common.querysuite.queryobject.IParameterizedCondition;
import edu.wustl.common.querysuite.queryobject.RelationalOperator;

/**
 * @author chetan_patil
 * @created Aug 31, 2007, 1:55:48 PM
 * 
 * @hibernate.joined-subclass table="QUERY_PARAMETERIZED_CONDITION"
 * @hibernate.joined-subclass-key column="IDENTIFIER" 
 */
public class ParameterizedCondition extends Condition implements IParameterizedCondition {
    private static final long serialVersionUID = 1L;

    private Integer index;

    private String name;

    /**
     * Default Constructor
     */
    public ParameterizedCondition() {

    }

    /**
     * Parameterized Constructor
     * @param condition
     */
    public ParameterizedCondition(ICondition condition) {
        super(condition.getAttribute(), condition.getRelationalOperator(), condition.getValues());
    }

    /**
     * Parameterized Constructor
     * @param condition
     * @param index
     * @param name
     */
    public ParameterizedCondition(ICondition condition, Integer index, String name) {
        super(condition.getAttribute(), condition.getRelationalOperator(), condition.getValues());
        this.index = index;
        this.name = name;
    }

    /**
     * Parameterized Constructor
     * @param attribute
     * @param relationalOperator
     * @param values
     * @param index
     * @param name
     * @param description
     */
    public ParameterizedCondition(
            AttributeInterface attribute,
            RelationalOperator relationalOperator,
            List<String> values,
            Integer index,
            String name) {
        super(attribute, relationalOperator, values);
        this.index = index;
        this.name = name;
    }

    /**
     * @see edu.wustl.common.querysuite.queryobject.IParameterizedCondition#getIndex()
     * 
     * @hibernate.property name="index" column="CONDITION_INDEX" type="integer" 
     */
    public Integer getIndex() {
        return index;
    }

    /**
     * @see edu.wustl.common.querysuite.queryobject.IParameterizedCondition#setIndex(java.lang.Integer)
     */
    public void setIndex(Integer index) {
        this.index = index;
    }

    /**
     * @see edu.wustl.common.querysuite.queryobject.IDescribable#getName()
     * 
     * @hibernate.property name="name" column="CONDITION_NAME" type="string" length="255"
     */
    public String getName() {
        return name;
    }

    /**
     * @see edu.wustl.common.querysuite.queryobject.IDescribable#setName(java.lang.String)
     */
    public void setName(String name) {
        this.name = name;
    }

}
