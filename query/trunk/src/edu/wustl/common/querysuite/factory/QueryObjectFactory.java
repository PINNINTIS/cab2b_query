package edu.wustl.common.querysuite.factory;

import java.util.ArrayList;
import java.util.List;

import edu.common.dynamicextensions.domaininterface.AssociationInterface;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.AttributeTypeInformationInterface;
import edu.common.dynamicextensions.domaininterface.DateTypeInformationInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.domaininterface.NumericTypeInformationInterface;
import edu.wustl.common.querysuite.metadata.associations.IIntraModelAssociation;
import edu.wustl.common.querysuite.metadata.associations.impl.IntraModelAssociation;
import edu.wustl.common.querysuite.queryobject.ArithmeticOperator;
import edu.wustl.common.querysuite.queryobject.DSInterval;
import edu.wustl.common.querysuite.queryobject.ICondition;
import edu.wustl.common.querysuite.queryobject.IConnector;
import edu.wustl.common.querysuite.queryobject.IConstraints;
import edu.wustl.common.querysuite.queryobject.ICustomFormula;
import edu.wustl.common.querysuite.queryobject.IDateOffsetAttribute;
import edu.wustl.common.querysuite.queryobject.IDateOffsetLiteral;
import edu.wustl.common.querysuite.queryobject.IExpressionAttribute;
import edu.wustl.common.querysuite.queryobject.IExpressionId;
import edu.wustl.common.querysuite.queryobject.ILiteral;
import edu.wustl.common.querysuite.queryobject.IOutputTerm;
import edu.wustl.common.querysuite.queryobject.IOutputAttribute;
import edu.wustl.common.querysuite.queryobject.IOutputEntity;
import edu.wustl.common.querysuite.queryobject.IParameterizedCondition;
import edu.wustl.common.querysuite.queryobject.IParameterizedQuery;
import edu.wustl.common.querysuite.queryobject.IQuery;
import edu.wustl.common.querysuite.queryobject.IQueryEntity;
import edu.wustl.common.querysuite.queryobject.IRule;
import edu.wustl.common.querysuite.queryobject.ITerm;
import edu.wustl.common.querysuite.queryobject.ITimeIntervalEnum;
import edu.wustl.common.querysuite.queryobject.LogicalOperator;
import edu.wustl.common.querysuite.queryobject.OutputTerm;
import edu.wustl.common.querysuite.queryobject.RelationalOperator;
import edu.wustl.common.querysuite.queryobject.TermType;
import edu.wustl.common.querysuite.queryobject.YMInterval;
import edu.wustl.common.querysuite.queryobject.impl.Condition;
import edu.wustl.common.querysuite.queryobject.impl.Connector;
import edu.wustl.common.querysuite.queryobject.impl.Constraints;
import edu.wustl.common.querysuite.queryobject.impl.CustomFormula;
import edu.wustl.common.querysuite.queryobject.impl.DateOffsetAttribute;
import edu.wustl.common.querysuite.queryobject.impl.DateOffsetLiteral;
import edu.wustl.common.querysuite.queryobject.impl.ExpressionAttribute;
import edu.wustl.common.querysuite.queryobject.impl.ExpressionId;
import edu.wustl.common.querysuite.queryobject.impl.Literal;
import edu.wustl.common.querysuite.queryobject.impl.OutputAttribute;
import edu.wustl.common.querysuite.queryobject.impl.OutputEntity;
import edu.wustl.common.querysuite.queryobject.impl.ParameterizedCondition;
import edu.wustl.common.querysuite.queryobject.impl.ParameterizedQuery;
import edu.wustl.common.querysuite.queryobject.impl.Query;
import edu.wustl.common.querysuite.queryobject.impl.QueryEntity;
import edu.wustl.common.querysuite.queryobject.impl.Rule;
import edu.wustl.common.querysuite.queryobject.impl.Term;

/**
 * factory to create the query objects, query engine etc...
 * 
 * @version 1.0
 * @updated 11-Oct-2006 02:57:23 PM
 */
public abstract class QueryObjectFactory {

    /**
     * To instantiate Logical connector.
     * 
     * @param logicalOperator The Logical operator that connector will hold.
     * @return a logical connector
     */
    public static IConnector<LogicalOperator> createLogicalConnector(LogicalOperator logicalOperator) {
        return new Connector<LogicalOperator>(logicalOperator);
    }

