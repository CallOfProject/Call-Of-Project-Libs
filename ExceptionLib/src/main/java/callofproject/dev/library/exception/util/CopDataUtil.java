package callofproject.dev.library.exception.util;

import callofproject.dev.library.exception.IRunnable;
import callofproject.dev.library.exception.ISupplier;
import callofproject.dev.library.exception.repository.RepositoryException;
import callofproject.dev.library.exception.service.DataServiceException;

import java.util.function.Consumer;

public final class CopDataUtil
{
    private CopDataUtil()
    {
    }

    public static <R> R doForRepository(ISupplier<R> supplier, String message)
    {
        try
        {
            return supplier.get();
        } catch (Throwable ex)
        {
            throw new RepositoryException(message, ex);
        }
    }

    public static <R> R doForRepository(ISupplier<R> supplier, Consumer<Throwable> consumer, String message)
    {
        try
        {
            return supplier.get();
        } catch (Throwable ex)
        {
            consumer.accept(ex);
            throw new RepositoryException(message, ex);
        }
    }

    public static void doForRepository(IRunnable action, String message)
    {
        try
        {
            action.run();
        } catch (Throwable ex)
        {
            throw new RepositoryException(message, ex);
        }
    }

    public static void doForRepository(IRunnable action, Consumer<Throwable> consumer, String message)
    {
        try
        {
            action.run();
        } catch (Throwable ex)
        {
            consumer.accept(ex);
            throw new RepositoryException(message, ex);
        }
    }

    public static <R> R doForDataService(ISupplier<R> supplier, String message)
    {
        try
        {
            return supplier.get();
        } catch (RepositoryException ex)
        {
            throw new DataServiceException(message, ex.getCause());
        } catch (Throwable ex)
        {
            throw new DataServiceException(message, ex);
        }
    }

    public static <R> R doForDataService(ISupplier<R> supplier, Consumer<Throwable> consumer, String message)
    {
        try
        {
            return supplier.get();
        } catch (RepositoryException ex)
        {
            consumer.accept(ex);
            throw new DataServiceException(message, ex.getCause());
        } catch (Throwable ex)
        {
            consumer.accept(ex);
            throw new DataServiceException(message, ex);
        }
    }

    public static <R> R doForDataService(ISupplier<R> supplier, Consumer<Throwable> consumerRepository, Consumer<Throwable> consumerOthers, String message)
    {
        try
        {
            return supplier.get();
        } catch (RepositoryException ex)
        {
            consumerRepository.accept(ex);
            throw new DataServiceException(message, ex.getCause());
        } catch (Throwable ex)
        {
            consumerOthers.accept(ex);
            throw new DataServiceException(message, ex);
        }
    }

    public static void doForDataService(IRunnable action, String message)
    {
        try
        {
            action.run();
        } catch (RepositoryException ex)
        {
            throw new DataServiceException(message, ex.getCause());
        } catch (Throwable ex)
        {
            throw new DataServiceException(message, ex);
        }
    }

    public static void doForDataService(IRunnable action, Consumer<Throwable> consumer, String message)
    {
        try
        {
            action.run();
        } catch (RepositoryException ex)
        {
            consumer.accept(ex);
            throw new DataServiceException(message, ex.getCause());
        } catch (Throwable ex)
        {
            consumer.accept(ex);
            throw new DataServiceException(message, ex);
        }
    }

    public static void doForDataService(IRunnable runnable, Consumer<Throwable> consumerRepository, Consumer<Throwable> consumerOthers, String message)
    {
        try
        {
            runnable.run();
        } catch (RepositoryException ex)
        {
            consumerRepository.accept(ex);
            throw new DataServiceException(message, ex.getCause());
        }
        catch (Throwable ex)
        {
            consumerOthers.accept(ex);
            throw new DataServiceException(message, ex);
        }
    }
}