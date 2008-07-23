package edu.wustl.common.querysuite.queryobject;

public interface IParameter<T> extends INameable {
    T getParameterizedObject();
}
