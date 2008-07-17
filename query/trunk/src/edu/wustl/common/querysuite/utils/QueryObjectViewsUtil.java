package edu.wustl.common.querysuite.utils;

import java.util.HashSet;
import java.util.Set;

import edu.wustl.common.querysuite.queryobject.IArithmeticOperand;
import edu.wustl.common.querysuite.queryobject.IConstraints;
import edu.wustl.common.querysuite.queryobject.ICustomFormula;
import edu.wustl.common.querysuite.queryobject.IExpression;
import edu.wustl.common.querysuite.queryobject.IExpressionAttribute;
import edu.wustl.common.querysuite.queryobject.IExpressionOperand;
import edu.wustl.common.querysuite.queryobject.IQuery;
import edu.wustl.common.querysuite.queryobject.ITerm;

/**
 * Provides methods to obtain different views of a query object.
 * 
 * @author srinath_k
 * 
 */
public class QueryObjectViewsUtil {
    private QueryObjectViewsUtil() {

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
}
