package edu.wustl.common.querysuite.queryobject.impl;

import edu.wustl.common.querysuite.queryobject.IOperation;
import edu.wustl.common.querysuite.queryobject.IQuery;


/**
 * @author vijay_pande
 * Base class for operation to be performed for composite query
 */
public class Operation implements IOperation
{
	/**
	 * identifier of the operation
	 */
	protected Long id;
	
	/**
	 * first operand of the operation
	 */
	protected IQuery operandOne;
	
	/**
	 * second operand of the operation
	 */
	protected IQuery operandTwo;
	
	
	/**
	 * Method to get identifier of the operation
	 * @return id of type Long
	 */
	public Long getId()
	{
		return id;
	}
	
	/**
	 * Method to set identifier of the operation
	 * @param id of type Long
	 */
	public void setId(Long id)
	{
		this.id = id;
	}
	
	/**
	 * Method to get first operand of the operation
	 * @return id of type Long
	 */
	public IQuery getOperandOne()
	{
		return operandOne;
	}
	
	/**
	 * Method to set first operand of the operation
	 * @param id of type Long
	 */
	public void setOperandOne(IQuery operandOne)
	{
		this.operandOne = operandOne;
	}
	
	/**
	 * Method to get second operand of the operation
	 * @return id of type Long
	 */
	public IQuery getOperandTwo()
	{
		return operandTwo;
	}
	
	/**
	 * Method to set second operand of the operation
	 * @param id of type Long
	 */
	public void setOperandTwo(IQuery operandTwo)
	{
		this.operandTwo = operandTwo;
	}
	
}
