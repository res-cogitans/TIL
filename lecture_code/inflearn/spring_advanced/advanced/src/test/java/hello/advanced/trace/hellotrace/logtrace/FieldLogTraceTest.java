package hello.advanced.trace.hellotrace.logtrace;

import org.junit.jupiter.api.Test;

import hello.advanced.trace.TraceStatus;

class FieldLogTraceTest {

	 FieldLogTrace trace = new FieldLogTrace();

	@Test
	void begin_end() {
		TraceStatus status1 = trace.begin("hello");
		TraceStatus status2 = trace.begin("hello2");
		trace.end(status2);
		trace.end(status1);
	}

	@Test
	void begin_exception() {
		TraceStatus status1 = trace.begin("hello");
		TraceStatus status2 = trace.begin("hello2");
		trace.exception(status2, new IllegalStateException());
		trace.exception(status1, new IllegalStateException());
	}
}