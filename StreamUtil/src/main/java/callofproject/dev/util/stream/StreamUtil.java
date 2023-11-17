package callofproject.dev.util.stream;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@SuppressWarnings("unchecked")
public final class StreamUtil
{
    private StreamUtil()
    {

    }

    public static <T, R> Stream<R> toStream(Iterable<T> iterable, Function<? super T, ? extends R> mapper)
    {
        return StreamSupport.stream(iterable.spliterator(), false).map(mapper);
    }


    public static <T> Stream<T> toStream(Iterable<T> iterable)
    {
        return StreamSupport.stream(iterable.spliterator(), false);
    }


    public static <T, R> List<R> toList(Iterable<T> iterable, Function<? super T, ? extends R> mapper)
    {
        return (List<R>) StreamSupport.stream(iterable.spliterator(), false).map(mapper).toList();
    }


    public static <T, R> List<R> toList(Iterable<T> iterable)
    {
        return (List<R>) StreamSupport.stream(iterable.spliterator(), false).toList();
    }


    public static <T, R> Stream<R> toStreamConcurrent(Iterable<T> iterable, Function<? super T, ? extends R> mapper)
    {
        return StreamSupport.stream(iterable.spliterator(), true).map(mapper);
    }

    public static <T> Stream<T> toStreamConcurrent(Iterable<T> iterable)
    {
        return StreamSupport.stream(iterable.spliterator(), true);
    }

    public static <T, R> List<R> toListConcurrent(Iterable<T> iterable, Function<? super T, ? extends R> mapper)
    {
        return (List<R>) StreamSupport.stream(iterable.spliterator(), true).map(mapper).toList();
    }

    public static <T, R> List<R> toListConcurrent(Iterable<T> iterable)
    {
        return (List<R>) StreamSupport.stream(iterable.spliterator(), true).toList();
    }
}
