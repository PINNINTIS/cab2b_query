/**
 * 
 */
package edu.wustl.common.querysuite.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.common.querysuite.queryobject.IArithmeticOperand;
import edu.wustl.common.querysuite.queryobject.ICondition;
import edu.wustl.common.querysuite.queryobject.IConstraints;
import edu.wustl.common.querysuite.queryobject.ICustomFormula;
import edu.wustl.common.querysuite.queryobject.IExpression;
import edu.wustl.common.querysuite.queryobject.IExpressionAttribute;
import edu.wustl.common.querysuite.queryobject.IExpressionOperand;
import edu.wustl.common.querysuite.queryobject.IParameterizedCondition;
import edu.wustl.common.querysuite.queryobject.IQuery;
import edu.wustl.common.querysuite.queryobject.IQueryEntity;
import edu.wustl.common.querysuite.queryobject.IRule;
import edu.wustl.common.querysuite.queryobject.ITerm;

/**
 * @author chetan_patil
 * @created Sep 20, 2007, 12:07:59 PM
 */
public class QueryUtility {

    /**
     * This method returns all the selected Condition for a given query.
     * 
     * @param query
     * @return Map of ExpressionId -> Collection of Condition
     */
    public static Map<IExpression, Collection<ICondition>> getAllSelectedConditions(IQuery query) {
        Map<IExpression, Collection<ICondition>> expressionIdConditionCollectionMap = null;
        if (query != null) {
            expressionIdConditionCollectionMap = new HashMap<IExpression, Collection<ICondition>>();

            IConstraints constraints = query.getConstraints();
            for (IExpression expression : constraints) {
                for (int index = 0; index < expression.numberOfOperands(); index++) {
                    IExpressionOperand expressionOperand = expression.getOperand(index);
                    if (expressionOperand instanceof IRule) {
                        IRule rule = (IRule) expressionOperand;
                        Collection<ICondition> conditionList = rule.getConditions();

                        expressionIdConditionCollectionMap.put(expression, conditionList);
                    }
                }
            }
        }

        return expressionIdConditionCollectionMap;
    }

    /**
     * This method returns all the selected Condition for a given query.
     * 
     * @param query
     * @return Map of ExpressionId -> Collection of Condition
     */
    public static Map<IExpression, Collection<IParameterizedCondition>> getAllParameterizedConditions(IQuery query) {
        Map<IExpression, Collection<IParameterizedCondition>> expressionIdConditionCollectionMap = null;
        if (query != null) {
            expressionIdConditionCollectionMap = new HashMap<IExpression, Collection<IParameterizedCondition>>();

            IConstraints constraints = query.getConstraints();
            for (IExpression expression : constraints) {
                for (int index = 0; index < expression.numberOfOperands(); index++) {
                    IExpressionOperand expressionOperand = expression.getOperand(index);
                    if (expressionOperand instanceof IRule) {
                        IRule rule = (IRule) expressionOperand;

                        Collection<IParameterizedCondition> parameterizedConditions = new ArrayList<IParameterizedCondition>();

                        Collection<ICondition> conditionList = rule.getConditions();
                        for (ICondition condition : conditionList) {
                            if (condition instanceof IParameterizedCondition) {
                                parameterizedConditions.add((IParameterizedCondition) condition);
                            }
                        }
                        if (!parameterizedConditions.isEmpty()) {
                            expressionIdConditionCollectionMap.put(expression, parameterizedConditions);
                        }
                    }
                }
            }
        }

        return expressionIdConditionCollectionMap;
    }

    /**
     * This method returns all the attributes of the expressions involved in a
     * given query.
     * 
     * @param query
     * @return Map of ExpressionId -> Collection of Attribute
     */
    public static Map<IExpression, Collection<AttributeInterface>> getAllAttributes(IQuery query) {
        Map<IExpression, Collection<AttributeInterface>> expressionIdAttributeCollectionMap = null;
        if (query != null) {
            expressionIdAttributeCollectionMap = new HashMap<IExpression, Collection<AttributeInterface>>();

            IConstraints constraints = query.getConstraints();
            for (IExpression expression : constraints) {
                IQueryEntity queryEntity = expression.getQueryEntity();
                EntityInterface deEntity = queryEntity.getDynamicExtensionsEntity();
                Collection<AttributeInterface> attributeCollection = deEntity.getEntityAttributesForQuery();
                expressionIdAttributeCollectionMap.put(expression, attributeCollection);
            }
        }

        return expressionIdAttributeCollectionMap;
    }

    public static Set<ICustomFormula> getCustomFormulas(IQuery query) {
        return getCustomFormulas(query.getConstraints());
    }

    public static Set<ICustomFormula> getCustomFormulas(IConstraints constraints) {
        Set<ICustomFormula> res = new HashSet<ICustomFormula>();
        for (IExpression expression : constraints) {
            res.addAll(getCustomFormulas(expression));
        }
        return res;
    }

    public static Set<ICustomFormula> getCustomFormulas(IExpression expression) {
        Set<ICustomFormula> res = new HashSet<ICustomFormula>();
        for (IExpressionOperand operand : expression) {
            if (operand instanceof ICustomFormula) {
                res.add((ICustomFormula) operand);
            }
        }
        return res;
    }

    public static Set<IExpression> getExpressionsInFormula(ICustomFormula formula) {
        Set<IExpression> res = new HashSet<IExpression>();
        res.addAll(getExpressionsInTerm(formula.getLhs()));
        for (ITerm rhs : formula.getAllRhs()) {
            res.addAll(getExpressionsInTerm(rhs));
        }
        return res;
    }

    public static Set<IExpressionAttribute> getAttributesInFormula(ICustomFormula formula) {
        Set<IExpressionAttribute> res = new HashSet<IExpressionAttribute>();
        res.addAll(getAttributesInTerm(formula.getLhs()));
        for (ITerm rhs : formula.getAllRhs()) {
            res.addAll(getAttributesInTerm(rhs));
        }
        return res;
    }

    public static Set<IExpression> getExpressionsInTerm(ITerm term) {
        Set<IExpression> res = new HashSet<IExpression>();
        for (IExpressionAttribute attr : getAttributesInTerm(term)) {
            res.add(attr.getExpression());
        }
        return res;
    }

    public static Set<IExpressionAttribute> getAttributesInTerm(ITerm term) {
        Set<IExpressionAttribute> res = new HashSet<IExpressionAttribute>();
        for (IArithmeticOperand operand : term) {
            if (operand instanceof ITerm) {
                res.add((IExpressionAttribute) operand);
            }
        }
        return res;
    }

    public static Set<IExpression> getContainingExpressions(IConstraints constraints, ICustomFormula formula) {
        Set<IExpression> res = new HashSet<IExpression>();
        for (IExpression expression : constraints) {
            if (expression.containsOperand(formula)) {
                res.add(expression);
            }
        }
        return res;
    }
}
