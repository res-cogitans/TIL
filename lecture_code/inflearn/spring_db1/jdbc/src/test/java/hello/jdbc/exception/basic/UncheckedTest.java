package hello.jdbc.exception.basic;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Slf4j
public class UncheckedTest {

    @Test
    @DisplayName("UncheckedException 처리 - catch")
    void catchUnchecked() {
        var service = new Service();
        service.callCatch();
    }

    @Test
    @DisplayName("UncheckedException 처리 - throw")
    void throwUnchecked() {
        var service = new Service();
        assertThatThrownBy(service::callThrow)
                .isInstanceOf(MyUncheckedException.class);
    }


    /**
     * RuntimeException 상속 받은 예외는 UncheckedException
     */

    static class MyUncheckedException extends RuntimeException {
        public MyUncheckedException(String message) {
            super(message);
        }
    }

    /**
     * UncheckedException
     * 예외를 잡거나 던지지 않아도 된다.
     * 예외를 잡지 않으면 자동으로 밖으로 던짐
     */
    static class Service {
        Repository repository = new Repository();

        /**
         * 필요한 경우 예외를 잡아서 처리하면 된다.
         */
        void callCatch() {
            try {
                repository.call();
            } catch (MyUncheckedException e) {
                //예외 처리 로직
                log.info("예외 처리, message={}", e.getMessage(), e);
            }
        }

        /**
         * 예외를 잡지 않아도 됨, 자연스럽게 상위로 전달됨
         * CheckedException 과는 달리 throws 예외 선언이 불필요
         */
        void callThrow() {
            repository.call();
        }

    }

    static class Repository {
        void call() {
            throw new MyUncheckedException("UncheckedException message");
        }
    }
}
