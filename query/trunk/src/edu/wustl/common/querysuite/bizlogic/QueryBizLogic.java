package edu.wustl.common.querysuite.bizlogic;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;

import edu.common.dynamicextensions.domaininterface.AssociationInterface;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.cab2b.common.cache.AbstractEntityCache;
import edu.wustl.common.hibernate.HibernateDatabaseOperations;
import edu.wustl.common.querysuite.exceptions.CyclicException;
import edu.wustl.common.querysuite.metadata.associations.IAssociation;
import edu.wustl.common.querysuite.metadata.associations.impl.InterModelAssociation;
import edu.wustl.common.querysuite.metadata.associations.impl.IntraModelAssociation;
import edu.wustl.common.querysuite.queryobject.ICondition;
import edu.wustl.common.querysuite.queryobject.IConnector;
import edu.wustl.common.querysuite.queryobject.IExpressionId;
import edu.wustl.common.querysuite.queryobject.IExpressionOperand;
import edu.wustl.common.querysuite.queryobject.IOutputAttribute;
import edu.wustl.common.querysuite.queryobject.IParameterizedQuery;
import edu.wustl.common.querysuite.queryobject.IRule;
import edu.wustl.common.querysuite.queryobject.LogicalOperator;
import edu.wustl.common.querysuite.queryobject.RelationalOperator;
import edu.wustl.common.querysuite.queryobject.impl.Condition;
import edu.wustl.common.querysuite.queryobject.impl.Connector;
import edu.wustl.common.querysuite.queryobject.impl.Constraints;
import edu.wustl.common.querysuite.queryobject.impl.Expression;
import edu.wustl.common.querysuite.queryobject.impl.GraphEntry;
import edu.wustl.common.querysuite.queryobject.impl.JoinGraph;
import edu.wustl.common.querysuite.queryobject.impl.OutputAttribute;
import edu.wustl.common.querysuite.queryobject.impl.ParameterizedQuery;
import edu.wustl.common.querysuite.queryobject.impl.QueryEntity;
import edu.wustl.common.querysuite.queryobject.impl.Rule;

/**
 * This class processes the Query object before persisting and after retreival.
 * Note that this class assumes that {@link AbstractEntityCache} has already
 * been initialized.
 * 
 * @author chetan_patil
 * @created Aug 30, 2007, 11:31:45 AM
 * @param
 *          <Q>
 */
public class QueryBizLogic<Q extends IParameterizedQuery> {

    /**
     * Default Constructor
     */
    public QueryBizLogic() {

    }

    /**
     * This method returns the name of the name of the class.
     * 
     * @return
     */
    protected String getQueryClassName() {
        return ParameterizedQuery.class.getName();
    }

    /**
     * This method persists the Query object
     * 
     * @param query
     * @throws RemoteException
     */
    public final void saveQuery(Q query) throws RemoteException {
        try {
            preProcessQuery(query);
            new HibernateDatabaseOperations<Q>().insert(query);
        } catch (Exception e) {
            throw new RuntimeException("Unable to save query, Exception:" + e.getMessage());
        }
    }

    /**
     * This method persists the Query object
     * 
     * @param query
     * @throws RemoteException
     */
    public final void updateQuery(Q query) throws RemoteException {
        try {
            preProcessQuery(query);
            new HibernateDatabaseOperations<Q>().update(query);
        } catch (Exception e) {
            throw new RuntimeException("Unable to update query, Exception:" + e.getMessage());
        }
    }

    /**
     * This method retrieves the query object given its identifier.
     * 
     * @param categoryId Id of the category
     * @return The Category for given id.
     * @throws RemoteException EBJ specific Exception
     */
    public Q getQueryById(Long queryId) throws RemoteException {
        List<Q> queryList = new HibernateDatabaseOperations<Q>().retrieve(getQueryClassName(), "id", queryId);

        Q query = null;
        if (queryList != null && !queryList.isEmpty()) {
            if (queryList.size() > 1) {
                throw new RuntimeException("Problem in code; probably database schema");
            } else {
                query = (Q) queryList.get(0);
            }
        }
        postProcessQuery(query);
        return query;
    }

    /**
     * This method retrieves all the query objects in the system. Returns all
     * the categories available in the system.
     * 
     * @return List of all categories.
     */
    public List<Q> getAllQueries() throws RemoteException {
        List<Q> queryList = new HibernateDatabaseOperations<Q>().retrieve(getQueryClassName());
        for (Q query : queryList) {
            postProcessQuery(query);
        }
        return queryList;
    }

    /**
     * This method processes the query object after retreival.
     * 
     * @param query
     */
    public void postProcessQuery(Q query) {
        Constraints constraints = (Constraints) query.getConstraints();
        constraints.contemporizeExpressionsWithExpressionList();

        Enumeration<IExpressionId> enumeration = constraints.getExpressionIds();
        while (enumeration.hasMoreElements()) {
            IExpressionId expressionId = enumeration.nextElement();
            Expression expression = (Expression) constraints.getExpression(expressionId);
            postProcessExpression(expression);
        }

        JoinGraph joinGraph = (JoinGraph) constraints.getJoinGraph();
        postProcessJoinGraph(joinGraph, constraints.getExpressionIds());
        if (query instanceof ParameterizedQuery) {
            postProcessParameterizedQuery((ParameterizedQuery) query);
        }
    }

