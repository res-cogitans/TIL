package hello.advanced.trace.threadlocal;

import org.junit.jupiter.api.Test;

import hello.advanced.trace.threadlocal.code.FieldService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FieldServiceTest {

	private FieldService fieldService = new FieldService();

	@Test
	void field() {
		log.info("main Start");
		Runnable userA = () -> {
			fieldService.logic("userA");
		};
		Runnable userB = () -> {
			fieldService.logic("userB");
		};

		Thread threadA = new Thread(userA);
		threadA.setName("thread-A");
		Thread threadB = new Thread(userB);
		threadB.setName("thread-B");

		threadA.start();
		// sleep(2000);	//동시성 문제 발생하지 않음
		sleep(100);	//동시성 문제 발생
		threadB.start();

		//threadB가 도는 중에 메인이 닫힐 수 있기에
		sleep(3000);	//제대로 만들고 싶다면 countDownLatch 참고
		log.info("main exit");
	}

	private void sleep(int millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
