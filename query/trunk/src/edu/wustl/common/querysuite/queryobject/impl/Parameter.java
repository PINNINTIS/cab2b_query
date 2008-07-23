package edu.wustl.common.querysuite.queryobject.impl;

import edu.wustl.common.querysuite.queryobject.IParameter;

public class Parameter<T> implements IParameter<T> {
    private T parameterizedObject;

    private String name;

    private Parameter() {
    // for hibernate
    }

    public Parameter(T parameterizedObject) {
        this.parameterizedObject = parameterizedObject;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public T getParameterizedObject() {
        return parameterizedObject;
    }

    public void setParameterizedObject(T parameterizedObject) {
        this.parameterizedObject = parameterizedObject;
    }
}
