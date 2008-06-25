package edu.wustl.common.querysuite.queryobject.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.wustl.common.querysuite.queryobject.ArithmeticOperator;
import edu.wustl.common.querysuite.queryobject.IBinaryOperator;
import edu.wustl.common.querysuite.queryobject.LogicalOperator;

public class BinaryOperatorCompoundEnum<T extends Enum<?> & IBinaryOperator> implements Serializable {
    private static final long serialVersionUID = 7794336702657368626L;

    private static class MapKey<K extends Enum<?> & IBinaryOperator> {
        private K k;

        MapKey(K k) {
            if (k == null) {
                throw new NullPointerException();
            }
            this.k = k;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof MapKey)) {
                return false;
            }
            return k == ((MapKey<?>) obj).k;
        }

        @Override
        public int hashCode() {
            return k.hashCode();
        }
    }

    private static final Map<MapKey<?>, BinaryOperatorCompoundEnum<?>> map = new HashMap<MapKey<?>, BinaryOperatorCompoundEnum<?>>();

    private static final List<IBinaryOperator> values = new ArrayList<IBinaryOperator>();

    private static final List<BinaryOperatorCompoundEnum<?>> compoundValues = new ArrayList<BinaryOperatorCompoundEnum<?>>();

    private static int nextOrdinal = 1;

    static {
        addEnums(LogicalOperator.class);
        addEnums(ArithmeticOperator.class);
    }

    private static <T extends Enum<?> & IBinaryOperator> void addEnums(Class<T> klass) {
        for (T e : klass.getEnumConstants()) {
            values.add(e);
            compoundValues.add(newCompoundEnum(e));
        }
    }

    private static <T extends Enum<?> & IBinaryOperator> BinaryOperatorCompoundEnum<T> newCompoundEnum(T e) {
        BinaryOperatorCompoundEnum<T> compoundEnum = new BinaryOperatorCompoundEnum<T>(e.name(), nextOrdinal++, e);
        map.put(mapKey(e), compoundEnum);
        return compoundEnum;
    }

    private static <K extends Enum<?> & IBinaryOperator> MapKey<K> mapKey(K k) {
        return new MapKey<K>(k);
    }

    private final String name;

    private final int ordinal;

    private final T primitiveEnum;

    private BinaryOperatorCompoundEnum(String name, int ordinal, T primitiveEnum) {
        this.name = name;
        this.ordinal = ordinal;
        this.primitiveEnum = primitiveEnum;
    }

    public String name() {
        return name;
    }

    public int ordinal() {
        return ordinal;
    }

    public T primitiveEnum() {
        return primitiveEnum;
    }

    public static <T extends Enum<?> & IBinaryOperator> BinaryOperatorCompoundEnum<T> compoundEnum(T primitiveEnum) {
        return (BinaryOperatorCompoundEnum<T>) map.get(mapKey(primitiveEnum));
    }

    public static IBinaryOperator[] values() {
        return values.toArray(new IBinaryOperator[0]);
    }

    public static BinaryOperatorCompoundEnum<?>[] compoundValues() {
        return compoundValues.toArray(new BinaryOperatorCompoundEnum<?>[0]);
    }
}
