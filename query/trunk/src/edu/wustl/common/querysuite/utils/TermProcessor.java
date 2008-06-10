package edu.wustl.common.querysuite.utils;

import org.apache.commons.lang.builder.HashCodeBuilder;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.wustl.common.querysuite.factory.QueryObjectFactory;
import edu.wustl.common.querysuite.queryobject.ArithmeticOperator;
import edu.wustl.common.querysuite.queryobject.DSInterval;
import edu.wustl.common.querysuite.queryobject.IArithmeticOperand;
import edu.wustl.common.querysuite.queryobject.IConnector;
import edu.wustl.common.querysuite.queryobject.IDateOffset;
import edu.wustl.common.querysuite.queryobject.IDateOffsetLiteral;
import edu.wustl.common.querysuite.queryobject.IExpressionAttribute;
import edu.wustl.common.querysuite.queryobject.ILiteral;
import edu.wustl.common.querysuite.queryobject.ITerm;
import edu.wustl.common.querysuite.queryobject.ITimeIntervalEnum;
import edu.wustl.common.querysuite.queryobject.TermType;

// TODO TERM IN OUTPUT OF SQL
// TODO Date op Numeric to be treated as Date op DateOffset(Day)

public class TermProcessor {

    public interface IAttributeAliasProvider {
        String getAliasFor(IExpressionAttribute exprAttr);
    }

    static final IAttributeAliasProvider defaultAliasProvider = new IAttributeAliasProvider() {

        public String getAliasFor(IExpressionAttribute exprAttr) {
            AttributeInterface attribute = exprAttr.getAttribute();
            String entityName = attribute.getEntity().getName();
            entityName = entityName.substring(entityName.lastIndexOf(".") + 1);
            return entityName + "." + attribute.getName();
        }

    };

    private IAttributeAliasProvider aliasProvider;

    private PrimitiveOperationProcessor primitiveOperationProcessor;

    public TermProcessor() {
        this.aliasProvider = defaultAliasProvider;
        this.primitiveOperationProcessor = new PrimitiveOperationProcessor();
    }

    public TermProcessor(IAttributeAliasProvider aliasProvider, DatabaseSQLSettings databaseSQLSettings) {
        this.aliasProvider = aliasProvider;
        switch (databaseSQLSettings.getDatabaseType()) {
            case MySQL :
                this.primitiveOperationProcessor = new MySQLPrimitiveOperationProcessor(databaseSQLSettings
                        .getDateFormat());
                break;
            case Oracle :
                this.primitiveOperationProcessor = new OraclePrimitiveOperationProcessor(databaseSQLSettings
                        .getDateFormat());
                break;
            default :
                throw new RuntimeException("Can't occur.");
        }
    }

    public static class TermString {
        private String string;

        private TermType termType;

        static final TermString INVALID = new TermString("", TermType.Invalid);

        TermString(String s, TermType termType) {
            if (s == null || termType == null) {
                throw new IllegalArgumentException();
            }
            this.string = s;
            this.termType = termType;
        }

        public String getString() {
            return string;
        }

