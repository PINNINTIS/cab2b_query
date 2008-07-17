package edu.wustl.common.querysuite.utils;

import static edu.wustl.common.querysuite.queryobject.RelationalOperator.getSQL;

import java.util.List;

import edu.wustl.common.querysuite.factory.QueryObjectFactory;
import edu.wustl.common.querysuite.queryobject.ArithmeticOperator;
import edu.wustl.common.querysuite.queryobject.IConnector;
import edu.wustl.common.querysuite.queryobject.ICustomFormula;
import edu.wustl.common.querysuite.queryobject.ITerm;
import edu.wustl.common.querysuite.queryobject.RelationalOperator;
import edu.wustl.common.querysuite.queryobject.TermType;
import edu.wustl.common.querysuite.utils.TermProcessor.IAttributeAliasProvider;
import edu.wustl.common.util.ObjectCloner;

/**
 * Provides the string representation of a custom formula. It uses
 * {@link TermProcessor} to obtain string representations of the LHS and RHS
 * terms and connects them based on the relational operator.
 * 
 * @author srinath_k
 * @see TermProcessor
 */
public class CustomFormulaProcessor {
    private TermProcessor termProcessor;

    private static final String SPACE = " ";

    /**
     * Uses the default {@link TermProcessor} to obtain string representation of
     * the terms.
     * 
     * @see TermProcessor#TermProcessor()
     */
    public CustomFormulaProcessor() {
        this.termProcessor = new TermProcessor();
    }

    /**
     * Uses a {@link TermProcessor} initialized with specified parameters to
     * obtain string representation of the terms.
     * 
     * @see TermProcessor#TermProcessor(IAttributeAliasProvider,
     *      DatabaseSQLSettings)
     */
    public CustomFormulaProcessor(IAttributeAliasProvider aliasProvider, DatabaseSQLSettings databaseSQLSettings) {
        this.termProcessor = new TermProcessor(aliasProvider, databaseSQLSettings);
    }

    /**
     * @return the string representation of the specified formula.
     */
    public String asString(ICustomFormula formula) {
        formula = modifyYMInterval(formula);
        if (!isValid2(formula)) {
            throw new IllegalArgumentException("invalid custom formula.");
        }
        String lhs = getString(formula.getLhs());
        RelationalOperator relationalOperator = formula.getOperator();
        if (relationalOperator.numberOfValuesRequired() == 0) {
            // is (not) null
            return lhs + SPACE + getSQL(relationalOperator);
        }
        List<ITerm> rhses = formula.getAllRhs();
        if (rhses.isEmpty()) {
            throw new IllegalArgumentException("No value for operator " + getSQL(relationalOperator));
        }
        String rhs = getString(rhses.get(0));
        if (relationalOperator.numberOfValuesRequired() == 1) {
            // unary operators
            return lhs + SPACE + getSQL(relationalOperator) + SPACE + rhs;
        }
        if (relationalOperator == RelationalOperator.Between) {
            String rhs2 = getString(rhses.get(1));
            // (lhs >= rhs1 and lhs <= rhs2) or (lhs <= rhs1 and lhs >= rhs2)
            String res = "(" + lhs + " >= " + rhs + " and " + lhs + " <= " + rhs2 + ")";
            res += " or (" + lhs + " <= " + rhs + " and " + lhs + " >= " + rhs2 + ")";
            return res;
        }
        // In : lhs = rhs1 or lhs = rhs2 or lhs = rhs3...
        // NotIn : lhs != rhs1 and lhs != rhs2 and lhs != rhs3...
        String sqlOper = relationalOperator == RelationalOperator.In
                ? getSQL(RelationalOperator.Equals)
                : getSQL(RelationalOperator.NotEquals);
        String logicOper = relationalOperator == RelationalOperator.In ? "or" : "and";
        sqlOper = SPACE + sqlOper + SPACE;
        logicOper = SPACE + logicOper + SPACE;
        String res = lhs + sqlOper + rhs;

        for (int i = 1, n = rhses.size(); i < n; i++) {
            rhs = getString(rhses.get(i));
            String next = logicOper + lhs + sqlOper + rhs;
            res += next;
        }
        return res;
    }

