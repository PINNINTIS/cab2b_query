package edu.wustl.common.querysuite.queryobject;


/**
 * @author vijay_pande
 * Interface to declare operations for composite query operation
 */
public interface IOperation
{
	/**
	 * Method to get id of the operation
	 * @return id identifier object of Long
	 */
	public Long getId();
	
	/**
	 * Method to get first operand for operation
	 * @return query object of type IQuery
	 */
	public IAbstractQuery getOperandOne();
	
	/**
	 * Method to get second operand for operation
	 * @return query object of type IQuery
	 */
	public IAbstractQuery getOperandTwo();
}