        public TermType getTermType() {
            return termType;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof TermString)) {
                return false;
            }
            TermString o = (TermString) obj;
            return string.equals(o.string) && termType == o.termType;
        }

        @Override
        public int hashCode() {
            return new HashCodeBuilder().append(string).append(termType).toHashCode();
        }

        @Override
        public String toString() {
            return string + "[" + termType + "]";
        }
    }

    static class TermStringOpnd implements IArithmeticOperand {
        private static final long serialVersionUID = 7952975305036738122L;

        private final String string;

        private final TermType termType;

        private final ITimeIntervalEnum timeInterval;

        static final TermStringOpnd INVALID_TERM_STRING_OPND = new TermStringOpnd("", TermType.Invalid);

        TermStringOpnd(String string, TermType termType) {
            this.string = string;
            this.termType = termType;
            if (TermType.isInterval(termType)) {
                timeInterval = DSInterval.Day;
            } else {
                timeInterval = null;
            }
        }

        TermStringOpnd(String string, ITimeIntervalEnum timeInterval) {
            this.string = string;
            this.timeInterval = timeInterval;
            this.termType = TermType.termType(timeInterval);
        }

        public String getString() {
            return string;
        }

        public TermType getTermType() {
            return termType;
        }

        public ITimeIntervalEnum getTimeInterval() {
            if (!TermType.isInterval(termType)) {
                throw new UnsupportedOperationException();
            }
            return timeInterval;
        }

        public void setTermType(TermType termType) {
            throw new UnsupportedOperationException();
        }

        public Long getId() {
            throw new UnsupportedOperationException();
        }

        public void setId(Long id) {
            throw new UnsupportedOperationException();
        }
    }

    private static class SubTerm implements IArithmeticOperand {
        private static final long serialVersionUID = 7342856030098944697L;

        private static final SubTerm INVALID_SUBTERM = new SubTerm(-1, TermStringOpnd.INVALID_TERM_STRING_OPND, -1);

        private final int endIdx;

        private final TermStringOpnd termStringOpnd;

        private final int numRightPs;

        private SubTerm(int endIdx, TermStringOpnd termStringOpnd, int numRightPs) {
            this.endIdx = endIdx;
            this.termStringOpnd = termStringOpnd;
            this.numRightPs = numRightPs;
        }

        private String string() {
            return termStringOpnd.getString();
        }

        private String getOperandString() {
            return string().substring(0, string().length() - numRightPs);
        }

        public TermType getTermType() {
            return termStringOpnd.getTermType();
        }

        public void setTermType(TermType termType) {
            throw new UnsupportedOperationException();
        }

        public Long getId() {
            throw new UnsupportedOperationException();
        }

        public void setId(Long id) {
            throw new UnsupportedOperationException();
        }
    }

    public TermString convertTerm(ITerm term) {
        term = replaceDateLiterals(term);
        if (term.numberOfOperands() == 0) {
            return TermString.INVALID;
        }
        if (term.numberOfOperands() == 1) {
            IArithmeticOperand opnd = term.getOperand(0);
            if (opnd.getTermType() == TermType.YMInterval) {
                return TermString.INVALID;
            }
            if (opnd.getTermType() == TermType.DSInterval && opnd instanceof IDateOffsetLiteral) {
                IDateOffsetLiteral<DSInterval> lit = (IDateOffsetLiteral<DSInterval>) opnd;
                String s = primitiveOperationProcessor.getIntervalString(lit.getLiteral(), lit.getTimeInterval());
                return new TermString(s, TermType.DSInterval);
            }
            TermStringOpnd termStrOpnd = convertOperand(opnd);
            String s = termStrOpnd.getString();
            if (opnd.getTermType() == TermType.Date) {
                s = primitiveOperationProcessor.dateToTimestamp(s);
            }
            return new TermString(s, termStrOpnd.getTermType());
        }
        SubTerm subTerm = convertSubTerm(term, 0);
        String res = subTerm.string();
        if (subTerm != SubTerm.INVALID_SUBTERM) {
            res = res.substring(1, res.length() - 1);
        }
        return new TermString(res, subTerm.getTermType());
    }

    private ITerm replaceDateLiterals(ITerm term) {
        ITerm res = QueryObjectFactory.createTerm();
        if (term.numberOfOperands() == 0) {
            return res;
        }
        res.addOperand(dateCheckedOperand(term.getOperand(0)));
        for (int i = 1; i < term.numberOfOperands(); i++) {
            res.addOperand(term.getConnector(i - 1, i), dateCheckedOperand(term.getOperand(i)));
        }
        return res;
    }

    private IArithmeticOperand dateCheckedOperand(IArithmeticOperand opnd) {
        // TODO support timestamp literal?
        IArithmeticOperand res = opnd;
        if (opnd instanceof ILiteral && opnd.getTermType() == TermType.Date) {
            ILiteral literal = (ILiteral) opnd;
            String dateStr = primitiveOperationProcessor.modifyDateLiteral(literal.getLiteral());
            ILiteral newLit = QueryObjectFactory.createLiteral(literal.getTermType());
            newLit.setLiteral(dateStr);
            res = newLit;
        }
        return res;
    }

    private SubTerm convertSubTerm(ITerm term, int startIdx) {
        int operatorBeforeTermNesting = term.getConnector(startIdx - 1, startIdx).getNestingNumber();
        if (term.nestingNumberOfOperand(startIdx) <= operatorBeforeTermNesting) {
            throw new IllegalArgumentException();
        }

        String res = "";
        int numLeftPs = term.nestingNumberOfOperand(startIdx) - operatorBeforeTermNesting;
        res += getLeftParentheses(numLeftPs);
        res += convertOperand(term.getOperand(startIdx)).getString();
        int i = startIdx + 1;
        TermType termType = term.getOperand(startIdx).getTermType();

        while (true) {
            if (i == term.numberOfOperands()) {
                break;
            }
            int currOpndNesting = term.nestingNumberOfOperand(i);
            if (currOpndNesting <= operatorBeforeTermNesting) {
                break;
            }
            String leftOpndString = res.substring(numLeftPs);
            TermStringOpnd leftOpnd = new TermStringOpnd(leftOpndString, termType);
            IConnector<ArithmeticOperator> prevConn = term.getConnector(i - 1, i);

            IArithmeticOperand rightOpnd;
            int nextI;
            int numRightPs;
            if (currOpndNesting > prevConn.getNestingNumber()) {
                SubTerm subTerm = convertSubTerm(term, i);
                rightOpnd = subTerm;
                numRightPs = subTerm.numRightPs;
                nextI = subTerm.endIdx + 1;
            } else {
                rightOpnd = term.getOperand(i);
                numRightPs = currOpndNesting - term.getConnector(i, i + 1).getNestingNumber();
                nextI = i + 1;
            }
            TermStringOpnd resLit = convertBasicTerm(leftOpnd, prevConn.getOperator(), rightOpnd);
            if (resLit == TermStringOpnd.INVALID_TERM_STRING_OPND) {
                return SubTerm.INVALID_SUBTERM;
            }
            res = getLeftParentheses(numLeftPs) + resLit.getString();
            termType = resLit.getTermType();
            res += getRightParentheses(numRightPs);
            numLeftPs -= numRightPs;
            i = nextI;
        }
        TermStringOpnd termStringOpnd = new TermStringOpnd(res, termType);
        return new SubTerm(i - 1, termStringOpnd, -numLeftPs);
    }

    private String getLeftParentheses(int i) {
        return getParantheses(i, "(");
    }

    private String getRightParentheses(int i) {
        return getParantheses(i, ")");
    }

    private String getParantheses(int n, String paranthesis) {
        StringBuffer s = new StringBuffer();
        for (int i = 0; i < n; i++) {
            s.append(paranthesis);
        }
        return s.toString();
    }

    private TermStringOpnd numToDateOffset(IArithmeticOperand opnd) {
        TermStringOpnd strOpnd = convertOperand(opnd);
        String res = primitiveOperationProcessor.getIntervalString(strOpnd.getString(), DSInterval.Day);
        return new TermStringOpnd(res, DSInterval.Day);
    }

    private TermStringOpnd convertBasicTerm(IArithmeticOperand leftOpnd, ArithmeticOperator operator,
            IArithmeticOperand rightOpnd) {
        TermType leftType = leftOpnd.getTermType();
        TermType rightType = rightOpnd.getTermType();
        TermType termType = TermType.getResultTermType(leftType, rightType, operator);
        if (termType == TermType.Invalid) {
            return TermStringOpnd.INVALID_TERM_STRING_OPND;
        }
        TermStringOpnd leftTermStrOpnd;
        TermStringOpnd rightTermStrOpnd;
        if (isNumericCompatible(leftType) && rightType == TermType.Numeric) {
            leftTermStrOpnd = convertOperand(leftOpnd);
            rightTermStrOpnd = numToDateOffset(rightOpnd);
        } else if (leftType == TermType.Numeric && isNumericCompatible(rightType)) {
            leftTermStrOpnd = numToDateOffset(leftOpnd);
            rightTermStrOpnd = convertOperand(rightOpnd);
        } else {
            leftTermStrOpnd = convertOperand(leftOpnd);
            rightTermStrOpnd = convertOperand(rightOpnd);
        }
        // if (leftType == TermType.DSInterval && rightType ==
        // TermType.DSInterval) {
        // database independent
        // return dsIntervalMath(leftTermStrOpnd, rightTermStrOpnd,
        // operator);
        // }
        return new TermStringOpnd(primitiveOperation(leftTermStrOpnd, operator, rightTermStrOpnd), termType);
    }

    private boolean isNumericCompatible(TermType termType) {
        return termType == TermType.Date || termType == TermType.Timestamp || termType == TermType.DSInterval;
    }

    private String primitiveOperation(TermStringOpnd leftTermStrOpnd, ArithmeticOperator operator,
            TermStringOpnd rightTermStrOpnd) {
        return primitiveOperationProcessor.getResultString(leftTermStrOpnd, operator, rightTermStrOpnd);
    }

    private TermStringOpnd convertOperand(IArithmeticOperand operand) {
        String termStr;

        if (operand instanceof ILiteral) {
            ILiteral literal = (ILiteral) operand;
            termStr = literal.getLiteral();
        } else if (operand instanceof SubTerm) {
            SubTerm subTerm = (SubTerm) operand;
            termStr = subTerm.getOperandString();
        } else if (operand instanceof IExpressionAttribute) {
            IExpressionAttribute expressionAttribute = (IExpressionAttribute) operand;
            termStr = aliasProvider.getAliasFor(expressionAttribute);
        } else if (operand instanceof TermStringOpnd) {
            TermStringOpnd termStringOpnd = (TermStringOpnd) operand;
            termStr = termStringOpnd.getString();
        } else {
            throw new RuntimeException("Can't occur.");
        }
        if (operand instanceof IDateOffset) {
            IDateOffset offset = (IDateOffset) operand;
            if (operand instanceof ILiteral || operand instanceof IExpressionAttribute)
                termStr = primitiveOperationProcessor.getIntervalString(termStr, offset.getTimeInterval());
            return new TermStringOpnd(termStr, offset.getTimeInterval());
        } else {
            return new TermStringOpnd(termStr, operand.getTermType());
        }
    }

    // for testing
    final void setPrimitiveOperationProcessor(PrimitiveOperationProcessor primitiveOperationProcessor) {
        this.primitiveOperationProcessor = primitiveOperationProcessor;
    }

    final IAttributeAliasProvider getAliasProvider() {
        return aliasProvider;
    }

    final PrimitiveOperationProcessor getPrimitiveOperationProcessor() {
        return primitiveOperationProcessor;
    }

    // DS INTERVAL MATH
    /*
     * private static final int[] multipliers = {60, 24, 7};
     * 
     * final TermStringOpnd dsIntervalMath(TermStringOpnd leftOpnd,
     * TermStringOpnd rightOpnd, ArithmeticOperator oper) { DSInterval
     * smallInterval = (DSInterval) leftOpnd.getTimeInterval(); DSInterval
     * bigInterval = (DSInterval) rightOpnd.getTimeInterval(); String smallS =
     * leftOpnd.getString(); String bigS = rightOpnd.getString(); int diff =
     * smallInterval.compareTo(bigInterval); if (diff > 0) { DSInterval temp =
     * smallInterval; smallInterval = bigInterval; bigInterval = temp;
     * 
     * String tempS = smallS; smallS = bigS; bigS = tempS; }
     * 
     * DSInterval[] intervals = DSInterval.values(); int smallIdx =
     * Arrays.binarySearch(intervals, smallInterval); int bigIdx =
     * Arrays.binarySearch(intervals, bigInterval);
     * 
     * for (int i = bigIdx - 1; i >= smallIdx; i--) { bigS = bigS + "*" +
     * multipliers[i]; } String res = smallS + " " + oper.mathString() + " " +
     * bigS; return new TermStringOpnd(res, smallInterval); }
     */
}
