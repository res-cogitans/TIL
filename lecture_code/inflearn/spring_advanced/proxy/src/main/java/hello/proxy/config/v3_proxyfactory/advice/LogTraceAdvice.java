package hello.proxy.config.v3_proxyfactory.advice;

import java.lang.reflect.Method;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import hello.proxy.trace.TraceStatus;
import hello.proxy.trace.logtrace.LogTrace;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LogTraceAdvice implements MethodInterceptor {

	private final LogTrace logTrace;

	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		TraceStatus status = null;
		try{
			Method method = invocation.getMethod();
			String message = method.getDeclaringClass().getSimpleName() + "." + method.getName() + "()";
			status = logTrace.begin(message);

			Object result = invocation.proceed();

			logTrace.end(status);
			return result;
		} catch (Exception e) {
			logTrace.exception(status, e);
			throw e;
		}
	}
}