    /**
     * This method processes the parameterized query object after retreival.
     * 
     * @param query
     */
    private void postProcessParameterizedQuery(ParameterizedQuery parameterizedQuery) {
        AbstractEntityCache abstractEntityCache = AbstractEntityCache.getCache();

        List<IOutputAttribute> outputAttributeList = parameterizedQuery.getOutputAttributeList();
        for (IOutputAttribute outputAttribute : outputAttributeList) {
            OutputAttribute opAttribute = (OutputAttribute) outputAttribute;

            Long attributeId = opAttribute.getAttributeId();
            AttributeInterface attribute = abstractEntityCache.getAttributeById(attributeId);
            opAttribute.setAttribute(attribute);
        }

    }

    /**
     * This method processes the expression object after retreival.
     * 
     * @param expression
     */
    private void postProcessExpression(Expression expression) {
        QueryEntity queryEntity = (QueryEntity) expression.getQueryEntity();
        postProcessQueryEntity(queryEntity);

        List<IExpressionOperand> expressionOperands = expression.getExpressionOperands();
        for (IExpressionOperand expressionOperand : expressionOperands) {
            if (!expressionOperand.isSubExpressionOperand()) {
                Rule rule = (Rule) expressionOperand;
                postProcessRule(rule);
            }
        }

        Collection<IConnector<LogicalOperator>> logicalConnectorCollection = expression.getLogicalConnectors();
        for (IConnector<LogicalOperator> logicalConnector : logicalConnectorCollection) {
            Connector logicalConnectorImpl = (Connector) logicalConnector;
            String operatorString = logicalConnectorImpl.getOperatorString();
            logicalConnectorImpl.setOperator(LogicalOperator.getLogicalOperator(operatorString));
        }
    }

    /**
     * This method processes the JoinGraph object after retreival.
     * 
     * @param joinGraph
     * @param enumeration
     */
    private void postProcessJoinGraph(JoinGraph joinGraph, Enumeration<IExpressionId> enumeration) {
        Collection<GraphEntry> graphEntryList = joinGraph.getGraphEntryCollection();

        if (graphEntryList.isEmpty()) {
            while (enumeration.hasMoreElements()) {
                IExpressionId expressionId = enumeration.nextElement();
                joinGraph.addIExpressionId(expressionId);
            }
        } else {
            for (GraphEntry graphEntry : graphEntryList) {
                IAssociation association = graphEntry.getAssociation();
                postProcessAssociation(association);

                IExpressionId sourceExpressionId = graphEntry.getSourceExpressionId();
                IExpressionId targetExpressionId = graphEntry.getTargetExpressionId();
                try {
                    joinGraph.putAssociation(sourceExpressionId, targetExpressionId, association);
                } catch (CyclicException e) {
                    throw new RuntimeException("Unable to Process object, Exception:" + e.getMessage());
                }
            }
        }
    }

    /**
     * This method processes the QueryEntity object after retreival.
     * 
     * @param queryEntity
     */
    private void postProcessQueryEntity(QueryEntity queryEntity) {
        Long entityId = queryEntity.getEntityId();
        AbstractEntityCache abstractEntityCache = AbstractEntityCache.getCache();
        EntityInterface entity = abstractEntityCache.getEntityById(entityId);
        queryEntity.setDynamicExtensionsEntity(entity);
    }

    /**
     * This method processes the Rule object after retreival.
     * 
     * @param rule
     */
    private void postProcessRule(IRule rule) {
        List<ICondition> conditions = rule.getConditions();
        if (!conditions.isEmpty()) {
            AbstractEntityCache abstractEntityCache = AbstractEntityCache.getCache();

            for (ICondition condition : conditions) {
                Condition conditionImpl = (Condition) condition;

                Long attributeId = conditionImpl.getAttributeId();
                AttributeInterface attribute = abstractEntityCache.getAttributeById(attributeId);
                conditionImpl.setAttribute(attribute);

                String relationalOperatorString = conditionImpl.getRelationalOperatorString();
                if (relationalOperatorString != null) {
                    RelationalOperator relationalOperator = RelationalOperator
                            .getOperatorForStringRepresentation(relationalOperatorString);
                    conditionImpl.setRelationalOperator(relationalOperator);
                }
            }
        }
    }

    /**
     * This method processes the Association object after retrieval.
     * 
     * @param association
     */
    private void postProcessAssociation(IAssociation association) {
        AbstractEntityCache abstractEntityCache = AbstractEntityCache.getCache();

        if (association instanceof InterModelAssociation) {
            InterModelAssociation interModelAssociation = (InterModelAssociation) association;

            Long sourceAttributeId = interModelAssociation.getSourceAttributeId();
            AttributeInterface sourceAttribute = abstractEntityCache.getAttributeById(sourceAttributeId);
            interModelAssociation.setSourceAttribute(sourceAttribute);

            Long targetAttributeId = interModelAssociation.getTargetAttributeId();
            AttributeInterface targetAttribute = abstractEntityCache.getAttributeById(targetAttributeId);
            interModelAssociation.setTargetAttribute(targetAttribute);

        } else if (association instanceof IntraModelAssociation) {
            IntraModelAssociation intraModelAssociation = (IntraModelAssociation) association;

            Long deAssociationId = intraModelAssociation.getDynamicExtensionsAssociationId();
            AssociationInterface deAssociation = abstractEntityCache.getAssociationById(deAssociationId);
            intraModelAssociation.setDynamicExtensionsAssociation(deAssociation);
        }
    }

