package edu.wustl.common.querysuite.queryobject;


/**
 * @author vijay_pande
 *
 */
public interface IAbstractQuery extends IBaseQueryObject, INameable, IDescribable
{
	public String getType();
	public void setType(String type);	
}
