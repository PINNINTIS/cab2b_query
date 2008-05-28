package edu.wustl.common.querysuite.utils;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.wustl.common.querysuite.queryobject.ArithmeticOperator;
import edu.wustl.common.querysuite.queryobject.IArithmeticOperand;
import edu.wustl.common.querysuite.queryobject.IConnector;
import edu.wustl.common.querysuite.queryobject.IDateOffset;
import edu.wustl.common.querysuite.queryobject.IExpressionAttribute;
import edu.wustl.common.querysuite.queryobject.ILiteral;
import edu.wustl.common.querysuite.queryobject.ITerm;
import edu.wustl.common.querysuite.queryobject.TermType;
import edu.wustl.common.querysuite.queryobject.TimeInterval;

// TODO TERM IN OUTPUT OF SQL
// TODO Date op Numeric to be treated as Date op DateOffset(Day)

public class TermProcessor {

    public interface AttributeAliasProvider {
        String getAliasFor(IExpressionAttribute exprAttr);
    }

    private static AttributeAliasProvider defaultAliasProvider = new AttributeAliasProvider() {

        public String getAliasFor(IExpressionAttribute exprAttr) {
            AttributeInterface attribute = exprAttr.getAttribute();
            String entityName = attribute.getEntity().getName();
            entityName = entityName.substring(entityName.lastIndexOf(".") + 1);
            return entityName + attribute.getName();
        }

    };

    private AttributeAliasProvider aliasProvider;

    private PrimitiveOperationProcessor primitiveOperationProcessor;

    public TermProcessor() {
        this.aliasProvider = defaultAliasProvider;
        this.primitiveOperationProcessor = new PrimitiveOperationProcessor();
    }

    public TermProcessor(AttributeAliasProvider aliasProvider, DatabaseType databaseType) {
        this.aliasProvider = aliasProvider;
        switch (databaseType) {
            case MySQL :
                this.primitiveOperationProcessor = new MySQLPrimitiveOperationProcessor();
                break;
            case Oracle :
                this.primitiveOperationProcessor = new OraclePrimitiveOperationProcessor();
                break;
            default :
                throw new RuntimeException("Can't occur.");
        }
    }

    public static class TermString {
        private String string;

        private TermType termType;

        private TermString(TermStringOpnd termStringOpnd) {
            this.string = termStringOpnd.string;
            this.termType = termStringOpnd.termType;
        }

        public String getString() {
            return string;
        }

        public TermType getTermType() {
            return termType;
        }

    }

    static class TermStringOpnd implements IArithmeticOperand {
        private static final long serialVersionUID = 7952975305036738122L;

        private final String string;

        private final TermType termType;

        private final TimeInterval timeInterval;

        private final boolean literal;

        private TermStringOpnd(String string, TermType termType, boolean literal) {
            this.string = string;
            this.termType = termType;
            if (termType == TermType.DateOffset) {
                timeInterval = TimeInterval.Day;
            } else {
                timeInterval = null;
            }
            this.literal = literal;
        }

        TermStringOpnd(String string, TimeInterval timeInterval, boolean literal) {
            this.string = string;
            this.timeInterval = timeInterval;
            this.termType = TermType.DateOffset;
            this.literal = literal;
        }

        public String getString() {
            return string;
        }

        public TermType getTermType() {
            return termType;
        }

        public TimeInterval getTimeInterval() {
            if (termType != TermType.DateOffset) {
                throw new UnsupportedOperationException();
            }
            return timeInterval;
        }

