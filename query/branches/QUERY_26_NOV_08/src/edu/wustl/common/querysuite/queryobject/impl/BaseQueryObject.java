/**
 * 
 */
package edu.wustl.common.querysuite.queryobject.impl;

import edu.wustl.common.querysuite.queryobject.IBaseQueryObject;

/**
 * This is the base class for all the Query objects. Hence all Query objects must extend this class.
 *
 * @author chetan_patil
 * @created Aug 7, 2007
 */
public class BaseQueryObject implements IBaseQueryObject {
    private static final long serialVersionUID = 1L;

    /**
     * Internally generated identifier.
     */
    protected Long id;

    public Long getId() {
        return id;
    }

    /**
     * Sets the given id to the domain object
     */
    final public void setId(Long id) {
        this.id = id;
    }
}
