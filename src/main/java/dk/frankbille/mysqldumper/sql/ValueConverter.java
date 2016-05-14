package dk.frankbille.mysqldumper.sql;

public interface ValueConverter<T> {

    T convert(String unconvertedValue);

}
