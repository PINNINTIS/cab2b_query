package edu.wustl.common.querysuite.queryobject;

import java.util.List;

public interface ICustomFormula extends IExpressionOperand {
    ITerm getLhs();

    void setLhs(ITerm lhs);

    List<ITerm> getAllRhs();

    void addRhs(ITerm rhs);

    RelationalOperator getOperator();

    void setOperator(RelationalOperator relationalOperator);
}
