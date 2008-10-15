/**
 * 
 */
package edu.wustl.common.querysuite.queryobject;

import java.util.Date;
import java.util.List;

/**
 * @author chetan_patil
 * @created Aug 31, 2007, 1:46:07 PM
 */
public interface IParameterizedQuery extends IQuery, INameable, IDescribable {
    List<IOutputAttribute> getOutputAttributeList();

    void setOutputAttributeList(List<IOutputAttribute> outputAttributeList);

    List<IParameter<?>> getParameters();
    
    Date getCreatedDate();
    
    void setCreatedDate(Date createdDate);
}
