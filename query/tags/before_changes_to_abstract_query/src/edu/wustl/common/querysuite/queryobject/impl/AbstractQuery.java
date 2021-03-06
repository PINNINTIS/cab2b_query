package edu.wustl.common.querysuite.queryobject.impl;

import edu.wustl.common.querysuite.queryobject.IAbstractQuery;
import edu.wustl.common.querysuite.queryobject.IDescribable;
import edu.wustl.common.querysuite.queryobject.INameable;


/**
 * @author vijay_pande
 * Class created for the model changes for Composite Query. 
 */
public class AbstractQuery extends BaseQueryObject implements IAbstractQuery
{
	/**
	 * Default serial version id
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Name of the query
	 */
	protected String name;
	
	/**
	 * Type of the query
	 */
	protected String type;
	
	/**
	 * Description of the query
	 */
	protected String description;
	
    /**
     * @see edu.wustl.common.querysuite.queryobject.IDescribable#getName()
     * 
     * @hibernate.property name="name" column="QUERY_NAME" type="string"
     *                     length="255" unique="true"
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
     * @hibernate.property name="description" column="DESCRIPTION" type="string"
     *                     length="1024"
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
	 * @hibernate.property name="type" column="DESCRIPTION" type="string"
     *                     length="255"
	 * @return the type
	 */
	public String getType()
	{
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type)
	{
		this.type = type;
	}
}
