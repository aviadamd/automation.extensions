package org.staticData;

public class GenericObject<T> {

    private GenericObject<T> genericObject;
    private Class<T> typeArgumentClass;
    public GenericObject(Class<T> typeArgumentClass) {
        this.typeArgumentClass = typeArgumentClass;
    }

    public GenericObject(GenericObject<T> genericObject) {
        this.genericObject = genericObject;
    }

    public Object newInstance() throws Exception {
        return typeArgumentClass.newInstance().getClass();
    }
}
