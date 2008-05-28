package edu.wustl.common.querysuite.queryobject.impl;

import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import edu.wustl.common.querysuite.exceptions.MultipleRootsException;
import edu.wustl.common.querysuite.queryobject.IConstraints;
import edu.wustl.common.querysuite.queryobject.IExpression;
import edu.wustl.common.querysuite.queryobject.IExpressionId;
import edu.wustl.common.querysuite.queryobject.IJoinGraph;
import edu.wustl.common.querysuite.queryobject.IQueryEntity;

/**
 * @author Mandar Shidhore
 * @author chetan_patil
 * @version 1.0
 * @created 12-Oct-2006 13.35.04 AM
 * @updated Aug 10, 2007, 12:22:17 PM
 *
 * @hibernate.class table="QUERY_CONSTRAINTS"
 * @hibernate.cache usage="read-write"
 */
public class Constraints extends BaseQueryObject implements IConstraints {
    private static final long serialVersionUID = 6169601255945564445L;

    private Map<IExpressionId, IExpression> expressions = new HashMap<IExpressionId, IExpression>();

    private Collection<IExpression> expressionCollection = new HashSet<IExpression>();

    private IJoinGraph joinGraph = new JoinGraph();

    private int currentExpressionId = 0;

    /**
     * Default Constructor
     */
    public Constraints() {

    }

    /**
     * Returns the identifier assigned to BaseQueryObject.
     * @return a unique id assigned to the Condition.
     * 
     * @hibernate.id name="id" column="IDENTIFIER" type="long" length="30" unsaved-value="null" generator-class="native"
     * @hibernate.generator-param name="sequence" value="CONSTRAINT_SEQ"
     */
    public Long getId() {
        return id;
    }

    /**
     * This method returns the List of IExpression associated with this Constraint.
     * This method will only be used by Hibernate to save into the database.
     * @return the expressionCollection
     * 
     * @hibernate.set name="expressionCollection" cascade="all-delete-orphan" inverse="false" lazy="false"
     * @hibernate.collection-key column="QUERY_CONSTRAINT_ID"
     * @hibernate.collection-one-to-many class="edu.wustl.common.querysuite.queryobject.impl.Expression"
     * @hibernate.cache usage="read-write"
     */
    private Collection<IExpression> getExpressionCollection() {
        return expressionCollection;
    }

    /**
     * This method sets the List of IExpression associated with this Constraint.
     * This method will only be used by Hibernate to restore form the database.
     * @param expressionCollection the expressionCollection to set
     */
    private void setExpressionCollection(Collection<IExpression> expressionCollection) {
        this.expressionCollection = expressionCollection;
    }

    /**
     * This method refreshes the expression collection with the entries in expressions map.
     * This method is used during pre-processing query before persisting it.
     */
    public void contemporizeExpressionListWithExpressions() {
        expressionCollection.clear();

        for (IExpression expression : expressions.values()) {
            expressionCollection.add(expression);
        }
    }

    /**
     * This method refreshes the expressions map with the entries in expression collection.
     * This method is used during post-processing query after retrieving it.
     */
    public void contemporizeExpressionsWithExpressionList() {
       expressions.clear();

        for (IExpression expression : expressionCollection) {
            expressions.put(expression.getExpressionId(), expression);

            int expressionId = expression.getExpressionId().getInt();
            if (expressionId > currentExpressionId) {
                currentExpressionId = expressionId;
            }
        }
    }

    /**
	 * Name: Abhishek Mehta
	 * Reviewer Name : Deepti 
	 * Bug ID: 5661
	 * Patch ID: 5661_4
	 * See also: 1-7 
	 * Description : Making cascade all-delete-orphan from cascade all
	 */
    
    /**
     * This method returns the JoinGraph of this Constraint
     * @return the reference to joingraph.
     * @see edu.wustl.common.querysuite.queryobject.IConstraints#getJoinGraph()
     * 
     * @hibernate.many-to-one column="QUERY_JOIN_GRAPH_ID" unique="true" class="edu.wustl.common.querysuite.queryobject.impl.JoinGraph" cascade="all-delete-orphan" lazy="false"
     */
    public IJoinGraph getJoinGraph() {
        return joinGraph;
    }

    /**
     * @param joinGraph the joinGraph to set
     */
    public void setJoinGraph(IJoinGraph joinGraph) {
        this.joinGraph = joinGraph;
    }

    /**
     * @return the root expression of the join graph.
     * @throws MultipleRootsException When there exists multiple roots in joingraph.
     * @see edu.wustl.common.querysuite.queryobject.IConstraints#getRootExpressionId()
     */
    public IExpressionId getRootExpressionId() throws MultipleRootsException {
        return joinGraph.getRoot();
    }

    /**
     * @param constraintEntity the constraint Entity for which the new expr is created.
     * @return the newly created expression.
     * @see edu.wustl.common.querysuite.queryobject.IConstraints#addExpression(edu.wustl.common.querysuite.queryobject.IQueryEntity)
     */
    public IExpression addExpression(IQueryEntity constraintEntity) {
        IExpression expression = new Expression(constraintEntity, ++currentExpressionId);

        expressions.put(expression.getExpressionId(), expression);

        ((JoinGraph) joinGraph).addIExpressionId(expression.getExpressionId());
        return expression;
    }

    /**
     * @return an enumeration of the expressions' ids.
     * @see edu.wustl.common.querysuite.queryobject.IConstraints#getExpressionIds()
     */
    public Enumeration<IExpressionId> getExpressionIds() {
        Set<IExpressionId> set = expressions.keySet();
        Vector<IExpressionId> vector = new Vector<IExpressionId>(set);
        return vector.elements();
    }

    /**
     * @param expressionId the id of the expression to be removed.
     * @return the removed expression.
     * @see edu.wustl.common.querysuite.queryobject.IConstraints#removeExpressionWithId(edu.wustl.common.querysuite.queryobject.IExpressionId)
     */
    public IExpression removeExpressionWithId(IExpressionId expressionId) {
        JoinGraph theJoinGraph = (JoinGraph) joinGraph;

        List<IExpressionId> parents = theJoinGraph.getParentList(expressionId);
        for (int i = 0; i < parents.size(); i++) {
            IExpression parentExpression = expressions.get(parents.get(i));
            parentExpression.removeOperand(expressionId);
        }
        theJoinGraph.removeIExpressionId(expressionId);

        return expressions.remove(expressionId);
    }

    /**
     * @param id the id (usually obtained from getExpressionIds)
     * @return the reference to the IExpression associatied with the given IExpressionId.
     * @see edu.wustl.common.querysuite.queryobject.IConstraints#getExpression(edu.wustl.common.querysuite.queryobject.IExpressionId)
     */
    public IExpression getExpression(IExpressionId id) {
        return expressions.get(id);
    }

    /**
     * TO get the Set of all ConstraintEntites present in the Constraints object.
     * @return Set of all Constraint Entities.
     * @see edu.wustl.common.querysuite.queryobject.IConstraints#getQueryEntities()
     */
    public Set<IQueryEntity> getQueryEntities() {
        Set<IQueryEntity> constraintEntitySet = new HashSet<IQueryEntity>();
        Collection<IExpression> allExpressions = expressions.values();
        for (IExpression expression : allExpressions) {
            constraintEntitySet.add(expression.getQueryEntity());
        }
        return constraintEntitySet;
    }

}