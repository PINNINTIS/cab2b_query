package edu.wustl.common.querysuite.queryobject.impl;

import edu.wustl.common.querysuite.queryobject.IDescribable;
import edu.wustl.common.querysuite.queryobject.INameable;


/**
 * @author vijay_pande
 * Class created for the model changes for Composite Query. 
 */
public class AbstractQuery extends BaseQueryObject implements INameable, IDescribable
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

}
