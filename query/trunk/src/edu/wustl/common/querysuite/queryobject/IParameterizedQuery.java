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

    @Deprecated// added setter method for list of outputAttribute ojects
    boolean addOutputAttribute(IOutputAttribute outputAttribute);
    
    @Deprecated// added setter method for list of outputAttribute ojects
    boolean removeOutputAttribute(IOutputAttribute outputAttribute);
    
    public List<IOutputAttribute> getOutputAttributeList() ;

    public void setOutputAttributeList(List<IOutputAttribute> outputAttributeList);

}
