package edu.wustl.common.querysuite.utils;

import static edu.wustl.common.querysuite.queryobject.RelationalOperator.getSQL;

import java.util.List;

import edu.wustl.common.querysuite.queryobject.ITerm;
import edu.wustl.common.querysuite.queryobject.RelationalOperator;
import edu.wustl.common.querysuite.queryobject.impl.CustomFormula;
import edu.wustl.common.querysuite.utils.TermProcessor.IAttributeAliasProvider;

public class CustomFormulaProcessor {
    private TermProcessor termProcessor;

    public CustomFormulaProcessor() {
        this.termProcessor = new TermProcessor();
    }

    public CustomFormulaProcessor(IAttributeAliasProvider aliasProvider, DatabaseSQLSettings databaseSQLSettings) {
        this.termProcessor = new TermProcessor(aliasProvider, databaseSQLSettings);
    }

    public String asString(CustomFormula formula) {
        // TODO in parantheses??
        String lhs = getString(formula.getLhs());
        RelationalOperator relationalOperator = formula.getOperator();
        if (relationalOperator.numberOfValuesRequired() == 0) {
            // is (not) null
            return lhs + getSQL(relationalOperator);
        }
        List<ITerm> rhses = formula.getAllRhs();
        ITerm rhsTerm = rhses.get(0);
        String rhs = getString(rhsTerm);
        if (relationalOperator.numberOfValuesRequired() == 1) {
            return lhs + " " + getSQL(relationalOperator) + " " + rhs;
        }
        if (relationalOperator == RelationalOperator.Between) {
            if (rhses.size() != 2) {
                throw new IllegalArgumentException("Between doesn't have 2 values.");
            }
            String rhs2 = getString(rhses.get(1));
            String res = "(" + lhs + ">=" + rhs + " and " + lhs + "<=" + rhs2 + ")";
            res += " or (" + lhs + "<=" + rhs + " and " + lhs + ">=" + rhs2 + ")";
            return res;
        }
        // IN, NOT IN
        String sqlOper = relationalOperator == RelationalOperator.In
                ? getSQL(RelationalOperator.Equals)
                : getSQL(RelationalOperator.NotEquals);
        String res = lhs + sqlOper + rhs + " and";
        for (int i = 1, n = rhses.size(); i < n; i++) {
            rhs = getString(rhses.get(i));
            String next = lhs + sqlOper + rhs + " and";
            res += next;
        }
        return res;
    }

    private String getString(ITerm term) {
        return termProcessor.convertTerm(term).getString();
    }
}
