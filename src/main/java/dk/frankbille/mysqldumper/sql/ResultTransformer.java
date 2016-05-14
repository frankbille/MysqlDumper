package dk.frankbille.mysqldumper.sql;

import java.util.*;

public class ResultTransformer {

    private final Map<String, ValueConverter> converters = new HashMap<>();

    public ResultTransformer() {
    }

    public void addConverter(String columnName, ValueConverter converter) {
        converters.put(columnName, converter);
    }

    public Object convert(String columnName, String unconvertedString) {
        final ValueConverter valueConverter = converters.get(columnName);
        if (valueConverter != null) {
            return valueConverter.convert(unconvertedString);
        }
        return unconvertedString;
    }

}
