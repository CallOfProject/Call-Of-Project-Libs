package callofproject.dev.library.exception;

@FunctionalInterface
public interface ISupplier<R> {
    R get() throws Exception;
}