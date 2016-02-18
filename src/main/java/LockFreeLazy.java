import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import java.util.function.Supplier;

/**
 * Created by olga on 11.02.16.
 */
public class LockFreeLazy <T> implements Lazy<T> {
    private final static Object NONE = new Object();
    private final static AtomicReferenceFieldUpdater<LockFreeLazy, Object> updater =
            AtomicReferenceFieldUpdater.newUpdater(LockFreeLazy.class, Object.class, "result");

    private Supplier<T> supplier;
    private volatile T result = (T)NONE;

    LockFreeLazy(Supplier<T> supplier) {
        this.supplier = supplier;
    }

    public T get() {
        if (result == (T)NONE) {
            Supplier<T> tmpSupplier = supplier;
            if (tmpSupplier == null) {
                return result;
            }
            if (updater.compareAndSet(this, NONE, tmpSupplier.get())) {
                supplier = null;
            }
        }
        return result;
    }
}