    /**
     * To instantiate Logical connector.
     * 
     * @param logicalOperator The Logical operator that connector will hold.
     * @param nestingNumber The nesting number, that represents no. of
     *            parantheses sorrounding this connector.
     * @return a logical connector
     */
    public static IConnector<LogicalOperator> createLogicalConnector(LogicalOperator logicalOperator,
            int nestingNumber) {
        return new Connector<LogicalOperator>(logicalOperator, nestingNumber);
    }

    /**
     * To instantiate Arithmetic connector.
     * 
     * @param arithmeticOperator The Arithmetic operator that connector will
     *            hold.
     * @return an arithmetic connector
     */
    public static IConnector<ArithmeticOperator> createArithmeticConnector(ArithmeticOperator arithmeticOperator) {
        return new Connector<ArithmeticOperator>(arithmeticOperator);
    }

    /**
     * To instantiate Arithmetic connector.
     * 
     * @param arithmeticOperator The Arithmetic operator that connector will
     *            hold.
     * @param nestingNumber The nesting number, that represents no. of
     *            parantheses sorrounding this connector.
     * @return an arithmetic connector
     */
    public static IConnector<ArithmeticOperator> createArithmeticConnector(ArithmeticOperator arithmeticOperator,
            int nestingNumber) {
        return new Connector<ArithmeticOperator>(arithmeticOperator, nestingNumber);
    }

    /**
     * To instantiate Condition object.
     * 
     * @param attribute The reference to Dynamic Extension attribute on which
     *            condition to be created.
     * @param relationalOperator The relational operator between attribute &
     *            values.
     * @param values The List of String representing values of the condition.
     * @return The instance of the Condition class.
     */
    public static ICondition createCondition(AttributeInterface attribute, RelationalOperator relationalOperator,
            List<String> values) {
        return new Condition(attribute, relationalOperator, values);
    }

    /**
     * To create an empty Condition.
     * 
     * @return The instance of the Condition class.
     */
    public static ICondition createCondition() {
        return new Condition(null, null, null);
    }

    /**
     * To create an empty Condition.
     * 
     * @return The instance of the Condition class.
     */
    public static IParameterizedCondition createParameterizedCondition() {
        return new ParameterizedCondition();
    }

    /**
     * To create an ParameterizedCondition object out of Condition object.
     * 
     * @return The instance of the ParameterizedCondition class.
     */
    public static IParameterizedCondition createParameterizedCondition(ICondition condition) {
        return new ParameterizedCondition(condition);
    }

    /**
     * To instantiate ParameterizedCondition object.
     * 
     * @param attribute The reference to Dynamic Extension attribute on which
     *            condition to be created.
     * @param relationalOperator The relational operator between attribute &
     *            values.
     * @param values The List of String representing values of the condition.
     * @return The instance of the Condition class.
     */
    public static IParameterizedCondition createParameterizedCondition(AttributeInterface attribute,
            RelationalOperator relationalOperator, List<String> values, Integer index, String name) {
        return new ParameterizedCondition(attribute, relationalOperator, values, index, name);
    }

    /**
     * To instantiate Expression Id object.
     * 
     * @param id The id to set.
     * @return The reference to the ExpressionId object.
     */
    public static IExpressionId createExpressionId(int id) {
        return new ExpressionId(id);
    }

    /**
     * To instantiate Constraints class object.
     * 
     * @return The object of Constraints Class.
     */
    public static IConstraints createConstraints() {
        return new Constraints();
    }

    /**
     * To instantiate object of Rule class, with the given condition list.
     * 
     * @param conditions The list of Conditions to set.
     * @return The object of class Rule.
     */
    public static IRule createRule(List<ICondition> conditions) {
        return new Rule(conditions);
    }

    /**
     * To instantiate object of Rule class, with no conditions.
     * 
     * @return The object of class Rule.
     */
    public static IRule createRule() {
        return new Rule(new ArrayList<ICondition>());
    }

    /**
     * To instantiate object of IntraModelAssociation class.
     * 
     * @param association The reference to the dynamic Extension associated with
     *            this object.
     * @return The object of class IntraModelAssociation.
     */
    public static IIntraModelAssociation createIntraModelAssociation(AssociationInterface association) {
        return new IntraModelAssociation(association);
    }