    /**
     * @param query
     */
    public void preProcessQuery(Q query) {
        Constraints constraints = (Constraints) query.getConstraints();
        constraints.contemporizeExpressionListWithExpressions();

        Enumeration<IExpressionId> enumeration = constraints.getExpressionIds();
        while (enumeration.hasMoreElements()) {
            IExpressionId expressionId = enumeration.nextElement();
            Expression expression = (Expression) constraints.getExpression(expressionId);
            preProcessExpression(expression);
        }

        JoinGraph joinGraph = (JoinGraph) constraints.getJoinGraph();
        joinGraph.contemporizeGraphEntryCollectionWithJoinGraph();

        Collection<GraphEntry> graphEntryCollection = joinGraph.getGraphEntryCollection();
        for (GraphEntry graphEntry : graphEntryCollection) {
            IAssociation association = graphEntry.getAssociation();
            preProcessAssociation(association);
        }

        if (query instanceof ParameterizedQuery) {
            preProcessParameterizedQuery((ParameterizedQuery) query);
        }
    }

    /**
     * This method processes the parameterized query object before saving.
     * 
     * @param query
     */
    private void preProcessParameterizedQuery(ParameterizedQuery parameterizedQuery) {
        List<IOutputAttribute> outputAttributeList = parameterizedQuery.getOutputAttributeList();
        for (IOutputAttribute outputAttribute : outputAttributeList) {
            AttributeInterface attribute = outputAttribute.getAttribute();
            ((OutputAttribute) outputAttribute).setAttributeId(attribute.getId());
        }
    }

    /**
     * This method processes the Expression object before persisting it.
     * 
     * @param expression
     */
    private void preProcessExpression(Expression expression) {
        QueryEntity queryEntity = (QueryEntity) expression.getQueryEntity();
        preProcessQueryEntity(queryEntity);

        List<IExpressionOperand> expressionOperands = expression.getExpressionOperands();
        for (IExpressionOperand expressionOperand : expressionOperands) {
            if (!expressionOperand.isSubExpressionOperand()) {
                Rule rule = (Rule) expressionOperand;
                preProcessRule(rule);
            }
        }

        Collection<IConnector<LogicalOperator>> logicalConnectorCollection = expression.getLogicalConnectors();
        for (IConnector<LogicalOperator> logicalConnector : logicalConnectorCollection) {
            LogicalOperator logicalOperator = logicalConnector.getOperator();
            ((Connector) logicalConnector).setOperatorString(logicalOperator.getOperatorString());
        }
    }

    /**
     * This method processes the QueryEntity object before persisting it.
     * 
     * @param queryEntity
     */
    private void preProcessQueryEntity(QueryEntity queryEntity) {
        EntityInterface entity = queryEntity.getEntityInterface();
        queryEntity.setEntityId(entity.getId());
    }

    /**
     * This method processes the Rule object before persisting it.
     * 
     * @param rule
     */
    private void preProcessRule(IRule rule) {
        List<ICondition> conditions = rule.getConditions();
        if (!conditions.isEmpty()) {
            for (ICondition condition : conditions) {
                Condition conditionImpl = (Condition) condition;

                AttributeInterface attribute = condition.getAttribute();
                conditionImpl.setAttributeId(attribute.getId());

                RelationalOperator relationalOperator = conditionImpl.getRelationalOperator();
                conditionImpl.setRelationalOperatorString(relationalOperator.getStringRepresentation());
            }
        }
    }

    /**
     * This method processes the Association object before persisting it.
     * 
     * @param association
     */
    private void preProcessAssociation(IAssociation association) {
        if (association instanceof IntraModelAssociation) {
            IntraModelAssociation intraModelAssociation = (IntraModelAssociation) association;
            AssociationInterface dynamicExtensionsAssociation = intraModelAssociation
                    .getDynamicExtensionsAssociation();
            intraModelAssociation.setDynamicExtensionsAssociationId(dynamicExtensionsAssociation.getId());
        } else if (association instanceof InterModelAssociation) {
            InterModelAssociation interModelAssociation = (InterModelAssociation) association;

            AttributeInterface sourceAttribute = interModelAssociation.getSourceAttribute();
            interModelAssociation.setSourceAttributeId(sourceAttribute.getId());

            AttributeInterface targetAttribute = interModelAssociation.getTargetAttribute();
            interModelAssociation.setTargetAttributeId(targetAttribute.getId());
        }
    }

    public void delete(Q query) {
        // TODO check this.
        new HibernateDatabaseOperations<Q>().delete(query);
    }
}