        public boolean isLiteral() {
            return literal;
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

        private static final SubTerm INVALID_SUBTERM = new SubTerm(-1, INVALID_TERM_STRING_OPND, -1);

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

    public TermString convertTerm(ITerm term, boolean postFix) {
        SubTerm subTerm = convertSubTerm(term, 0, postFix);
        return new TermString(subTerm.termStringOpnd);
    }

    private static final TermStringOpnd INVALID_TERM_STRING_OPND = new TermStringOpnd("", TermType.Invalid, true);

    private SubTerm convertSubTerm(ITerm term, int startIdx, boolean postFix) {
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
            TermStringOpnd leftOpnd = new TermStringOpnd(leftOpndString, termType, i == 1);
            IConnector<ArithmeticOperator> prevConn = term.getConnector(i - 1, i);

            IArithmeticOperand rightOpnd;
            int nextI;
            int numRightPs;
            if (currOpndNesting > prevConn.getNestingNumber()) {
                SubTerm subTerm = convertSubTerm(term, i, postFix);
                rightOpnd = subTerm;
                numRightPs = subTerm.numRightPs;
                nextI = subTerm.endIdx + 1;
            } else {
                rightOpnd = term.getOperand(i);
                numRightPs = currOpndNesting - term.getConnector(i, i + 1).getNestingNumber();
                nextI = i + 1;
            }
            TermStringOpnd resLit = convertBasicTerm(leftOpnd, prevConn.getOperator(), rightOpnd, postFix);
            if (resLit == INVALID_TERM_STRING_OPND) {
                return SubTerm.INVALID_SUBTERM;
            }
            res = getLeftParentheses(numLeftPs) + resLit.getString();
            termType = resLit.getTermType();
            res += getRightParentheses(numRightPs);
            numLeftPs -= numRightPs;
            i = nextI;
        }
        TermStringOpnd termStringOpnd = new TermStringOpnd(res, termType, false);
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
        return new TermStringOpnd(strOpnd.getString(), TimeInterval.Day, strOpnd.literal);
    }

    private TermStringOpnd convertBasicTerm(IArithmeticOperand leftOpnd, ArithmeticOperator operator,
            IArithmeticOperand rightOpnd, boolean postFix) {
        TermType leftType = leftOpnd.getTermType();
        TermType rightType = rightOpnd.getTermType();
        TermType termType = TermType.getResultTermType(leftType, rightType, operator);
        if (termType == TermType.Invalid) {
            return INVALID_TERM_STRING_OPND;
        }
        TermStringOpnd leftTermStrOpnd;
        TermStringOpnd rightTermStrOpnd;
        if (leftType == TermType.Date && rightType == TermType.Numeric) {
            leftTermStrOpnd = convertOperand(leftOpnd);
            rightTermStrOpnd = numToDateOffset(rightOpnd);
        } else if (leftType == TermType.Numeric && rightType == TermType.Date) {
            leftTermStrOpnd = numToDateOffset(leftOpnd);
            rightTermStrOpnd = convertOperand(rightOpnd);
        } else {
            leftTermStrOpnd = convertOperand(leftOpnd);
            rightTermStrOpnd = convertOperand(rightOpnd);
        }
        return new TermStringOpnd(primitiveOperation(leftTermStrOpnd, operator, rightTermStrOpnd, postFix),
                termType, false);
    }

    private String primitiveOperation(TermStringOpnd leftTermStrOpnd, ArithmeticOperator operator,
            TermStringOpnd rightTermStrOpnd, boolean postFix) {
        return primitiveOperationProcessor.getResultString(leftTermStrOpnd, operator, rightTermStrOpnd, postFix);
    }

    private TermStringOpnd convertOperand(IArithmeticOperand operand) {
        String termStr;
        boolean literalOpnd = false;
        if (operand instanceof ILiteral) {
            ILiteral literal = (ILiteral) operand;
            termStr = literal.getLiteral();
            literalOpnd = true;
        } else if (operand instanceof SubTerm) {
            SubTerm subTerm = (SubTerm) operand;
            termStr = subTerm.getOperandString();
        } else if (operand instanceof IExpressionAttribute) {
            IExpressionAttribute expressionAttribute = (IExpressionAttribute) operand;
            termStr = aliasProvider.getAliasFor(expressionAttribute);
        } else if (operand instanceof TermStringOpnd) {
            TermStringOpnd termStringOpnd = (TermStringOpnd) operand;
            termStr = termStringOpnd.getString();
            literalOpnd = termStringOpnd.literal;
        } else {
            throw new RuntimeException("Can't occur.");
        }
        if (operand instanceof IDateOffset) {
            IDateOffset offset = (IDateOffset) operand;
            return new TermStringOpnd(termStr, offset.getTimeInterval(), literalOpnd);
        } else {
            return new TermStringOpnd(termStr, operand.getTermType(), literalOpnd);
        }
    }
}
