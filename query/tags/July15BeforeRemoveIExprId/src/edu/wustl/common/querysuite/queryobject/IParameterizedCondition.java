/**
 * 
 */
package edu.wustl.common.querysuite.queryobject;

/**
 * @author chetan_patil
 * @created Aug 31, 2007, 1:39:30 PM
 */
public interface IParameterizedCondition extends ICondition {
    String getName();
    
    void setName(String name);
}