    private String getString(ITerm term) {
        return termProcessor.convertTerm(term).getString();
    }

    private ICustomFormula modifyYMInterval(ICustomFormula formula) {
        ITerm lhs = formula.getLhs();
        if (!isValidNonYMInterval(lhs)) {
            return null;
        }
        boolean ymRhsPresent = false;
        for (ITerm rhs : formula.getAllRhs()) {
            if (!isValidTermType(rhs)) {
                return null;
            }
            if (isYMInterval(rhs)) {
                ymRhsPresent = true;
            }
        }
        if (!ymRhsPresent) {
            return formula;
        }
        // YMinterval present in atleast one rhs.
        if (lhs.getTermType() != TermType.DSInterval || lhs.numberOfOperands() == 1) {
            return null;
        }
        formula = ObjectCloner.clone(formula);

        SplitTerm splitTerm = new SplitTerm(lhs);
        splitTerm.term2.addParantheses();
        IConnector<ArithmeticOperator> conn = conn(negate(splitTerm.operator));
        for (ITerm rhs : formula.getAllRhs()) {
            rhs.addParantheses();
            rhs.addAll(conn, splitTerm.term2);
        }
        formula.setLhs(splitTerm.term1);

        return formula;
    }

    private IConnector<ArithmeticOperator> conn(ArithmeticOperator operator) {
        return QueryObjectFactory.createArithmeticConnector(operator, 0);
    }

    private ArithmeticOperator negate(ArithmeticOperator operator) {
        switch (operator) {
            case Minus :
                return ArithmeticOperator.Plus;
            case Plus :
                return ArithmeticOperator.Minus;
            default :
                throw new RuntimeException("problem in code.");
        }
    }

    private boolean isYMInterval(ITerm term) {
        return term.getTermType() == TermType.YMInterval;
    }

    private boolean isValidTermType(ITerm term) {
        return term.getTermType() != TermType.Invalid;
    }

    private boolean isValidNonYMInterval(ITerm term) {
        return isValidTermType(term) && !isYMInterval(term);
    }

    private static class SplitTerm {
        private final ITerm term1;

        private final ITerm term2;

        private final ArithmeticOperator operator;

        private SplitTerm(ITerm term) {
            int splitIdx = indexOfSplitConn(term);
            operator = term.getConnector(splitIdx, splitIdx + 1).getOperator();
            term1 = (ITerm) term.subExpression(0, splitIdx);
            term2 = (ITerm) term.subExpression(splitIdx + 1, term.numberOfOperands() - 1);
        }

        private int indexOfSplitConn(ITerm term) {
            int splitIdx = -1;
            int minNesting = -1;
            for (int i = 0, n = term.numberOfOperands(); i < n - 1; i++) {
                IConnector<ArithmeticOperator> conn = term.getConnector(i, i + 1);
                // take last connector with min nesting
                if (conn.getNestingNumber() >= minNesting) {
                    splitIdx = i;
                    minNesting = conn.getNestingNumber();
                }
            }
            return splitIdx;
        }
    }

    public boolean isValid(ICustomFormula customFormula) {
        return isValid2(modifyYMInterval(customFormula));
    }

    // called only after modifying YMInterval.
    private boolean isValid2(ICustomFormula customFormula) {
        if (customFormula == null) {
            return false;
        }
        RelationalOperator operator = customFormula.getOperator();
        if (operator == null) {
            return false;
        }

        int numRhs = customFormula.getAllRhs().size();
        if (operator == RelationalOperator.Between) {
            if (numRhs != 2) {
                return false;
            }
        }
        if (operator == RelationalOperator.In || operator == RelationalOperator.NotIn) {
            if (numRhs == 0) {
                return false;
            }
        } else {
            if (operator.numberOfValuesRequired() != numRhs) {
                return false;
            }
        }

        TermType lhsType = customFormula.getLhs().getTermType();
        for (ITerm rhs : customFormula.getAllRhs()) {
            if (rhs.getTermType() != lhsType) {
                return false;
            }
        }
        return true;
    }
}
