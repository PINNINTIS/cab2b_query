package edu.wustl.common.querysuite.utils;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.wustl.common.querysuite.factory.QueryObjectFactory;
import edu.wustl.common.querysuite.queryobject.ArithmeticOperator;
import edu.wustl.common.querysuite.queryobject.IArithmeticOperand;
import edu.wustl.common.querysuite.queryobject.IConnector;
import edu.wustl.common.querysuite.queryobject.IExpressionAttribute;
import edu.wustl.common.querysuite.queryobject.ILiteral;
import edu.wustl.common.querysuite.queryobject.ITerm;
import edu.wustl.common.querysuite.queryobject.TermType;

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

    public TermProcessor() {
        this(defaultAliasProvider);
    }

    public TermProcessor(AttributeAliasProvider aliasProvider) {
        this.aliasProvider = aliasProvider;
    }

    private static class SubTerm implements IArithmeticOperand {
        private static final long serialVersionUID = 7342856030098944697L;

        final int startIdx;

        final int endIdx;

        final ILiteral literal;

        final int numRightPs;

        private SubTerm(int startIdx, int endIdx, ILiteral literal, int numRightPs) {
            this.startIdx = startIdx;
            this.endIdx = endIdx;
            this.literal = literal;
            this.numRightPs = numRightPs;
        }

        private String string() {
            return literal.getLiteral();
        }

        private String getOperandString() {
            return string().substring(0, string().length() - numRightPs);
        }

        public TermType getTermType() {
            return literal.getTermType();
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

    public ILiteral convertTerm(ITerm term, boolean postFix) {
        return convertSubTerm(term, 0, postFix).literal;
    }

    private SubTerm convertSubTerm(ITerm term, int startIdx, boolean postFix) {
        int operatorBeforeTermNesting = term.getConnector(startIdx - 1, startIdx).getNestingNumber();
        if (term.nestingNumberOfOperand(startIdx) <= operatorBeforeTermNesting) {
            throw new IllegalArgumentException();
        }

        String res = "";
        int numLeftPs = term.nestingNumberOfOperand(startIdx) - operatorBeforeTermNesting;
        res += getLeftParentheses(numLeftPs);
        res += convertOperand(term.getOperand(startIdx));
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
            ILiteral leftOpnd = QueryObjectFactory.createLiteral(leftOpndString, termType);
            IConnector<ArithmeticOperator> prevConn = term.getConnector(i - 1, i);
            if (currOpndNesting > prevConn.getNestingNumber()) {
                SubTerm subTerm = convertSubTerm(term, i, postFix);
                res = getLeftParentheses(numLeftPs)
                        + convertBasicTerm(leftOpnd, prevConn.getOperator(), subTerm, postFix);
                termType = TermType.getResultTermType(termType, subTerm.getTermType(), prevConn.getOperator());
                res += getRightParentheses(subTerm.numRightPs);
                i = subTerm.endIdx + 1;
                numLeftPs -= subTerm.numRightPs;
            } else {
                IArithmeticOperand rightOpnd = term.getOperand(i);
                res = getLeftParentheses(numLeftPs)
                        + convertBasicTerm(leftOpnd, prevConn.getOperator(), rightOpnd, postFix);
                termType = TermType.getResultTermType(termType, rightOpnd.getTermType(), prevConn.getOperator());
                int numRightPs = currOpndNesting - term.getConnector(i, i + 1).getNestingNumber();
                res += getRightParentheses(numRightPs);
                numLeftPs -= numRightPs;
                i++;
            }
        }
        ILiteral literal = QueryObjectFactory.createLiteral(res, termType);
        return new SubTerm(startIdx, i - 1, literal, -numLeftPs);
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

    private String convertBasicTerm(IArithmeticOperand leftOpnd, ArithmeticOperator operator,
            IArithmeticOperand rightOpnd, boolean postFix) {
        // TODO remove postFix
        String leftOpndString = convertOperand(leftOpnd);
        String rightOpndString = convertOperand(rightOpnd);
        if (postFix) {
            return operator.mathString() + "[" + leftOpndString + ", " + rightOpndString + "]";
        } else {
            return leftOpndString + " " + operator.mathString() + " " + rightOpndString;
        }
    }

    private String convertOperand(IArithmeticOperand operand) {
        if (operand instanceof ILiteral) {
            ILiteral literal = (ILiteral) operand;
            return literal.getLiteral();
        } else if (operand instanceof SubTerm) {
            SubTerm subTerm = (SubTerm) operand;
            return subTerm.getOperandString();
        } else if (operand instanceof IExpressionAttribute) {
            IExpressionAttribute expressionAttribute = (IExpressionAttribute) operand;
            return aliasProvider.getAliasFor(expressionAttribute);
        }
        throw new RuntimeException("Can't occur.");
    }
}
