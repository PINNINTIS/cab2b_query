/**
 * 
 */
package edu.wustl.common.querysuite.queryobject;

import java.util.List;

/**
 * @author chetan_patil
 * @created Aug 31, 2007, 1:46:07 PM
 */
public interface IParameterizedQuery extends IQuery {

    String getName();

    void setName(String name);

    String getDescription();

    void setDescription(String description);

    List<IOutputAttribute> getOutputAttributeList();

    void setOutputAttributeList(List<IOutputAttribute> outputAttributeList);

    List<IOutputTerm> getOutputTerms();

    List<IParameter<?>> getParameters();
}
