package edu.wustl.common.querysuite.queryobject.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import edu.wustl.common.querysuite.queryobject.ArithmeticOperator;
import edu.wustl.common.querysuite.queryobject.IBinaryOperator;
import edu.wustl.common.querysuite.queryobject.LogicalOperator;
import edu.wustl.common.util.CompoundEnum;

public class BinaryOperatorCompoundEnum<T extends Enum<?> & IBinaryOperator>
        extends
            CompoundEnum<BinaryOperatorCompoundEnum<T>, T> implements Serializable {
    private static final long serialVersionUID = 7794336702657368626L;

    private static final List<IBinaryOperator> values = new ArrayList<IBinaryOperator>();

    private static final List<BinaryOperatorCompoundEnum<?>> compoundValues = new ArrayList<BinaryOperatorCompoundEnum<?>>();

    private static int nextOrdinal = 1;

    // STATIC INIT
    static {
        addEnums(LogicalOperator.class);
        addEnums(ArithmeticOperator.class);
    }

    private static int nextOrdinal() {
        return nextOrdinal++;
    }

    private static <T extends Enum<?> & IBinaryOperator> void addEnums(Class<T> klass) {
        for (T e : klass.getEnumConstants()) {
            values.add(e);
            compoundValues.add(newCompoundEnum(e));
        }
    }

    private static <T extends Enum<?> & IBinaryOperator> BinaryOperatorCompoundEnum<T> newCompoundEnum(T e) {
        BinaryOperatorCompoundEnum<T> compoundEnum = new BinaryOperatorCompoundEnum<T>(e, nextOrdinal());
        return compoundEnum;
    }

    // END STATIC INIT

    public static <T extends Enum<?> & IBinaryOperator> BinaryOperatorCompoundEnum<T> compoundEnum(T primitiveEnum) {
        if (primitiveEnum == null) {
            return null;
        }
        int index = values.indexOf(primitiveEnum);
        return (BinaryOperatorCompoundEnum<T>) compoundValues.get(index);
    }

    public static IBinaryOperator[] values() {
        return values.toArray(new IBinaryOperator[0]);
    }

    public static BinaryOperatorCompoundEnum<?>[] compoundValues() {
        return compoundValues.toArray(new BinaryOperatorCompoundEnum<?>[0]);
    }

    private BinaryOperatorCompoundEnum(T primitiveEnum, int ordinal) {
        super(primitiveEnum, ordinal);
    }
}