    /**
     * To create empty Query object.
     * 
     * @return the reference to the Query object.
     */
    public static IQuery createQuery() {
        return new Query();
    }

    /**
     * To create empty ParameterizedQuery object.
     * 
     * @return the reference to the ParameterizedQuery object.
     */
    public static IParameterizedQuery createParameterizedQuery() {
        return new ParameterizedQuery();
    }

    /**
     * To create ParameterizedQuery object out of Query object
     * 
     * @param query
     * @return the reference to the ParameterizedQuery object
     */
    public static IParameterizedQuery createParameterizedQuery(IQuery query) {
        return new ParameterizedQuery(query);
    }

    /**
     * To instantiate object of class implementing IOutputEntity interface.
     * 
     * @param entityInterface The Dynamic Extension entity reference associated
     *            with this object.
     * @return The reference to the OutputEntity object.
     */
    public static IOutputEntity createOutputEntity(EntityInterface entityInterface) {
        return new OutputEntity(entityInterface);
    }

    /**
     * To instantiate object of class implementing IConstraintEntity interface.
     * 
     * @param entityInterface The Dynamic Extension entity reference associated
     *            with this object.
     * @return The reference to the ConstraintEntity object.
     */
    public static IQueryEntity createQueryEntity(EntityInterface entityInterface) {
        return new QueryEntity(entityInterface);
    }

    /**
     * This method instantiate object of class implementing IOutputAttribute
     * interface.
     * 
     * @param expressionId
     * @param attribute
     * @return
     */
    public static IOutputAttribute createOutputAttribute(IExpressionId expressionId, AttributeInterface attribute) {
        return new OutputAttribute(expressionId, attribute);
    }

    public static ILiteral createLiteral(TermType termType) {
        if (termType == TermType.DSInterval) {
            return createDateOffsetLiteral(DSInterval.Day);
        } else if (termType == TermType.YMInterval) {
            return createDateOffsetLiteral(YMInterval.Month);
        }
        return new Literal(termType);
    }

    public static ILiteral createLiteral(String s, TermType termType) {
        ILiteral res;
        if (termType == TermType.DSInterval) {
            res = createDateOffsetLiteral(s, DSInterval.Day);
        } else if (termType == TermType.YMInterval) {
            res = createDateOffsetLiteral(s, YMInterval.Month);
        } else {
            res = new Literal(termType);
        }
        res.setLiteral(s);
        return res;
    }

    public static ICustomFormula createCustomFormula() {
        return new CustomFormula();
    }

    public static IExpressionAttribute createExpressionAttribute(IExpressionId expressionId,
            AttributeInterface attribute) {
        AttributeTypeInformationInterface attrTypeInfo = attribute.getAttributeTypeInformation();
        TermType termType;
        if (attrTypeInfo instanceof NumericTypeInformationInterface) {
            termType = TermType.Numeric;
        } else if (attrTypeInfo instanceof DateTypeInformationInterface) {
            termType = TermType.Date;
        } else {
            throw new UnsupportedOperationException(
                    "Only numeric and date attributes supported in custom formulas.");
        }
        return new ExpressionAttribute(expressionId, attribute, termType);
    }

    public static ITerm createTerm() {
        return new Term();
    }

    public static IOutputTerm createNamedTerm() {
        return new OutputTerm();
    }

    public static IOutputTerm createNamedTerm(ITerm term, String name) {
        return new OutputTerm(name, term);
    }

    public static <T extends ITimeIntervalEnum> IDateOffsetAttribute<T> createDateOffsetAttribute(
            IExpressionId expressionId, AttributeInterface attribute, T timeInterval) {
        return new DateOffsetAttribute<T>(expressionId, attribute, timeInterval);
    }

    public static <T extends ITimeIntervalEnum> IDateOffsetLiteral<T> createDateOffsetLiteral(T timeInterval) {
        return new DateOffsetLiteral<T>(timeInterval);
    }

    public static <T extends ITimeIntervalEnum> IDateOffsetLiteral<T> createDateOffsetLiteral(String s,
            T timeInterval) {
        IDateOffsetLiteral<T> res = new DateOffsetLiteral<T>(timeInterval);
        res.setLiteral(s);
        return res;
    }

}