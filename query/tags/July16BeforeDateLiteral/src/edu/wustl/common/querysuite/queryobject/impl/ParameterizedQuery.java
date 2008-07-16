/**
 * 
 */
package edu.wustl.common.querysuite.queryobject.impl;

import java.util.ArrayList;
import java.util.List;

import edu.wustl.common.querysuite.queryobject.IOutputAttribute;
import edu.wustl.common.querysuite.queryobject.IParameterizedQuery;
import edu.wustl.common.querysuite.queryobject.IQuery;

/**
 * @author chetan_patil
 * @created Aug 31, 2007, 4:22:00 PM
 * 
 * @hibernate.joined-subclass table="QUERY_PARAMETERIZED_QUERY"
 * @hibernate.joined-subclass-key column="IDENTIFIER" 
 */
public class ParameterizedQuery extends Query implements IParameterizedQuery {
    private static final long serialVersionUID = 1L;

    private List<IOutputAttribute> outputAttributeList = new ArrayList<IOutputAttribute>();

    private String name;

    private String description;

    /**
     * Default Constructor
     */
    public ParameterizedQuery() {

    }

    /**
     * Parameterized Constructor. This constructor will be used by Hibernate internally.
     * @param id
     * @param name
     * @param description
     */
    public ParameterizedQuery(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    /**
     * Parameterized Constructor
     * @param query
     */
    public ParameterizedQuery(IQuery query) {
        this.setConstraints(query.getConstraints());
    }

    /**
     * Parameterized Constructor
     * @param name
     * @param description
     * @param query
     */
    public ParameterizedQuery(IQuery query, String name, String description) {
        this.setConstraints(query.getConstraints());
        this.name = name;
        this.description = description;
    }

    /**
     * @see edu.wustl.common.querysuite.queryobject.IParameterizedQuery#addParameterizedCondition(edu.wustl.common.querysuite.queryobject.IParameterizedCondition)
     */
    public void addParameterizedCondition(String name, String description) {
        this.name = name;
        this.description = description;
    }

    /**
     * @see edu.wustl.common.querysuite.queryobject.IDescribable#getName()
     * 
     * @hibernate.property name="name" column="QUERY_NAME" type="string" length="255" unique="true" 
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

    /**
     * @see edu.wustl.common.querysuite.queryobject.IDescribable#getDescription()
     * 
     * @hibernate.property name="description" column="DESCRIPTION" type="string" length="1024"
     */
    public String getDescription() {
        return description;
    }

    /**
     * @see edu.wustl.common.querysuite.queryobject.IDescribable#setDescription()
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the outputAttributeList
     * 
     * @hibernate.list name="outputAttributeList" table="OUTPUT_ATTRIBUTES" cascade="all-delete-orphan" inverse="false" lazy="false"
     * @hibernate.collection-key column="PARAMETERIZED_QUERY_ID"
     * @hibernate.collection-index column="POSITION" type="int"
     * @hibernate.collection-one-to-many class="edu.wustl.common.querysuite.queryobject.impl.OutputAttribute"
     * @hibernate.cache usage="read-write"
     */
    public List<IOutputAttribute> getOutputAttributeList() {
        return outputAttributeList;
    }

    /**
     * @param outputAttributeList the outputAttributeList to set
     */
    public void setOutputAttributeList(List<IOutputAttribute> outputAttributeList) {
    	if (outputAttributeList==null)
    	{
    		outputAttributeList = new ArrayList<IOutputAttribute>();
    	}
        this.outputAttributeList = outputAttributeList;
    }

    /**
     * This method adds a given OutputAttribute into the OutputAtributeList.
     * @param outputAttribute
     * @return
     */
    public boolean addOutputAttribute(IOutputAttribute outputAttribute) {
        boolean isAdded = false;
        if (!outputAttributeList.contains(outputAttribute)) {
            outputAttributeList.add(outputAttribute);
            isAdded = true;
        }

        return isAdded;
    }

    /**
     * This method removes a given OutputAttribute from the OutputAtributeList
     * @param outputAttribute
     * @return
     */
    public boolean removeOutputAttribute(IOutputAttribute outputAttribute) {
        boolean isRemovevd = false;
        if (outputAttributeList.contains(outputAttribute)) {
            outputAttributeList.remove(outputAttribute);
            isRemovevd = true;
        }

        return isRemovevd;
    }

}
