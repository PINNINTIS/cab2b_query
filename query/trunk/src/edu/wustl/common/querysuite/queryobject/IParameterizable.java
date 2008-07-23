package edu.wustl.common.querysuite.queryobject;

/**
 * NOT TO BE USED by clients of the query API. This is for internal use ONLY.
 * 
 * @author srinath_k
 * 
 * @param <T>
 */
public interface IParameterizable<T extends IParameterizable<T>> {
    IParameter<T> getParameter();

    void setParameter(IParameter<T> parameter);
}
