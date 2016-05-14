package dk.frankbille.mysqldumper.sql;

public class LongValueConverter implements ValueConverter<Long> {
    public static final LongValueConverter INSTANCE = new LongValueConverter();

    private LongValueConverter() {
    }

    @Override
    public Long convert(String unconvertedValue) {
        if (unconvertedValue != null) {
            return Long.valueOf(unconvertedValue);
        }
        return null;
    }
}
