package hello.advanced.trace.callback;

@FunctionalInterface
public interface TraceCallBack<T> {
	T call();
}
