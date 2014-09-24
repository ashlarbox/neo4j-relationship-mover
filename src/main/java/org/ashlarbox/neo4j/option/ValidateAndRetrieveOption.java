package org.ashlarbox.neo4j.option;

import java.util.HashMap;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.format;
import static org.ashlarbox.neo4j.option.ObjectValidator.validForClass;

public class ValidateAndRetrieveOption {

    private ValidateAndRetrieveOption() {}

    public static Object validateAndRetrieve(HashMap<String, Object> options, String key, Class clazz) {
        checkArgument(validForClass(options.get(key), clazz),
                format("Option %s value is not of expected type %s", key, clazz.getName()));
        return clazz.cast(options.get(key));
    }

}
