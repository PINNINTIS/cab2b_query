package edu.wustl.common.querysuite.queryobject.impl;

import edu.wustl.common.querysuite.queryobject.ICompositeQuery;
import edu.wustl.common.querysuite.queryobject.IOperation;


/**
 * @author vijay_pande
 * Class for composite query
 */
public class CompositeQuery extends AbstractQuery implements ICompositeQuery
{
	/**
	 * Default serial version id
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * operation associated with the composite query
	 */
	private IOperation operation;

	
	/**
	 * Method to get operation associated with the composite query
	 * @return operation Object of type IOperation 
	 */
	public IOperation getOperation()
	{
		return operation;
	}

	/**
	 * Method to set operation associated with the composite query
	 * @param operation Object of type IOperation 
	 */
	public void setOperation(IOperation operation)
	{
		this.operation = operation;
	}
}
