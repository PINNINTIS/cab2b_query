package edu.wustl.common.querysuite.queryobject;

// TODO (date - date) = numeric (days). this is a limitation.
// NOTE: Thus, DateOffset is NEVER a result of any operation.

// TODO use termtype of LHS to validate RHS.
public enum TermType {
    // TODO String?
    // TODO DateOffset + Numeric??

    // TODO Boolean ??
    Date, Timestamp, YMInterval, DSInterval, Numeric, Invalid;

    /**
     * Returns the term type resulting from the specified arithmetic operation.
     * Following is the result term type for various cases : <br>
     * Date (D), Numeric (N), dateOffset(O).
     * <ul>
     * <li>D +/- N = D</li>
     * <li>D +/- O = D</li>
     * <li>D - D = N</li>
     * </ul>
     * otherwise Invalid.
     * 
     * @param leftOpndType the type of the left operand.
     * @param rightOpndType the type of the right operand.
     * @param operator the operator.
     * @return the resultant term type.
     */
    public static TermType getResultTermType(TermType leftOpndType, TermType rightOpndType,
            ArithmeticOperator operator) {
        if (leftOpndType == Invalid || rightOpndType == Invalid) {
            return Invalid;
        }
        if (leftOpndType == Numeric && rightOpndType == Numeric) {
            return Numeric;
        }
        if (leftOpndType == Date) {
            leftOpndType = Timestamp;
        }
        if (rightOpndType == Date) {
            rightOpndType = Timestamp;
        }

        // rest is Temporal
        if (leftOpndType == Numeric) {
            // becomes DAY
            leftOpndType = DSInterval;
        }
        if (rightOpndType == Numeric) {
            // becomes DAY
            rightOpndType = DSInterval;
        }
        if (leftOpndType == DSInterval && rightOpndType == DSInterval) {
            if (operator == ArithmeticOperator.Plus || operator == ArithmeticOperator.Minus) {
                return DSInterval;
            }
            return Invalid;
        }
        if (leftOpndType == Timestamp && rightOpndType == Timestamp && operator == ArithmeticOperator.Minus) {
            return DSInterval;
        }

        if (leftOpndType == Timestamp && isInterval(rightOpndType)
                && (operator == ArithmeticOperator.Plus || operator == ArithmeticOperator.Minus)) {
            return Timestamp;
        }
        if (isInterval(leftOpndType) && rightOpndType == Timestamp && operator == ArithmeticOperator.Plus) {
            return Timestamp;
        }
        return Invalid;
    }

    public static boolean isInterval(TermType termType) {
        return termType == DSInterval || termType == YMInterval;
    }

    public static TermType termType(ITimeIntervalEnum timeInterval) {
        return timeInterval instanceof DSInterval ? DSInterval : YMInterval;
    }
}
