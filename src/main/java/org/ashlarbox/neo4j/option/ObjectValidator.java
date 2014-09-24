package org.ashlarbox.neo4j.option;

public class ObjectValidator {

    private ObjectValidator() {}

    public static boolean validForClass(Object object, Class clazz) {
        return object == null || clazz.isInstance(object);
    }
}
