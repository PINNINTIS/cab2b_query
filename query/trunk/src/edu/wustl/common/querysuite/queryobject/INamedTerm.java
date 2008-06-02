package edu.wustl.common.querysuite.queryobject;

public interface INamedTerm extends IBaseQueryObject {

    ITerm getTerm();

    void setTerm(ITerm term);

    String getName();

    void setName(String name);
}
