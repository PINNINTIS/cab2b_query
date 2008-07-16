package edu.wustl.common.querysuite.queryobject;

/**
 * Represents a term that can be present in the output of a query. An output
 * term can be given a meaningful name (possibly by the user).
 * 
 * @author srinath_k
 * @see IQuery#getOutputTerms()
 */
public interface IOutputTerm extends IBaseQueryObject {

    ITerm getTerm();

    void setTerm(ITerm term);

    String getName();

    void setName(String name);
}
