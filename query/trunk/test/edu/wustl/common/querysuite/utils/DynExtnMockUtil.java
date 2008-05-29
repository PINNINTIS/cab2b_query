package edu.wustl.common.querysuite.utils;

import org.jmock.Expectations;
import org.jmock.Mockery;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;

public class DynExtnMockUtil {
    private static Mockery context = new Mockery();

    private static int i = 0;

    private static synchronized String getI() {
        return String.valueOf(i++);
    }

    public static AttributeInterface createAttribute(final String name, final EntityInterface entity) {
        final AttributeInterface attribute = context.mock(AttributeInterface.class, getI());

        context.checking(new Expectations() {
            {
                allowing(attribute).getName();
                will(returnValue(name));
                allowing(attribute).getEntity();
                will(returnValue(entity));
            }
        });
        return attribute;
    }

    public static EntityInterface createEntity(final String name) {
        final EntityInterface entity = context.mock(EntityInterface.class, getI());
        context.checking(new Expectations() {
            {
                allowing(entity).getName();
                will(returnValue(name));
            }
        });
        return entity;
    }
}
